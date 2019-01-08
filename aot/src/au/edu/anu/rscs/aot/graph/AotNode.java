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

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Element;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.impl.DefaultGraphFactory;
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
 * Reimplementation of AOTNode
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
public class AotNode implements Node, TreeNode, 
								ExtendablePropertyList, NamedAndLabelled, Configurable {

	// this only holds the node edges
	private Node node;
	// this only holds the children and parent nodes
	private TreeNode treenode;
	// this holds the properties
	private ExtendablePropertyList properties;
	// the name
	private String name;
	// the label - remember that label+name = uniqueID within the graph context
	private String label;
	// the factory for such nodes - constructors must be protected
	private AotGraph factory;

	// Constructors
	protected AotNode(AotGraph factory) {
		super();
		this.label = "AOTNode";
		this.factory = factory;
		this.node =  DefaultGraphFactory.makeSimpleNode(factory);
		this.treenode = DefaultTreeFactory.makeSimpleTreeNode(factory);
		this.properties = new ExtendablePropertyListImpl();
	}

	// ---------------------------Identifiable (from both Node and TreeNode). Is
	// this conflict a problem??
	// apparently not because it does not refer to super.classId()
	@Override
	public String classId() {
		return label;
	}

	@Override
	public String instanceId() {
		return name;
	}

	// -------------- ExtendablePropertyList
	@Override
	public ResizeablePropertyList addProperties(List<String> arg0) {
		return properties.addProperties(arg0);
	}

	@Override
	public ResizeablePropertyList addProperties(String... keys) {
		properties.addProperties(keys);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperties(ReadOnlyPropertyList list) {
		properties.addProperties(list);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperty(Property property) {
		properties.addProperty(property);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperty(String key) {
		properties.addProperty(key);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperty(String key, Object value) {
		properties.addProperty(key, value);
		return properties;
	}

	@Override
	public Object getPropertyValue(String key, Object defaultValue) {
		properties.getPropertyValue(key, defaultValue);
		return properties;
	}

	@Override
	public ResizeablePropertyList removeAllProperties() {
		properties.removeAllProperties();
		return properties;
	}

	@Override
	public ResizeablePropertyList removeProperty(String key) {
		properties.removeProperty(key);
		return properties;
	}
	
	// Not sure about this one - maybe it should be disabled by throwing an exception
	// need careful checking
	@Override
	public AotNode clone() {
		AotNode n = new AotNode(factory);
		n.setProperties(this.properties);
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

	@Override
	public void setParent(TreeNode parent) {
		treenode.setParent(parent);
	}

	@Override
	public AotGraph treeNodeFactory() {
		return factory;
	}

	// --------------- NODE
	@Override
	public Element disconnect() {
		return node.disconnect();
	}

	@Override
	public Collection<Node> traversal(int arg0) {
		return node.traversal(arg0);
	}

	@Override
	public Collection<? extends Node> traversal(int arg0, Direction arg1) {
		return node.traversal(arg0, arg1);
	}

	@Override
	public boolean addEdge(Edge edge) {
		return node.addEdge(edge);
	}

	@Override
	public boolean addEdge(Edge edge, Direction direction) {
		return node.addEdge(edge, direction);
	}

	@Override
	public int degree(Direction direction) {
		return node.degree(direction);
	}

	@Override
	public Iterable<? extends Edge> getEdges() {
		return node.getEdges();
	}

	@Override
	public Iterable<? extends Edge> getEdges(Direction direction) {
		return node.getEdges(direction);
	}

	@Override
	public boolean isLeaf() {
		return node.isLeaf();
	}

	@Override
	public boolean isRoot() {
		return node.isRoot();
	}

	@Override
	public boolean removeEdge(Edge edge, Direction direction) {
		return node.removeEdge(edge, direction);
	}

	@Override
	public AotGraph graphElementFactory() {
		return factory;
	}

	// -------------------------- NamedAndLabelled

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Named setName(String name) {
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

	@Override
	public Labelled setLabel(String label) {
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
		return this;
	}

}
