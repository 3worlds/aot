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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import au.edu.anu.rscs.aot.util.IntegerRange;
import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.io.FileImporter;

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
	
	private List<Exception> checkFailList = new LinkedList<Exception>();
	
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
		log.info("Checking against archetype: " + archetype);
		// first, check that the graph to check is a tree or a treegraph
		Tree<? extends TreeNode> treeToCheck = null;
		try {
			treeToCheck = (Tree<? extends TreeNode>) graphToCheck;
		} catch (ClassCastException e) {
			checkFailList.add(e);
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
						checkFailList.add(new AotException(message));
				}
			}
			if (exclusive && complyCount != treeToCheck.size()) {
				checkFailList.add(new AotException("Expected all nodes to comply (got " 
					+ (treeToCheck.size() - complyCount)
					+ " nodes which didn't comply)"));
			}
		}
	}
	
	private void check(TreeNode nodeToCheck, 
			NodeSpec hasNode, 
			Tree<? extends TreeNode> treeToCheck) {
		// TODO: code this
	}
	
	// temporary, for debugging
	public String toString() {
		return archetypeArchetype.toString();
	}

}
