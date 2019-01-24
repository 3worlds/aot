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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;
import fr.cnrs.iees.tree.TreeNode;

/**
 * 
 * @author Jacques Gignoux - 10 janv. 2019
 *
 */
class AotNodeTest {
	
	private AotNode node = null;
	private AotNode node1, node2, node3, node4, node5;
	@SuppressWarnings("unused")
	private AotEdge e1, e2, e3, e4, e5, e6;
	private AotGraph graph = null;
	
	@BeforeEach
	private void init() {
		graph = new AotGraph();
		node = new AotNode("bidon",graph);
	}
	
	@BeforeEach
	private void init2() {
		AotGraph graph = new AotGraph();
		node1 = new AotNode(null,"1",graph);
		node2 = new AotNode(null,"2",graph);
		node3 = new AotNode(null,"3",graph);
		node4 = new AotNode(null,"4",graph);
		node5 = new AotNode(null,"5",graph);
		e1 = graph.makeEdge(node1, node2);
		e2 = graph.makeEdge(node1, node3);
		e3 = graph.makeEdge(node3, node4);
		e4 = graph.makeEdge(node5, node3);
		e5 = graph.makeEdge(node4, node2);
		e6 = graph.makeEdge(node3, node3);		
	}
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}

	@Test
	void testClassId() {
		assertEquals(node.classId(),"AOTNode");
	}

	@Test
	void testInstanceId() {
		assertEquals(node.instanceId(),"bidon");
		assertEquals(node1.instanceId(),"1");
	}

	@Test
	void testAddPropertiesListOfString() {
		List<String> l = new ArrayList<String>();
		l.add("x");
		l.add("y"); 
		l.add("z");
		node.addProperties(l);
		node.setProperty("x", 1);
		node.setProperty("y", 2);
		node.setProperty("z", 3.0);
//		show("testAddPropertiesListOfString",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT x=1 y=2 z=3.0]");
	}

	@Test
	void testAddPropertiesStringArray() {
		node.addProperties("x","y","z");
		node.setProperty("x", 1);
		node.setProperty("y", 2);
		node.setProperty("z", 3.0);
//		show("testAddPropertiesStringArray",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT x=1 y=2 z=3.0]");
	}

	@Test
	void testAddPropertiesReadOnlyPropertyList() {
		SimplePropertyList pl = new SimplePropertyListImpl("a","b","c");
		pl.setProperty("a",1);
		pl.setProperty("b",2);
		pl.setProperty("c",3);
		node.addProperties(pl);
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT a=1 b=2 c=3]");
	}

	@Test
	void testAddPropertyProperty() {
		Property p = new Property("a",1);
		node.addProperty(p);
//		show("testAddPropertyProperty",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT a=1]");
	}

	@Test
	void testAddPropertyString() {
		node.addProperty("a");
		node.setProperty("a",2);
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT a=2]");
	}

	@Test
	void testAddPropertyStringObject() {
		node.addProperty("a",3);
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT a=3]");
	}

	@Test
	void testGetPropertyValueStringObject() {
		int z = (Integer) node.getPropertyValue("k",3);
//		show("testGetPropertyValueStringObject",String.valueOf(node.getPropertyValue("a",3)));
		assertEquals(z,3);
		assertEquals(node.getPropertyValue("k",5),3);
	}

	@Test
	void testRemoveAllProperties() {
		node.addProperties("x","y","z");
		show("testRemoveAllProperties",node.toDetailedString());
		node.removeAllProperties();
		show("testRemoveAllProperties",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT]");
	}

	@Test
	void testRemoveProperty() {
		node.addProperties("x","y","z");
		node.setProperty("x", 1);
		node.setProperty("y", 2);
		node.setProperty("z", 3.0);
		show("testRemoveProperty",node.toDetailedString());
		node.removeProperty("y");
		show("testRemoveProperty",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT x=1 z=3.0]");
	}

	@Test
	void testClone() {
		node.addProperties("x","y","z");
		node.setProperty("x", 1);
		node.setProperty("y", 2);
		node.setProperty("z", 3.0);
		show("testClone",node.toDetailedString());
		AotNode n = node.clone();
		show("testClone",n.toDetailedString());
		assertNotNull(n);
	}

	@Test
	void testSetProperty() {
		node.addProperties("x","y","z");
//		show("testSetProperty",String.valueOf(node.getPropertyValue("x")));
		node.setProperty("x", 1);
//		show("testSetProperty",String.valueOf(node.getPropertyValue("x")));
		assertEquals(node.getPropertyValue("x"),1);
	}

	@Test
	void testGetKeysAsSet() {
		node.addProperties("x","y","z");
//		show("testGetKeysAsSet",node.getKeysAsSet().toString());
		assertEquals(node.getKeysAsSet().toString(),"[x, y, z]");
	}

	@Test
	void testGetPropertyValueString() {
		node.addProperty("w",125);
//		show("testGetPropertyValueString",String.valueOf(node.getPropertyValue("w")));
		assertEquals(node.getPropertyValue("w"),125);
	}

	@Test
	void testHasProperty() {
		node.addProperties("x","y","z");
		assertTrue(node.hasProperty("x"));
		assertFalse(node.hasProperty("xx"));
	}

	@Test
	void testSize() {
		node.addProperties("x","y","z");
		assertEquals(node.size(),3);
	}

	@Test
	void testSeal() {
		assertFalse(node.isSealed());
		node.seal();
		assertThrows(OmugiException.class,()->node.addProperty("q"));
	}

	@Test
	void testIsSealed() {
		assertFalse(node.isSealed());
		node.seal();
		assertTrue(node.isSealed());
	}

	@Test
	void testAddChild() {
		AotNode n = new AotNode(graph);
		node.addChild(n);
		assertTrue(node.hasChild(n));
		assertFalse(n.getParent()==node);
	}

	@Test
	void testGetChildren() {
		AotNode n = new AotNode("1",graph);
		AotNode n2 = new AotNode("2",graph);
		node.addChild(n);
		node.addChild(n); // this should fail silently because container is a Set
		node.addChild(n2);
		int i=0;
		for (TreeNode tn:node.getChildren()) {
			show("testGetChildren",tn.toDetailedString());
			i++;
		}
		assertEquals(i,2);
	}

	@Test
	void testGetParent() {
		AotNode n = new AotNode(graph);
		node.setParent(n);
		show("testGetParent",node.toDetailedString());
		assertTrue(node.getParent()==n);
		assertFalse(n.hasChild(node));
	}

	@Test
	void testHasChildren() {
		assertFalse(node.hasChildren());
		AotNode n = new AotNode(graph);
		node.addChild(n);
		assertTrue(node.hasChildren());
	}

	@Test
	void testSetChildrenTreeNodeArray() {
		AotNode n = new AotNode("1",graph);
		AotNode n2 = new AotNode("2",graph);
		assertFalse(node.hasChildren());
		node.setChildren(n,n2);
		assertTrue(node.hasChildren());
	}

	@Test
	void testSetChildrenIterableOfTreeNode() {
		AotNode n = new AotNode("1",graph);
		AotNode n2 = new AotNode("2",graph);
		List<TreeNode> l = new LinkedList<>();
		l.add(n);
		l.add(n2);
		assertFalse(node.hasChildren());
		Iterable<TreeNode> it = l;
		node.setChildren(it);
		assertTrue(node.hasChildren());
	}

	@Test
	void testSetChildrenCollectionOfTreeNode() {
		AotNode n = new AotNode("1",graph);
		AotNode n2 = new AotNode("2",graph);
		List<TreeNode> l = new LinkedList<>();
		l.add(n);
		l.add(n2);
		assertFalse(node.hasChildren());
		node.setChildren(l);
		assertTrue(node.hasChildren());
	}

	@Test
	void testSetParent() {
		AotNode n = new AotNode(graph);
		node.setParent(n);
		assertTrue(n==node.getParent());
		assertFalse(n.hasChild(node));
	}

	@Test
	void testTreeNodeFactory() {
		show("testTreeNodeFactory",node.treeNodeFactory().toString());
		assertNotNull(node.treeNodeFactory());
	}

	@SuppressWarnings({"unchecked" })
	@Test
	void testDisconnect() {
		AotNode n = new AotNode(null,"1",node.nodeFactory());
		AotNode n2 = new AotNode(null,"2",node.nodeFactory());
		Edge ed1 = node.nodeFactory().makeEdge(node, n);
		Edge ed2 = node.nodeFactory().makeEdge(n2, node);
		Edge ed3 = node.nodeFactory().makeEdge(node, node);
		node.setParent(n);
		node.addChild(n2);
		show("testDisconnect",node.toDetailedString());
		Iterable<Edge> i = (Iterable<Edge>) node.getEdges();
		List<Edge> l = new LinkedList<Edge>();
		for (Edge e:i)
			l.add(e);
		assertTrue(l.contains(ed1));
		assertTrue(l.contains(ed2));
		assertTrue(l.contains(ed3));
		node.disconnect();
		i = (Iterable<Edge>) node.getEdges();
		l.clear();
		for (Edge e:i)
			l.add(e);
		show("testDisconnect",node.toDetailedString());
		assertFalse(l.contains(ed1));
		assertFalse(l.contains(ed2));
		assertFalse(l.contains(ed3));
	}

	@Test
	void testTraversalInt() {
		Collection<Node> c = node1.traversal(2);
		show("testTraversalInt",c.toString());
		assertTrue(c.contains(node1));
		assertTrue(c.contains(node2));
		assertTrue(c.contains(node3));
		assertFalse(c.contains(node4));
		assertFalse(c.contains(node5));
	}

	@SuppressWarnings("unchecked")
	@Test
	void testTraversalIntDirection() {
		Collection<AotNode> c = (Collection<AotNode>) node1.traversal(2,Direction.OUT);
		show("testTraversalInt",c.toString());
		assertTrue(c.contains(node1));
		assertTrue(c.contains(node2));
		assertTrue(c.contains(node3));
		assertFalse(c.contains(node4));
		assertFalse(c.contains(node5));
		c = (Collection<AotNode>) node1.traversal(2,Direction.IN);
		show("testTraversalInt",c.toString());
		assertTrue(c.contains(node1));
		assertFalse(c.contains(node2));
		assertFalse(c.contains(node3));
		assertFalse(c.contains(node4));
		assertFalse(c.contains(node5));
	}

	@Test
	void testGetEdges() {
//		show("testGetEdges",node3.getEdges().toString());
		List<Edge> l = new ArrayList<Edge>();
		for (Edge e:node3.getEdges())
			l.add(e);
		assertTrue(l.contains(e3));
		assertFalse(l.contains(e5));
		assertTrue(l.contains(e2));
	}

	@Test
	void testGetEdgesDirection() {
		List<Edge> l = new ArrayList<Edge>();
		for (Edge e:node3.getEdges(Direction.IN))
			l.add(e);
//		show("testGetEdgesDirection",l.toString());
		assertFalse(l.contains(e3));
		assertFalse(l.contains(e5));
		assertTrue(l.contains(e2));
	}

	@Test
	void testIsLeaf() {
		assertTrue(node2.isLeaf());
		assertFalse(node1.isLeaf());
		assertFalse(node3.isLeaf());
	}

	@Test
	void testIsRoot() {
		assertTrue(node1.isRoot());
		assertTrue(node5.isRoot());
		assertFalse(node2.isRoot());
	}

	@Test
	void testRemoveEdge() {
		show("testRemoveEdge",node3.toDetailedString());
		node3.removeEdge(e3);
		show("testRemoveEdge",node3.toDetailedString());
	}

	@Test
	void testnodeFactory() {
		show("testnodeFactory",node2.nodeFactory().toString());
		assertFalse(node.nodeFactory().equals(node1.nodeFactory()));
	}

	@Test
	void testInitialise() {
		node.initialise();
	}

}
