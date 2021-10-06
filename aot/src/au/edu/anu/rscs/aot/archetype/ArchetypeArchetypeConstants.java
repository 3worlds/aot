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
 * <p>Definition of the vocabulary used in the <em>archetype for archetypes</em>.</p>
 * 
 * <p>This interface only contains static constant fields.</p>
 * <p>CAUTION: changing anything here will trash the whole aot checking procedure.</p>
 * 
 * @author Ian Davies - 8 Aug 2019
 */
// prefixed with 'aa' (Archetype Archetype - TODO could convert to enum (but don't really 
// need the class field)
public interface ArchetypeArchetypeConstants {
	
	/** requirement for the class of a graph element or property - 
	 * matches the {@code isOfClass} keyword in the <em>archetype for archetypes</em> */
	public static final String aaIsOfClass = "isOfClass";
	
	/** requirement for the multiplicity of a graph element or property - 
	 * matches the {@code multiplicity} keyword in the <em>archetype for archetypes</em> */
	public static final String aaMultiplicity = "multiplicity";
	
	/** requirement of possible parents for a node - 
	 * matches the {@code hasParent} keyword in the <em>archetype for archetypes</em> */
	public static final String aaHasParent = "hasParent";
	
	/** requirement for an edge - 
	 * matches the {@code hasEdge} keyword in the <em>archetype for archetypes</em> */
	public static final String aaHasEdge = "hasEdge";
	
	/** requirement for a Node - 
	 * matches the {@code hasNode} keyword in the <em>archetype for archetypes</em> */
	public static final String aaHasNode ="hasNode" ;
	
	/** requirement for a property - 
	 * matches the {@code hasProperty} keyword in the <em>archetype for archetypes</em> */
	public static final String aaHasProperty = "hasProperty";
	
	/** requirement for a graph element or property to satisfy a query - 
	 * matches the {@code mustSatisfyQuery} keyword in the <em>archetype for archetypes</em> */
	public static final String aaMustSatisfyQuery = "mustSatisfyQuery";
	
	/** requirement for a property to have a particular name - 
	 * matches the {@code hasName} keyword in the <em>archetype for archetypes</em> */
	public static final String aaHasName = "hasName";
	
	/** requirement for a query to be of a particular class - 
	 * matches the {@code className} keyword in the <em>archetype for archetypes</em> */
	public static final String aaClassName = "className";
	
	/** requirement for the end node of an edge - 
	 * matches the {@code toNode} keyword in the <em>archetype for archetypes</em> */
	public final static String aaToNode = "toNode";
	
	/** requirement for a graph element to have a particular identifier - 
	 * matches the {@code hasId} keyword in the <em>archetype for archetypes</em> */
	public final static String aaHasId = "hasId";
	
	/** requirement for a property to be of a certain type - 
	 * matches the {@code type} keyword in the <em>archetype for archetypes</em> */
	public final static String aaType = "type";
}
