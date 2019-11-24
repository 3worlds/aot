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

import java.util.List;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.archetype.EdgeSpec;
import au.edu.anu.rscs.aot.archetype.NodeSpec;
import au.edu.anu.rscs.aot.archetype.PropertySpec;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.errorMessaging.ErrorMessagable;
import au.edu.anu.rscs.aot.graph.property.Property;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Element;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.impl.SimpleDataTreeNode;

/**
 * A class to store error messages from archetype checks.
 * 
 * A class to store all available data for message text construction. Generally
 * messages are either brief or verbose. Brief means the elements noted as
 * classId:id pairs. Verbose is "Brief" plus the detailed String of Elements.
 * 
 * NOTE: Ian, feel free to adapt this class to your needs by putting in any
 * useful information for user feedback. These messages are created by
 * Archetypes.check(...) methods.
 * 
 * Ok
 * 
 * @author Jacques Gignoux - 6 mai 2019
 *
 */
public class SpecificationErrorMsg implements ErrorMessagable {

	/** the error raised by the check() method */
	private Exception exc;

	private SpecificationErrors errorType;

	private Object[] args;

	private String verbose1;

	private String verbose2;

	/**
	 * @param errorType
	 * @param exception
	 * @param args
	 */
	public SpecificationErrorMsg(SpecificationErrors errorType, Exception exception, Object... args) {
		this.errorType = errorType;
		this.exc = exception;
		this.args = args;
		buildDescriptions();
	}

	public Object[] args() {
		return args;
	}

	public SpecificationErrors errorType() {
		return errorType;
	}

	private static String getRef(Element e) {
		return e.classId() + ":" + e.id();
	}

	private String errorCodeString() {
		return "[Error Code = " + code() + "]";
	}

	private void buildDescriptions() {
		String cat = errorType.category();
		switch (errorType) {
		case code2_GraphIsExclusiveButHasNoncompilantNodes: {
			/*
			 * "Expected all nodes to comply (got " + (treeToCheck.nNodes() - complyCount) +
			 * " nodes which didn't comply)";
			 */
			System.out.println(errorType);
			String classOfTree = args[0].getClass().getSimpleName();
			int complyCount = (int) args[1];
			List<TreeNode> nonCompliantNodes = (List<TreeNode>) args[2];
			verbose1 = cat + "Node(s) found with no matching specification(s).";
			verbose2 = verbose1 + "\n[";
			for (TreeNode node : nonCompliantNodes)
				verbose2 += node.classId() + ":" + node.id() + ",";
			verbose2 += "]";
			verbose2 = verbose2.replace(",]", "]" + errorCodeString());
			break;
		}
		case code5_EdgeSpecsMissing: {
			/*-	"'" + aaToNode + "' property missing for edge specification " + edgeSpec);*/
			System.out.println(errorType);
			TreeNode nodeToCheck = (TreeNode) args[0];
			NodeSpec nodeSpec = (NodeSpec) args[1];
			EdgeSpec edgeSpec = (EdgeSpec) args[2];
			String key = (String) args[3];
			verbose1 = cat + "Property '" + key + "' is missing from the specification when checking '"
					+ nodeToCheck.toUniqueString() + "' against '" + edgeSpec.toUniqueString() + code();
			verbose2 = cat + "Property '" + key + "' is missing from the specification when checking '" + nodeToCheck
					+ "' against '" + edgeSpec + errorCodeString();
			break;
		}
		/*-"'" + aaHasName + "' property missing for property specification " + propertyArchetype);*/
		case code10_PropertyMissingInArchetype: {
			System.out.println(errorType);
			Object element = args[0];
			PropertySpec spec = (PropertySpec) args[1];
			String key = (String) args[2];
			verbose1 = cat + "Property '" + key + "' is missing from the specification '" + spec.toUniqueString()
					+ "'.";
			if (element instanceof Element) {
				Element el = (Element) element;
				verbose2 = cat + "Property '" + key + "' is missing from the specification when checking '" + getRef(el)
						+ "' against '" + getRef(spec.getParent()) + "'→'" + getRef(spec);
			} else
				verbose2 = cat + "Property '" + key + "' is missing from the specification [" + spec + "].";
			break;
		}
		/*-	"Expected " + range + " nodes of class '" + requiredClass
									+ "' with parents '" + parentList + "' (got " + count + ") archetype="
									+ hasNode.toUniqueString();
		*/
		case code1_NodeRangeError: {
			System.out.println(errorType);
			Tree<? extends TreeNode> treeToCheck = (Tree<? extends TreeNode>) args[0];
			NodeSpec nodeSpec = (NodeSpec) args[1];
			String requiredClass = (String) args[2];
			StringTable parents = (StringTable) args[3];
			IntegerRange range = (IntegerRange) args[4];
			Integer count = (Integer) args[5];
			TreeNode specs = (TreeNode) args[6];
			/*
			 * Suppress this message if no nodes exist in treeToCheck that are contained in
			 * the parents table.
			 */

			if (!findClass(treeToCheck, parents))
				break;
			// can rephrase depending on count being less or greater than range.last
			verbose1 = cat + "Unexpected child. Expected " + range + " nodes of class '" + requiredClass
					+ "' with parents '" + parents + " but found " + count;
			verbose2 = verbose1 + " Specifications [" + specs + "]";

			if (count < range.getLast())
				verbose1 = cat + "Missing child of class '" + requiredClass + "'.";
			break;
		}
		/*-"Expected " + childMult + " nodes of class '" + childClassName
			+ "' with parent '" + n.classId() + "' (got " + children.size()
			+ ") archetype=" + hasNode.toUniqueString();*/
		case code18_ChildrenOfClassRangeError: {
			System.out.println(errorType);
			TreeNode targetNode = (TreeNode) args[0];
			String childClassName = (String) args[1];
			StringTable parents = (StringTable) args[2];
			IntegerRange range = (IntegerRange) args[3];
			Integer nChildren = (Integer) args[4];
			TreeNode specs = (TreeNode) args[5];
			verbose2 = cat + "Expected " + range + " nodes of class '" + childClassName + "' with parent '"
					+ targetNode.classId() + "' but found " + nChildren + " for specification [" + specs + "]";

			if (nChildren < range.getLast()) {
				verbose1 = cat + "'" + getRef(targetNode) + "' requires child of class '" + childClassName + "'";
			}
			verbose1 = cat + "Unexpected child. Expected " + range + " nodes of class '" + childClassName
					+ "' with parent '" + targetNode.classId() + "' but found " + nChildren;
			break;
		}
		case code4_QueryEdge: {
			/* Should also mention the query class simpleName for debugging */
			/* we need secret codes in query msgs for parsing */
			/* String s= "Sea of shit ||Nice little message|| in an ocean of shit"; */
			// On the otherhand we could have brief and verbose msgs for each query.
			System.out.println(errorType);
			break;
		}
		case code4_QueryNode: {
			System.out.println(errorType);
			break;
		}
		case code4_QueryProperty: {
			System.out.println(errorType);
			break;
		}
		case code4_QueryItem: {
			System.out.println(errorType);
			break;
		}
		case codex_EdgeFromNodeClassMissing: {
			System.out.println(errorType);
			SimpleDataTreeNode spec = (SimpleDataTreeNode) args[0];
			SimpleDataTreeNode nodeSpec = (SimpleDataTreeNode) spec.getParent();
			String key = (String) args[1];
			break;
		}
		case code6_EdgeClassUnknown: {
			/* "Class '" + edgeLabel + "' not found for edge " + ed */
			System.out.println(errorType);
			Edge edge = (Edge) args[0];
			SimpleDataTreeNode edgeSpec = (SimpleDataTreeNode) args[1];
			break;
		}
		case code7_EdgeClassWrong: {
			/*
			 * "Edge " + ed + " should be of class [" + edgeLabel+ "]. Class [" +
			 * ed.classId() + "] found instead.");
			 */
			System.out.println(errorType);
			Edge edge = (Edge) args[0];
			SimpleDataTreeNode edgeSpec = (SimpleDataTreeNode) args[1];
			String edgeId = (String) args[2];

			break;
		}
		case code8_EdgeIdWrong: {
			/*
			 * "Edge " + ed + " should have id [" + edgeId + "]. Id ["+ ed.id() +
			 * "] found instead.");
			 */
			System.out.println(errorType);
			Edge edge = (Edge) args[0];
			SimpleDataTreeNode edgeSpec = (SimpleDataTreeNode) args[1];

			break;
		}
		case code9_EdgeRangeError: {
			/*
			 * "Expected " + nodeToCheck + " to have " + edgeMult +
			 * " out edge(s) to nodes that match [" + toNodeRef + "] with label '" +
			 * edgeLabel + "' (found " + edgeEnds.size() + ") ");
			 */
			System.out.println(errorType);
			Node nodetoCheck = (Node) args[0];
			TreeNode edgeSpec = (TreeNode) args[1];
			IntegerRange edgeMult = (IntegerRange) args[2];

			break;
		}
		case code3_PropertyClass: {
			System.out.println(errorType);
			TreeNode queryContraint = (TreeNode) args[0];
			Property property = (Property) args[1];
			// TODO the class required to parameterise a query is unknown.
			break;
		}
		case code13_PropertyMissing: {
			// unused
			break;
		}
		case code14_PropertyTypeUnknown: {
			/* "Unknown property type for property '" + key + "' in element " + element); */

			System.out.println(errorType);
			Element element = (Element) args[0];
			TreeNode propertySpec = (TreeNode) args[1];
			String key = (String) args[2];

			break;
		}
		case code15_PropertyWrongType: {
			/*
			 * "Property '" + key + "' in element '" + element +
			 * "' is not of the required type '"+ typeName + "' (type '" + ptype +
			 * "' found instead)");
			 */
			System.out.println(errorType);
			Element element = (Element) args[0];
			TreeNode propertySpecs = (TreeNode) args[1];
			String key = (String) args[2];
			String typeName = (String) args[3];
			String ptype = (String) args[4];

			break;
		}
		case code16_ElementHasNoPropertyList: {
			/* "Element '" + element + "' has no property list" */
			System.out.println(errorType);
			Element element = (Element) args[0];
			TreeNode propertySpecs = (TreeNode) args[1];
			break;
		}
		default: {
			System.out.println(errorType);
			throw new AotException("Unrecognized ErrorType in CheckMessage");
		}
		}
	}

	private boolean findClass(Tree<? extends TreeNode> tree, StringTable parents) {
		for (Node node : tree.nodes())
			if (parents.contains(node.classId()))
				return true;
		return false;
	}

	@Override
	public String verbose1() {
		return verbose1;
	}

	@Override
	public String verbose2() {
		return verbose2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("short: ");
		sb.append(verbose1);
		sb.append("\n");
		sb.append("long: ");
		sb.append(verbose2);
		sb.append("\n");
		sb.append("Exception: ");
		sb.append(exc.toString());
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public String code() {
		return errorType.name();
	}

	@Override
	public String category() {
		return errorType.category();
	}
}
