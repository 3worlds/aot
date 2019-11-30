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

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.errorMessaging.ErrorMessagable;
import au.edu.anu.rscs.aot.graph.property.Property;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Element;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.ens.biologie.generic.utils.Duple;
import static au.edu.anu.rscs.aot.queries.Query.*;

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

	private void buildDescriptions() {
		String cat = errorType.category();
		switch (errorType) {
		case QUERY_PROPERTY_CLASS_UNKNOWN: {
			/*-	queryNode, property));
			log.severe("Cannot get class for archetype check property" + queryNode);
			e.printStackTrace();
			*/
			TreeNode specs = (TreeNode) args[0];
			Property property = (Property) args[1];
			verbose1 = category() + " Cannot find property value '" + property.getKey() + "' in '" + getRef(specs)
					+ "'.";
			verbose2 = category() + errorName() + " Cannot find property value '" + property.getKey() + "' in '" + specs
					+ "'.";
			break;
		}
		case ELEMENT_MISSING_PROPERTY_LIST: {
			/*-	properties specified but object has no property list
				"Element '" + element + "' has no property list");
				element, propertyArchetype));
			  */
			Element element = (Element) args[0];
			Node specs = (Node) args[1];
			verbose1 = category() + "No property list found for '" + getRef(element) + "'.";
			verbose2 = category() + errorName() + "No property list found for '" + element + "'.\nSpecification=["
					+ specs + "].";
			break;
		}

		case NODE_MISSING_SPECIFICATION: {
			/*
			 * "Expected all nodes to comply (got " + (treeToCheck.nNodes() - complyCount) +
			 * " nodes which didn't comply)";
			 */
			Tree<? extends TreeNode> treeToCheck = (Tree<? extends TreeNode>) args[0];
			int complyCount = (int) args[1];
			// The archetype does not really do its job here. More testing required.
			List<TreeNode> compliantNodes = (List<TreeNode>) args[2];
			List<TreeNode> nonCompliantNodes = new ArrayList<>();
			for (TreeNode node : treeToCheck.nodes())
				if (!compliantNodes.contains(node))
					nonCompliantNodes.add(node);
			verbose1 = category() + "Expected all nodes to have matching specifications. Found "
					+ (treeToCheck.nNodes() - complyCount) + " which didn't comply.";
			verbose2 = category() + errorName() + "Expected all nodes to have matching specifications. Found "
					+ (treeToCheck.nNodes() - complyCount) + " which didn't comply:\n";
			for (TreeNode node : nonCompliantNodes)
				verbose2 += getRef(node) + ",\n";
			break;
		}
		case PROPERTY_MISSING: {
			/*-	"'" + aaToNode + "' property missing for edge specification " + edgeSpec);*/

			Node target = (Node) args[0];
			Node spec = (Node) args[1];
			String key = (String) args[2];
			verbose1 = category() + "Property '" + key + "' is missing from '" + getRef(target) + "'.";
			verbose2 = category() + errorName() + "Property '" + key + "' is missing from " + target
					+ ".\n[Specification: " + spec + "].";
			break;
		}

		case NODE_RANGE_INCORRECT1: {
			/*-	"Expected " + range + " nodes of class '" + requiredClass
			+ "' with parents '" + parentList + "' (got " + count + ") archetype="
			+ hasNode.toUniqueString();
			*/
			Node spec = (Node) args[0];
			String requiredClass = (String) args[1];
			StringTable parentList = (StringTable) args[2];
			IntegerRange range = (IntegerRange) args[3];
			Integer count = (Integer) args[4];
			// could rephrase depending on count being less or greater than range.last
			verbose1 = category() + "Expected " + range + " nodes of class '" + requiredClass + "' with parents '"
					+ parentList + "' but found " + count + ".";
			verbose2 = category() + errorName() + "Expected " + range + " nodes of class '" + requiredClass
					+ "' with parents '" + parentList + "' but found " + count + ".\n[Specification: " + spec + "].";
			break;
		}
		/*-"Expected " + childMult + " nodes of class '" + childClassName
			+ "' with parent '" + n.classId() + "' (got " + children.size()
			+ ") archetype=" + hasNode.toUniqueString();*/
		case NODE_RANGE_INCORRECT2: {
			/*-targetNode, childClassName, childMult,children.size(), hasNode*/
			Node parent = (Node) args[0];
			String childClassName = (String) args[1];
			IntegerRange range = (IntegerRange) args[2];
			Integer nChildren = (Integer) args[3];
			TreeNode spec = (TreeNode) args[4];
			// can rephase for not enough or too many
			verbose1 = category() + "Expected " + range + " child nodes with reference [" + childClassName
					+ ":] from parent '" + getRef(parent) + "' but found " + nChildren + ".";
			verbose2 = category() + errorName() + "Expected " + range + " child nodes with reference [" + childClassName
					+ "] from parent '" + parent + " but found " + nChildren + ".\n[Specification: " + spec + "].";
			break;
		}
		case EDGE_QUERY_UNSATISFIED: {
			/*-item, queryNode, qClassName*/
			String excmsg = exc.getMessage().replaceAll(queryFailedStr, "");
			Duple<String, String> result = getSubArchMsg(excmsg);
			if (result != null) {
				verbose1 = result.getFirst();
				verbose2 = result.getSecond();
			} else {
				Edge edge = (Edge) args[0];
				Node qNode = (Node) args[1];
				String msg = parseQueryMsg(excmsg);
				verbose1 = category() + getRef(edge) + ": " + msg;
				verbose2 = category() + errorName() + edge + ": " + msg + "\n[Specification: " + qNode + "].";
			}
			break;
		}
		case NODE_QUERY_UNSATISFIED: {
			/*-item, queryNode*/
			String excmsg = exc.getMessage().replaceAll(queryFailedStr, "");
			Duple<String, String> result = getSubArchMsg(excmsg);
			if (result != null) {
				verbose1 = result.getFirst();
				verbose2 = result.getSecond();
			} else {
				Node node = (Node) args[0];
				Node qNode = (Node) args[1];
				String msg = parseQueryMsg(excmsg);
				String prompt = getRef(node);
				if (!msg.contains(prompt))
					verbose1 = category() + getRef(node) + " " + msg;
				else
					verbose1 = category() + msg;
				verbose2 = category() + errorName() + node + " " + msg + "\n[Specification: " + qNode + "].";
			}

			break;
		}
		case PROPERTY_QUERY_UNSATISFIED: {
			/*-item, queryNode*/
			String excmsg = exc.getMessage().replaceAll(queryFailedStr, "");
			Duple<String, String> result = getSubArchMsg(excmsg);
			if (result != null) {
				verbose1 = result.getFirst();
				verbose2 = result.getSecond();
			} else {
				Property property = (Property) args[0];
				Node qNode = (Node) args[1];
				String msg = parseQueryMsg(excmsg);
				String prompt = "Property '" + property.getKey() + "=" + property.getValue() + "'";
				if (!msg.contains(prompt))
					verbose1 = category() + "Property '" + property.getKey() + "=" + property.getValue() + "' " + msg;
				else
					verbose1 = category() + msg;
				verbose2 = category() + errorName() + "Property '" + property.getKey() + "=" + property.getValue()
						+ "': " + msg + "\n[Specification: " + qNode + "].";
			}
			break;
		}
		case ITEM_QUERY_UNSATISFIED: {
			/*-item, queryNode*/
			String excmsg = exc.getMessage().replaceAll(queryFailedStr, "");
			Duple<String, String> result = getSubArchMsg(excmsg);
			if (result != null) {
				verbose1 = result.getFirst();
				verbose2 = result.getSecond();
			} else {
				Object item = args[0];
				Node qNode = (Node) args[1];
				String msg = parseQueryMsg(excmsg);
				verbose1 = category() + item + ": " + msg;
				verbose2 = category() + errorName() + item + ": " + msg + "\n[Specification=" + qNode + "].";
			}
			break;
		}
		case EDGE_CLASS_UNKNOWN: {
			/*-ed,edgeSpec, edgeLabel*/
			/* "Class '" + edgeLabel + "' not found for edge " + ed */
			Element target = (Element) args[0];
			Node specs = (Node) args[1];
			String label = (String) args[2];
			verbose1 = category() + "Class '" + label + "' not found for edge " + getRef(target) + ".";
			verbose2 = category() + errorName() + "Class '" + label + "' not found for edge " + getRef(target)
					+ "[Specification=" + specs + "].";
			break;
		}
		case EDGE_CLASS_INCORRECT: {
			/*-
			 * "Edge " + ed + " should be of class [" + edgeLabel+ "]. Class [" + ed.classId() + "] found instead.");
			ed, edgeSpec,edgeLabel));
			 */
			Element target = (Element) args[0];
			Node spec = (Node) args[1];
			String label = (String) args[2];
			verbose1 = category() + "Edge " + getRef(target) + " should be of class [" + label + "]. Class ["
					+ target.classId() + "] found instead.";
			verbose2 = category() + errorName() + "Edge " + target + " should be of class [" + label + "]. Class ["
					+ target.classId() + "] found instead." + "[Specification=" + spec + "].";

			break;
		}
		case EDGE_ID_INCORRECT: {
			/*-
			 * "Edge " + ed + " should have id [" + edgeId + "]. Id ["+ ed.id() + "] found instead.");
				ed, edgeId));
			 */
			Element edge = (Element) args[0];
			String id = (String) args[1];
			verbose1 = category() + "Edge " + getRef(edge) + " should have id [" + id + "]. Id [" + edge.id()
					+ "] found instead.";
			verbose2 = category() + errorName() + "Edge " + edge + " should have id [" + id + "]. Id [" + edge.id()
					+ "] found instead.";

			break;
		}
		case EDGE_RANGE_INCORRECT: {
			/*-
			 "Expected " + nodeToCheck + " to have " + edgeMult + " out edge(s) to nodes that match ["
				+ toNodeRef + "] with label '" + edgeLabel + "' (found " + edgeEnds.size() + ") ");
				
			nodeToCheck,edgeMult, edgeLabel, toNodeRef,edgeEnds.size(),edgeSpec
			 */
			Node nodeToCheck = (Node) args[0];
			IntegerRange edgeMult = (IntegerRange) args[1];
			String edgeLabel = (String) args[2];
			String toNodeRef = (String) args[3];
			Integer edgeEndsSize = (Integer) args[4];
			Node spec = (Node) args[5];
			verbose1 = category() + "Expected " + getRef(nodeToCheck) + " to have " + edgeMult
					+ " out edge(s) to nodes that match '" + toNodeRef + "' with label '" + edgeLabel + "' but found "
					+ edgeEndsSize + "'.";
			verbose2 = category() + errorName() + "Expected " + nodeToCheck + " to have " + edgeMult
					+ " out edge(s) to nodes that match '" + toNodeRef + "' with label '" + edgeLabel + "' but found "
					+ edgeEndsSize + "'." + "[Specification=" + spec + "].";

			break;
		}
//		case code13_PropertyMissing: {
//			// unused
//			break;
//		}
		case PROPERTY_UNKNOWN: {
			/*-	"Unknown property type for property '" + key + "' in element " + element);
			element, propertyArchetype, key
			*/
			Element element = (Element) args[0];
			TreeNode spec = (TreeNode) args[1];
			String key = (String) args[2];
			verbose1 = category() + "Unknown property type for property '" + key + "' in '" + getRef(element) + "'.";
			verbose2 = category() + errorName() + "Unknown property type for property '" + key + "' in '" + element
					+ "'." + "[Specification=" + spec + "].";

			break;
		}
		case PROPERTY_TYPE_INCORRECT: {
			/*-
			 * "Property '" + key + "' in element '" + element + "' is not of the required type '"
										+ typeName + "' (type '" + ptype + "' found instead)");
			
				element, propertyArchetype, key, typeName, ptype;
			 */
			Element element = (Element) args[0];
			Node specs = (Node) args[1];
			String key = (String) args[2];
			String typeName = (String) args[3];
			String ptype = (String) args[4];
			verbose1 = category() + "Property '" + key + "' in '" + getRef(element) + "' is not of the required type '"
					+ typeName + "'. Type '" + ptype + "' found instead.";
			verbose2 = category() + errorName() + "Property '" + key + "' in '" + element
					+ "' is not of the required type '" + typeName + "'. Type '" + ptype
					+ "' found instead.\nSpecification=[" + specs + "].";

			break;
		}
		
		default: {
			System.out.println(errorType);
			throw new AotException("Unrecognized ErrorType in CheckMessage");
		}
		}
	}

	private Duple<String, String> getSubArchMsg(String message) {
		int v1 = message.indexOf("verbose1:");
		int v2 = message.indexOf("verbose2:");
		int e3 = message.indexOf("Exception:");
		if (v1 >= 0 && v2 >= 0 && e3 >= 0) {
			String s1 = message.substring(v1, v2 - 1).replace("verbose1: ", "");
			String s2 = message.substring(v2, e3 - 1).replace("verbose2: ", "");
			return new Duple<String, String>(s1, s2);
		}
		return null;
	}

	private String parseQueryMsg(String msg) {
		if (msg.contains("||")) {
			String[] parts = msg.split("\\|");
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].equals("") && (i + 1) < parts.length) {
					return parts[i + 1];
				}
			}
			return msg;
		}
		return msg;
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
	public String errorName() {
		return "[" + errorType.name() + "] ";
	}

	@Override
	public String category() {
		return "[" + errorType.category() + "] ";
	}

	public SpecificationErrors error() {
		return errorType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("verbose1: ");
		sb.append(verbose1);
		sb.append("\n");
		sb.append("verbose2: ");
		sb.append(verbose2);
		sb.append("\n");
		sb.append("Exception: ");
		sb.append(exc.toString());
		sb.append("\n");
		return sb.toString();
	}

}
