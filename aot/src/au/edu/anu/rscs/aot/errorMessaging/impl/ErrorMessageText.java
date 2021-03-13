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

package au.edu.anu.rscs.aot.errorMessaging.impl;

import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.Element;

/**
 * @author Ian Davies
 *
 * @date 14 Mar. 2021
 */
public class ErrorMessageText {
	private static final String language = System.getProperty("user.language");

	protected ErrorMessageText() {
	}

	public static boolean isFrench() {
		return language.equals("fr");
	}
	
	public static String[] getNODE_RANGE_INCORRECT2(String childClassName, Element target,
			IntegerRange range, Integer nChildren) {
		String am;
		String cm;
		if (isFrench()) {
			if (nChildren < range.getLast())
				am = "Ajouter le nœud <<" + childClassName + ":>> à <<" + target.toShortString() + ">>.";
			else
				am = "Supprimer le nœud <<" + childClassName + ":>> de <<" + target.toShortString() + ">>.";
			cm = "Nœuds enfants " + range + " attendus avec la référence <<" + childClassName + ">>  du parent <<"
					+ target.toShortString() + ">> mais trouvé " + nChildren + ".";
		} else  {
			if (nChildren < range.getLast())
				am = "Add node '" + childClassName + ":' to '" + target.toShortString() + "'.";
			else
				am = "Delete node '" + childClassName + ":' from '" + target.toShortString() + "'.";
			cm = "Expected " + range + " child nodes with reference '" + childClassName + "' from parent '"
					+ target.toShortString() + "' but found " + nChildren + ".";
		}
		String[] result= {am,cm};
		return result;
	};

}
