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
package au.edu.anu.rscs.aot.archetype;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.io.FileImporter;

class ArchetypesTest {

	@Test
	void testArchetypes() {
		Archetypes arch = new Archetypes();
		System.out.println(arch.toString());
		assertNotNull(arch);		
	}

	@SuppressWarnings("unchecked")
	@Test
	void testCheckArchetype() {
		Archetypes arch = new Archetypes();
		String archetypefile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "src" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "ArchetypeArchetype.ugt";
		File file = new File(archetypefile);
		FileImporter fi = new FileImporter(file);
		Tree<? extends TreeNode> graph  = (Tree<? extends TreeNode>) fi.getGraph();
		arch.checkArchetype(graph);
		String indent = "";
		printTree(graph.root(),indent);
		Iterable<CheckMessage> errors = arch.errorList();
		if (errors!=null) {
			System.out.println("There were errors in specifications: ");
			for (CheckMessage m:errors)
				System.out.println(m.toString()+"\n");
		}
		else 
			System.out.println("Specifications checked with no error.");
		assertNull(errors);
	}
	
	private void printTree(TreeNode parent,String indent) {
		if (parent.getParent()!=null)
			System.out.println(indent+parent.getParent().id()+"->"+parent.classId()+":"+parent.id());
		else
			System.out.println(indent+parent.classId()+":"+parent.id());
		for (TreeNode child:parent.getChildren())
			printTree(child,indent+"  ");		
	}
}
