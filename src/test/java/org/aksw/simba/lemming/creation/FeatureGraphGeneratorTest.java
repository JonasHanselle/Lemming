package org.aksw.simba.lemming.creation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.aksw.simba.lemming.ColouredGraph;
import org.aksw.simba.lemming.metrics.dist.IntDistribution;
import org.aksw.simba.lemming.metrics.dist.ObjectDistribution;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredInDegreeDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredOutDegreeDistributionMetric;
import org.junit.Test;

import com.carrotsearch.hppc.BitSet;

import junit.framework.Assert;

public class FeatureGraphGeneratorTest {

	@Test
	public void test() {

		FeatureGraphGenerator g = new FeatureGraphGenerator();
		int colourCount = 10;
		BitSet[] colours = new BitSet[colourCount];
		double[] colorValues = new double[colourCount];
		for (int i = 0; i < colourCount; i++) {
			BitSet colour = new BitSet(i);
			colour.set(i);
			colours[i] = colour;
			// add some appropriate values for testing here
			colorValues[i] = 3;
		}
		Map<BitSet, IntDistribution> cIndegreeDistribution = new HashMap<BitSet, IntDistribution>();
		for (int i = 0; i < colourCount; i++) {
			int[] sampleSpace = { 1, 2, 3 };
			double[] degreeValues = { 1., 1., 1. };
			IntDistribution dist = new IntDistribution(sampleSpace, degreeValues);
			cIndegreeDistribution.put(colours[i], dist);
		}
		ObjectDistribution<BitSet> vertexColourDistribution = new ObjectDistribution<BitSet>(colours, colorValues);
		ColouredGraph cGraph = g.generateGraphColouredInDegree(vertexColourDistribution, cIndegreeDistribution, null);
		// analyze whether cGraph fulfills the desired properties
		ColouredInDegreeDistributionMetric cidm = new ColouredInDegreeDistributionMetric();
		System.out.println("" + cIndegreeDistribution);
		System.out.println("" + cidm.apply(cGraph));
		assertEquals(cidm.apply(cGraph), cIndegreeDistribution);
	}
}
