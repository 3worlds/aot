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
		String plainNode = "au.edu.anu.rscs.aot.graph.AotNode";
		// we need a function to obtain all Node class names from the archetype
		Map<String, String> labels = new HashMap<>();
		String rootLabel = "3Worlds";
		String codeSrcLabel = "codeSrc";
		labels.put(rootLabel, plainNode);
		labels.put(codeSrcLabel, plainNode);
		 // Wrong: one class can have many names!
		AotGraph graph = new AotGraph(labels);

		String name = "test";
		String rootId = rootLabel + PairIdentity.LABEL_NAME_STR_SEPARATOR + name;
		AotNode parentNode = graph.makeTreeNode(null, rootId);
		assertTrue(parentNode != null);
		// Previously a typecast error here
		// Fails: one class can have many names!
		assertTrue(parentNode.getLabel().equals(rootLabel));
		
		String srcId = codeSrcLabel + PairIdentity.LABEL_NAME_STR_SEPARATOR + name;
		AotNode childNode = graph.makeTreeNode(parentNode,srcId);
		assertTrue(childNode.getLabel().equals(codeSrcLabel));
		

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
