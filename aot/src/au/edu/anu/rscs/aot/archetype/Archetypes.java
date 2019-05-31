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
package au.edu.anu.rscs.aot.archetype;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.graph.property.Property;
import au.edu.anu.rscs.aot.queries.Query;

import static au.edu.anu.rscs.aot.queries.base.SequenceQuery.*;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.impl.SimpleDataTreeNode;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.io.parsing.impl.NodeReference;

import static au.edu.anu.rscs.aot.queries.CoreQueries.*;

/**
 * <p>This code was initially developed by Shayne Flint as the core of Aspect-Oriented Thinking 
 * (AOT) and has been deeply refactored by J. Gignoux.</p> 
 * <p>It checks that a graph (a {@linkplain TreeGraph}, actually, i.e. a tree with cross-links) complies with
 * an <em>archetype</em> describing how graphs should look like. In other words, it is a tool
 * for checking that specifications or configurations comply with requirements. The archetype
 * describes the requirements. Notice that the first check that is provided here is to check
 * that an archetype... is an archetype, i.e. complies with the requirements needed to call 
 * a graph an archetype.</p>
 * <p>There are two sets of methods here: the <em>check(...)</em> methods perform the checks and
 * capture all errors (as exceptions) in a list. the <em>complies(...)</em> methods perform a check
 * and return false if an Exception was found during the checking process.</p>
 * <p>Afer a call to {@linkp check()}, {@link checkList()} may be called to retrieve all
 * checking errors.</p>
 * <p>NB: an archetype is a {@linkplain Tree}, but a configuration/specification graph is a 
 * {@linkplain TreeGraph}.</p>
 * <p>NOTE: by default this class spits a lot of log messages. These are logged at the
 * INFO level (="debugging"), so if you want to get rid of them just set the log level to
 * WARNING.</p>
 * 
 * @author Jacques Gignoux - 6 mars 2019
 * @author Shayne Flint - looong ago		
 *
 */
// tested OK with version 0.1.2 on 21/5/2019
public class Archetypes {
	
	/** The universal archetype - the archetype for archetypes */
	private Tree<? extends TreeNode> archetypeArchetype = null;
	
	private Logger log = Logger.getLogger(Archetypes.class.getName());
	
	private List<CheckMessage> checkFailList = new LinkedList<CheckMessage>();
	
	@SuppressWarnings("unchecked")
	public Archetypes() {
		super();
		archetypeArchetype = (Tree<? extends TreeNode>) 
			GraphImporter.importGraph("ArchetypeArchetype.ugt",this.getClass());
	}
	
	/**
	 * checks that an archetype is an archetype (= check it against archetypeArchetype)
	 * @param archetype the archetype to check
	 */
	public void checkArchetype(Tree<? extends TreeNode> graphToCheck) {		
		if (archetypeArchetype!=null)
			check(graphToCheck,archetypeArchetype);
		else
			log.warning("Archetype for archetypes not found - no check performed");
	}
	
	/**
	 * checks that an archetype is an archetype and returns true if it complies.
	 * @param graphToCheck the tree to check
	 * @return true if graphToCheck is a valid archetype
	 */
	public boolean isArchetype(Tree<? extends TreeNode> graphToCheck) {
		checkArchetype(graphToCheck);
		if (checkFailList.isEmpty())
			return true;
		else
			return false;
	}
	
	/**
	 * checks that <strong>graphToCheck</strong> complies with <strong>archetype</strong>.
	 * @param graphToCheck the graph to check (usually a Tree or a TreeGraph)
	 * @param archetype the archetype tree to check against
	 */
	public void check(NodeSet<?> graphToCheck, Tree<? extends TreeNode> archetype) {
		for (TreeNode arch: archetype.nodes()) 
			if (arch instanceof ArchetypeRootSpec)
				check(graphToCheck,(ArchetypeRootSpec)arch);
	}
	
	// returns true if the parent label (=class name) of 'child' matches one of 
	// the names passed in 'parentlist' OR if parentList=null and child is the root node
	private boolean matchesParent(TreeNode child, StringTable parentList) {
		// root node - has no parent
		if (child.getParent()==null ) {
			if (parentList==null)
				return true;
			if (parentList.size()==0)
				return true;
			for (int i=0; i<parentList.size(); i++)
				if ((parentList.getWithFlatIndex(i)==null)||(parentList.getWithFlatIndex(i).length()==0))
					return true;	
			return false;
		}
		// parent exists, must match at least one id of parentList
		else  {
//			String pid = child.getParent().classId();
			for (int i=0; i<parentList.size(); i++)
				if (NodeReference.matchesRef(child.getParent(),parentList.getWithFlatIndex(i)))
//				if (pid.equals(parentList.getWithFlatIndex(i)))
					return true;
		}
		return false;
	}
	
	private boolean matchesClass(TreeNode node, String requiredClass) {
		return node.classId().equals(requiredClass);
	}
	
	/**
	 * checks that <strong>graphToCheck</strong> complies with <strong>archetype</strong>.
	 * Here, <strong>archetype</strong> is the root node of an archetype tree.
	 * @param graphToCheck the graph to check (usually a Tree or a TreeGraph)
	 * @param archetype  the archetype root node to check against
	 */
	@SuppressWarnings("unchecked")
	public void check(NodeSet<?> graphToCheck, ArchetypeRootSpec archetype) {
		checkFailList.clear();
		log.info("Checking against archetype: " + archetype.id());
		// first, check that the graph to check is a tree or a treegraph
		Tree<? extends TreeNode> treeToCheck = null;
		try {
			treeToCheck = (Tree<? extends TreeNode>) graphToCheck;
		} catch (ClassCastException e) {
			checkFailList.add(new CheckMessage(treeToCheck,e,null));
		}
		if (treeToCheck!=null) {
			boolean exclusive = (Boolean) archetype.properties().getPropertyValue("exclusive");
			int complyCount = 0;
			for (TreeNode tn:archetype.getChildren())
				if (tn instanceof NodeSpec) {
					NodeSpec hasNode = (NodeSpec) tn;
					StringTable parentList = (StringTable) hasNode.properties().getPropertyValue("hasParent");
					String requiredClass = (String) hasNode.properties().getPropertyValue("isOfClass");
					int count = 0;
					for (TreeNode n:treeToCheck.nodes())
						if (matchesClass(n,requiredClass) && matchesParent(n,parentList)) {
							log.info("checking node: " + n.toUniqueString());
							check(n,hasNode);
							complyCount++;
							count++;
					}
					IntegerRange range = null;
					if (hasNode.properties().hasProperty("multiplicity"))
						range = (IntegerRange) hasNode.properties().getPropertyValue("multiplicity");
					else
						range = new IntegerRange(0, Integer.MAX_VALUE);
					if (!range.inRange(count)) {
						String message = "Expected " + range 
							+ " nodes of class '" + requiredClass
							+ "' with parents '" + parentList 
							+ "' (got " + count
							+ ") archetype=" + hasNode.toUniqueString();
						checkFailList.add(new CheckMessage(treeToCheck,new AotException(message),hasNode));
				}
			}
			if (exclusive && (complyCount != treeToCheck.nNodes())) {
				checkFailList.add(new CheckMessage(treeToCheck,
					new AotException("Expected all nodes to comply (got " 
						+ (treeToCheck.nNodes() - complyCount)
						+ " nodes which didn't comply)"),null));
			}
		}
	}
	
	// returns the list of property names possible for a given archetype node
	@SuppressWarnings("unchecked")
	private Set<String> getArchetypePropertyList(String archetypeNodeLabel) {
		Set<String> result = new HashSet<String>();
		TreeNode an = (TreeNode) get(archetypeArchetype.root(),
			children(),
			selectOne(hasTheName(archetypeNodeLabel)));
		for (TreeNode prop: (List<TreeNode>) get(an,
			children(),
			selectZeroOrMany(hasTheLabel("hasProperty"))))
			if (SimpleDataTreeNode.class.isAssignableFrom(prop.getClass()))
				result.add((String)((SimpleDataTreeNode)prop).properties().getPropertyValue("hasName"));
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void checkConstraints(Object item, TreeNode spec) {
		// get the 'mustSatisfyQuery' label from the archetype factory
		String qLabel = spec.factory().nodeClassName(ConstraintSpec.class);
		for (ConstraintSpec queryNode: (List<ConstraintSpec>) get(spec, 
			children(), 
			selectZeroOrMany(hasTheLabel(qLabel)))) {
			ReadOnlyPropertyList queryProps = queryNode.properties();
			String queryClassName = (String) queryProps.getPropertyValue("className");
			log.info("checking query: " + queryClassName);	
			// default properties: potential list
			Set<String> defaultProps = getArchetypePropertyList("ConstraintSpec");
			// find the default props that are effectively present in the above list and count them 
			int defaultPropCount = 0;
			for (String key:queryProps.getKeysAsSet())
				if (defaultProps.contains(key))
					defaultPropCount += 1;
			int parameterCount = queryProps.size() - defaultPropCount;
			String[] parameterNames = new String[parameterCount];
			Class<?>[] parameterTypes = new Class[parameterCount];
			Object[] parameterValues = new Object[parameterCount];
			int cnt = 0;
			for (String key : queryProps.getKeysAsSet()) {
				Property property = queryProps.getProperty(key);
				if (!defaultProps.contains(key)) {
					parameterNames[cnt] = property.getKey();
					try {
						parameterTypes[cnt] = Class.forName(property.getClassName());
					} catch (ClassNotFoundException e) {
						checkFailList.add(new CheckMessage(queryNode,e,null));
//						log.severe("Cannot get class for archetype check property" + queryNode);
//						e.printStackTrace();
					}
					parameterValues[cnt] = property.getValue();
					cnt++;
				}
			}
			Query query;
			Class<? extends Query> queryClass;
			try {
				queryClass = (Class<? extends Query>) Class.forName(queryClassName);
				// Caution here: since properties can come in any order, the Query constructors
				// have to be able to handle changes in argument order. This means
				// there must be a constructor for every possible argument order.
				// cf issue #4 in aot
				// if argument order matters ObjectTable should be used as a unique argument
				Constructor<? extends Query> queryConstructor;
				queryConstructor = queryClass.getConstructor(parameterTypes);
				query = (Query) queryConstructor.newInstance(parameterValues);
				query.check(item);
			// this is bad. means there is an error in query name
			// it should crash because archetypes are not supposed to be written by hand
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | 
					InstantiationException | IllegalAccessException | IllegalArgumentException |
					InvocationTargetException e) {
				log.severe("cannot instantiate Query '"+queryClassName+"'");
				e.printStackTrace();
			// this only means the query failed and it should be reported to the user
			} catch (Exception e) {
				checkFailList.add(new CheckMessage(item,e,queryNode));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void checkEdges(TreeNode nodeToCheck, NodeSpec hasNode) {
		// get the 'hasEdge' label from the archetype factory
		String eLabel = hasNode.factory().nodeClassName(EdgeSpec.class);
		int toNodeCount = 0;
//		int fromNodeCount = 0; // fromNode disabled for the moment
		for (EdgeSpec edgeSpec: (List<EdgeSpec>) get(hasNode,
				children(),
				selectZeroOrMany(hasTheLabel(eLabel)))) {
			log.info("checking edge spec: " + edgeSpec.toUniqueString());
			SimplePropertyList eprops = edgeSpec.properties();
			// edge spec toNode
			String toNodeRef = null;
			if (eprops.hasProperty("toNode"))
				toNodeRef = (String) eprops.getPropertyValue("toNode");
			else { // this is an error, an edge spec must have a toNode property
				Exception e = new AotException("'toNode' property missing for edge specification "+ edgeSpec);
				checkFailList.add(new CheckMessage(edgeSpec,e,null));
			}
			// edge spec multiplicity
			IntegerRange edgeMult = new IntegerRange(1, 1);
			if (eprops.hasProperty("multiplicity"))
				edgeMult = (IntegerRange) eprops.getPropertyValue("multiplicity");
			// edge spec label
			String edgeLabel = null; // valid default value ???
			if (eprops.hasProperty("isOfClass"))
				edgeLabel = (String) eprops.getPropertyValue("isOfClass");
			// edge spec id
			String edgeId = null;
			if (eprops.hasProperty("hasId"))
				edgeId = (String) eprops.getPropertyValue("hasId");
			// search for edges that point to nodes types or names listed in the spec
			List<Node> toNodes = new LinkedList<Node>();
			// for nodeToCheck to have edges, it must be a subclass of Node
			if (nodeToCheck instanceof Node) {
				Node node = (Node) nodeToCheck;
				for (Edge ed:node.edges(Direction.OUT))
					if (NodeReference.matchesRef(nodeToCheck,toNodeRef)) {
						boolean ok = true;
						// check edge label
						if (edgeLabel!=null)
							if (!ed.classId().equals(edgeLabel)) {
								Exception e = new AotException("Edge "+ed+" should be of class ["+
									edgeLabel+"]. Class ["+ed.classId()+"] found instead.");
								checkFailList.add(new CheckMessage(ed, e, edgeSpec));
								ok = false;
						}
						// check edge id
						if (edgeId!=null)
							if (!ed.id().equals(edgeId)) {
								Exception e = new AotException("Edge "+ed+" should have id ["+
									edgeId+"]. Id ["+ed.id()+"] found instead.");
								checkFailList.add(new CheckMessage(ed, e, edgeSpec));
								ok = false;
						}
						// check queries on edge
						int nprobs = checkFailList.size();
						checkConstraints(ed,edgeSpec);
						if (checkFailList.size()>nprobs)
							ok = false;
						if (ok) {
							toNodeCount++;
							toNodes.add(node); // what's the use of this list now ?
						}
				}
				// check edge multiplicity
				try {
					edgeMult.check(toNodeCount);
				} catch (Exception e) {
					checkFailList.add(new CheckMessage(node, e, edgeSpec));
				}
			}
			// else error ? we must have a Node here ?
			// edges specified but object is not a node ???
			
		} // loop on EdgeSpecs 

		// I havent put this code in (from Shayne's Archetype class) because I suspect its useless
		// I copy it here just to remember we may need it one day
		
		// 31/5/2019 We probably need it to check cross link multiplicities - at the moment we
		// get an OMhtkException  if multiplicity is not correct, that should be an AotException.
		
// check other edge counts
//
//		int otherOutCount = node.degree(Direction.OUT) - toNodeCount;
//		if (!otherOutEdges.inRange(otherOutCount)) {
//			recordError("Expected " + otherOutEdges + " other out edges from node '" + node.toShortString()
//					+ "' referenced by [" + ref + "] (found " + otherOutCount + ")", node);
//		}
//
//		int otherInCount = node.degree(Direction.IN) - fromNodeCount;
//		if (!otherInEdges.inRange(otherInCount)) {
//			recordError("Expected " + otherInEdges + " other in edges from node '" + node.toShortString()
//					+ "' referenced by [" + ref + "] (found " + otherInCount + ")", node);
	}
	
	@SuppressWarnings("unchecked")
	private void checkProperties(Object element, NodeSpec hasNode) {
		// get the 'hasProperty' label from the archetype factory
		String pLabel = hasNode.factory().nodeClassName(PropertySpec.class);
		for (PropertySpec propertyArchetype: (List<PropertySpec>) get(hasNode,
				children(),
				selectZeroOrMany(hasTheLabel(pLabel)))) {
			log.info("checking property spec: " + propertyArchetype.toUniqueString());
			SimplePropertyList pprops = propertyArchetype.properties();
			// property spec name
			String key = null;
			if (pprops.hasProperty("hasName"))
				key = (String) pprops.getPropertyValue("hasName");
			else { // this is an error, a property must have a name
				Exception e = new AotException("'hasName' property missing for property specification "+ propertyArchetype);
				checkFailList.add(new CheckMessage(propertyArchetype, e, null));
			}
			// property spec type
			String typeName = null;
			if (pprops.hasProperty("type"))
				typeName = (String) pprops.getPropertyValue("type");
			else { // this is an error, a property must have a name
				Exception e = new AotException("'type' property missing for property specification "+ propertyArchetype);
				checkFailList.add(new CheckMessage(propertyArchetype, e, null));
			}
			// property spec multiplicity
			IntegerRange multiplicity = new IntegerRange(1, 1);
			if (pprops.hasProperty("multiplicity"))
				multiplicity = (IntegerRange) pprops.getPropertyValue("multiplicity");
			else { // this is an error, a property must have a name
				Exception e = new AotException("'multiplicity' property missing for property specification "+ propertyArchetype);
				checkFailList.add(new CheckMessage(propertyArchetype, e, null));
			}
			if (element instanceof ReadOnlyDataHolder) {
				ReadOnlyPropertyList nprops = ((ReadOnlyDataHolder)element).properties(); 
				if (!nprops.hasProperty(key)) { // property not found
					if (!multiplicity.inRange(0)) { // this is an error, this property should be there!
						Exception e = new AotException("Required property '"+key+"' missing for element "+ element);
						checkFailList.add(new CheckMessage(element, e, propertyArchetype));
					}
				}
				else { // property is here
					Property prop = nprops.getProperty(key);
					Object pvalue = prop.getValue();
					String ptype = null;
					if (pvalue!=null)
						ptype = ValidPropertyTypes.typeOf(pvalue);
					if (ptype==null) { // the property type is not in the valid property type list
						Exception e = new AotException("Unknown property type for property '"+key
							+"' in element "+ element);
						checkFailList.add(new CheckMessage(element, e, propertyArchetype));
					}
					else if (!ptype.equals(typeName)) { // the property type is not the one required
						Exception e = new AotException("Property '"+key
							+"' in element '"+ element 
							+"' is not of the required type '" + typeName
							+"' (type '"+ptype
							+"' found instead)");
						checkFailList.add(new CheckMessage(element,e,propertyArchetype));
					}
					checkConstraints(prop,propertyArchetype);
				}
			}
			else {
				// properties specified but object has no property list
				Exception e = new AotException("Element '"+element+"' has no property list");
				checkFailList.add(new CheckMessage(element,e,propertyArchetype));
			}
		} // loop on PropertySpecs
	}
	
	private void check(TreeNode nodeToCheck, 
			NodeSpec hasNode) {
		checkConstraints(nodeToCheck, hasNode);
		checkEdges(nodeToCheck, hasNode);
		checkProperties(nodeToCheck,hasNode);
	}
	
	/**
	 * Get the checking errors.
	 * @return null if no checking errors, the (read-only) list of errors otherwise
	 */
	public Iterable<CheckMessage> errorList() {
		if (checkFailList.isEmpty())
			return null;
		else
			return checkFailList;
	}
	
	// temporary, for debugging
	public String toString() {
		return archetypeArchetype.toString();
	}

}
