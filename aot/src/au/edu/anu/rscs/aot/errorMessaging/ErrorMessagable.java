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
 * messages are to interpret. But there are contexts and its a problem to write
 * msg at a low level that have meaning as they bubble up to new contexts. So
 * for any error these is the error and action.
 * 
 * Faced with a Graph: 3Worlds:projec1->DataDefinition:dataDef1, it seems better
 * to say:
 * 
 * "Add 'dataDefinition' node to '3worlds.prjt1'"
 * 
 * instead of:
 * 
 * "Expected 1..* children of class 'dataDefinition' for parents of{ 3worlds}"
 * 
 * So aims are: Have all msg strings in one place rather than dist. over many
 * classes.
 * 
 * Allow modification / addition of messages in new context.
 * 
 * Allow different levels of detail (verbosity) so ultimately everything can be
 * examined but also you can choose not to be overwhelmed with detail.
 */

public interface ErrorMessagable {
	/* Brief description */
	public String verbose1();

	/* Detailed description */
	public String verbose2();

	/*
	 * Allows some way of organizing msgs in some category order. Best used as a
	 * prefix to all verbose1/2 strings
	 */
	public String category();

	/*
	 * Largely for debugging. Some string that indicates exactly the type of msg.
	 * For example implementations may use enums and so the type could be the
	 * enum.name(). Additionl prefix to verbose2 strings
	 */
	public String errorName();

}
