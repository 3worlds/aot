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

import java.util.List;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.Element;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;

/**
 * A class to store error messages from archetype checks.
 * 
 * NOTE: Ian, feel free to adapt this class to your needs by putting in any
 * useful information for user feedback. These messages are created by
 * Archetypes.check(...) methods.
 * 
 * @author Jacques Gignoux - 6 mai 2019
 *
 */
public class CheckMessage {
	/* temporary set of codes to flag the error context */

	/** the error raised by the check() method */
	private Exception exc = null;

	private ErrorTypes errorType;

	private Object[] args;

	private String shortDesc;

	private String longDesc;

	/**
	 * @param errorType
	 * @param exception
	 * @param args
	 */
	public CheckMessage(ErrorTypes errorType, Exception exception, Object... args) {
		this.errorType = errorType;
		this.exc = exception;
		this.args = args;
		buildDescriptions();
	}

	private static String getRef(Element e) {
		return e.classId() + ":" + e.id();
	}

	// Some messages are not immediately relevant. To prevent cluttering the UI,
	// use the shortDesc as a flag for
	// this and don't add to the checklist
	public boolean suppress() {
		return (shortDesc == null);
	}

	private void buildDescriptions() {
		String cat = errorType.category();
		switch (errorType) {
		case code2_GraphIsExclusiveButHasNoncompilantNodes: {
			String classOfTree = args[0].getClass().getSimpleName();
			int complyCount = (int) args[1];
			List<TreeNode> nonCompliantNodes = (List<TreeNode>) args[2];
			shortDesc = cat + "Nodes found with no matching specifications.";
			longDesc = shortDesc + "\n[";
			for (TreeNode node : nonCompliantNodes)
				longDesc += node.classId() + ":" + node.id() + ",";
			longDesc += "]";
			longDesc = longDesc.replace(",]", "]");
			break;
		}
		case code5_EdgeSpecsMissing: {
			/*-	"'" + aaToNode + "' property missing for edge specification " + edgeSpec);*/
			TreeNode nodeToCheck = (TreeNode) args[0];
			NodeSpec nodeSpec = (NodeSpec) args[1];
			EdgeSpec edgeSpec = (EdgeSpec) args[2];
			String key = (String) args[3];
			shortDesc = cat + "Property '" + key + "' is missing from the specification '" + getRef(edgeSpec) + "'.";
			longDesc = cat + "Property '" + key + "' is missing from the specification when checking '"
					+ getRef(nodeToCheck) + "' against '" + getRef(nodeSpec) + "'→" + getRef(edgeSpec);
			break;
		}
		/*-"'" + aaHasName + "' property missing for property specification " + propertyArchetype);*/
		case code10_PropertyMissingInArchetype: {
			Object element = args[0];
			PropertySpec spec = (PropertySpec) args[1];
			String key = (String) args[2];
			shortDesc = cat + "Property '" + key + "' is missing from the specification '" + getRef(spec) + "'.";
			if (element instanceof Element) {
				Element el = (Element) element;
				longDesc = cat + "Property '" + key + "' is missing from the specification when checking '" + getRef(el)
						+ "' against '" + getRef(spec.getParent()) + "'→'" + getRef(spec);
			} else
				longDesc = cat + "Property '" + key + "' is missing from the specification [" + spec + "].";
			break;
		}
		/*-	"Expected " + range + " nodes of class '" + requiredClass
									+ "' with parents '" + parentList + "' (got " + count + ") archetype="
									+ hasNode.toUniqueString();
		*/
			case code1_NodeRangeError: {
			Tree<? extends TreeNode> treeToCheck = (Tree<? extends TreeNode>) args[0];
			NodeSpec nodeSpec = (NodeSpec) args[1];
			String requiredClass = (String) args[2];
			StringTable parents = (StringTable) args[3];
			IntegerRange range = (IntegerRange) args[4];
			Integer count = (Integer) args[5];
			// we should suppress this message if no nodes exist in treeToCheck within the
			// parents table;
			// Actually, we need the potential parents list
			
			if (!findClass(treeToCheck,parents))
				break;
			shortDesc = cat+"Expected " + range + " nodes of class '" + requiredClass
					+ "' with parents '" + parents+" but found "+count;
			longDesc = shortDesc;
			break;
		}
		default: {
			throw new AotException("Unrecognized ErrorType in CheckMessage");
		}
		}
	}

	private boolean findClass(Tree<? extends TreeNode> tree, StringTable parents) {
		for (Node node:tree.nodes())
			if (parents.contains(node.classId()))
				return true;
		return false;
	}

	public String verbose1() {
		return shortDesc;
	}

	
	public String verbose2() {
		return longDesc;
	}

	public String verbose3() {
		return longDesc + "\n" + exc.getMessage();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("short: ");
		sb.append(shortDesc);
		sb.append("\n");
		sb.append("long: ");
		sb.append(longDesc);
		sb.append("\n");
		sb.append("Exception: ");
		sb.append(exc.toString());
		sb.append("\n");
		return sb.toString();
	}
}
