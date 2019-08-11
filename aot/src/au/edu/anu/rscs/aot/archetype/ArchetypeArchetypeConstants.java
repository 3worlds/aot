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
/**
 * @author Ian Davies
 *
 * @date 8 Aug 2019
 */
// prefixed with 'aa' (Archetype Archetype - TODO could convert to enum (but don't really need the class field)
public interface ArchetypeArchetypeConstants {
	public static final String aaIsOfClass = "isOfClass";
	public static final String aaMultiplicity = "multiplicity";
	public static final String aaHasParent = "hasParent";
	public static final String aaHasEdge = "hasEdge";
	public static final String aaHasNode ="hasNode" ;
	public static final String aaHasProperty = "hasProperty";
	public static final String aaMustSatisfyQuery = "mustSatisfyQuery";
	public static final String aaHasName = "hasName";
	public static final String aaClassName = "className";
	public final static String aaToNode = "toNode";
	public final static String aaHasId = "hasId";
	public final static String aaType = "type";

	

}
