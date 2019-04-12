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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.QGraphException;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.graph.property.Property;
import au.edu.anu.rscs.aot.queries.Query;

import static au.edu.anu.rscs.aot.queries.base.SequenceQuery.*;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.DataTreeNode;
import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.io.FileImporter;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

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
 * <p>NB: an archetype is a {@linkplain Tree}, but a configuration/specification graph is a 
 * {@linkplain TreeGraph}.</p>
 * 
 * @author Jacques Gignoux - 6 mars 2019
 * @author Shayne Flint - looong ago		
 *
 */
public class Archetypes {
	
	/** The universal archetype - the archetype for archetypes */
	private Tree<? extends TreeNode> archetypeArchetype = null;
	
	private Logger log = Logger.getLogger(Archetypes.class.getName());
	
	private Map<Exception,Object> checkFailList = new HashMap<Exception,Object>();
	
	@SuppressWarnings("unchecked")
	public Archetypes() {
		super();
		String archetypefile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "src" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "ArchetypeArchetype.ugt";
		File file = new File(archetypefile);
		FileImporter fi = new FileImporter(file);
		archetypeArchetype  = (Tree<? extends TreeNode>) fi.getGraph();
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
	 * @param graphToCheck
	 * @return
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
	public void check(MinimalGraph<?> graphToCheck, Tree<? extends TreeNode> archetype) {
		for (TreeNode arch: archetype.nodes()) 
			if (ArchetypeRootSpec.class.isAssignableFrom(arch.getClass()))
				check(graphToCheck,(ArchetypeRootSpec)arch);
	}
	
	// returns true if the parent label (=class name) of 'child' matches one of 
	// the names passed in 'parentlist' OR if parentList=null and child is the root node
	private boolean matchesParent(TreeNode child, StringTable parentList) {
		// no parent required and root node
		if ((parentList==null) && (child.getParent()==null))
			return true;
		if ((parentList.size()==0) && (child.getParent()==null))
			return true;
		// parent exists, must match at least one id of parentList
		if (child.getParent()!=null) {
			String pid = child.getParent().id().toString();
			for (int i=0; i<parentList.size(); i++)
				if (pid.equals(parentList.getWithFlatIndex(i)))
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
	public void check(MinimalGraph<?> graphToCheck, ArchetypeRootSpec archetype) {
		checkFailList.clear();
		log.info("Checking against archetype: " + archetype.id());
		// first, check that the graph to check is a tree or a treegraph
		Tree<? extends TreeNode> treeToCheck = null;
		try {
			treeToCheck = (Tree<? extends TreeNode>) graphToCheck;
		} catch (ClassCastException e) {
			checkFailList.put(e,treeToCheck);
		}
		if (treeToCheck!=null) {
			boolean exclusive = (Boolean) archetype.properties().getPropertyValue("exclusive");
			int complyCount = 0;
			for (TreeNode tn:archetype.getChildren())
				if (NodeSpec.class.isAssignableFrom(tn.getClass())) {
					NodeSpec hasNode = (NodeSpec) tn;
					StringTable parentList = (StringTable) hasNode.properties().getPropertyValue("hasParent");
					String requiredClass = (String) hasNode.properties().getPropertyValue("isOfClass");
					int count = 0;
					for (TreeNode n:treeToCheck.nodes()) 
						if (matchesClass(n,requiredClass) && matchesParent(n,parentList)) {
							log.info("checking node: " + n.toUniqueString());
							check(n,hasNode,treeToCheck);
							count++;
							complyCount++;
					}
					IntegerRange range = null;
					if (hasNode.properties().hasProperty("multiplicity"))
						range = (IntegerRange) hasNode.properties().getPropertyValue("multiplicity");
					else
						range = new IntegerRange(0, Integer.MAX_VALUE);
					if (!range.inRange(count)) {
						String message = "Expected " + range 
							+ " nodes with parents '" + parentList 
							+ "' (got " + count
							+ ") archetype=" + hasNode.toUniqueString();
						checkFailList.put(new AotException(message),hasNode);
				}
			}
			if (exclusive && complyCount != treeToCheck.size()) {
				checkFailList.put(new AotException("Expected all nodes to comply (got " 
					+ (treeToCheck.size() - complyCount)
					+ " nodes which didn't comply)"),treeToCheck);
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
			if (DataTreeNode.class.isAssignableFrom(prop.getClass()))
				result.add((String)((DataTreeNode)prop).properties().getPropertyValue("hasName"));
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void checkQuery(Object item, NodeSpec hasNode) {
		// get the 'mustSatisfyQuery' label from the archetype factory
		String qLabel = hasNode.treeNodeFactory().treeNodeClassName(ConstraintSpec.class);
		for (ConstraintSpec queryNode: (List<ConstraintSpec>) get(hasNode, 
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
//						e.printStackTrace();
						checkFailList.put(e,queryNode);
						log.severe("Cannot get class for archetype check property" + queryNode);
					}
					parameterValues[cnt] = property.getValue();
					cnt++;
				}
			}
			Query query;
			Class<? extends Query> queryClass;
			try {
				queryClass = (Class<? extends Query>) Class.forName(queryClassName);
				// BUG: FLAW here - properties come in any order, hence the constructor
				// signature may not be correct
				// it will only work with single-argument constructors...
				// I think the only way to fix this is to impose a unique ObjectTable as an
				// entry, with properly ordered parameters
				Constructor<? extends Query> queryConstructor;
				queryConstructor = queryClass.getConstructor(parameterTypes);
				query = (Query) queryConstructor.newInstance(parameterValues);
				query.check(item);
			// this is bad. means there is an error in query name
			// NB: should it crash ? if not, group it with next catch block
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | 
					InstantiationException | IllegalAccessException | IllegalArgumentException |
					InvocationTargetException e) {
				log.severe("cannot instantiate Query '"+queryClassName+"'");
				e.printStackTrace();
			// this only means the query failed
			} catch (QGraphException e) {
				checkFailList.put(e,queryNode);
			}
		}
	}
	
	private void check(TreeNode nodeToCheck, 
			NodeSpec hasNode, 
			Tree<? extends TreeNode> treeToCheck) {
		int toNodeCount = 0;
		int fromNodeCount = 0;
		checkQuery(nodeToCheck, hasNode);
// crashes on the Trees Query because Trees is using the old system with _CHILD edges - have to fix this first.
	}
	
	// temporary, for debugging
	public String toString() {
		return archetypeArchetype.toString();
	}

}
