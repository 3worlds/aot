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

public enum SpecificationErrors {
	GRAPH_MISSING_SPECIFICATION_NODE/*             */("Archetype"), //

	GRAPH_MISSING_SPECIFICATION_PROPERTY/*         */("Archetype"), //

	QUERY_PROPERTY_CLASS_UNKNOWN/*                 */("Archetype"), // 

	EDGE_CLASS_UNKNOWN/*                           */("Archetype"), //

	EDGE_CLASS_INCORRECT/*                         */("Archetype"), //

	EDGE_ID_INCORRECT/*                            */("Archetype"), //

	PROPERTY_UNKNOWN/*                             */("Archetype"), //
 
	PROPERTY_INCORRECT_TYPE/*                      */("Archetype"), //
	
	ELEMENT_MISSING_PROPERTY_LIST/*                */("Archetype"), //

	NODE_WRONG_MULTIPLICITY/*                      */("Node"), //

	CHILD_MULTIPLICITY_INCORRECT/*                 */("Node"), //

	QUERY_EDGE_UNSATISFIED/*                       */("Edge"), //
	QUERY_NODE_UNSATISFIED/*                       */("Node"), //
	QUERY_PROPERTY_UNSATISFIED/*                   */("Property"), //
	QUERY_ITEM_UNSATISFIED/*                       */("Item"), //

	EDGE_OUT_OF_RANGE/*                            */("Edge"), //
	
	;
	private final String category;

	private SpecificationErrors(String category) {
		this.category = category;
	}

	public String category() {
		return category;
	}
}


