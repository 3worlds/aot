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

import java.io.File;

import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.io.FileImporter;

/**
 * Just a test class for the moment -
 * 
 * @author Jacques Gignoux - 6 mars 2019
 *
 */
public class Archetypes {
	
	private Tree<? extends TreeNode> archetypeArchetype;
	
	@SuppressWarnings("unchecked")
	public Archetypes() {
		super();
		String archetypefile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "src" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "ArchetypeArchetype.ugt";
		File file = new File(archetypefile);
		FileImporter fi = new FileImporter(file);
		archetypeArchetype  = (Tree<? extends TreeNode>) fi.getGraph();
		// doesn't work
//		TreeNode n = archetypeArchetype.findNodeByReference("ArchetypeRootSpec:hasRootNode");
//		TreeNode n = archetypeArchetype.findNodeByReference("hasRootNode");
//		if (n!=null)
//			System.out.println(n.toDetailedString());
		// you can't know what the id is because its fabricated by the system: hasProperty7 etc
		for (TreeNode tn:archetypeArchetype.nodes())
			System.out.println(tn.id());
//			System.out.println(tn.toDetailedString());
	}
	
	public void checkArchetype(Tree<? extends TreeNode> archetype) {
		
	}

}
