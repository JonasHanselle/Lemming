package org.aksw.simba.lemming.creation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

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

		Map<BitSet, IntDistribution> expectedDistribution = new HashMap<BitSet, IntDistribution>();
		for (int i = 0; i < vertexColourCount; i++) {
			int[] sampleSpace = { 1, 2, 3 };
			double[] degreeValues = { 1., 1., 1. };
			IntDistribution dist = new IntDistribution(sampleSpace, degreeValues);
			expectedDistribution.put(vertexColours[i], dist);
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
		ColouredGraph cGraph = g.generateGraphColouredInDegree(vertexColourDistribution, expectedDistribution,
				edgeColourDistribution);

		MultipleIntDistributionMetric<BitSet> metric = new ColouredInDegreeDistributionMetric();
		// for negative testing
		// cGraph.setVertexColour(5, new BitSet(20));
		Map<BitSet, IntDistribution> distributions = metric.apply(cGraph);

		BitSet colour;
		colour = new BitSet();

		IntDistribution distribution;
		double expectedValues[];
		for (BitSet c : expectedDistribution.keySet()) {
			Assert.assertTrue("There is no distribution for colour " + c.toString(), distributions.containsKey(c));
			distribution = distributions.get(c);
			Assert.assertArrayEquals("Sampe space of colour " + c.toString() + " does not match.",
					expectedDistribution.get(c).sampleSpace, distribution.sampleSpace);
			expectedValues = expectedDistribution.get(c).values;
			for (int i = 0; i < expectedValues.length; ++i) {
				Assert.assertEquals("Error for colour " + c.toString(), expectedValues[i], distribution.values[i],
						DELTA);
			}
			Assert.assertEquals("Error for colour " + c.toString(), expectedValues.length, distribution.values.length);
		}
	}
}
