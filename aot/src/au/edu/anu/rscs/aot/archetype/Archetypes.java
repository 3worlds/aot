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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.errorMessaging.ErrorMessagable;
import au.edu.anu.rscs.aot.errorMessaging.impl.SpecificationErrorMsg;
import au.edu.anu.rscs.aot.errorMessaging.impl.SpecificationErrors;
import au.edu.anu.rscs.aot.graph.property.Property;
import au.edu.anu.rscs.aot.queries.Queryable;

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
import fr.ens.biologie.generic.utils.Duple;
import fr.ens.biologie.generic.utils.Logging;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.io.parsing.impl.NodeReference;
import static au.edu.anu.rscs.aot.queries.base.SequenceQuery.*;
import static au.edu.anu.rscs.aot.queries.CoreQueries.*;

/**
 * <p>
 * This code was initially developed by Shayne Flint as the core of
 * Aspect-Oriented Thinking (AOT) and has been deeply refactored by J. Gignoux.
 * </p>
 * <p>
 * It checks that a graph (a {@linkplain TreeGraph}, actually, i.e. a tree with
 * cross-links) complies with an <em>archetype</em> graph (actually, a
 * {@linkplain Tree}) describing how graphs should look like. In other words, it
 * is a tool for checking that specifications or configurations comply with
 * requirements. The archetype describes the requirements. Notice that the first
 * check that is provided here is to check that an archetype... is an archetype,
 * i.e. complies with the requirements needed to call a graph an archetype.
 * </p>
 * <p>
 * There are two sets of methods here: the <em>check(...)</em> methods perform
 * the checks and capture all errors (as exceptions) in a list. the
 * <em>complies(...)</em> methods perform a check and return false if an
 * Exception was found during the checking process.
 * </p>
 * <p>
 * Afer a call to {@linkplain check()}, {@link checkList()} may be called to
 * retrieve all checking errors.
 * </p>
 * <p>
 * NB: an archetype is a {@linkplain Tree}, but a configuration/specification
 * graph is a {@linkplain TreeGraph}.
 * </p>
 * <p>
 * NOTE: by default this class spits a lot of log messages. These are logged at
 * the INFO level (="debugging"), so if you want to get rid of them just set the
 * log level to WARNING.
 * </p>
 *
 * @author Jacques Gignoux - 6 mars 2019
 * @author Shayne Flint - looong ago
 *
 */
// tested OK with version 0.1.2 on 21/5/2019
public class Archetypes implements ArchetypeArchetypeConstants {

	/** The universal archetype - the archetype for archetypes */
	private Tree<? extends TreeNode> archetypeArchetype = null;

	private static Logger log = Logging.getLogger(Archetypes.class);

	private List<ErrorMessagable> checkFailList = new LinkedList<ErrorMessagable>();

	@SuppressWarnings("unchecked")
	public Archetypes() {
		super();
		archetypeArchetype = (Tree<? extends TreeNode>) GraphImporter.importGraph("ArchetypeArchetype.ugt",
				this.getClass());
	}

	/**
	 * checks that an archetype is an archetype (= check it against
	 * archetypeArchetype)
	 *
	 * @param archetype the archetype to check
	 */
	public void checkArchetype(Tree<? extends TreeNode> graphToCheck) {
		if (archetypeArchetype != null)
			check(graphToCheck, archetypeArchetype);
		else
			log.warning("Archetype for archetypes not found - no check performed");
	}

	/**
	 * checks that an archetype is an archetype and returns true if it complies.
	 *
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
	 * checks that <strong>graphToCheck</strong> complies with
	 * <strong>archetype</strong>.
	 *
	 * @param graphToCheck the graph to check (usually a Tree or a TreeGraph)
	 * @param archetype    the archetype tree to check against
	 */
	public void check(NodeSet<?> graphToCheck, Tree<? extends TreeNode> archetype) {
		if (!(archetype.root() instanceof ArchetypeRootSpec))
			throw new AotException("Archetype does not have " + ArchetypeRootSpec.class.getSimpleName()
					+ " as its root! " + archetype.root());
		for (TreeNode arch : archetype.nodes())
			if (arch instanceof ArchetypeRootSpec)
				check(graphToCheck, (ArchetypeRootSpec) arch);
	}

	// returns true if the parent label (=class name) of 'child' matches one of
	// the names passed in 'parentlist' OR if parentList=null and child is the root
	// node
	private boolean matchesParent(TreeNode child, StringTable parentList) {
		// root node - has no parent
		if (child.getParent() == null) {
			if (parentList == null)
				return true;
			if (parentList.size() == 0)
				return true;
			for (int i = 0; i < parentList.size(); i++)
				if ((parentList.getWithFlatIndex(i) == null) || (parentList.getWithFlatIndex(i).length() == 0))
					return true;
			return false;
		}
		// parent exists, must match at least one id of parentList
		else {
			for (int i = 0; i < parentList.size(); i++)
				if (NodeReference.matchesRef(child.getParent(), parentList.getWithFlatIndex(i)))
					return true;
		}
		return false;
	}

	private boolean matchesClass(TreeNode node, String requiredClass) {
		return node.classId().equals(requiredClass);
	}

	private boolean matchesId(TreeNode node, String requiredId) {
		if (requiredId != null)
			return node.id().equals(requiredId);
		else
			return true;
	}

	// this is probably making the code very slow... if the archetype has n nodes,
	// this will cause n² loops on the archetype.
	private List<NodeSpec> getChildrenSpecs(NodeSpec parentReq, ArchetypeRootSpec archetype) {
		List<NodeSpec> result = new ArrayList<>();
		String parentClassName = (String) parentReq.properties().getPropertyValue(aaIsOfClass);
		for (TreeNode tn : archetype.getChildren())
			if (tn instanceof NodeSpec) {
				NodeSpec child = (NodeSpec) tn;
				StringTable parentList = (StringTable) child.properties().getPropertyValue(aaHasParent);
				for (int i = 0; i < parentList.size(); i++) {
					String s = parentList.getWithFlatIndex(i);
					if (s!=null)
						// NB this is not very good coding - the ":" may appear as dangerous flaw later
						if (s.equals(parentClassName + ":"))
							result.add(child);
				}
			}
		return result;
	}

	/**
	 * checks that <strong>graphToCheck</strong> complies with
	 * <strong>archetype</strong>. Here, <strong>archetype</strong> is the root node
	 * of an archetype tree.
	 *
	 * @param graphToCheck the graph to check (usually a Tree or a TreeGraph)
	 * @param archetype    the archetype root node to check against
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void check(NodeSet<?> graphToCheck, ArchetypeRootSpec archetype) {
		checkFailList.clear();
		log.info("Checking against archetype: " + archetype.id());
		// first, check that the graph to check is a tree or a treegraph
		Tree<? extends TreeNode> treeToCheck = null;
		try {
			treeToCheck = (Tree<? extends TreeNode>) graphToCheck;
		} catch (ClassCastException e) {
			throw new AotException("Graph to be checked must be a Tree.");
		}
		// Check that the tree has only one root - otherwise disaster!
		Integer nRoots = 0;
		for (TreeNode root : treeToCheck.roots()) {
			nRoots++;
		}
		if (nRoots > 1) {
			checkFailList.add(new SpecificationErrorMsg(SpecificationErrors.TREE_MULTIPLE_ROOTS,
					null,null, treeToCheck,archetype));
		}

		if (treeToCheck != null) {
			boolean exclusive = false;
			if (archetype.properties().hasProperty("exclusive"))
				exclusive = (Boolean) archetype.properties().getPropertyValue("exclusive");
			int complyCount = 0;
			List<TreeNode> compliantNodes = new ArrayList<>();
			for (TreeNode tn : archetype.getChildren())
				if (tn instanceof NodeSpec) {
					NodeSpec hasNode = (NodeSpec) tn;
					StringTable parentList = (StringTable) hasNode.properties().getPropertyValue(aaHasParent);
					String requiredClass = (String) hasNode.properties().getPropertyValue(aaIsOfClass);
					String requiredId = null;
					if (hasNode.properties().hasProperty(aaHasId))
						requiredId = (String) hasNode.properties().getPropertyValue(aaHasId);
					List<NodeSpec> childrenSpec = getChildrenSpecs(hasNode, archetype);
					// count must be made by parent, because multiplicities apply to parents
					Map<TreeNode, Integer> countByParent = new HashMap<>();
					for (TreeNode targetNode : treeToCheck.nodes()) {
						if (matchesClass(targetNode, requiredClass) && matchesParent(targetNode, parentList)
								&& matchesId(targetNode, requiredId)) {
							log.info("checking node: " + targetNode.toUniqueString());
							checkNode(targetNode, hasNode);
							complyCount++;
							compliantNodes.add(targetNode);
							TreeNode parent = targetNode.getParent();
							// counting realized multiplicity
							if (parent != null) {
								if (!countByParent.containsKey(parent))
									countByParent.put(parent, 1);
								else
									countByParent.put(parent, countByParent.get(parent) + 1);
							}
							// checking that required children are here
							for (NodeSpec childSpec : childrenSpec) {
								IntegerRange childMult = (IntegerRange) childSpec.properties()
										.getPropertyValue(aaMultiplicity);
								String childClassName = (String) childSpec.properties().getPropertyValue(aaIsOfClass);
								List<TreeNode> children = (List<TreeNode>) get(targetNode.getChildren(),
										selectZeroOrMany(hasTheLabel(childClassName)));
								if (!childMult.inRange(children.size())) {
									checkFailList.add(new SpecificationErrorMsg(
										SpecificationErrors.NODE_RANGE_INCORRECT2, null,null, targetNode,
										childClassName, childMult, children.size(), hasNode));
								}
							}
						}
					}
					// checking multiplicity within parent
					IntegerRange range = null;
					if (hasNode.properties().hasProperty(aaMultiplicity))
						range = (IntegerRange) hasNode.properties().getPropertyValue(aaMultiplicity);
					else
						range = new IntegerRange(0, Integer.MAX_VALUE);
					for (int count : countByParent.values())
						if (!range.inRange(count)) {
							SpecificationErrorMsg chkmsg = new SpecificationErrorMsg(
								SpecificationErrors.NODE_RANGE_INCORRECT1, null,null, hasNode, requiredClass,
								parentList, range, count);
							checkFailList.add(chkmsg);
						}
				}
			// PROBLEM here: nodes added in sub-archetypes are not counted as valid here...
			if (exclusive && (complyCount != treeToCheck.nNodes())) {
				checkFailList.add(new SpecificationErrorMsg(SpecificationErrors.NODE_MISSING_SPECIFICATION, null,null,
					treeToCheck, complyCount, compliantNodes));
			}
		}
	}

	// returns the list of property names possible for a given archetype node
	@SuppressWarnings("unchecked")
	private Set<String> getArchetypePropertyList(String archetypeNodeLabel) {
		Set<String> result = new HashSet<String>();
		TreeNode an = (TreeNode) get(archetypeArchetype.root(), children(), selectOne(hasTheName(archetypeNodeLabel)));
		for (TreeNode prop : (List<TreeNode>) get(an, children(), selectZeroOrMany(hasTheLabel(aaHasProperty))))
			if (SimpleDataTreeNode.class.isAssignableFrom(prop.getClass()))
				result.add((String) ((SimpleDataTreeNode) prop).properties().getPropertyValue(aaHasName));
		return result;
	}

	@SuppressWarnings("unchecked")
	private void checkConstraints(Object item, TreeNode spec) {
		// get the 'mustSatisfyQuery' label from the archetype factory
		String qLabel = spec.factory().nodeClassName(ConstraintSpec.class);
		for (ConstraintSpec queryNode : (List<ConstraintSpec>) get(spec, children(),
				selectZeroOrMany(hasTheLabel(qLabel)))) {
			ReadOnlyPropertyList queryProps = queryNode.properties();
			String queryClassName = (String) queryProps.getPropertyValue(aaClassName);
			log.info("checking query: " + queryClassName);
			// default properties: potential list
			Set<String> defaultProps = getArchetypePropertyList("ConstraintSpec");
			// find the default props that are effectively present in the above list and
			// count them
			int defaultPropCount = 0;
			for (String key : queryProps.getKeysAsSet())
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
						// IDD: the property class is unknown
					} catch (ClassNotFoundException e) {
						checkFailList.add(new SpecificationErrorMsg(SpecificationErrors.QUERY_PROPERTY_CLASS_UNKNOWN,
								null,null,e, queryNode, property));
					}
					parameterValues[cnt] = property.getValue();
					cnt++;
				}
			}
			Queryable query = null;
			Class<? extends Queryable> queryClass;
			try {
				queryClass = (Class<? extends Queryable>) Class.forName(queryClassName);
				/**
				 * Caution here: since properties can come in any order, the Query constructors
				 * have to be able to handle changes in argument order. This means there must be
				 * a constructor for every possible argument order. cf issue #4 in aot. If
				 * argument order matters a Table should be used as a unique argument.
				 */
				Constructor<? extends Queryable> queryConstructor;
				queryConstructor = queryClass.getConstructor(parameterTypes);
				query = (Queryable) queryConstructor.newInstance(parameterValues);
				query.submit(item);
				// this to handle sub-archetypes, which return a list of check messages
//				if (query.getResult() instanceof Iterable<?>)
				// TODO: Test this - can this work?? IDD
				if (query.result() instanceof Iterable<?>) {
					for (Object o : (Iterable<?>) query.result())
						if (o instanceof ErrorMessagable) {
//							System.out.println("From sub-archetype: " + o);
							checkFailList.add((ErrorMessagable) o);
						}
				} else {
					String queryNameStr = query.getClass().getSimpleName();
					String msg = query.errorMsg();
					if (msg != null) {
						if (item instanceof Edge)
							checkFailList.add(new SpecificationErrorMsg(SpecificationErrors.EDGE_QUERY_UNSATISFIED, query.actionMsg(),msg,
									item, queryNameStr,queryNode));
						else if (item instanceof TreeNode)
							checkFailList.add(new SpecificationErrorMsg(SpecificationErrors.NODE_QUERY_UNSATISFIED,query.actionMsg(), msg,
									item,queryNameStr,queryNode));
						else if (item instanceof Property)
							checkFailList.add(new SpecificationErrorMsg(SpecificationErrors.PROPERTY_QUERY_UNSATISFIED,
									query.actionMsg(),msg, item, queryNameStr,queryNode));
						else
							checkFailList.add(new SpecificationErrorMsg(SpecificationErrors.ITEM_QUERY_UNSATISFIED,query.actionMsg(), msg,
									item, queryNameStr,queryNode));
					}
				}
			} catch (Exception e) {
				log.severe("Cannot instantiate Query '" + queryClassName + "'");
				e.printStackTrace();
				// this only means the query failed and it should be reported to the user
			} // catch (Exception e) {
		}
	}

	private boolean edgeLabelMatch(Edge e, String label) {
		if (label != null)
			return (e.classId().equals(label));
		return false;
	}

	@SuppressWarnings("unchecked")
	private void checkEdges(TreeNode nodeToCheck, NodeSpec hasNode) {
		// get the 'hasEdge' label from the archetype factory
		String eLabel = hasNode.factory().nodeClassName(EdgeSpec.class);
//		int toNodeCount = 0;
//		int fromNodeCount = 0; // fromNode disabled for the moment
		for (EdgeSpec edgeSpec : (List<EdgeSpec>) get(hasNode, children(), selectZeroOrMany(hasTheLabel(eLabel)))) {
			log.info("checking edge spec: " + edgeSpec.toUniqueString());
			SimplePropertyList eprops = edgeSpec.properties();
			// edge spec toNode
			String toNodeRef = null;
			if (eprops.hasProperty(aaToNode))
				toNodeRef = (String) eprops.getPropertyValue(aaToNode);
			else { // this is an error, an edge spec must have a toNode property
				checkFailList.add(
					new SpecificationErrorMsg(SpecificationErrors.PROPERTY_MISSING, null,null, nodeToCheck,
					edgeSpec, aaToNode));
			}
			// edge spec fromNode (= the parent hasNode class type)
			String fromNodeRef = null;
			if (hasNode.properties().hasProperty(aaIsOfClass))
				fromNodeRef = (String) hasNode.properties().getPropertyValue(aaIsOfClass);
			else { // error, parent must have a class
				String msg = "'" + aaIsOfClass + "' property missing for edge 'from' node specification " + edgeSpec;
				checkFailList.add(
					new SpecificationErrorMsg(SpecificationErrors.PROPERTY_MISSING, null,msg, nodeToCheck,
					edgeSpec, aaIsOfClass));
			}
			// edge spec multiplicity
			IntegerRange edgeMult = new IntegerRange(1, 1);
			if (eprops.hasProperty(aaMultiplicity))
				edgeMult = (IntegerRange) eprops.getPropertyValue(aaMultiplicity);
			// edge spec label
			String edgeLabel = null; // valid default value ???
			if (eprops.hasProperty(aaIsOfClass))
				edgeLabel = (String) eprops.getPropertyValue(aaIsOfClass);
			// edge spec id
			String edgeId = null;
			if (eprops.hasProperty(aaHasId))
				edgeId = (String) eprops.getPropertyValue(aaHasId);
			// search for edges that point to nodes types or names listed in the spec
			List<Duple<Node, Node>> edgeEnds = new LinkedList<>();
			// for nodeToCheck to have edges, it must be a subclass of Node
			if (nodeToCheck instanceof Node) {
				Node node = (Node) nodeToCheck;
				for (Edge ed : node.edges(Direction.OUT)) {
					// check edge class exists
					if (ed.factory().edgeClass(ed.classId()) == null) {
						checkFailList.add(
							new SpecificationErrorMsg(SpecificationErrors.EDGE_CLASS_UNKNOWN,null, null, ed,
							edgeSpec, edgeLabel));
					}
					if (NodeReference.matchesRef((TreeNode) ed.endNode(), toNodeRef)
							&& NodeReference.matchesRef((TreeNode) ed.startNode(), fromNodeRef)
							&& edgeLabelMatch(ed, edgeLabel)) {
						boolean ok = true;
						// check edge label
						if (edgeLabel != null)
							if (!ed.classId().equals(edgeLabel)) {
								checkFailList.add(
									new SpecificationErrorMsg(SpecificationErrors.EDGE_CLASS_INCORRECT,
									null,null, ed, edgeSpec, edgeLabel));
								ok = false;
							}
						// check edge id
						if (edgeId != null)
							if (!ed.id().equals(edgeId)) {
								String msg = "Edge " + ed + " should have id [" + edgeId + "]. Id [" + ed.id()
									+ "] found instead.";
								checkFailList.add(
									new SpecificationErrorMsg(SpecificationErrors.EDGE_ID_INCORRECT,null, msg,
									ed, edgeId));
								ok = false;
							}
						// above errors cause an edge multiplicity error
						if (ok)
							edgeEnds.add(new Duple<>(ed.startNode(), ed.endNode()));
						// check queries on edge & edge properties
						// these do not cause multiplicity errors
						if (ed instanceof ReadOnlyDataHolder)
							checkEdgeProperties(ed, edgeSpec);
						checkConstraints(ed, edgeSpec);
					}
				} // loop on edges
				// check edge multiplicity
				try {
					edgeMult.check(edgeEnds.size());
				} catch (Exception e) {
					checkFailList.add(new SpecificationErrorMsg(
						SpecificationErrors.EDGE_RANGE_INCORRECT,null, null,
						nodeToCheck, edgeMult, edgeLabel, toNodeRef, edgeEnds.size(), edgeSpec));
				}
			}
			// else error ? we must have a Node here ?
			// edges specified but object is not a node ???

		} // loop on EdgeSpecs

		// I havent put this code in (from Shayne's Archetype class) because I suspect
		// its useless
		// I copy it here just to remember we may need it one day

// check other edge counts
////
//		IntegerRange otherOutEdges = new IntegerRange(0, Integer.MAX_VALUE);
//		int otherOutCount = nodeToCheck.degree(Direction.OUT) - toNodeCount;
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
	private void checkNodeProperties(Object element, NodeSpec hasNode) {
		// get the 'hasProperty' label from the archetype factory
		String pLabel = hasNode.factory().nodeClassName(PropertySpec.class);
		List<PropertySpec> lp = (List<PropertySpec>) get(hasNode, children(), selectZeroOrMany(hasTheLabel(pLabel)));
		checkProperties(element, lp);
	}

	@SuppressWarnings("unchecked")
	private void checkEdgeProperties(Object element, EdgeSpec hasEdge) {
		// get the 'hasProperty' label from the archetype factory
		String pLabel = hasEdge.factory().nodeClassName(PropertySpec.class);
		List<PropertySpec> lp = (List<PropertySpec>) get(hasEdge, children(), selectZeroOrMany(hasTheLabel(pLabel)));
		checkProperties(element, lp);
	}

	private void checkProperties(Object element, List<PropertySpec> pSpecList) {
		for (PropertySpec propertyArchetype : pSpecList) {
			log.info("checking property spec: " + propertyArchetype.toUniqueString());
			SimplePropertyList pprops = propertyArchetype.properties();
			// property spec name
			String key = null;
			if (pprops.hasProperty(aaHasName))
				key = (String) pprops.getPropertyValue(aaHasName);
			else { // this is an error, a property must have a name
				checkFailList.add(
					new SpecificationErrorMsg(SpecificationErrors.PROPERTY_MISSING,null, null, element,
					propertyArchetype, aaHasName));
			}
			// property spec type
			String typeName = null;
			if (pprops.hasProperty(aaType))
				typeName = (String) pprops.getPropertyValue(aaType);
			else { // this is an error, a property must have a name
				String msg = "'" + aaType + "' property missing for property specification " + propertyArchetype;
				checkFailList.add(
					new SpecificationErrorMsg(SpecificationErrors.PROPERTY_MISSING,null, msg, element,
					propertyArchetype, aaType));
			}
			// property spec multiplicity
			IntegerRange multiplicity = new IntegerRange(1, 1);
			if (pprops.hasProperty(aaMultiplicity))
				multiplicity = (IntegerRange) pprops.getPropertyValue(aaMultiplicity);
			else { // this is an error, a property must have a name
				String msg = "'" + aaMultiplicity + "' property missing for property specification "
					+ propertyArchetype;
				checkFailList.add(
					new SpecificationErrorMsg(SpecificationErrors.PROPERTY_MISSING, null,msg, element,
					propertyArchetype, aaMultiplicity));
			}
			if (element instanceof ReadOnlyDataHolder) {
				ReadOnlyPropertyList nprops = ((ReadOnlyDataHolder) element).properties();
				if (!nprops.hasProperty(key)) { // property not found
					if (!multiplicity.inRange(0)) { // this is an error, this property should be there!
						String msg = "Required property '" + key + "' missing for element " + element;
						checkFailList.add(
							new SpecificationErrorMsg(SpecificationErrors.PROPERTY_MISSING,null, msg, element,
							propertyArchetype, key));
					}
				} else { // property is here
					Property prop = nprops.getProperty(key);
					Object pvalue = prop.getValue();
					String ptype = null;
					if (pvalue != null) {
						ptype = ValidPropertyTypes.typeOf(pvalue);
						// JG 13/8/2021 hack for properties represented by a superclass (usually, an interface
						// - case of the geometric classes in uit
						if (ptype==null) {
							Class<?>[] interfaces = pvalue.getClass().getInterfaces();
							for (Class<?> ic:interfaces) {
								if (ValidPropertyTypes.isValid(ic.getSimpleName()))
									ptype = ic.getSimpleName();
							}
							if (ptype==null) {// Last chance!
								interfaces = pvalue.getClass().getSuperclass().getInterfaces();
								for (Class<?> ic:interfaces) {
									if (ValidPropertyTypes.isValid(ic.getSimpleName()))
										ptype = ic.getSimpleName();
								}
							}
						}
						// end hack
					}
					if (ptype == null) { // the property type is not in the valid property type list
						checkFailList.add(
							new SpecificationErrorMsg(SpecificationErrors.PROPERTY_UNKNOWN, null,null, element,
							propertyArchetype, key));
					} else if (!ptype.equals(typeName)) { // the property type is not the one required
						checkFailList.add(
							new SpecificationErrorMsg(SpecificationErrors.PROPERTY_TYPE_INCORRECT, null,null,
							element, propertyArchetype, key, typeName, ptype));
					}
					checkConstraints(prop, propertyArchetype);
				}
			} else {
				// properties specified but object has no property list
				checkFailList.add(
					new SpecificationErrorMsg(SpecificationErrors.ELEMENT_MISSING_PROPERTY_LIST,null, null,
					element, propertyArchetype));
			}
		} // loop on PropertySpecs
	}

	private void checkNode(TreeNode nodeToCheck, NodeSpec hasNode) {
		checkConstraints(nodeToCheck, hasNode);
		checkEdges(nodeToCheck, hasNode);
		checkNodeProperties(nodeToCheck, hasNode);
	}

	/**
	 * Get the checking errors.
	 *
	 * @return null if no checking errors, the (read-only) list of errors otherwise
	 */
	public Iterable<ErrorMessagable> errorList() {
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
