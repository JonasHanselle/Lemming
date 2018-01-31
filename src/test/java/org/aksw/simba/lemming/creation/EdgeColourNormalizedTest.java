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

public class EdgeColourNormalizedTest {
	
	private static final double DELTA = 0.01;

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
				edges += sampleSpace[j] * (degreeValues[j] * numberNodes + 1);
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

		EdgeColourDistributionMetric metric = new EdgeColourDistributionMetric();
		ObjectDistribution<BitSet> distribution = metric.apply(cGraph);

		ObjectDoubleOpenHashMap<BitSet> expectedCounts = new ObjectDoubleOpenHashMap<BitSet>();
		for (int i = 0; i < edgeColourDistribution.getSampleSpace().length; ++i) {
			expectedCounts.put(edgeColourDistribution.getSampleSpace()[i], edgeColourDistribution.getValues()[i]);
		}

		for (int i = 0; i < distribution.sampleSpace.length; ++i) {			
			Assert.assertTrue(expectedCounts.containsKey(distribution.sampleSpace[i]));
			Assert.assertEquals(expectedCounts.get(distribution.sampleSpace[i]), distribution.values[i]/edges, DELTA);
		}
		Assert.assertEquals(expectedCounts.size(), distribution.sampleSpace.length);
	}
}
