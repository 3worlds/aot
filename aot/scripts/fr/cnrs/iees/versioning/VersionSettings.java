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
package fr.cnrs.iees.versioning;

public class VersionSettings {

	// Change these fields to suit the project ====================================================

	/** The organisation name as will appear in the ivy module specification - it is a good idea
	 * to keep it consistent with the project src directory (although not required).*/
	protected static String ORG = "au.edu.anu.aot";

	/** The name of the ivy module (this will be the name of the generated jar file for
	 * dependent projects).*/
	protected static String MODULE = "aot";

	/** The ivy status of the module: integration, milestone, or release are the ivy defaults
	 * But we can define ours like bronze, gold, silver, or crap, supercrap, ultracrap. */
	protected static String STATUS = "integration";

	/** The license under which this module (= jar) is distributed */
	protected static String LICENSE = "gpl3";

	/**The url to the text of the license */
	protected static String LICENSE_URL = "https://www.gnu.org/licenses/gpl-3.0.txt";

	/**A (long) description of the ivy module */
	protected static String DESCRIPTION =
		"AOT is a method to generate elaborate software code from a series of " +
		"independent domains of knowledge. It enables one to manage and " +
		"maintain software from explicit specifications that can be translated " +
		"into any programming language.";

	/**
	 * <p>Dependencies on other modules (they will be integrated in the ivy script).</p>
	 *
	 * <p>This is a (n * 3) table of Strings.<br/>
	 * Every line is a new dependency.
	 * On every line, the 3 Strings must match the ivy fields 'org' (for organisation),
	 * 'name' (for the module name), and 'rev' (for the revision or version number). The '+' can
	 * be conveniently used to specify 'any version'.
	 * The field can be empty (just needs the external braces).<br/>
	 * Example value:
	 * <pre>{{"org.galaxy.jupiter","crap","1.0.+"},
	 * {"org.ocean.lostIsland","strungk","3.12.254"}}</pre> </p>
	 * <p>Wildcards for revision numbers are indicated <a href="http://ant.apache.org/ivy/history/master/ivyfile/dependency.html">there</a>.</p>
	 *
	 */
	protected static String[][] DEPS = {
		{"fr.cnrs.iees.omhtk", "generics", "[0.5.1,)", null},
		{"fr.cnrs.iees.omugi", "omugi", "[0.7.0,)", null},
		{"au.edu.anu.qgraph", "qgraph", "[0.3.0,)", null}
	};

	/** The name of the main class to put in the jar manifest, if any. This enables users to
	 * run the jar using this class as the entry point. Of course this must be a fully qualified
	 * valid java class name found in the jar.
	 */
	protected static String MAINCLASS = null;

}
