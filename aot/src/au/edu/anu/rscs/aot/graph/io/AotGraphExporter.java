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

import static fr.cnrs.iees.io.parsing.impl.TreeGraphTokens.COMMENT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.graph.AotGraph;
import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.io.impl.OmugiGraphExporter;

/**
 * An Exporter for aot graphs.
 * 
 * @author Jacques Gignoux - 11 janv. 2019
 *
 */
public class AotGraphExporter extends OmugiGraphExporter {

	private Logger log = Logger.getLogger(AotGraphExporter.class.getName());

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
			AotGraph g = (AotGraph) graph;
			try {
				PrintWriter writer = new PrintWriter(file);
				Date now = new Date();
				writer.println("aot "+TreeGraphTokens.COMMENT.prefix()+" saved by "
						+AotGraphExporter.class.getSimpleName()
						+" on "+now+"\n");
				// 1. export tree
				writer.print(TreeGraphTokens.COMMENT.prefix());
				writer.print(' ');
				writer.println("TREE");
				if (g.root()!=null)
					writeTree(g.root(),writer, 0);
				// 2. export edge list
				writer.println();
				writer.print(TreeGraphTokens.COMMENT.prefix());
				writer.print(' ');
				writer.println("CROSS-LINKS");
				exportEdges(g.edges(),writer);
				writer.close();
			} catch (FileNotFoundException e) {
				log.severe("cannot save AOT graph to file \""+file.getPath()+"\" - file not found");
			}
		}
	}

}
