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

import java.util.ArrayList;
import java.util.List;

/**
 * Error message sender. Sends messages to any number of {@link ErrorListListener}s.
 * 
 * @author Ian Davies - 12, 2018
 */
public class ErrorMessageManager {
	
	private static List<ErrorListListener> listeners = new ArrayList<>();

	private static boolean haveErrors;

	/**
	 * 
	 * @return {@code true} if any error was found
	 */
	public static boolean haveErrors() {
		return haveErrors;
	}

	/**
	 * Send an error message to all its listeners.
	 * 
	 * @param msg the message to send
	 */
	public static void dispatch(ErrorMessagable msg) {
		haveErrors = true;
		for (ErrorListListener listener : listeners)
			listener.onReceiveMsg(msg);
	}

	/**
	 * Initialise all listeners
	 */
	public static void startCheck() {
		haveErrors = false;
		for (ErrorListListener listener : listeners)
			listener.onStartCheck();
	}

	/**
	 * Add a listener to send messages to.
	 * @param listener
	 */
	public static void addListener(ErrorListListener listener) {
		listeners.add(listener);
	}

	/**
	 * Stops error sending operations.
	 */
	public static void endCheck() {
		for (ErrorListListener listener : listeners)
			listener.onEndCheck(!haveErrors);
	}

}
