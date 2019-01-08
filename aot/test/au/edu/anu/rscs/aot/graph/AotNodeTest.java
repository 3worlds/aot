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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		show("testAddPropertiesListOfString",node.toUniqueString());
	}

	@Test
	void testAddPropertiesStringArray() {
		node.addProperties("x","y","z");
		show("testAddPropertiesStringArray",node.toUniqueString());
	}

	@Test
	void testAddPropertiesReadOnlyPropertyList() {
		fail("Not yet implemented");
	}

	@Test
	void testAddPropertyProperty() {
		fail("Not yet implemented");
	}

	@Test
	void testAddPropertyString() {
		fail("Not yet implemented");
	}

	@Test
	void testAddPropertyStringObject() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPropertyValueStringObject() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveAllProperties() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveProperty() {
		fail("Not yet implemented");
	}

	@Test
	void testClone() {
		fail("Not yet implemented");
	}

	@Test
	void testSetProperty() {
		fail("Not yet implemented");
	}

	@Test
	void testGetKeysAsSet() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPropertyValueString() {
		fail("Not yet implemented");
	}

	@Test
	void testHasProperty() {
		fail("Not yet implemented");
	}

	@Test
	void testSize() {
		fail("Not yet implemented");
	}

	@Test
	void testSeal() {
		fail("Not yet implemented");
	}

	@Test
	void testIsSealed() {
		fail("Not yet implemented");
	}

	@Test
	void testAddChild() {
		fail("Not yet implemented");
	}

	@Test
	void testGetChildren() {
		fail("Not yet implemented");
	}

	@Test
	void testGetParent() {
		fail("Not yet implemented");
	}

	@Test
	void testHasChildren() {
		fail("Not yet implemented");
	}

	@Test
	void testSetChildrenTreeNodeArray() {
		fail("Not yet implemented");
	}

	@Test
	void testSetChildrenIterableOfTreeNode() {
		fail("Not yet implemented");
	}

	@Test
	void testSetChildrenCollectionOfTreeNode() {
		fail("Not yet implemented");
	}

	@Test
	void testSetParent() {
		fail("Not yet implemented");
	}

	@Test
	void testTreeNodeFactory() {
		fail("Not yet implemented");
	}

	@Test
	void testDisconnect() {
		fail("Not yet implemented");
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
