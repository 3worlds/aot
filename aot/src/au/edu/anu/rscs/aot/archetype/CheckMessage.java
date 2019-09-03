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

import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.TreeNode;

/**
 * A class to store error messages from archetype checks.
 * 
 * NOTE: Ian, feel free to adapt this class to your needs by putting in any
 * useful information for user feedback. These messages are created by
 * Archetypes.check(...) methods.
 * 
 * @author Jacques Gignoux - 6 mai 2019
 *
 */
public class CheckMessage {
	/* temporary set of codes to flag the error context */
	public static final int code0NotATree = 0;
	public static final int code1 = 1;
	public static final int code2Exclusive = 2;
	public static final int code3PropertyClass = 3;
	public static final int code4Query = 4;
	public static final int code5 = 5;
	public static final int code6OutEdgeMissing = 6;
	public static final int code7 = 7;
	public static final int code8 = 8;
	public static final int code9OutEdgeRangeCheck = 9;
	public static final int code10 = 10;
	public static final int code11 = 11;
	public static final int code12 = 12;
	public static final int code13MissingProperty = 13;
	public static final int code14UnknowPropertyType = 14;
	public static final int code15WrongPropertyType = 15;
	public static final int code16 = 16;
	public static final int code17MoreThanOneRoot = 17;// spare...
	public static final int code18 = 18;
	public static final int code19 = 19;
	public static final int code20Deploy = 20;

	/** the error raised by the check() method */
	private Exception exc = null;
	/**
	 * the object which caused the error - NB it may be an archetype node (if there
	 * was an error in the archetype file)
	 */
	private Object target = null;
	/**
	 * the archetype constraint which was being checked - NB it may be null if the
	 * check was on an archetype node
	 */
	private TreeNode archetypeNode = null;

	private String requiredClass;

	private StringTable parentList;

	private int code;

	private IntegerRange range;
	
	private int count;

	/**
	 * 
	 * @param check
	 * @param failed
	 * @param onNode
	 */
	// Could have many constructors to assist with context
	public CheckMessage(int code, Object check, Exception failed, TreeNode onNode, String requiredClass,
			StringTable parentList, IntegerRange range,int count) {
		super();
		this.code = code;
		target = check;
		exc = failed;
		archetypeNode = onNode;
		this.requiredClass = requiredClass;
		this.parentList = parentList;
		this.range = range;
		this.count = count;
	}

	public int count() {
		return count;
	}
	public String requiredClass() {
		return requiredClass;
	}

	public StringTable parentList() {
		return parentList;
	}

	public Exception getException() {
		return exc;
	}

	public IntegerRange range() {
		return range;
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
		if (archetypeNode == null)
			sb.append("Archetype check failed \n");
		else
			sb.append("Archetype check failed on requirement:\n\t").append(archetypeNode.toString()).append('\n');
		if (target != null)
			sb.append("--for object:\n\t").append(target.toString()).append('\n');
		if (exc != null)
			sb.append("--with Error:\n\t").append(exc.toString()).append('\n');
		return sb.toString();
	}
}
