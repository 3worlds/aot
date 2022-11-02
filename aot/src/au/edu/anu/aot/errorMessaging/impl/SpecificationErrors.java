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
package au.edu.anu.aot.errorMessaging.impl;

/**
 * Type of errors encountered when checking a specification against an
 * archetype.
 * 
 * @author Ian Davies - 30 Nov 2019
 */
public enum SpecificationErrors {

	/**
	 * Indicates a class referenced in a query property table could not be found.
	 * This is categorized as an 'Archetype' problem and likely indicates a typo in
	 * the archetype specification or some refactoring of classes without updating
	 * graph files.
	 */
	QUERY_PROPERTY_CLASS_UNKNOWN("Archetype"), //

	/**
	 * Indicates that a TreeGraph has more than one root. This can happen during
	 * editing of a TreeGraph when parent-child relationships are altered.
	 */
	TREE_MULTIPLE_ROOTS("Graph"), //
	/**
	 * Indicates the expected property list of node or edge is missing. This can
	 * result when there is a mismatch between defining the node/edge java class and
	 * a graph file. Either the node/edge class should be defined as having a
	 * property list OR the graph file should not have properties for this node or
	 * edge.
	 */
	ELEMENT_MISSING_PROPERTY_LIST("Node/Edge"), //
	/**
	 * The specification for a node in a graph is missing in the graph file. This
	 * only occurs if the Archetype checker has the {@code exclusive} flag set to
	 * {@code true}. It could also occur due to a parsing error for a corrupted
	 * graph file.
	 */
	NODE_MISSING_SPECIFICATION("Node"), //
	/**
	 * Indicates the node has too few or too many children of a particular class.
	 */
	NODE_RANGE_INCORRECT1("Node"), //
	/**
	 * Indicates the node either requires a child of a particular class or has a
	 * child node of the wrong class.
	 */
	NODE_RANGE_INCORRECT2("Node"), //
	/**
	 * A query associated with a node is unsatisfied.
	 */
	NODE_QUERY_UNSATISFIED("Node"), //
	/**
	 * A query associated with an edge is unsatisfied.
	 */
	EDGE_QUERY_UNSATISFIED("Edge"), //
	/**
	 * The number of edges is not within the specified range.
	 */
	EDGE_RANGE_INCORRECT("Edge"), //
	/**
	 * The class of an edge specified in a file is not in the java class path.
	 */
	EDGE_CLASS_UNKNOWN("Edge"), //
	/**
	 * The class of an edge specified in a file is the wrong type.
	 */
	EDGE_CLASS_INCORRECT("Edge"), //
	/**
	 * The unique identifier of the edge is not the expected one.
	 */
	EDGE_ID_INCORRECT("Edge"), //
	/**
	 * A query associated with a property is unsatisfied.
	 */
	PROPERTY_QUERY_UNSATISFIED("Property"), //
	/**
	 * The number of entries for this property is not within the specified range.
	 */
	PROPERTY_MISSING("Property"), //
	/**
	 * The property type is not in the property type list.
	 */
	PROPERTY_UNKNOWN("Property"), //
	/**
	 * The property type is not the one required type.
	 */
	PROPERTY_TYPE_INCORRECT("Property"), //
	/**
	 * A query associated with an object that is not a Node, Edge or Property is
	 * unsatisfied.
	 */
	ITEM_QUERY_UNSATISFIED("Item"), //

	;

	private final String category;

	private SpecificationErrors(String category) {
		this.category = category;
	}

	/**
	 * @return The category of error: either [Archetype, Graph, Node/Edge, Node,
	 *         Edge, Property or Item].
	 */
	public String category() {
		return category;
	}
}
