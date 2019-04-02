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

import au.edu.anu.rscs.aot.AotException;
import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.graph.DataTreeNode;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.identity.impl.LocalScope;
import fr.cnrs.iees.properties.SimplePropertyList;

@SuppressWarnings("unchecked")
public class ArchetypeFactory implements TreeNodeFactory {
	
	private static Map<String,Class<? extends TreeNode>> labelMappings = new HashMap<>();
	private IdentityScope scope;

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
			labelMappings.put("archetype", (Class<? extends TreeNode>) 
				Class.forName("au.edu.anu.rscs.aot.archetype.ArchetypeRootSpec",false,cl)); // same label two classes!!
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public ArchetypeFactory() {
		super();
		scope = new LocalScope("AOT-archetype");
	}
	
	@Override
	public TreeNode makeTreeNode(TreeNode parent, String proposedId, SimplePropertyList properties) {
		throw new AotException("Unknown node type for archetype factory");
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent) {
		return makeTreeNode(treeNodeClass,parent,null,null);
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent,
			SimplePropertyList properties) {
		return makeTreeNode(treeNodeClass,parent,null,properties);
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, TreeNode parent, String proposedId) {
		return makeTreeNode(treeNodeClass,parent,proposedId,null);
	}

	@Override
	public TreeNode makeTreeNode(Class<? extends TreeNode> treeNodeClass, 
			TreeNode parent, 
			String proposedId,
			SimplePropertyList properties) {
		DataTreeNode result = null;
		if (NodeSpec.class.isAssignableFrom(treeNodeClass))
			result = new NodeSpec(scope.newId(proposedId),properties,this);
		else if (EdgeSpec.class.isAssignableFrom(treeNodeClass))
			result = new EdgeSpec(scope.newId(proposedId),properties,this);
		else if (ConstraintSpec.class.isAssignableFrom(treeNodeClass))
			result = new ConstraintSpec(scope.newId(proposedId),properties,this);
		else if (PropertySpec.class.isAssignableFrom(treeNodeClass))
			result = new PropertySpec(scope.newId(proposedId),properties,this);
		else if (ArchetypeRootSpec.class.isAssignableFrom(treeNodeClass))
			result = new ArchetypeRootSpec(scope.newId(proposedId),properties,this);
		if (parent!=null) {
			result.setParent(parent);
			parent.addChild(result);
		}
		return result;
	}

	public Class<? extends TreeNode> treeNodeClass(String label) {
		return labelMappings.get(label);
	}

	
}
