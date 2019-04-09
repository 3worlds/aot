package au.edu.anu.rscs.aot.archetype;

import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.graph.impl.DataTreeNodeImpl;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

/**
 * 
 * @author gignoux 20/3/2019
 *
 */
public class ConstraintSpec extends DataTreeNodeImpl {

	// NOTE: the constructor must be made public for the TreeFactory to find it as the TreeFactory
	// belongs to package fr.cnrs.iees.graph.impl
	public ConstraintSpec(Identity id, ReadOnlyPropertyList props, TreeNodeFactory factory) {
		super(id, props, factory);
	}

}
