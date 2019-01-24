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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.graph.AotGraph;
import au.edu.anu.rscs.aot.graph.AotNode;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.impl.DefaultGraphFactory;
import fr.cnrs.iees.io.parsing.impl.MinimalGraphParser;
import fr.cnrs.iees.io.parsing.impl.GraphTokenizer.graphToken;
import fr.cnrs.iees.io.parsing.impl.TreeTokenizer.treeToken;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author Jacques Gignoux - 22 janv. 2019
 *
 */
//tested OK with version 0.0.5 on 23/1/2019
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
	// remind that an AotGraph is its own Node, Edge and TreeNode factory
	private AotGraph graph = null;
	
	// lazy init: nothing is done before it's needed
	public AotGraphParser(AotGraphTokenizer tokenizer) {
		super();
		this.tokenizer =tokenizer;
	}
		
	@SuppressWarnings("unused")
	private void buildGraph() {
		// parse token if not yet done
		if (lastItem==null)
			parse();
		// do nothing with graph-level properties - see later
		for (propSpec p:graphProps); 
		// setup the factories
		graph = new AotGraph();
		propertyListFactory = new DefaultGraphFactory();
		// make tree nodes
		Map<String,AotNode> nodes = new HashMap<>();
		for (treeNodeSpec ns:nodeSpecs) {
			AotNode n = null;
			AotNode parent = null;
			SimplePropertyList pl = null;
			if (!ns.props.isEmpty()) 
				pl = makePropertyList(ns.props,log);
			if (ns.parent!=null) 
				// the parent has always been set before
				parent = nodes.get(ns.parent.label.trim()+":"+ns.parent.name.trim());
			// this puts the node in the graph
			n = graph.makeTreeNode(parent,ns.label.trim(),ns.name.trim(),pl);
			nodes.put(n.classId()+":"+n.instanceId(),n);
		}
		// make cross links
		for (edgeSpec es:edgeSpecs) {
			SimplePropertyList pl = null;
			if (!es.props.isEmpty()) 
				pl = makePropertyList(es.props,log);
			String[] refs = es.start.split(":");
			String ref = refs[0].trim()+":"+refs[1].trim();
			Node start = nodes.get(ref);
			if (start==null)
				log.severe("start node "+ref+" not found for edge "+es.label+":"+es.name);
			refs = es.end.split(":");
			ref = refs[0].trim()+":"+refs[1].trim();
			Node end = nodes.get(ref);
			if (end==null)
				log.severe("end node "+ref+" not found for edge "+es.label+":"+es.name);
			if ((start!=null)&&(end!=null))
				// this attaches the edge properly to the nodes
				graph.makeEdge(start,end,es.label.trim(),es.name.trim(),pl);
		}
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
		lastNodes = new treeNodeSpec[tokenizer.treeTokenizer().maxDepth()+1];
		lastItem = itemType.GRAPH;
		// 1 analyse the tree part
		while (tokenizer.treeTokenizer().hasNext()) {
			treeToken tk = tokenizer.treeTokenizer().getNextToken();
			switch (tk.type) {
				case COMMENT:
					break;
				case LABEL:
					int level = tk.level;
					lastNodes[level] = new treeNodeSpec();
					lastNodes[level].label = tk.value;
					if (level>0)
						lastNodes[level].parent = lastNodes[level-1];
					lastItem = itemType.NODE;
					break;
				case LEVEL:
					// such tokens should never be created
					break;
				case NAME:
					level = tk.level;
					lastNodes[level].name = tk.value;
					nodeSpecs.add(lastNodes[level]);
					break;
				case PROPERTY_NAME:
					lastProp = new propSpec();
					lastProp.name = tk.value;
					break;
				case PROPERTY_TYPE:
					lastProp.type = tk.value;
					break;
				case PROPERTY_VALUE:
					lastProp.value = tk.value;
					if (lastItem==itemType.GRAPH)
						graphProps.add(lastProp);
					else
						lastNodes[tk.level-1].props.add(lastProp);
					break;
			case NODE_REF:
				throw new OmugiException("Invalid token type for a tree");
			default:
				break;
			}
		}
		// 2 analyse the cross-links
		while (tokenizer.graphTokenizer().hasNext()) {
			graphToken tk = tokenizer.graphTokenizer().getNextToken();
			switch (tk.type) {
				case COMMENT:
					break;
				case PROPERTY_NAME:
					lastProp = new propSpec();
					lastProp.name = tk.value;
					break;
				case PROPERTY_VALUE:
					lastProp.value = tk.value;
					switch (lastItem) {
						case GRAPH: // this is a graph property
							graphProps.add(lastProp);
							break;
						case NODE: // this is a node property
							// this is an error - there shouldnt be any node properties left here
							throw new AotException("There should not be any node property definition here.");
						case EDGE: // this is an edge property
							lastEdge.props.add(lastProp);
							break;
					}
					break;
				case PROPERTY_TYPE:	
					lastProp.type = tk.value;
					break;
				case LABEL:		
					switch (lastItem) {
						case GRAPH:
						case NODE:
							throw new AotException("There should not be any node definition here.");
						case EDGE:
							if (lastEdge.label==null)
								lastEdge.label = tk.value;
							else  // this is a node label
								throw new AotException("There should not be any node definition here.");
							break;
					}
					break;
				case NAME:			
					switch (lastItem) {
						case GRAPH:
							log.severe("missing node label declaration");
							break;
						case NODE:
							throw new AotException("There should not be any node definition here.");
						case EDGE:
							lastEdge.name = tk.value;
					}
					break;
				case NODE_REF:
					switch (lastItem) {
						case GRAPH:
						case NODE:
							lastEdge = new edgeSpec();
							lastEdge.start = tk.value;
							lastItem = itemType.EDGE;
							break;
						case EDGE:
							if (lastEdge.end==null) {
								lastEdge.end = tk.value;
								edgeSpecs.add(lastEdge);
							}
							else {
								lastEdge = new edgeSpec();
								lastEdge.start = tk.value;
							}
							break;
					}
					break;
			case LEVEL:
				throw new OmugiException("Invalid token type for a cross-link only graph");
			default:
				break;
			}
		}		
	}
	
	// for debugging only
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Aot graph specification\n");
		if (!graphProps.isEmpty())
			sb.append("Graph properties:\n");
		for (propSpec p:graphProps)
			sb.append('\t').append(p.toString()).append('\n');
		if (!nodeSpecs.isEmpty())
			sb.append("Nodes:\n");
		for (treeNodeSpec n:nodeSpecs) {
			sb.append(n.toString()).append('\n');
			for (propSpec p:n.props)
				sb.append("\t").append(p.toString()).append('\n');
			if (n.parent==null)
				sb.append("\tROOT NODE\n");
			else
				sb.append("\tparent ").append(n.parent.toString()).append('\n');
		}
		if (!edgeSpecs.isEmpty())
			sb.append("Edges:\n");
		for (edgeSpec e:edgeSpecs) {
			sb.append('\t').append(e.toString()).append('\n');
			for (propSpec p:e.props)
				sb.append("\t\t").append(p.toString()).append('\n');
		}
		return sb.toString();
	}

}
