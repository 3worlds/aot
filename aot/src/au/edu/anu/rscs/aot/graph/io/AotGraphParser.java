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

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.graph.AotGraph;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.io.parsing.impl.MinimalGraphParser;

public class AotGraphParser extends MinimalGraphParser {

	private Logger log = Logger.getLogger(AotGraphParser.class.getName());

	//----------------------------------------------------
	// which type of item is currently being constructed
	private enum itemType {
		GRAPH,
		NODE,
		EDGE
	}
	// the tokenizer used to read the file
	private AotGraphTokenizer tokenizer = null;
	
	// the factories used to build the graph
	private NodeFactory nodeFactory = null;
	private EdgeFactory edgeFactory = null;
	
	// the list of specifications built from the token list
	private List<propSpec> graphProps = new LinkedList<propSpec>();
	private List<treeNodeSpec> nodeSpecs =  new LinkedList<treeNodeSpec>();
	private List<edgeSpec> edgeSpecs =  new LinkedList<edgeSpec>();
	
	// the last processed item
	private itemType lastItem = null;
	private propSpec lastProp = null;
	private treeNodeSpec[] lastNodes = null;
	private edgeSpec lastEdge = null;
	
	// the result of this parsing
	private AotGraph graph = null;
	
	// lazy init: nothing is done before it's needed
	public AotGraphParser(AotGraphTokenizer tokenizer) {
		super();
		this.tokenizer =tokenizer;
	}
	
	private void buildGraph() {
		// parse token if not yet done
		if (lastItem==null)
			parse();
		
	}

	@Override
	public AotGraph graph() {
		if (graph==null)
			buildGraph();
		return graph;
	}

	@Override
	protected void parse() {
		if (!tokenizer.tokenized())
			tokenizer.tokenize();
		lastNodes = new treeNodeSpec[tokenizer.maxDepth()+1];
		lastItem = itemType.GRAPH;

	}

}
