package au.edu.anu.rscs.aot.archetype;

import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.impl.SimpleDataTreeNode;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author gignoux 20/3/2019
 *
 */
public class EdgeSpec extends SimpleDataTreeNode {

	// NOTE: the constructor must be made public for the TreeFactory to find it as the TreeFactory
	// belongs to package fr.cnrs.iees.graph.impl
	public EdgeSpec(Identity id, SimplePropertyList props, NodeFactory factory) {
		super(id, props, factory);
	}

}
