package au.edu.anu.rscs.aot.graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.impl.TreeGraphNode;
import fr.cnrs.iees.identity.impl.PairIdentity;

class AotGraphTest {

	@Test
	void test() {
		// TODO we need a function to obtain all Node labels (classes) from the
		// archetype
		String rootLabel = "3Worlds";
		String codeSrcLabel = "codeSrc";

		AotGraph graph = new AotGraph();

		String name = "test";

		String rootId = rootLabel + PairIdentity.LABEL_NAME_STR_SEPARATOR + name;
		AotNode parentNode = graph.makeTreeNode(null, rootId);
		assertTrue(parentNode != null);
		// Previously a typecast error here in getLabel when typecasting
		// TreeGraphFactory instance as AotGraph
		assertTrue(parentNode.getLabel().equals(rootLabel));

		String srcId = codeSrcLabel + PairIdentity.LABEL_NAME_STR_SEPARATOR + name;
		AotNode childNode = graph.makeTreeNode(parentNode, srcId);
		assertTrue(childNode.getLabel().equals(codeSrcLabel));
		assertTrue(childNode.getParent().equals(parentNode));

		AotNode childNode1 = graph.makeTreeNode(parentNode, srcId);

		String srcId1 = childNode1.id();
		assertTrue(childNode1.getLabel().equals(codeSrcLabel));
		assertTrue(childNode1.getParent().equals(parentNode));
		assertTrue(srcId1.split(PairIdentity.LABEL_NAME_STR_SEPARATOR)[1].equals("test1"));

		/*
		 * Adding node to graph.nodes() - seems to be missing - can't be done outside
		 * graph as nodes() returns an iterator - you can't add to an iterator therefore
		 * I've added a private "addNode(...)" to AotGraph to be called by every
		 * "makeTreeNode(...)"??
		 */
		for (TreeGraphNode n : graph.nodes())
			System.out.println(n.toString());

	}

}
