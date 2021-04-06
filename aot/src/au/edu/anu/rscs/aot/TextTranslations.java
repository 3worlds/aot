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

package au.edu.anu.rscs.aot;

import java.util.List;

import au.edu.anu.omhtk.Language;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.Element;

/**
 * @author Ian Davies
 *
 * @date 14 Mar. 2021
 */
public class TextTranslations {

	// NB: for archetype checking msgs, name the static method after the relevant
	// enum (i.e. SpecificationErrors.java)
	private static String frOpen = "«";
	private static String frClose = "»";

	public static String[] getTREE_MULTIPLE_ROOTS(List<String> refs, int nRoots) {
		String am;// action message
		String cm;// constraint message
		if (Language.French()) {
			//Attendu 1 nœud racine mais trouvé 10. "
			am = "Attribuez un parent à tous les nœuds " +Language.oq+ refs +Language.cq+" sauf un.";
			cm = "Attendu 1 nœud racine mais trouvé "+nRoots+".";
		} else {// make sure default is English
			am = "Assign a parent to all but one of " + refs + " nodes.";
			cm = "Expected 1 root node but found " + nRoots + ".";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getQUERY_PROPERTY_CLASS_UNKNOWN(String propertyKey, String spec) {
		String am;
		String cm;
		if (Language.French()) {
			am = "Correct the property class for '" + propertyKey + "' property in '" + spec + "'.";
			cm = "Cannot get class for archetype check property '" + spec + "'.";
		} else {
			am = "Correct the property class for '" + propertyKey + "' property in '" + spec + "'.";
			cm = "Cannot get class for archetype check property '" + spec + "'.";
		}

		String[] result = { am, cm };
		return result;
	}

	public static String[] getELEMENT_MISSING_PROPERTY_LIST(String element) {
		String am;
		String cm;
		if (Language.French()) {
			am = "Add a property list.";
			cm = "Expected '" + element + "' to contain a property list but found none.";
		} else {
			am = "Add a property list.";
			cm = "Expected '" + element + "' to contain a property list but found none.";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getNODE_MISSING_SPECIFICATION(int nMissing, List<String> elementNames) {
		String am;
		String cm;
		if (Language.French()) {
			am = "Add " + nMissing + " specifications.";
			cm = "Expected all nodes to have matching specifications. Found " + nMissing + " which did not comply "
					+ elementNames + ".";
		} else {
			am = "Add " + nMissing + " specifications.";
			cm = "Expected all nodes to have matching specifications. Found " + nMissing + " which did not comply "
					+ elementNames + ".";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getPROPERTY_MISSING(String propertyKey, String element) {
		String am;
		String cm;
		if (Language.French()) {
			am = "Add '" + propertyKey + "' property to '" + element + "'.";
			cm = "Expected property '" + propertyKey + "' in " + element + "'.";
		} else {
			am = "Add '" + propertyKey + "' property to '" + element + "'.";
			cm = "Expected property '" + propertyKey + "' in " + element + "'.";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getNODE_RANGE_INCORRECT1(String requiredClass, StringTable parentList, IntegerRange range,
			int count) {
		String am;
		String cm;
		if (Language.French()) {
			if (count < range.getLast())
				am = "Add node '" + requiredClass + "' to " + parentList + ".";
			else
				am = "Remove child node '" + requiredClass + "' from " + parentList + ".";
			cm = "Expected " + range + " nodes of class '" + requiredClass + "' with parents '" + parentList
					+ "' but found " + count + ".";
		} else {
			if (count < range.getLast())
				am = "Add node '" + requiredClass + "' to " + parentList + ".";
			else
				am = "Remove child node '" + requiredClass + "' from " + parentList + ".";
			cm = "Expected " + range + " nodes of class '" + requiredClass + "' with parents '" + parentList
					+ "' but found " + count + ".";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getNODE_RANGE_INCORRECT2(String childClassName, Element target, IntegerRange range,
			Integer nChildren) {
		String am;
		String cm;
		if (Language.French()) {
			if (nChildren < range.getLast())
				am = "Ajouter le nœud «" + childClassName + ":» à «" + target.toShortString() + "».";
			else
				am = "Supprimer le nœud «" + childClassName + ":» de «" + target.toShortString() + "».";
			cm = "Nœuds enfants " + range + " attendus avec la référence «" + childClassName + "»  du parent «"
					+ target.toShortString() + "» mais trouvé " + nChildren + ".";
		} else {
			if (nChildren < range.getLast())
				am = "Add node '" + childClassName + ":' to '" + target.toShortString() + "'.";
			else
				am = "Delete node '" + childClassName + ":' from '" + target.toShortString() + "'.";
			cm = "Expected " + range + " child nodes with reference '" + childClassName + "' from parent '"
					+ target.toShortString() + "' but found " + nChildren + ".";
		}
		String[] result = { am, cm };
		return result;
	};


	public static String[] getEDGE_CLASS_UNKNOWN(String klass, String element) {
		// TODO: we need the factory name
		String am;
		String cm;
		if (Language.French()) {
			am = "Add edge class '" + klass + ":' to TwConfigFactory";
			cm = "Class '" + klass + "' not found for edge " + element + ".";
		} else {
			am = "Add edge class '" + klass + ":' to TwConfigFactory";
			cm = "Class '" + klass + "' not found for edge " + element + ".";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getEDGE_CLASS_INCORRECT(String element, String klass, String foundClass) {
		String am;
		String cm;
		if (Language.French()) {
			am = "Change edge class of '" + element + "' to '" + klass + "'.";
			cm = "Edge " + element + " should be of class [" + klass + "]. Class [" + foundClass + "] found instead.";
		} else {
			am = "Change edge class of '" + element + "' to '" + klass + "'.";
			cm = "Edge " + element + " should be of class [" + klass + "]. Class [" + foundClass + "] found instead.";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getEDGE_ID_INCORRECT(String targetName, String requiredId, String foundId) {
		String am;
		String cm;
		if (Language.French()) {
			am = "Expected '" + targetName + "' to have id '" + requiredId + "' but found '" + foundId + "'.";
			cm = "Expected '" + targetName + "' to have id '" + requiredId + "' but found '" + foundId + "'.";
		} else {
			am = "Expected '" + targetName + "' to have id '" + requiredId + "' but found '" + foundId + "'.";
			cm = "Expected '" + targetName + "' to have id '" + requiredId + "' but found '" + foundId + "'.";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getEDGE_RANGE_INCORRECT(int nEdges, IntegerRange range, String edgeLabel, String edgeName,
			String toNodeRef) {
		String am;
		String cm;
		if (Language.French()) {
			if (nEdges < range.getLast())
				am = "Add edge '" + edgeLabel + ":' from '" + edgeName + "' to '" + toNodeRef + "'.";
			else
				am = "Remove edge '" + edgeLabel + ":' from '" + edgeName + "' to '" + toNodeRef + "'.";
			cm = "Expected '" + edgeName + "' to have " + range + " out edge(s) to nodes that match '" + toNodeRef
					+ "' with label '" + edgeLabel + "' but found " + nEdges + "'.";
		} else {
			if (nEdges < range.getLast())
				am = "Add edge '" + edgeLabel + ":' from '" + edgeName + "' to '" + toNodeRef + "'.";
			else
				am = "Remove edge '" + edgeLabel + ":' from '" + edgeName + "' to '" + toNodeRef + "'.";
			cm = "Expected '" + edgeName + "' to have " + range + " out edge(s) to nodes that match '" + toNodeRef
					+ "' with label '" + edgeLabel + "' but found " + nEdges + "'.";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getPROPERTY_UNKNOWN(String propertyKey, String element) {
		String am;
		String cm;
		if (Language.French()) {
			am = "Add property type '" + propertyKey + "' to the system.";
			cm = "Unknown property type for property '" + propertyKey + "' in '" + element + "'.";
		} else {
			am = "Add property type '" + propertyKey + "' to the system.";
			cm = "Unknown property type for property '" + propertyKey + "' in '" + element + "'.";
		}
		String[] result = { am, cm };
		return result;
	}

	public static String[] getPROPERTY_TYPE_INCORRECT(String propertyKey, String foundType, String requiredType,
			String element) {
		String am;
		String cm;
		if (Language.French()) {
			am = "Change property type '" + foundType + "' to '" + requiredType + "' in '" + element + "'.";
			cm = "Expected property '" + propertyKey + "' in '" + element + "' to have type '" + requiredType
					+ "' but found '" + foundType + "'.";
		} else {
			am = "Change property type '" + foundType + "' to '" + requiredType + "' in '" + element + "'.";
			cm = "Expected property '" + propertyKey + "' in '" + element + "' to have type '" + requiredType
					+ "' but found '" + foundType + "'.";
		}
		String[] result = { am, cm };
		return result;
	}

}
