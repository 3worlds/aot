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
package au.edu.anu.rscs.aot.graph.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.AotGraph;
import au.edu.anu.rscs.aot.graph.AotNode;
import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.SimplePropertyListImpl;

class AotGraphExporterTest {

	private AotGraph g = new AotGraph();

	// little test graph mimicking a 3w spec:
	@BeforeEach
	private void init() {
		SimplePropertyList pl1 = new SimplePropertyListImpl("x","y","z");
		SimplePropertyList pl2 = new SimplePropertyListImpl("a","b");
		SimplePropertyList pl3 = new SimplePropertyListImpl("i","j","k","l");
		AotNode tw = g.makeTreeNode(null,"3Worlds",null,null);
		AotNode eco = g.makeTreeNode(tw,"ecology","my model",pl2);
		g.makeTreeNode(tw,"experiment","my experiment",null);
		AotNode cat = g.makeTreeNode(eco,"category","animal",pl1);
		AotNode cat2 = g.makeTreeNode(eco,"category","plant",pl1);
		g.makeTreeNode(eco,"engine","my simulator",null);
		AotNode sys = g.makeTreeNode(eco,"system","entity",pl3);
		AotNode proc = g.makeTreeNode(eco,"process","growth",null);
		AotNode cds = g.makeTreeNode(tw,"codeSource",null,null);
		AotNode fu = g.makeTreeNode(cds,"function","some computation",pl2);
		g.makeEdge(proc,cat,"appliesTo",null,null);
		g.makeEdge(proc,cat2,"appliesTo",null,null);
		g.makeEdge(sys,cat,"belongsTo","random name",null);
		g.makeEdge(proc,fu,"function",null,null);		
		g.root();
		// add a stupid node
		g.makeTreeNode(cds);
		// add a duplicate node - this should issue a warning and not insert the node
		g.makeTreeNode(tw,"experiment","my experiment",null);
	}
	
	@Test
	void testExportGraph() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon.aot";		
		File f = new File(testfile);
		try {
			Files.deleteIfExists(f.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GraphExporter age = new AotGraphExporter(f);
		age.exportGraph(g);
		assertTrue(f.exists());
	}

/*	This is how bidon.aot should look like 
 * (notice that nodes and edges are saved in random order): 

aot // saved by AotGraphExporter on Mon Jan 14 13:37:23 CET 2019

// TREE
3Worlds 
	ecology my model
		a = java.lang.Object(null)
		b = java.lang.Object(null)
		category animal
			x = java.lang.Object(null)
			y = java.lang.Object(null)
			z = java.lang.Object(null)
		system entity
			i = java.lang.Object(null)
			j = java.lang.Object(null)
			k = java.lang.Object(null)
			l = java.lang.Object(null)
		category plant
			x = java.lang.Object(null)
			y = java.lang.Object(null)
			z = java.lang.Object(null)
		engine my simulator
		process growth
	codeSource 
		function some computation
			a = java.lang.Object(null)
			b = java.lang.Object(null)
		AOTNode D89EF3043496-000001684C5DD17F-0000
	experiment my experiment

// CROSS-LINKS
[system:entity] belongsTo random name [category:animal]
[process:growth] appliesTo  [category:animal]
[process:growth] appliesTo  [category:plant]
[process:growth] function  [function:some computation]

*/
}
