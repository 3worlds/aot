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

package au.edu.anu.rscs.aot.errorMessaging.impl;

import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;

public enum SpecificationErrors {
	/*-Tree<? extends TreeNode> treeToCheck, Integer complyCount, List<TreeNode>nonCompliantNoeldes*/
	code2_GraphIsExclusiveButHasNoncompilantNodes/**/("Archetype: "), //

	/*-TreeNode nodeToCheck,SimpleDataTreeNode nodeSpec,SimpleDataTreeNode edgeSpec,String key*/
	code5_EdgeSpecsMissing/*                       */("Archetype: "), //

	/*-Object element,PropertySpec propertyArchetype,String key*/
	code10_PropertyMissingInArchetype/*            */("Archetype: "), //
	
	/*-treeToCheck, NodeSpec hasNode, String requiredClass, StringTable parentList, IntegerRange range, Integer count*/
	code1_NodeRangeError/*                         */("Node: "), //

	/*-TreeNode n, String childClassName, StringTable parentList, IntegerRange childMult,Integer children.size())*/
	code18_ChildrenOfClassRangeError/*             */("Node: "), //

	/*-TreeNode item, queryNode,ConstraintSpec*/
	code4_QueryEdge/*   TODO                       */("Edge: "), //
	code4_QueryNode/*                              */("Node: "), //
	code4_QueryProperty/*                          */("Property: "), //
	code4_QueryItem/*                              */("Item: "), //

	/*- EdgeSpec edgeSpec,String key*/
	codex_EdgeFromNodeClassMissing/*               */("Archetype: "),//
	
	/*-Edge ed, EdgeSpec edgeSpec, String edgeLabel*/
	code6_EdgeClassUnknown/*                       */("Edge: "), //
	code7_EdgeClassWrong/*                         */("Edge: "), //
	code8_EdgeIdWrong/*                            */("Edge: "), //
	code9_EdgeRangeError/*                         */("Edge: "), //
	code3_PropertyClass/*                          */("Property_Query?: "), // 
	code13_PropertyMissing/*                       */("Property: "), // not used
	code14_PropertyTypeUnknown/*                   */("Property: "), //
	code15_PropertyWrongType/*                     */("Property: "), //
	code16_ElementHasNoPropertyList/*              */("Element: "), //
	;
	private final String category;

	private SpecificationErrors(String category) {
		this.category = category;
	}

	public String category() {
		return category;
	}
}
