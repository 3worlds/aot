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
package au.edu.anu.aot.archetype;

import fr.cnrs.iees.omugi.graph.NodeFactory;
import fr.cnrs.iees.omugi.graph.impl.SimpleDataTreeNode;
import fr.cnrs.iees.omugi.identity.Identity;
import fr.cnrs.iees.omugi.properties.SimplePropertyList;
import fr.cnrs.iees.omugi.properties.impl.ExtendablePropertyListImpl;

/**
 * An Edge specification. Edges in a specification represent relations between
 * entities of the target system.
 * 
 * @author Jacques Gignoux - 20/3/2019
 *
 */
public class EdgeSpec extends SimpleDataTreeNode {

	// NOTE: the constructor must be made public for the TreeFactory to find it as
	// the TreeFactory
	// belongs to package fr.cnrs.iees.graph.impl
	/**
	 * @param id      Unique identifier of the specification.
	 * @param props   Node property list.
	 * @param factory The factory to create nodes for this specification.
	 */
	public EdgeSpec(Identity id, SimplePropertyList props, NodeFactory factory) {
		super(id, props, factory);
	}

	/**
	 * @param id      Unique identifier of the specification.
	 * @param factory The factory to create nodes for this specification.
	 */
	public EdgeSpec(Identity id, NodeFactory factory) {
		super(id, new ExtendablePropertyListImpl(), factory);
	}

}
