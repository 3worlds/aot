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

import java.io.File;
import au.edu.anu.rscs.aot.graph.AotGraph;
import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.io.impl.OmugiGraphExporter;

/**
 * An Exporter for aot graphs.
 * 
 * @author Jacques Gignoux - 11 janv. 2019
 *
 */
//tested OK with version 0.0.5 on 23/1/2019
public class AotGraphExporter extends OmugiGraphExporter {

	// Constructors
	public AotGraphExporter(File file) {
		super(file);
	}
	
	public AotGraphExporter(String fileName) {
		super(fileName);
	}
	
	@Override
	public void exportGraph(MinimalGraph<?> graph) {
		if (AotGraph.class.isAssignableFrom(graph.getClass())) {
			header = "aot";
			exportTreeGraph((AotGraph)graph);
		}
	}

}
