package org.aksw.simba.lemming.creation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.aksw.simba.lemming.ColouredGraph;
import org.aksw.simba.lemming.colour.ColourPalette;
import org.aksw.simba.lemming.metrics.dist.IntDistribution;
import org.aksw.simba.lemming.metrics.dist.ObjectDistribution;
import org.aksw.simba.lemming.metrics.dist.VertexColourDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredInDegreeDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.MultipleIntDistributionMetric;
import org.junit.Test;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.ObjectDoubleOpenHashMap;

import org.junit.Assert;
import org.junit.Test;

public class ColouredIndegreeDistributionTest {

	private static final double DELTA = 0.000001;

	/*
	 * Test for FeatureGraphGenerator for ColouredIndegreeDistributions based on the
	 * ColouredInDegreeDistributionMetricTest
	 */

	final int vertexColourCount = 10;
	final int edgeColourCount = 5;

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
			double[] degreeValues = { (double) ThreadLocalRandom.current().nextInt(0, 250),
					(double) ThreadLocalRandom.current().nextInt(0, 250),
					(double) ThreadLocalRandom.current().nextInt(0, 250) };
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
		double[] edgeColourValues = { (double) edges / edgeColourCount + 1, (double) edges / edgeColourCount + 1,
				(double) edges / edgeColourCount + 1, (double) edges / edgeColourCount + 1,
				(double) edges / edgeColourCount + 1 };
		for (double x : edgeColourValues)
			System.out.println("edgevalue " + x);
		ObjectDistribution<BitSet> edgeColourDistribution = new ObjectDistribution<>(edgeColours, edgeColourValues);
		ColouredGraph cGraph = g.generateGraphColouredInDegree(expectedDistribution, edgeColourDistribution);
		MultipleIntDistributionMetric<BitSet> metric = new ColouredInDegreeDistributionMetric();
		// for negative testing
		// cGraph.setVertexColour(5, new BitSet(20));
		Map<BitSet, IntDistribution> distributions = metric.apply(cGraph);

		IntDistribution distribution;
		double expectedValues[];
		for (BitSet c : expectedDistribution.keySet()) {
			Assert.assertTrue("There is no distribution for colour " + c.toString(), distributions.containsKey(c));
			distribution = distributions.get(c);
			System.out.println("expected: " + expectedDistribution.get(c));
			System.out.println("achieved: " + distribution);
			Assert.assertArrayEquals("Sampe space of colour " + c.toString() + " does not match.",
					expectedDistribution.get(c).sampleSpace, distribution.sampleSpace);
			expectedValues = expectedDistribution.get(c).values;
			for (int i = 0; i < expectedValues.length; ++i) {
				Assert.assertEquals("Error for colour " + c.toString(), expectedValues[i], distribution.values[i],
						DELTA);
			}
			// System.out.println("edges " + cGraph.getGraph().getNumberOfEdges());
			Assert.assertEquals("Error for colour " + c.toString(), expectedValues.length, distribution.values.length);
		}
	}
}
