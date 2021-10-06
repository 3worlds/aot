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

/**
 * Type of errors encountered when checking a specification against an archetype.
 * 
 * @author Ian Davies - 30 Nov 2019
 */
public enum SpecificationErrors {
	QUERY_PROPERTY_CLASS_UNKNOWN/* */("Archetype"), // 
	TREE_MULTIPLE_ROOTS/*          */("Graph"),//
	ELEMENT_MISSING_PROPERTY_LIST/**/("Node/Edge"), //
	NODE_MISSING_SPECIFICATION/*   */("Node"), //
	NODE_RANGE_INCORRECT1/*        */("Node"), //
	NODE_RANGE_INCORRECT2/*        */("Node"), //
	NODE_QUERY_UNSATISFIED/*       */("Node"), //
	EDGE_QUERY_UNSATISFIED/*       */("Edge"), //
	EDGE_RANGE_INCORRECT/*         */("Edge"), //
	EDGE_CLASS_UNKNOWN/*           */("Edge"), //
	/** Aren't they all self-explained?*/
	EDGE_CLASS_INCORRECT/*         */("Edge"), //
	EDGE_ID_INCORRECT/*            */("Edge"), // 
	PROPERTY_QUERY_UNSATISFIED/*   */("Property"), //
	PROPERTY_MISSING/*             */("Property"), //
	PROPERTY_UNKNOWN/*             */("Property"), //
	PROPERTY_TYPE_INCORRECT/*      */("Property"), //	
	ITEM_QUERY_UNSATISFIED/*       */("Item"), //

	;
	private final String category;

	private SpecificationErrors(String category) {
		this.category = category;
	}

	public String category() {
		return category;
	}
}


