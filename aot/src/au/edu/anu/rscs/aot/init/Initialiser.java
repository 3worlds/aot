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
package au.edu.anu.rscs.aot.init;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A new version of Shayne's initialiser - much, much simpler.
 * @author Jacques Gignoux - 7 mai 2019
 *
 */
public class Initialiser {
	
	private SortedSet<Integer> priorities = new TreeSet<Integer>();
	private Map<Integer,List<Initialisable>> toInit = new HashMap<>();
	private List<InitialiseMessage> initFailList = new LinkedList<>();

	/**
	 * Constructor takes a list of Initialisable objects
	 * @param initList the list of objects ot initialise
	 */
	public Initialiser(Iterable<Initialisable> initList) {
		super();
		for (Initialisable init:initList) {
			int priority = init.initPriority();
			// the sorted set sorts the integers in increasing order
			priorities.add(priority);
			if (toInit.get(priority).isEmpty())
				toInit.put(priority, new LinkedList<>());
			toInit.get(priority).add(init);
		}
	}
	
	/**
	 * Initialises all objects passed to the constructor
	 * following their priority ranking, from the lowest to the highest priority
	 */
	public void initialise() {
		// the SortedSet iterator returns its content in ascending order
		for (int priority:priorities)
			for (Initialisable init:toInit.get(priority))
				try {
					init.initialise();
				}
				catch (Exception e) {
					initFailList.add(new InitialiseMessage(init,e));
				}
	}
	
	/**
	 * Returns the problems which occured during the initialisation process.
	 * @return null if no error, the error list otherwise
	 */
	public Iterable<InitialiseMessage> errorList() {
		if (initFailList.isEmpty())
			return null;
		else 
			return initFailList;
	}
	
}
