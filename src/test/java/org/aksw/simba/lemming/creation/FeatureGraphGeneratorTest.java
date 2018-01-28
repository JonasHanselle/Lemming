package org.aksw.simba.lemming.creation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.aksw.simba.lemming.ColouredGraph;
import org.aksw.simba.lemming.metrics.dist.EdgeColourDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.IntDistribution;
import org.aksw.simba.lemming.metrics.dist.ObjectDistribution;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredInDegreeDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredOutDegreeDistributionMetric;
import org.junit.Test;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.cursors.ObjectCursor;

import junit.framework.Assert;

public class FeatureGraphGeneratorTest {

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
			// add some appropriate values for testing here
			colorValues[i] = 3;
		}
		
		for(int i = 0; i < vertexColourCount; i++) {
			System.out.println(vertexColours[i]);
		}
		
		Map<BitSet, IntDistribution> cIndegreeDistribution = new HashMap<BitSet, IntDistribution>();
		for (int i = 0; i < vertexColourCount; i++) {
			int[] sampleSpace = { 1, 2, 3 };
			double[] degreeValues = { 1., 1., 1. };
			IntDistribution dist = new IntDistribution(sampleSpace, degreeValues);
			cIndegreeDistribution.put(vertexColours[i], dist);
		}
		
		BitSet[] edgeColours = new BitSet[5];
		for(int i = 0; i < edgeColours.length; i++){
			BitSet colour = new BitSet(i);
			colour.set(i);
			edgeColours[i] = colour;
		}
		for(BitSet ec : edgeColours)
			System.out.println(ec);
		double[] edgeColourValues = {13.,9.,11.,17.,10.};
		ObjectDistribution<BitSet> edgeColourDistribution = new ObjectDistribution<>(edgeColours, edgeColourValues);
		
 		ObjectDistribution<BitSet> vertexColourDistribution = new ObjectDistribution<BitSet>(vertexColours, colorValues);
		ColouredGraph cGraph = g.generateGraphColouredInDegree(vertexColourDistribution, cIndegreeDistribution);
		// analyze whether cGraph fulfills the desired properties
		ColouredInDegreeDistributionMetric cidm = new ColouredInDegreeDistributionMetric();
		assertEquals(cIndegreeDistribution, cidm.apply(cGraph));
		EdgeColourDistributionMetric ecdm = new EdgeColourDistributionMetric();
		ColouredGraph cGraph2 = g.generateGraphColouredInDegree(vertexColourDistribution, cIndegreeDistribution, edgeColourDistribution);
//		assertEquals(edgeColourDistribution, ecdm.apply(cGraph2));
		System.out.println(edgeColourDistribution);
		System.out.println(ecdm.apply(cGraph2));
		System.out.println(""+cGraph2.getEdgeColour(15));
	}
}
