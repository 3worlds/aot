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
package au.edu.anu.aot;

import java.util.List;

import static au.edu.anu.omhtk.Language.*;
import fr.cnrs.iees.omugi.collections.tables.StringTable;
import au.edu.anu.omhtk.util.IntegerRange;
import fr.cnrs.iees.omugi.graph.Element;

/**
 * <p>Error messages to use with specification archetypes. All methods are static and return an array of
 * two messages, an <em>action</em> message and a <em>constraint</em> message. The former explains
 * what should be done to fix the problem while the latter gives the cause of the problem.</p>
 * <p>Supports various languages (cf. {@link au.edu.anu.omhtk.Language Language}). <small>[Well, currently only English and French are supported. Translations to other languages
 * welcome]</small></p>
 * 
 * @author Ian Davies - 14 Mar. 2021
 */
public class TextTranslations {

	// NB: for archetype checking msgs, name the static method after the relevant
	// enum (i.e. SpecificationErrors.java)

	/**
	 * Error message: a tree has multiple roots.
	 * @param refs the list of tree nodes without a parent (= the multiple roots)
	 * @param nRoots number of tree root nodes found
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getTREE_MULTIPLE_ROOTS(List<String> refs, int nRoots) {
		String am;// action message
		String cm;// constraint message
		if (French()) {
			am = "Attribuer un parent à tous les nœuds " +oq+ refs +cq+" sauf un.";
			cm = "1 nœud racine attendu, mais "+nRoots+" trouvés.";
		} 
		else {// make sure default is English
			am = "Assign a parent to all but one of " +oq+ refs +cq+ " nodes.";
			cm = "Expected 1 root node but found " + nRoots + ".";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: property class is unknown.
	 * @param propertyKey the property name
	 * @param spec the node holding the properties
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getQUERY_PROPERTY_CLASS_UNKNOWN(String propertyKey, String spec) {
		String am;
		String cm;
		if (French()) {
			am = "Corriger la classe de la propriété " +oq+propertyKey+cq+ " du nœud " +oq+spec+cq+ ".";
			cm = "Classe de la propriété " +oq+ spec +cq+ " inconnue.";
		} 
		else {
			am = "Correct the property class for " +oq+ propertyKey +cq+ " property in " +oq+ spec +cq+ ".";
			cm = "Cannot get class for archetype check property " +oq+ spec +cq+ "'.";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: graph element lacks property list
	 * @param element the element
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getELEMENT_MISSING_PROPERTY_LIST(String element) {
		String am;
		String cm;
		if (French()) {
			am = "Ajouter une liste de propriétés.";
			cm = "L'élément " +oq+ element +cq+ " devrait avoir une liste de propriétés.";
		} 
		else {
			am = "Add a property list.";
			cm = "Expected " +oq+ element +cq+ " to contain a property list but found none.";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: nodes without specification found in an exclusive archetype
	 * @param nMissing number of nodes without matching specification
	 * @param elementNames identifiers of all nodes without matching specification
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getNODE_MISSING_SPECIFICATION(int nMissing, List<String> elementNames) {
		String am;
		String cm;
		if (French()) {
			if (nMissing==1) {
				am = "Rajouter " + nMissing + " spécification.";
				cm = "Tous les nœuds devraient correspondre à une spécification, mais " + nMissing + " n'en a pas : "
						+ elementNames + ".";
			}
			else {
				am = "Rajouter " + nMissing + " spécifications.";
				cm = "Tous les nœuds devraient correspondre à une spécification, mais " + nMissing + " n'en ont pas : "
					+ elementNames + ".";
			}
		} 
		else {
			if (nMissing==1)
				am = "Add " + nMissing + " specification.";
			else
				am = "Add " + nMissing + " specifications.";
			cm = "Expected all nodes to have matching specifications. Found " + nMissing + " which did not comply "
					+ elementNames + ".";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: missing property
	 * @param propertyKey the property name
	 * @param element the element lacking the property
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getPROPERTY_MISSING(String propertyKey, String element) {
		String am;
		String cm;
		if (French()) {
			am = "Rajouter la propriété " +oq+ propertyKey +cq+ " à l'élément " +oq+ element +cq+ ".";
			cm = "La propriété " +oq+ propertyKey +cq+ " manque à l'élément " +oq+ element +cq+ ".";
		} 
		else {
			am = "Add " +oq+ propertyKey +cq+ " property to " +oq+ element +cq+ ".";
			cm = "Expected property " +oq+ propertyKey +cq+  " in " +oq+ element +cq+ ".";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: wrong number of nodes under a parent compared to multiplicity
	 * @param requiredClass the class of this node
	 * @param parentList the list of possible parents
	 * @param range the expected multiplicity
	 * @param count the effective number of nodes
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getNODE_RANGE_INCORRECT1(String requiredClass, StringTable parentList, IntegerRange range,
			int count) {
		String am;
		String cm;
		if (French()) {
			if (count < range.getLast())
				am = "Rajouter un nœud enfant de type " +oq+ requiredClass +cq+ " à " + parentList + ".";
			else
				am = "Enlever un nœud enfant de type " +oq+ requiredClass +cq+ " à " + parentList + ".";
			cm = range + " nœuds de type " +oq+ requiredClass +cq+ ", de parents " +oq+ parentList +cq
					+ " attendus, mais " + count + " trouvés.";
		} 
		else {
			if (count < range.getLast())
				am = "Add node " +oq+ requiredClass +cq+ " to " + parentList + ".";
			else
				am = "Remove child node " +oq+ requiredClass +cq+ " from " + parentList + ".";
			cm = "Expected " + range + " nodes of class " +oq+ requiredClass +cq+ " with parents " +oq+ parentList +cq
					+ " but found " + count + ".";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: wrong number of children compared to multiplicity
	 * @param childClassName the class of child nodes
	 * @param target the parent node
	 * @param range the expected multiplicity
	 * @param nChildren the effective number of children found
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getNODE_RANGE_INCORRECT2(String childClassName, Element target, IntegerRange range,
			Integer nChildren) {
		String am;
		String cm;
		if (French()) {
			if (nChildren < range.getLast())
				am = "Ajouter le nœud " +oq+ childClassName +cq+ " à " +oq+ target.toShortString() +cq+ ".";
			else
				am = "Supprimer le nœud " +oq+ childClassName +cq+ " de " +oq+ target.toShortString() +cq+ ".";
			cm = range + " nœuds enfants de type " +oq+ childClassName +cq+ ", de parent " +oq
					+ target.toShortString() +cq+ " attendus, mais " + nChildren + "trouvé(s).";
		} 
		else {
			if (nChildren < range.getLast())
				am = "Add node " +oq+ childClassName +cq+ " to " +oq+ target.toShortString() +cq+ ".";
			else
				am = "Delete node " +oq+ childClassName +cq+ " from " +oq+ target.toShortString() +cq+ ".";
			cm = "Expected " + range + " child nodes with reference " +oq+ childClassName +cq+ " from parent " +oq
					+ target.toShortString() +cq+ " but found " + nChildren + ".";
		}
		String[] result = { am, cm };
		return result;
	};

	/**
	 * Error message: edge class is unknown
	 * @param klass the expected edge class
	 * @param element the edge id
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getEDGE_CLASS_UNKNOWN(String klass, String element, String factoryName) {
		// TODO: we need the factory name - DONE JG 6/10/2021
		String am;
		String cm;
		if (French()) {
			am = "Ajouter la classe " + klass + " à la fabrique de liens " +oq+ factoryName +cq+ ".";
			cm = "Classe " +oq+ klass +cq+ " du lien " +oq+ element +cq+ " inconnue.";
		} 
		else {
			am = "Add edge class " +oq+ klass +cq+ " to edge factory " +oq+ factoryName +cq+ ".";
			cm = "Class " +oq+ klass +cq+ " not found for edge " +oq+ element +cq+ ".";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: edge class is wrong
	 * @param element the edge id
	 * @param klass the expected edge class
	 * @param foundClass the effective edge class
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getEDGE_CLASS_INCORRECT(String element, String klass, String foundClass) {
		String am;
		String cm;
		if (French()) {
			am = "Remplacer la classe du lien " +oq+ element +cq+ " par " +oq+ klass +cq+ ".";
			cm = "Le lien " +oq+ element +cq+ " devrait être de la classe " +oq+ klass 
				+cq+ " au lieu de la classe " +oq+ foundClass +cq+ ".";
		} 
		else {
			am = "Change edge class of " +oq+ element +cq+ " to " +oq+ klass +cq+ ".";
			cm = "Edge " +oq+ element +cq+ " should be of class " +oq+ klass +cq+ ". Class " +oq+ foundClass +cq+ " found instead.";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: unexpected edge identifier
	 * @param targetName the edge
	 * @param requiredId the expected edge id
	 * @param foundId the effective edge id
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getEDGE_ID_INCORRECT(String targetName, String requiredId, String foundId) {
		String am;
		String cm;
		if (French()) {
			am = "Remplacer l'identifiant du lien " +oq+ targetName +cq+ " par " +oq+ requiredId +cq+ ".";
			cm = "L'identifiant du lien " +oq+ targetName +cq+ " devrait être " +oq+ requiredId +cq
				+ " au lieu de " +oq+ foundId +cq+ ".";
		} 
		else {
			am = "Change edge " +oq+ targetName +cq+ " identifier to " +oq+ requiredId +cq+ " instead of "+oq+ foundId +cq+ ".";
			cm = "Expected " +oq+ targetName +cq+ " to have id " +oq+ requiredId +cq+ " but found " +oq+ foundId +cq+ ".";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message: node has a wrong number of edges compared to multiplicity
	 * @param nEdges the number of edges found
	 * @param range the expected number of edges (multiplicity)
	 * @param edgeLabel the expected edge class id
	 * @param nodeName the node id
	 * @param toNodeRef the reference of the (other) end node
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getEDGE_RANGE_INCORRECT(int nEdges, IntegerRange range, 
			String edgeLabel, String nodeName, String toNodeRef) {
		String am;
		String cm;
		if (French()) {
			if (nEdges < range.getLast())
				am = "Rajouter un lien de classe " +oq+ edgeLabel +cq+ " entre le nœud " +oq
					+ nodeName +cq+ " et un nœud vérifiant " +oq+ toNodeRef +cq+ ".";
			else
				am = "Enlever le lien de classe " +oq+ edgeLabel +cq+ " entre le nœud " +oq
					+ nodeName +cq+ " et le nœud vérifiant " +oq+ toNodeRef +cq+ ".";
			if (nEdges==1)
				cm = "Le nœud " +oq+ nodeName +cq+ " devrait avoir " + range 
				+ " liens sortants vers des nœuds vérifiant " +oq+ toNodeRef +cq
				+ ", de classe " +oq+ edgeLabel +cq+ ", mais un seul a été trouvé.";
			else
				cm = "Le nœud " +oq+ nodeName +cq+ " devrait avoir " + range 
					+ " liens sortants vers des nœuds vérifiant " +oq+ toNodeRef +cq
					+ ", de classe " +oq+ edgeLabel +cq+ ", mais " + nEdges + " ont été trouvés.";
		} 
		else {
			if (nEdges < range.getLast())
				am = "Add edge " +oq+ edgeLabel +cq+ " from " +oq+ nodeName +cq+ " to " +oq+ toNodeRef +cq+ ".";
			else
				am = "Remove edge " +oq+ edgeLabel +cq+ " from " +oq+ nodeName +cq+ " to " +oq+ toNodeRef +cq+ ".";
			cm = "Expected " +oq+ nodeName +cq+ " to have " + range + " out edge(s) to nodes that match " +oq+ toNodeRef +cq
					+ " with label " +oq+ edgeLabel +cq+ " but found " + nEdges + ".";
		}
		String[] result = { am, cm };
		return result;
	}

	/**
	 * Error message:  property class is unknown.
	 * @param propertyKey the property name
	 * @param element the element to which the property belongs
	 * @return action and constraint messages in a {@code String} array
	 */
	// QUESTION: Isnt that the same method as getQUERY_PROPERTY_CLASS_UNKNOWN ???
	public static String[] getPROPERTY_UNKNOWN(String propertyKey, String element) {
		String am;
		String cm;
		if (French()) {
			am = "Rajouter la classe " +oq+ propertyKey +cq+ " aux types de propriétés reconnus par le système.";
			cm = "Classe inconnue pour la propriété " +oq+ propertyKey +cq+ " de l'élément " +oq+ element +cq+ ".";
		} 
		else {
			am = "Add property type " +oq+ propertyKey +cq+ " to the system.";
			cm = "Unknown property type for property " +oq+ propertyKey +cq+ " in " +oq+ element +cq+ ".";
		}
		String[] result = { am, cm };
		return result;
	}
	
	/**
	 * Error message: wrong property type
	 * @param propertyKey the property name
	 * @param foundType the effective property type
	 * @param requiredType the expected property type
	 * @param element the element to which the property belongs
	 * @return action and constraint messages in a {@code String} array
	 */
	public static String[] getPROPERTY_TYPE_INCORRECT(String propertyKey, String foundType, String requiredType,
			String element) {
		String am;
		String cm;
		if (French()) {
			am = "Remplacer le type de propriété " +oq+ foundType +cq+ " par " +oq+ requiredType +cq
				+ " pour la propriété " +oq+ propertyKey +cq+ " de l'élément " +oq+ element +cq+ ".";
			cm = "La propriété " +oq+ propertyKey +cq+ " de l'élément " +oq+ element +cq
				+ " devrait être de type " +oq+ requiredType +cq+ " plutôt que de type " +oq+ foundType +cq+ ".";
		} 
		else {
			am = "Change property type " +oq+ foundType +cq+ " to " +oq+ requiredType +cq
				+ " for property " +oq+ propertyKey +cq+ " in " +oq+ element +cq+ ".";
			cm = "Expected property " +oq+ propertyKey +cq+ " in " +oq+ element +cq+ " to have type " +oq+ requiredType
				+cq+ " but found " +oq+ foundType +cq+ ".";
		}
		String[] result = { am, cm };
		return result;
	}

}
