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

import au.edu.anu.rscs.aot.TextTranslations;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.errorMessaging.ErrorMessagable;
import au.edu.anu.rscs.aot.graph.property.Property;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.Element;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;

/**
 * <p>A class to store error messages from archetype checks.</p>
 * 
 * @author designed by Jacques Gignoux - 6 mai 2019
 * @author made useful by Ian Davies - 30 Nov 2019
 */
public class SpecificationErrorMsg implements ErrorMessagable {

	/** the error raised by the check() method */

	private SpecificationErrors errorType;

	private String actionMsg;

	private String constraintMsg;

	private Object[] args;

	private String actionInfo;

	private String detailsInfo;

	private String debugInfo;

	/**
	 * 
	 * @param errorType the type of error
	 * @param actionMsg the action message as produced by {@link au.edu.anu.rscs.aot.TextTranslations TextTranslations} methods
	 * @param constraintMsg the constraint message as produced by {@link au.edu.anu.rscs.aot.TextTranslations TextTranslations} methods
	 * @param args context-specific information on the error
	 */
	public SpecificationErrorMsg(SpecificationErrors errorType, String actionMsg, String constraintMsg,
			Object... args) {
		this.errorType = errorType;
		this.actionMsg = actionMsg;
		this.constraintMsg = constraintMsg;
		this.args = args;
		buildDescriptions();
	}

	/**
	 * Getter for context-specific arguments.
	 * @return
	 */
	public Object[] args() {
		return args;
	}

	/**
	 * Getter for error type.
	 * @return
	 */
	public SpecificationErrors errorType() {
		return errorType;
	}

	private void buildDescriptions() {
		switch (errorType) {
		case TREE_MULTIPLE_ROOTS: {
			@SuppressWarnings("unchecked")
			Tree<? extends TreeNode> treeToCheck = (Tree<? extends TreeNode>) args[0];
			Element constraintSpec = (Element) args[1];

			List<String> refs = new ArrayList<>();
			for (TreeNode node : treeToCheck.roots())
				refs.add(node.toShortString());
			String[] msgs = TextTranslations.getTREE_MULTIPLE_ROOTS(refs, treeToCheck.roots().size());

			actionMsg = msgs[0];
			constraintMsg = msgs[1];

			actionInfo = category() + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nConstraint Specification: " + constraintSpec;
			break;
		}
		case QUERY_PROPERTY_CLASS_UNKNOWN: {
			/*-	queryNode, property));
			log.severe("Cannot get class for archetype check property" + queryNode);
			e.printStackTrace();
			*/
			System.out.println("TODO!: " + errorType);
			Exception e = (Exception) args[0];
			TreeNode constraintSpec = (TreeNode) args[1];
			Property property = (Property) args[2];
			String[] msgs = TextTranslations.getQUERY_PROPERTY_CLASS_UNKNOWN(property.getKey(),constraintSpec.toShortString());
			actionMsg = msgs[0];
			constraintMsg = msgs[1];
			
			actionInfo = category() + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;

			detailsInfo += "\nException message: " + e.getMessage();
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nException: " + e;
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			break;
		}
		case ELEMENT_MISSING_PROPERTY_LIST: {
			/*-	properties specified but object has no property list
				"Element '" + element + "' has no property list");
				element, propertyArchetype));
			  */
			Element element = (Element) args[0];
			Element constraintSpec = (Element) args[1];
			
			String[] msgs = TextTranslations.getELEMENT_MISSING_PROPERTY_LIST(element.toShortString());
			actionMsg = msgs[0];
			constraintMsg = msgs[1];

			actionInfo = category() + element.toShortString() + ": " + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nTarget: " + element.toShortString();
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nTarget: " + element;
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			break;
		}

		case NODE_MISSING_SPECIFICATION: {
			System.out.println("TODO!: " + errorType);
			/*
			 * "Expected all nodes to comply (got " + (treeToCheck.nNodes() - complyCount) +
			 * " nodes which didn't comply)";
			 */
			@SuppressWarnings("unchecked")
			Tree<? extends TreeNode> treeToCheck = (Tree<? extends TreeNode>) args[0];
			int complyCount = (int) args[1];
			// The archetype does not really do its job here. More testing required.
			@SuppressWarnings("unchecked")
			List<TreeNode> compliantNodes = (List<TreeNode>) args[2];
			List<TreeNode> nonCompliantNodes = new ArrayList<>();
			List<String> ncNames = new ArrayList<>();
			for (TreeNode node : treeToCheck.nodes())
				if (!compliantNodes.contains(node)) {
					nonCompliantNodes.add(node);
					ncNames.add(node.toShortString());
				}

			int nMissing = treeToCheck.nNodes() - complyCount;
			String [] msgs = TextTranslations.getNODE_MISSING_SPECIFICATION(nMissing,ncNames);
			actionMsg = msgs[0];
			constraintMsg = msgs[1];

			actionInfo = category() + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;

			debugInfo = detailsInfo;
			debugInfo += "\nCategory class: " + errorType;
			break;
		}
		case PROPERTY_MISSING: {
			/*-	"'" + aaToNode + "' property missing for edge specification " + edgeSpec);*/
			/*- "'" + aaHasName + "' property missing for property specification " + propertyArchetype;
			*/
			Element target = (Element) args[0];
			Element constraintSpec = (Element) args[1];
			String key = (String) args[2];
			
			String[] msgs = TextTranslations.getPROPERTY_MISSING(key,target.toShortString());
			actionMsg = msgs[0];
			constraintMsg = msgs[1];

			actionInfo = category() + target.toShortString() + ": " + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nTarget: " + target.toShortString();
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nTarget: " + target.toDetailedString();
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			break;
		}

		case NODE_RANGE_INCORRECT1: {
			/*-null,null,hasNode, requiredClass,parentList, range, count*/
			Element constraintSpec = (Element) args[0];
			String requiredClass = (String) args[1];
			StringTable parentList = (StringTable) args[2];
			IntegerRange range = (IntegerRange) args[3];
			Integer count = (Integer) args[4];
			
			String[] msgs = TextTranslations.getNODE_RANGE_INCORRECT1(requiredClass, parentList, range, count);

			actionMsg = msgs[0];
			constraintMsg = msgs[1];

			actionInfo = category() + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nCategory: " + category();
			detailsInfo += "\nTarget(?):" + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nTarget(?):" + constraintSpec.toDetailedString();
			debugInfo += "TODO: Check this when circumstance arises!!";

			break;
		}
		case NODE_RANGE_INCORRECT2: {
			Element target = (Element) args[0];
			String childClassName = (String) args[1];
			IntegerRange range = (IntegerRange) args[2];
			Integer nChildren = (Integer) args[3];
			TreeNode constraintSpec = (TreeNode) args[4];
			String[] msg = TextTranslations.getNODE_RANGE_INCORRECT2(childClassName,target,range,nChildren);

			actionMsg = msg[0];
			constraintMsg = msg[1];

			actionInfo = category() + target.toShortString() + ": " + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nTarget: " + target.toShortString();
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nTarget: " + target.toDetailedString();
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			break;
		}
		case EDGE_QUERY_UNSATISFIED: {
			/*-actionMsg, constraintMsg,item,queryNameStr,queryNode*/
			Element edge = (Element) args[0];
			String queryClass = (String) args[1];
			Element constraintSpec = (Element) args[2];
			actionInfo = category() + edge.toShortString() + ": " + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nQuery class: " + queryClass;
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();
			detailsInfo += "\nQuery item: " + edge.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nQuery class: " + queryClass;
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			debugInfo += "\nQuery item: " + edge.toDetailedString();

			break;

		}
		case NODE_QUERY_UNSATISFIED: {
			/*-actionMsg, constraintMsg,item,queryNameStr,queryNode*/
			Element node = (Element) args[0];
			String queryClass = (String) args[1];
			Element constraintSpec = (Element) args[2];

			actionInfo = category() + node.toShortString() + ": " + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nQuery class: " + queryClass;
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();
			detailsInfo += "\nQuery item: " + node.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nQuery class: " + queryClass;
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			debugInfo += "\nQuery item: " + node.toDetailedString();
			break;
		}
		case PROPERTY_QUERY_UNSATISFIED: {
			/*-actionMsg, constraintMsg,item,queryNameStr,queryNode*/
			Property property = (Property) args[0];
			String queryClass = (String) args[1];
			Element constraintSpec = (Element) args[2];

			actionInfo = category() + property.getKey() + ": " + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nQuery class: " + queryClass;
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();
			detailsInfo += "\nQuery item: " + property;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nQuery class: " + queryClass;
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			detailsInfo += "\nQuery item: " + property;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;

			break;
		}
		case ITEM_QUERY_UNSATISFIED: {
			/*-item, queryNameStr,queryNode*/
			Object item = args[0];
			String queryClass = (String) args[1];
			Element constraintSpec = (Element) args[2];

			actionInfo = category() + item + ": " + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nQuery class: " + queryClass;
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();
			detailsInfo += "\nQuery item: " + item;

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nQuery class: " + queryClass;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			debugInfo += "\nQuery item: " + item;

			break;
		}
		case EDGE_CLASS_UNKNOWN: {
			/*-ed,edgeSpec, edgeLabel*/
			/* "Class '" + edgeLabel + "' not found for edge " + ed */
			Element target = (Element) args[0];
			Element constraintSpec = (Element) args[1];
			String label = (String) args[2];
			String factoryName = (String) args[3];
			
			String[] msgs = TextTranslations.getEDGE_CLASS_UNKNOWN(label, target.toShortString(), factoryName);
			actionMsg = msgs[0];
			constraintMsg = msgs[1];
			
			actionInfo = category() + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();
			detailsInfo += "\nQuery item: " + target.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			debugInfo += "\nQuery item: " + target.toDetailedString();

			break;
		}
		case EDGE_CLASS_INCORRECT: {
			/*-
			 * "Edge " + ed + " should be of class [" + edgeLabel+ "]. Class [" + ed.classId() + "] found instead.");
			ed, edgeSpec,edgeLabel));
			 */
			Element target = (Element) args[0];
			Element constraintSpec = (Element) args[1];
			String label = (String) args[2];
			
			String[] msgs = TextTranslations.getEDGE_CLASS_INCORRECT(target.toShortString(), label, target.classId());
			actionMsg = msgs[0];
			constraintMsg = msgs[1];

			actionInfo = category() + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();
			detailsInfo += "\nQuery item: " + target.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			debugInfo += "\nTarget: " + target.toDetailedString();
			break;
		}
		case EDGE_ID_INCORRECT: {
			/*-
			 * "Edge " + ed + " should have id [" + edgeId + "]. Id ["+ ed.id() + "] found instead.");
				ed, edgeId));
			 */
			
			//TODO test and finish actionmsg
			Element edge = (Element) args[0];
			String id = (String) args[1];
			
			String[] msgs = TextTranslations.getEDGE_ID_INCORRECT(edge.toShortString(), id, edge.id());
			actionMsg = msgs[0];
			constraintMsg = msgs[1];
			
			actionInfo = category() +actionMsg;
			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nTarget: " + edge.toDetailedString();
			break;
		}
		case EDGE_RANGE_INCORRECT: {
			/*-nodeToCheck, edgeMult, edgeLabel, toNodeRef, edgeEnds.size(), edgeSpec*/
			Element target = (Element) args[0];
			IntegerRange range = (IntegerRange) args[1];
			String edgeLabel = (String) args[2];
			String toNodeRef = (String) args[3];
			Integer nEdges = (Integer) args[4];
			Element constraintSpec = (Element) args[5];
			
			String[] msgs = TextTranslations.getEDGE_RANGE_INCORRECT(nEdges, range, edgeLabel, target.toShortString(), toNodeRef);

			actionMsg = msgs[0];
			constraintMsg = msgs[1];
			
			actionInfo = category() + target.toShortString() + ": " + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nTarget: " + target.toShortString();
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nTarget: " + target.toDetailedString();
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
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
			Element constraintSpec = (Element) args[1];
			String key = (String) args[2];
			String[] msgs = TextTranslations.getPROPERTY_UNKNOWN(key, element.toShortString());
			actionMsg = msgs[0];		
			constraintMsg =msgs[1];
			
			actionInfo = category() + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nTarget: " + element.toShortString();
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nTarget: " + element.toDetailedString();
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			break;
		}
		case PROPERTY_TYPE_INCORRECT: {
			/*-
			 * "Property '" + key + "' in element '" + element + "' is not of the required type '"
										+ typeName + "' (type '" + ptype + "' found instead)");
			
				element, propertyArchetype, key, typeName, ptype;
			 */
			Element element = (Element) args[0];
			Element constraintSpec = (Element) args[1];
			String key = (String) args[2];
			String requiredType = (String) args[3];
			String foundType = (String) args[4];
			
			String[] msgs = TextTranslations.getPROPERTY_TYPE_INCORRECT(key,foundType,requiredType,element.toShortString());

			actionMsg = msgs[0];
			
			constraintMsg = msgs[1];

			actionInfo = category() + actionMsg;

			detailsInfo = "\nAction: " + actionMsg;
			detailsInfo += "\nConstraint: " + constraintMsg;
			detailsInfo += "\nTarget: " + element.toShortString();
			detailsInfo += "\nConstraint Specification: " + constraintSpec.toShortString();

			debugInfo = "\nAction: " + actionMsg;
			debugInfo += "\nConstraint: " + constraintMsg;
			debugInfo += "\nCategory: " + category();
			debugInfo += "\nCategory class: " + errorType;
			debugInfo += "\nTarget: " + element.toDetailedString();
			debugInfo += "\nConstraint Specification: " + constraintSpec.toDetailedString();
			break;
		}

		default: {
//			System.out.println(errorType);
			throw new IllegalArgumentException("Unrecognized ErrorType in CheckMessage ["+errorType+"]");
		}
		}
	}

	@Override
	public String actionInfo() {
		return actionInfo;
	}

	@Override
	public String detailsInfo() {
		return detailsInfo;
	}

	@Override
	public String debugInfo() {
		return debugInfo;
	}

	@Override
	public String errorName() {
		return "[" + errorType.name() + "] ";
	}

	@Override
	public String category() {
		return "[" + errorType.category() + "] ";
	}

	/**
	 * Another getter for error type. Why?
	 * @return
	 */
	public SpecificationErrors error() {
		return errorType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("actionInfo: ");
		sb.append(actionInfo);
		sb.append("\n");
		sb.append("detailsInfo: ");
		sb.append(detailsInfo);
		sb.append("\n");
		sb.append("debugInfo: ");
		sb.append(debugInfo);
		sb.append("\n");
		return sb.toString();
	}

}
