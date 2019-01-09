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
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;
import fr.cnrs.iees.tree.TreeNode;

class AotNodeTest {
	
	private AotNode node = null;
	
	@BeforeEach
	private void init() {
		AotGraph graph = new AotGraph();
		node = new AotNode(graph);
	}
	
	private void show(String method,String text) {
		System.out.println(method+": "+text);
	}

	@Test
	void testClassId() {
		assertEquals(node.classId(),"AOTNode");
		node.setLabel("bidon");
		assertEquals(node.classId(),"bidon");
	}

	@Test
	void testInstanceId() {
		assertNull(node.instanceId());
		node.setName("bidon");
		assertEquals(node.instanceId(),"bidon");
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
		node.setName("bidon");
//		show("testAddPropertiesListOfString",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT x=1 y=2 z=3.0]");
	}

	@Test
	void testAddPropertiesStringArray() {
		node.addProperties("x","y","z");
		node.setProperty("x", 1);
		node.setProperty("y", 2);
		node.setProperty("z", 3.0);
		node.setName("bidon");
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
		node.setName("bidon");
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT a=1 b=2 c=3]");
	}

	@Test
	void testAddPropertyProperty() {
		Property p = new Property("a",1);
		node.addProperty(p);
		node.setName("bidon");
//		show("testAddPropertyProperty",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT a=1]");
	}

	@Test
	void testAddPropertyString() {
		node.addProperty("a");
		node.setProperty("a",2);
		node.setName("bidon");
		assertEquals(node.toDetailedString(),"AOTNode:bidon=[ROOT a=2]");
	}

	@Test
	void testAddPropertyStringObject() {
		node.addProperty("a",3);
		node.setName("bidon");
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
		assertEquals(node.toDetailedString(),"AOTNode:=[ROOT]");
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
		assertEquals(node.toDetailedString(),"AOTNode:=[ROOT x=1 z=3.0]");
	}

	@Test
	void testClone() {
		node.addProperties("x","y","z");
		node.setProperty("x", 1);
		node.setProperty("y", 2);
		node.setProperty("z", 3.0);
		node.setName("coucou");
		node.setLabel("zoziau");
		show("testClone",node.toDetailedString());
		AotNode n = node.clone();
		show("testClone",n.toDetailedString());
		assertEquals(node,n);
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
		AotNode n = node.graphElementFactory().makeNode();
		node.addChild(n);
		assertTrue(node.hasChild(n));
		assertFalse(n.getParent()==node);
	}

	@Test
	void testGetChildren() {
		AotNode n = node.graphElementFactory().makeNode();
		n.setName("1");		
		AotNode n2 = node.graphElementFactory().makeNode();
		n2.setName("2");
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
		AotNode n = node.graphElementFactory().makeNode();
		node.setParent(n);
		show("testGetParent",node.toDetailedString());
		assertTrue(node.getParent()==n);
		assertFalse(n.hasChild(node));
	}

	@Test
	void testHasChildren() {
		assertFalse(node.hasChildren());
		AotNode n = node.graphElementFactory().makeNode();
		node.addChild(n);
		assertTrue(node.hasChildren());
	}

	@Test
	void testSetChildrenTreeNodeArray() {
		AotNode n = node.graphElementFactory().makeNode();
		n.setName("1");		
		AotNode n2 = node.graphElementFactory().makeNode();
		n2.setName("2");
		assertFalse(node.hasChildren());
		node.setChildren(n,n2);
		assertTrue(node.hasChildren());
	}

	@Test
	void testSetChildrenIterableOfTreeNode() {
		AotNode n = node.graphElementFactory().makeNode();
		n.setName("1");		
		AotNode n2 = node.graphElementFactory().makeNode();
		n2.setName("2");
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
		AotNode n = node.graphElementFactory().makeNode();
		n.setName("1");		
		AotNode n2 = node.graphElementFactory().makeNode();
		n2.setName("2");
		List<TreeNode> l = new LinkedList<>();
		l.add(n);
		l.add(n2);
		assertFalse(node.hasChildren());
		node.setChildren(l);
		assertTrue(node.hasChildren());
	}

	@Test
	void testSetParent() {
		AotNode n = node.graphElementFactory().makeNode();
		node.setParent(n);
		assertTrue(n==node.getParent());
		assertFalse(n.hasChild(node));
	}

	@Test
	void testTreeNodeFactory() {
		show("testTreeNodeFactory",node.treeNodeFactory().toString());
		assertNotNull(node.treeNodeFactory());
	}

	@Test
	void testDisconnect() {
		AotNode n = node.graphElementFactory().makeNode();
		n.setName("1");		
		AotNode n2 = node.graphElementFactory().makeNode();
		n2.setName("2");
		node.graphElementFactory().makeEdge(node, n);
		node.graphElementFactory().makeEdge(n2, node);
		node.graphElementFactory().makeEdge(node, node);
		node.setParent(n);
		node.addChild(n2);
		show("testDisconnect",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:=[↑AOTNode:1 ↓AOTNode:2 ←AOTNode:2 ←AOTNode: →AOTNode:1 →AOTNode:]");
		node.disconnect();
		show("testDisconnect",node.toDetailedString());
		assertEquals(node.toDetailedString(),"AOTNode:=[↑AOTNode:1 ↓AOTNode:2]");
	}

	@Test
	void testTraversalInt() {
		fail("Not yet implemented");
	}

	@Test
	void testTraversalIntDirection() {
		fail("Not yet implemented");
	}

	@Test
	void testAddEdgeEdge() {
		fail("Not yet implemented");
	}

	@Test
	void testAddEdgeEdgeDirection() {
		fail("Not yet implemented");
	}

	@Test
	void testDegree() {
		fail("Not yet implemented");
	}

	@Test
	void testGetEdges() {
		fail("Not yet implemented");
	}

	@Test
	void testGetEdgesDirection() {
		fail("Not yet implemented");
	}

	@Test
	void testIsLeaf() {
		fail("Not yet implemented");
	}

	@Test
	void testIsRoot() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveEdge() {
		fail("Not yet implemented");
	}

	@Test
	void testGraphElementFactory() {
		fail("Not yet implemented");
	}

	@Test
	void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	void testSetName() {
		fail("Not yet implemented");
	}

	@Test
	void testSameName() {
		fail("Not yet implemented");
	}

	@Test
	void testHasName() {
		fail("Not yet implemented");
	}

	@Test
	void testGetLabel() {
		fail("Not yet implemented");
	}

	@Test
	void testSetLabel() {
		fail("Not yet implemented");
	}

	@Test
	void testSameLabel() {
		fail("Not yet implemented");
	}

	@Test
	void testHasLabel() {
		fail("Not yet implemented");
	}

	@Test
	void testInitialise() {
		fail("Not yet implemented");
	}

}
