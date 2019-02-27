package au.edu.anu.rscs.aot.graph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.identity.impl.PairIdentity;
class AotGraphTest {

	@Test
	void test() {
		AotGraph graph = new AotGraph();
		String rootId =  "3Worlds"+PairIdentity.LABEL_NAME_STR_SEPARATOR+"test";
		AotNode node = graph.makeTreeNode(null, rootId);
		System.out.println(node.getLabel());
		//TreeGraphFactory cannot be cast to au.edu.anu.rscs.aot.graph.AotGraph
		// null because the some fucking thing? The factory has nothing in the map???


		// Adding node to graph.nodes() - seems to be missing - can't be done outside
		// graph as nodes() returns an iterator - you can't add to an iterator
		assertTrue(node != null);
	}

}
