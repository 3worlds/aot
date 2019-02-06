package au.edu.anu.rscs.aot.graph;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AotGraphTest {

	@Test
	void test() {
		AotGraph graph = new AotGraph();
		// crash because factory contains ref to generic TreeGraphFactory. Instead the
		// hierarchy could be changed so AotGraph implements treeGraphFactory, then
		// there
		// is no need for a generic construction through reflection.
		AotNode node = graph.makeTreeNode(null, "3Worlds:test");

		// Adding node to graph.nodes() - seems to be missing - can't be done outside
		// graph as nodes() returns an iterator - you can't add to an iterator
		assertTrue(node != null);
	}

}
