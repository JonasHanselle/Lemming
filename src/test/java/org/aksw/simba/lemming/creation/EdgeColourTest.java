package org.aksw.simba.lemming.creation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.aksw.simba.lemming.ColouredGraph;
import org.aksw.simba.lemming.metrics.dist.EdgeColourDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.IntDistribution;
import org.aksw.simba.lemming.metrics.dist.ObjectDistribution;
import org.aksw.simba.lemming.metrics.dist.VertexColourDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredInDegreeDistributionMetric;
import org.junit.Test;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.ObjectDoubleOpenHashMap;

import junit.framework.Assert;

public class EdgeColourTest {

	// TODO set constant for testing

	@Test
	public void test() {

		FeatureGraphGenerator g = new FeatureGraphGenerator();
		int vertexColourCount = 10;
		BitSet[] vertexColours = new BitSet[vertexColourCount];
		double[] colorValues = new double[vertexColourCount];
		for (int i = 0; i < vertexColourCount; i++) {
			BitSet colour = new BitSet(i);
			colour.set(i);
			vertexColours[i] = colour;
			colorValues[i] = 3;
		}

		Map<BitSet, IntDistribution> cIndegreeDistribution = new HashMap<BitSet, IntDistribution>();
		for (int i = 0; i < vertexColourCount; i++) {
			int[] sampleSpace = { 1, 2, 3 };
			double[] degreeValues = { 1., 1., 1. };
			IntDistribution dist = new IntDistribution(sampleSpace, degreeValues);
			cIndegreeDistribution.put(vertexColours[i], dist);
		}

		BitSet[] edgeColours = new BitSet[5];
		for (int i = 0; i < edgeColours.length; i++) {
			BitSet colour = new BitSet(i);
			colour.set(i);
			edgeColours[i] = colour;
		}
		double[] edgeColourValues = { 13., 9., 11., 17., 10. };
		ObjectDistribution<BitSet> edgeColourDistribution = new ObjectDistribution<>(edgeColours, edgeColourValues);

		ObjectDistribution<BitSet> vertexColourDistribution = new ObjectDistribution<BitSet>(vertexColours,
				colorValues);
		ColouredGraph cGraph = g.generateGraphColouredInDegree(cIndegreeDistribution,
				edgeColourDistribution);

		EdgeColourDistributionMetric metric = new EdgeColourDistributionMetric();
		ObjectDistribution<BitSet> distribution = metric.apply(cGraph);

		ObjectDoubleOpenHashMap<BitSet> expectedCounts = new ObjectDoubleOpenHashMap<BitSet>();
		for (int i = 0; i < edgeColourDistribution.getSampleSpace().length; ++i) {
			expectedCounts.put(edgeColourDistribution.getSampleSpace()[i], edgeColourDistribution.getValues()[i]);
		}

		for (int i = 0; i < distribution.sampleSpace.length; ++i) {
			Assert.assertTrue(expectedCounts.containsKey(distribution.sampleSpace[i]));
			Assert.assertEquals(expectedCounts.get(distribution.sampleSpace[i]), distribution.values[i]);
		}
		Assert.assertEquals(expectedCounts.size(), distribution.sampleSpace.length);

	}

}
