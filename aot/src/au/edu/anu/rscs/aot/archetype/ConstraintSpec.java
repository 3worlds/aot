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

	protected ConstraintSpec(Identity id, ReadOnlyPropertyList props, TreeNodeFactory factory) {
		super(id, props, factory);
	}

}
