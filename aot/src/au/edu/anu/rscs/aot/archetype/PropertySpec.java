package au.edu.anu.rscs.aot.archetype;

import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.impl.SimpleDataTreeNode;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.ExtendablePropertyListImpl;

/**
 * 
 * @author gignoux 20/3/2019
 *
 */
public class PropertySpec extends SimpleDataTreeNode {

	// NOTE: the constructor must be made public for the TreeFactory to find it as the TreeFactory
	// belongs to package fr.cnrs.iees.graph.impl
	public PropertySpec(Identity id, SimplePropertyList props, NodeFactory factory) {
		super(id, props, factory);
	}

	public PropertySpec(Identity id, NodeFactory factory) {
		super(id, new ExtendablePropertyListImpl(), factory);
	}

}
