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

import java.util.HashMap;
import java.util.Map;

import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.properties.SimplePropertyList;

@SuppressWarnings("unchecked")
public class ArchetypeFactory implements TreeNodeFactory {
	
	private static Map<String,Class<? extends TreeNode>> labelMappings = new HashMap<>();

	static {
		try {
			ClassLoader cl = OmugiClassLoader.getClassLoader();
			labelMappings.put("hasNode", (Class<? extends TreeNode>) 
				Class.forName("au.edu.anu.rscs.aot.archetype.NodeSpec",false,cl));
			labelMappings.put("hasProperty", (Class<? extends TreeNode>) 
				Class.forName("au.edu.anu.rscs.aot.archetype.PropertySpec",false,cl));
			labelMappings.put("hasEdge", (Class<? extends TreeNode>) 
				Class.forName("au.edu.anu.rscs.aot.archetype.EdgeSpec",false,cl));
			labelMappings.put("mustSatisfyQuery", (Class<? extends TreeNode>) 
				Class.forName("au.edu.anu.rscs.aot.archetype.ConstraintSpec",false,cl));
			labelMappings.put("hasEdge", (Class<? extends TreeNode>) 
				Class.forName("au.edu.anu.rscs.aot.archetype.ArchetypeRootSpec",false,cl));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public TreeNode makeTreeNode(TreeNode parent, String proposedId, SimplePropertyList properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent,
			SimplePropertyList properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent, String proposedId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent, String proposedId,
			SimplePropertyList properties) {
		// TODO Auto-generated method stub
		return null;
	}

	public Class<? extends TreeNode> treeNodeClass(String label) {
		return labelMappings.get(label);
	}

	
}
