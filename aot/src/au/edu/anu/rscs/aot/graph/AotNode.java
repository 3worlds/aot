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

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Element;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.impl.SimpleNodeImpl;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.ResizeablePropertyList;
import fr.cnrs.iees.properties.impl.ExtendablePropertyListImpl;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.impl.SimpleTreeNodeImpl;
import fr.ens.biologie.generic.Labelled;
import fr.ens.biologie.generic.Named;
import fr.ens.biologie.generic.NamedAndLabelled;

/**
 * Reimplementation of AOTNode
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
public class AotNode 
	implements Node, TreeNode, ResizeablePropertyList, NamedAndLabelled, Configurable {
	
	// this only holds the node edges
	private SimpleNodeImpl node = null;
	// this only holds the children and parent nodes
	private SimpleTreeNodeImpl treenode = null;
	// this holds the properties
	private ExtendablePropertyListImpl properties = null;
	// the name
	private String name = null;
	// the label - remember that label+name = uniqueID within the graph context
	private String label =  "AOTnode";
	// the factory for such nodes - constructors must be protected
	private AotGraph factory = null;
	
	// Constructors
	
	// NODE
	
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
		return node.traversal(arg0,arg1);
	}
	
	@Override
	public String classId() {
		return label;
	}
	
	@Override
	public String instanceId() {
		return name;
	}
	@Override
	public ResizeablePropertyList addProperties(List<String> arg0) {
		return properties.addProperties(arg0);
	}
	
	// TODO: ETC...

	// PropertyList
	
	@Override
	public ResizeablePropertyList addProperties(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResizeablePropertyList addProperties(ReadOnlyPropertyList arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResizeablePropertyList addProperty(Property arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResizeablePropertyList addProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResizeablePropertyList addProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getPropertyValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResizeablePropertyList removeAllProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResizeablePropertyList removeProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// TreeNode
	
	@Override
	public void addChild(TreeNode arg0) {
		treenode.addChild(arg0);
	}

	@Override
	public Iterable<TreeNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setChildren(TreeNode... arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setChildren(Iterable<TreeNode> arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setChildren(Collection<TreeNode> arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setParent(TreeNode arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean addEdge(Edge arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean addEdge(Edge arg0, Direction arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int degree(Direction arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Iterable<? extends Edge> getEdges() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Iterable<? extends Edge> getEdges(Direction arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean removeEdge(Edge arg0, Direction arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public AotGraph factory() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Named
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public Named setName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean sameName(Named item) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasName(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	
	// Labelled
	
	public String getLabel() {
		return label;
	}
	@Override
	public Labelled setLabel(String label) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean sameLabel(Labelled item) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasLabel(String label) {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Configurable
	
	@Override
	public AotNode initialise() {
		return this;
	}
	
	

}
