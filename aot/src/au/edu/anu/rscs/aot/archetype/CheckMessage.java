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

import fr.cnrs.iees.graph.TreeNode;

/**
 * A class to store error messages from archetype checks.
 * @author Jacques Gignoux - 6 mai 2019
 *
 */
public class CheckMessage {

	/** the error raised by the check() method */
	private Exception exc = null;
	/** the object which caused the error - NB it may be an archetype node (if there was an error in the archetype file) */
	private Object target = null;
	/** the archetype constraint which was being checked - NB it may be null if the check was on an archetype node */
	private TreeNode archetypeNode = null;
	
	/**
	 * 
	 * @param check
	 * @param failed
	 * @param onNode
	 */
	public CheckMessage(Object check, Exception failed, TreeNode onNode) {
		super();
		target = check;
		exc = failed;
		archetypeNode = onNode;
	}

	public Exception getException() {
		return exc;
	}

	public Object getTarget() {
		return target;
	}

	public TreeNode getArchetypeNode() {
		return archetypeNode;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Archetype check failed on requirement:\n\t")
			.append(archetypeNode.toString())
			.append("\n--for object:\n\t")
			.append(target.toString())
			.append("\n--with Error:\n\t")
			.append(exc.toString());
		return sb.toString();
	}
}
