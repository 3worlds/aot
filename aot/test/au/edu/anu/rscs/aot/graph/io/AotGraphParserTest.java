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
package au.edu.anu.rscs.aot.graph.io;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AotGraphParserTest {

	String[] test = {"aot // saved by AotGraphExporter on Mon Jan 21 11:31:07 CET 2019\n", 
			"\n",
			"// TREE\n", 
			"3Worlds \n", 
			"	ecology my model\n", 
			"		a = java.lang.Object(null)\n", 
			"		b = java.lang.Object(null)\n", 
			"		category animal\n",
			"			x = java.lang.Object(null)\n", 
			"			y = java.lang.Object(null)\n", 
			"			z = java.lang.Object(null)\n", 
			"		system entity\n",
			"			i = java.lang.Object(null)\n", 
			"			j = java.lang.Object(null)\n", 
			"			k = java.lang.Object(null)\n", 
			"			l = java.lang.Object(null)\n", 
			"		category plant\n",
			"			x = java.lang.Object(null)\n", 
			"			y = java.lang.Object(null)\n", 
			"			z = java.lang.Object(null)\n", 
			"		engine my simulator\n",
			"		process growth\n",
			"	codeSource \n",
			"		function some computation\n", 
			"			a = java.lang.Object(null)\n", 
			"			b = java.lang.Object(null)\n", 
			"		AOTNode D89EF3043496-000001686FF6BA12-0000\n", 
			"	experiment my experiment\n",
			"\n",
			"// CROSS-LINKS\n", 
			"[system:entity] belongsTo random name [category:animal]\n", 
			"[process:growth] appliesTo  [category:animal]\n",
			"[process:growth] appliesTo  [category:plant]\n", 
			"[process:growth] function  [function:some computation]\n"};
	
	@Test
	void testParse() {
		AotGraphParser p = new AotGraphParser(new AotGraphTokenizer(test));
		p.parse();
		System.out.println(p.toString());
	}

	@Test
	void testGraph() {
		fail("Not yet implemented");
	}

}
