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

import java.util.List;
import java.util.Set;
import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.impl.TreeGraphNode;
import fr.cnrs.iees.properties.ExtendablePropertyList;
import fr.cnrs.iees.properties.PropertyListSetters;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.ResizeablePropertyList;
import fr.cnrs.iees.properties.impl.ExtendablePropertyListImpl;
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
// Tested OK with version 0.0.7 on 24/1/2019
public class AotNode extends TreeGraphNode 
		implements 	ExtendablePropertyList,	Configurable {

	// ----------------------- Constructors

	// this constructor sets the name to the uniqueID
	protected AotNode(String label, String name, AotGraph factory, ReadOnlyPropertyList props) {
		super(label,name,factory,factory,props); 
		ExtendablePropertyList pl = new ExtendablePropertyListImpl();
		if (props!=null)
			pl.addProperties(props);
		properties = pl;
	}

	// this is the constructor to use with descendant classes
	protected AotNode(String label, AotGraph factory, ReadOnlyPropertyList props) {
		super(label,factory,factory,props); // this generates a name
		ExtendablePropertyList pl = new ExtendablePropertyListImpl();
		if (props!=null)
			pl.addProperties(props);
		properties = pl;
	}
	
	// -------------- ExtendablePropertyList
	@Override
	public ResizeablePropertyList addProperties(List<String> arg0) {
		return ((ResizeablePropertyList) properties).addProperties(arg0);
	}

	@Override
	public ResizeablePropertyList addProperties(String... keys) {
		return ((ResizeablePropertyList) properties).addProperties(keys);
	}

	@Override
	public ResizeablePropertyList addProperties(ReadOnlyPropertyList list) {
		((ResizeablePropertyList) properties).addProperties(list);
		return (ResizeablePropertyList) properties;
	}

	@Override
	public ResizeablePropertyList addProperty(Property property) {
		return ((ResizeablePropertyList) properties).addProperty(property);
	}

	@Override
	public ResizeablePropertyList addProperty(String key) {
		return ((ResizeablePropertyList) properties).addProperty(key);
	}

	@Override
	public ResizeablePropertyList addProperty(String key, Object value) {
		return ((ResizeablePropertyList) properties).addProperty(key, value);
	}

	@Override
	public Object getPropertyValue(String key, Object defaultValue) {
		return ((ResizeablePropertyList) properties).getPropertyValue(key, defaultValue);
	}

	@Override
	public ResizeablePropertyList removeAllProperties() {
		return ((ResizeablePropertyList) properties).removeAllProperties();
	}

	@Override
	public ResizeablePropertyList removeProperty(String key) {
		return ((ResizeablePropertyList) properties).removeProperty(key);
	}
	
	// Not sure about this one - maybe it should be disabled by throwing an exception
	// need careful checking
	@Override
	public AotNode clone() {
		AotNode n = new AotNode(label,(AotGraph)nodeFactory(),properties);
		return n;
	}

	@Override
	public PropertyListSetters setProperty(String key, Object value) {
		return ((PropertyListSetters) properties).setProperty(key, value);
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
		return ((Sealable) properties).seal();
	}

	@Override
	public boolean isSealed() {
		return ((Sealable) properties).isSealed();
	}

	// Configurable
	
	@Override
	public Configurable initialise() {
		return this;
	}
	
	// Node
	
	@Override
	public AotGraph nodeFactory() {
		return (AotGraph) super.nodeFactory();
	}
	
}
