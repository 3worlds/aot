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
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.graph.AotGraph;
import fr.cnrs.iees.graph.io.GraphImporter;

/**
 * An Importer for aot graphs in plain text format
 * 
 * @author Jacques Gignoux - 23 janv. 2019
 *
 */
// tested OK with version 0.0.5 on 23/1/2019
public class AotGraphImporter implements GraphImporter {

	private Logger log = Logger.getLogger(AotGraphImporter.class.getName());
	private List<String> lines = null;
	private AotGraphTokenizer tokenizer = null;
	private AotGraphParser parser = null;
	
	public AotGraphImporter(File infile) {
		super();
		try {
			lines = Files.readAllLines(infile.toPath());
			String s = lines.get(0).trim();
			String[] ss = new String[1];
			if (s.startsWith("aot")) {
				tokenizer = new AotGraphTokenizer(lines.toArray(ss));
				parser = new AotGraphParser(tokenizer);
			}
			else
				log.severe("unrecognized file format - unable to load file \""+infile.getName()+"\"");
		} catch (IOException e) {
			log.severe("file could not be read - unable to load file \""+infile.getName()+"\"");
		}
	}

    public AotGraph getGraph() {
    	return parser.graph();
    }

}
