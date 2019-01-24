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

import java.io.File;

import org.junit.jupiter.api.Test;

import au.edu.anu.rscs.aot.graph.AotGraph;

/**
 * 
 * @author Jacques Gignoux - 23 janv. 2019
 *
 */
class AotGraphImporterTest {

	@Test
	void testGetGraph() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
				+ File.separator + "test" 
				+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
				+ File.separator + "bidon.aot";		
		File f = new File(testfile);
		AotGraphImporter gi = new AotGraphImporter(f);
		AotGraph g = gi.getGraph();
		System.out.println(g);
		assertTrue(g.toDetailedString().endsWith("(11 tree nodes / 4 cross-links) = {ecology:my model=[↑3Worlds: ↓category:animal ↓system:entity ↓category:plant ↓engine:my simulator ↓process:growth a=null b=null],codeSource:=[↑3Worlds: ↓function:some computation ↓AOTNode:D89EF3043496-0000016880369664-0001],function:some computation=[↑codeSource: ←process:growth a=null b=null],category:animal=[↑ecology:my model ←system:entity ←process:growth x=null y=null z=null],system:entity=[↑ecology:my model →category:animal i=null j=null k=null l=null],experiment:my experiment=[↑3Worlds:],category:plant=[↑ecology:my model ←process:growth x=null y=null z=null],engine:my simulator=[↑ecology:my model],3Worlds:=[ROOT ↓ecology:my model ↓codeSource: ↓experiment:my experiment],AOTNode:D89EF3043496-0000016880369664-0001=[↑codeSource:],process:growth=[↑ecology:my model →category:animal →category:plant →function:some computation]}"));
	}

}
