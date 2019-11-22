package au.edu.anu.rscs.aot.archetype;

import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;

public enum ErrorTypes {
	/*-Tree<? extends TreeNode> treeToCheck, Integer complyCount, List<TreeNode>nonCompliantNoeldes*/
	code2_GraphIsExclusiveButHasNoncompilantNodes/**/("Archetype Error: "), //

	/*-TreeNode nodeToCheck,NodeSpec nodeSpec,EdgeSpec edgeSpec,String key*/
	code5_EdgeSpecsMissing/*                 */("Archetype error: "), //

	/*-Object element,PropertySpec propertyArchetype,String key*/
	code10_PropertyMissingInArchetype/*            */("Archetype Error: "), //
	
	/*-treeToCheck, NodeSpec hasNode, String requiredClass, StringTable parentList, IntegerRange range, Integer count*/
	code1_NodeRangeError/*                         */("Node error: "), //

	/*-TreeNode n, String childClassName, StringTable parentList, IntegerRange childMult,Integer children.size())*/
	code18_ChildrenOfClassRangeError/*             */("Node error: "), //

	/*-TreeNode item, queryNode,ConstraintSpec*/
	code4_QueryEdge/*                              */("Edge query error: "), //
	code4_QueryNode/*                              */("Node query error: "), //
	code4_QueryProperty/*                          */("Property query error: "), //
	code4_QueryItem/*                              */("Item query error: "), //

	/*- EdgeSpec edgeSpec*/
	codex_EdgeFromNodeClassMissing/*               */("Edge error: "),//
	
	/*-Edge ed, EdgeSpec edgeSpec*/
	code6_EdgeClassUnknown/*                       */("Edge error: "), //
	code7_EdgeClassWrong/*                         */("Edge error: "), //
	code8_EdgeIdWrong/*                            */("Edge error: "), //
	code9_EdgeRangeError/*                         */("Edge error: "), //
	code3_PropertyClass/*                          */("Property error: "), //
	code13_PropertyMissing/*                       */("Property error: "), //
	code14_PropertyTypeUnknown/*                   */("Property error: "), //
	code15_PropertyWrongType/*                     */("Property error: "), //
	code16_ElementHasNoPropertyList/*              */("Element error: "), //
	;
	private final String category;

	private ErrorTypes(String category) {
		this.category = category;
	}

	public String category() {
		return category;
	}
}
