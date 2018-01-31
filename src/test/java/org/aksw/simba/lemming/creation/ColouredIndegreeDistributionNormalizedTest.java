package org.aksw.simba.lemming.creation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.aksw.simba.lemming.ColouredGraph;
import org.aksw.simba.lemming.metrics.dist.IntDistribution;
import org.aksw.simba.lemming.metrics.dist.ObjectDistribution;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredInDegreeDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredOutDegreeDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.MultipleIntDistributionMetric;
import org.junit.Test;

import com.carrotsearch.hppc.BitSet;
import org.junit.Assert;

public class ColouredIndegreeDistributionNormalizedTest {

	private static final double DELTA = 0.01;

	/*
	 * Test for FeatureGraphGenerator for ColouredIndegreeDistributions based on the
	 * ColouredInDegreeDistributionMetricTest
	 */

	final int vertexColourCount = 10;
	final int edgeColourCount = 5;
	final int numberNodes = 500;

	@Test
	public void test() {
		int edges = 0;
		FeatureGraphGenerator g = new FeatureGraphGenerator();
		BitSet[] vertexColours = new BitSet[vertexColourCount];
		// double[] colorValues = new double[vertexColourCount];
		for (int i = 0; i < vertexColourCount; i++) {
			BitSet colour = new BitSet(i);
			colour.set(i);
			vertexColours[i] = colour;
			// colorValues[i] = 3;
		}
		Map<BitSet, IntDistribution> expectedDistribution = new HashMap<BitSet, IntDistribution>();
		for (int i = 0; i < vertexColourCount; i++) {
			int[] sampleSpace = { 1, 2, 3 };
			double[] degreeValues = { 97. / (double) numberNodes, 224. / (double) numberNodes,
					179. / (double) numberNodes };
			for (int j = 0; j < sampleSpace.length; j++) {
				edges += sampleSpace[j] * degreeValues[j];
			}
			IntDistribution dist = new IntDistribution(sampleSpace, degreeValues);
			expectedDistribution.put(vertexColours[i], dist);
		}
		BitSet[] edgeColours = new BitSet[edgeColourCount];
		for (int i = 0; i < edgeColours.length; i++) {
			BitSet colour = new BitSet(i);
			colour.set(i);
			edgeColours[i] = colour;
		}
		double[] edgeColourValues = { 0.25, 0.20, 0.10, 0.15, 0.30 };
		ObjectDistribution<BitSet> edgeColourDistribution = new ObjectDistribution<>(edgeColours, edgeColourValues);
		ColouredGraph cGraph = g.generateGraphColouredInDegreeNormalized(expectedDistribution, edgeColourDistribution,
				numberNodes);
		MultipleIntDistributionMetric<BitSet> metric = new ColouredInDegreeDistributionMetric();
		// for negative testing
		// cGraph.setVertexColour(5, new BitSet(20));
		Map<BitSet, IntDistribution> distributions = metric.apply(cGraph);

		IntDistribution distribution;
		double expectedValues[];
		for (BitSet c : expectedDistribution.keySet()) {
			Assert.assertTrue("There is no distribution for colour " + c.toString(), distributions.containsKey(c));
			distribution = distributions.get(c);
			Assert.assertArrayEquals("Sampe space of colour " + c.toString() + " does not match.",
					expectedDistribution.get(c).sampleSpace, distribution.sampleSpace);
			expectedValues = expectedDistribution.get(c).values;
			for (int i = 0; i < expectedValues.length; ++i) {
				Assert.assertEquals("Error for colour " + c.toString(), expectedValues[i],
						distribution.values[i] / numberNodes, DELTA);
			}
			Assert.assertEquals("Error for colour " + c.toString(), expectedValues.length, distribution.values.length);
		}
	}

}
