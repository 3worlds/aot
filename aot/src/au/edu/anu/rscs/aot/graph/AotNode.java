/**************************************************************************
 *  AOT - Aspect-Oriented Thinking                                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  AOT is a method to generate elaborate software code from a series of  *
 *  independent domains of knowledge. It enables one to manage and        *
 *  maintain software from explicit specifications that can be translated *
 *  into any programming language.          							  *
 **************************************************************************                                       
 *  This file is part of AOT (Aspect-Oriented Thinking).                  *
 *                                                                        *
 *  AOT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  AOT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package au.edu.anu.rscs.aot.graph;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.impl.SimpleNodeImpl;
import fr.cnrs.iees.properties.ExtendablePropertyList;
import fr.cnrs.iees.properties.PropertyListSetters;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.ResizeablePropertyList;
import fr.cnrs.iees.properties.impl.ExtendablePropertyListImpl;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.impl.DefaultTreeFactory;
import fr.ens.biologie.generic.Labelled;
import fr.ens.biologie.generic.Named;
import fr.ens.biologie.generic.NamedAndLabelled;
import fr.ens.biologie.generic.Sealable;

/**
 * <p>Reimplementation of AOTNode</p>
 * <p>Some important points:</p>
 * <ul>
 * <li>The label:name pair is used to uniquely identify {@code AotNode}s in replacement for Uids.
 * This means that {@code .equals()} uses these fields to decide if {@code AotNode}s are identical.
 * This also means that {@code Set}s of {@code AotNode}s will rely on these to decide if 
 * items are duplicate. Therefore, name and label should be set at construction time, or 
 * "not too late" after that (eg when loading from a file). To prevent accidental change of 
 * name and label, the {@code setName()} and {@code setLabel()} methods have been protected:
 * name and label can only be changed if they are null, ie only once (or never if
 * a constructor with name and label arguments was used).</li>
 * <li>The {@code addEdge(...)} methods should never be used directly at runtime, because
 * linking two {@code Node}s through an {@code Edge} implies some consistency (ie the 
 * {@code Edge} startNode and endNode fields must be consistent with their respective Edge 
 * lists). In {@link NodeAdapter}, edges are stored in {@link HashSet}s so that it is impossible
 * to mistakenly add the same edge twice. {@link GraphElementFactory} edge creation methods
 * normally take care of node lists, so that an Edge is always consistently created. </li>
 * <li>The same applies to tree management: an {@code AotNode} is always created within the context of
 * a tree, so it must have a parent. {@link TreeNodeFactory} proposes methods to create 
 * a {@code TreeNode} with a parent, which take care of the connection settings.</li>
 * </ul>
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
// Tested OK with version 0.0.3 on 10/1/2019
// Tested OK with version 0.0.4 on 10/1/2019
public class AotNode extends SimpleNodeImpl 
		implements TreeNode, ExtendablePropertyList, NamedAndLabelled, Configurable {
	
	private static Logger log = Logger.getLogger(AotNode.class.getName());
	private static String defaultLabel = "AOTNode";

	// this only holds the children and parent nodes
	private TreeNode treenode;
	// this holds the properties
	private ExtendablePropertyList properties;
	// the name
	private String name=null;
	// the label - remember that label+name = uniqueID within the graph context
	private String label=null;

	// ----------------------- Constructors

	// this constructor sets the name to the uniqueID
	protected AotNode(AotGraph factory) {
		this(defaultLabel,null,factory);
		name = super.instanceId();
	}

	// this is the constructor to use with descendant classes
	protected AotNode(String label, String name, AotGraph factory) {
		super(factory);
		this.label = label;
		this.name = name;
		this.treenode = DefaultTreeFactory.makeSimpleTreeNode(null,factory);
		this.properties = new ExtendablePropertyListImpl();
	}

	// this is the constructor to use for plain aot nodes
	protected AotNode(String name, AotGraph factory) {
		this(defaultLabel,name,factory);
	}
	
	// ---------------------------Identifiable (from both Node and TreeNode). 
	@Override
	public String classId() {
		if (label==null)
			return defaultLabel;
		return label;
	}

	@Override
	public String instanceId() {
		if (name==null)
			return "";
		return name;
	}

	// -------------- ExtendablePropertyList
	@Override
	public ResizeablePropertyList addProperties(List<String> arg0) {
		return properties.addProperties(arg0);
	}

	@Override
	public ResizeablePropertyList addProperties(String... keys) {
		return properties.addProperties(keys);
	}

	@Override
	public ResizeablePropertyList addProperties(ReadOnlyPropertyList list) {
		properties.addProperties(list);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperty(Property property) {
		return properties.addProperty(property);
	}

	@Override
	public ResizeablePropertyList addProperty(String key) {
		return properties.addProperty(key);
	}

	@Override
	public ResizeablePropertyList addProperty(String key, Object value) {
		return properties.addProperty(key, value);
	}

	@Override
	public Object getPropertyValue(String key, Object defaultValue) {
		return properties.getPropertyValue(key, defaultValue);
	}

	@Override
	public ResizeablePropertyList removeAllProperties() {
		return properties.removeAllProperties();
	}

	@Override
	public ResizeablePropertyList removeProperty(String key) {
		return properties.removeProperty(key);
	}
	
	// Not sure about this one - maybe it should be disabled by throwing an exception
	// need careful checking
	@Override
	public AotNode clone() {
		AotNode n = new AotNode(graphElementFactory());
		n.addProperties(this.properties);
		n.setLabel(label);
		n.setName(name);
		return n;
	}

	@Override
	public PropertyListSetters setProperty(String key, Object value) {
		return properties.setProperty(key, value);
	}

	@Override
	public Set<String> getKeysAsSet() {
		return properties.getKeysAsSet();
	}

	@Override
	public Object getPropertyValue(String arg0) {
		return properties.getPropertyValue(arg0);
	}

	@Override
	public boolean hasProperty(String arg0) {
		return properties.hasProperty(arg0);
	}

	@Override
	public int size() {
		return properties.size();
	}

	@Override
	public Sealable seal() {
		return properties.seal();
	}

	@Override
	public boolean isSealed() {
		return properties.isSealed();
	}

	// ---------------- TreeNode

	@Override
	public void addChild(TreeNode child) {
		treenode.addChild(child);
	}

	@Override
	public Iterable<TreeNode> getChildren() {
		return treenode.getChildren();
	}

	@Override
	public TreeNode getParent() {
		return treenode.getParent();
	}

	@Override
	public boolean hasChildren() {
		return treenode.hasChildren();
	}

	@Override
	public void setChildren(TreeNode... children) {
		treenode.setChildren(children);
	}

	@Override
	public void setChildren(Iterable<TreeNode> children) {
		treenode.setChildren(children);
	}

	@Override
	public void setChildren(Collection<TreeNode> children) {
		treenode.setChildren(children);
	}

	/**
	 * CAUTION: to prevent problems, parent can only be set if it was null. It should
	 * be set by the factory in the makeTreeNode(...) methods
	 */
	@Override
	public void setParent(TreeNode parent) {
		if (treenode.getParent()==null)
			treenode.setParent(parent);
	}

	@Override
	public AotGraph treeNodeFactory() {
		return (AotGraph)graphElementFactory();
	}

	@Override
	public int nChildren() {
		return treenode.nChildren();
	}

	// --------------- NODE

	@Override
	public AotGraph graphElementFactory() {
		return (AotGraph) super.graphElementFactory();
	}

	// -------------------------- NamedAndLabelled

	@Override
	public String getName() {
		return name;
	}

	/**
	 * NOTE: name can only be set once, since it is used as unique ID in equality tests,
	 * on which sets base their unicity of element constraint.
	 */
	@Override
	public Named setName(String name) {
		if (this.name==null)
			this.name = name;
		return this;
	}

	@Override
	public boolean sameName(Named item) {
		return this.hasName(item.getName());
	}

	@Override
	public boolean hasName(String name) {
		return Objects.equals(this.name, name);
	}

	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * NOTE: label can only be set once, since it is used as unique ID in equality tests,
	 * on which sets base their unicity of element constraint.
	 */
	@Override
	public Labelled setLabel(String label) {
		if (this.label==null)
			this.label = label;
		return this;
	}

	@Override
	public boolean sameLabel(Labelled item) {
		return hasLabel(item.getLabel());
	}

	@Override
	public boolean hasLabel(String label) {
		return Objects.equals(this.label, label);
	}

	// ------------------- Configurable

	@Override
	public AotNode initialise() {
		log.fine(()->uniqueId()+" initialising");
		return this;
	}
	
	// -------------------  Textable

	@Override
	public String toUniqueString() {
		return uniqueId();
	}

	/**
	 * Displays an AotNode as follows (on a single line):
	 * 
	 * <pre>
	 * node_label:node_name=[
	 *    ↑parent_label:parent_name      // the parent node, or ROOT if null
	 *    ↓child_label:child_name        // child node, repeated as needed
	 *    →out_node_label:out_node_name  // end node of outgoing edge, repeated as needed
	 *    ←in_node_label:in_node_name    // start node of incoming edge, repeated as needed
	 * ] 
	 * </pre>
	 * <p>e.g.: {@code AOTNode:=[↑AOTNode:1 ↓AOTNode:2 ←AOTNode:2 ←AOTNode: →AOTNode: →AOTNode:1]}
	 *  (in this case, the name is missing).</p>
	 */
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(toUniqueString());
		sb.append("=[");
		if (treenode.getParent()!=null)
			sb.append("↑").append(treenode.getParent().toUniqueString());
		else
			sb.append("ROOT");
		if (treenode.hasChildren()) {
			for (TreeNode n:treenode.getChildren()) {
				sb.append(" ↓").append(n.toUniqueString());
			}
		}
		if (getEdges(Direction.IN).iterator().hasNext()) {
			for (Edge e:getEdges(Direction.IN))
				sb.append(" ←").append(e.startNode().toUniqueString());
		}
		if (getEdges(Direction.OUT).iterator().hasNext()) {
			for (Edge e:getEdges(Direction.OUT))
				sb.append(" →").append(e.endNode().toUniqueString());
		}		
		if (properties.size()>0)
			sb.append(' ').append(properties.toString());
		sb.append("]");
		return sb.toString();
	}
	
}
