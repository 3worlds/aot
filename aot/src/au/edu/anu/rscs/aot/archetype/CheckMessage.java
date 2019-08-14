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
 * 
 * NOTE: Ian, feel free to adapt this class to your needs by putting in any useful information
 * for user feedback. These messages are created by Archetypes.check(...) methods.
 * 
 * @author Jacques Gignoux - 6 mai 2019
 *
 */
public class CheckMessage {
	/* temporary set of codes to flag the error context */
	public static final int code0 = 0;
	public static final int code1 = 1;
	public static final int code2 = 2;
	public static final int code3 = 3;
	public static final int code4 = 4;
	public static final int code5 = 5;
	public static final int code6 = 6;
	public static final int code7 = 7;
	public static final int code8 = 8;
	public static final int code9 = 9;
	public static final int code10 = 10;
	public static final int code11 = 11;
	public static final int code12 = 12;
	public static final int code13 = 13;
	public static final int code14 = 14;
	public static final int code15 = 15;
	public static final int code16 = 16;
	public static final int code17 = 17;
	public static final int code18 = 18;
	public static final int code19 = 19;
	public static final int code20 = 20;

	/** the error raised by the check() method */
	private Exception exc = null;
	/** the object which caused the error - NB it may be an archetype node (if there was an error in the archetype file) */
	private Object target = null;
	/** the archetype constraint which was being checked - NB it may be null if the check was on an archetype node */
	private TreeNode archetypeNode = null;
	
	private int code;
	
	/**
	 * 
	 * @param check
	 * @param failed
	 * @param onNode
	 */
	// Could have many constructors to assist with context
	public CheckMessage(int code,Object check, Exception failed, TreeNode onNode) {
		super();
		this.code = code;
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
	
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (archetypeNode==null)
			sb.append("Archetype check failed \n");
		else
			sb.append("Archetype check failed on requirement:\n\t")
				.append(archetypeNode.toString())
				.append('\n');
		if (target!=null)
			sb.append("--for object:\n\t")
				.append(target.toString())
				.append('\n');
		if (exc!=null)
			sb.append("--with Error:\n\t")
				.append(exc.toString())
				.append('\n');
		return sb.toString();
	}
}
