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

package au.edu.anu.aot.errorMessaging;

/**
 * <p>An interface to manage error messages in a user-friendly way.</p>
 * 
 * <p>Purpose: these messages inform the user as to what steps to take to
 * get a possibly complex specification valid. I think it is no exaggeration to say
 * that the success of AOT, measured as uptake, depends largely on how easy these
 * messages are to interpret. But there are contexts and its a problem to write
 * a message at a low level that have meaning as they bubble up to new contexts. So:
 * </p>
 * 
 * <ol>
 * <li>for any error there is the error description and action to undertake to fix it, 
 * hence two messages;</li>
 * <li>the error context can be described by increasing message verbosity.</li>
 * </ol>
 * 
 * <ul>
 * <li>The lowest verbosity ('brief') just returns the action message</li>
 * <li>The intermediate verbosity ('detailed') adds to it the error message</li>
 * <li>The highest verbosity ('debug') adds to it context information</li> 
 * </ul>
 * 
 * @author Ian Davies - 30 Nov 2019
 */
public interface ErrorMessagable {
	
	/**
	 * Brief description of the error
	 * @return the action message
	 */
	public String actionInfo();

	/**
	 * Detailed description of the error
	 * @return the action message + error message
	 */
	public String detailsInfo();
	
	/**
	 * Debugging information about the error
	 * @return the action message + error message + context information
	 */
	public String debugInfo();

	/**
	 * Allows some way of organizing messages in some category order. Best used as a
	 * prefix to all verbose1/2 strings.
	 */
	public String category();

	/**
	 * Largely for debugging. Some string that indicates exactly the type of message.
	 * For example implementations may use enums and so the type could be the
	 * enum.name(). Additional prefix to verbose2 strings.
	 */
	public String errorName();
	

}
