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

package au.edu.anu.rscs.aot.errorMessaging;

/**
 * Message purpose: These messages inform the user as to what steps to take to
 * get a model constructed and running. I think it is no exaggeration to say
 * that the success of tw, measured as uptake, depends largely on how easy these
 * messages are to interpret. But there are different types of users and its
 * easy to fall into the trap of writing messages that are easy for us to write
 * (query not satisfied msgs) but not immediately obvious what this means in a
 * different context. It's the same problem with writing exceptions - the
 * message depends on context - which changes as errors bubble up through the
 * system. Yet as context changes, its important to preserve and pass on data as
 * the error bubbles up into new contexts.
 * 
 * In a sense, our 'errors' are not always really errors but instructions.
 * 
 * So what to do? I had thought that providing different levels of 'verbosity'
 * was the answer. I think this is useful to avoid being swamped in a sea of
 * shit but actually one needs at least an "instruction" as well as the "error".
 * 
 * There are at least two purposes to a msg: the problem and the action to take.
 * We need:
 * error codes which are complete
 * all possible information gathered.
 * adding and reinterpreting info at different levels.
 * be easy to extend as tw develops.
 * 
 * 
 * 
 * 
 * Faced with a Graph: 3Worlds:projec1->DataDefinition:dataDef1, it seems better
 * to say:
 * 
 * "Add 'record' node to 'DataDefinition:dataDef1'" or
 * 
 * 'DataDefinition:dataDef1' requires 0..* children called 'record'
 * 
 * "Expected 0..* children of class 'record' for parents {DataDefinition,
 * Table}"
 */

// Possibly goes in omhtk? Its really just for the construction and deployment of models from specifications etc etc
// but it would be good if qgraph used it directly.
public interface ErrorMessagable {
	/* Brief description */
	public String verbose1();

	/* Detailed description */
	public String verbose2();

	/*
	 * Allows some way of organizing msgs in some category order. Best used as a
	 * prefix to all 'verbose<n> methods
	 */
	public String category();

	/*
	 * Largely for debugging. Some string that indicates exactly the type of msg.
	 * For example implementations may use enums and so the type could be the
	 * enum.name(). Best used as a suffix to all verbose3() methods.
	 */
	public String code();


}
