package org.aksw.simba.lemming.creation;

import java.util.HashMap;
import java.util.Map;
import org.aksw.simba.lemming.ColouredGraph;
import org.aksw.simba.lemming.metrics.dist.IntDistribution;
import org.aksw.simba.lemming.metrics.dist.ObjectDistribution;
//import org.aksw.simba.lemming.metrics.dist.VertexColourDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredInDegreeDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredOutDegreeDistributionMetric;

import com.carrotsearch.hppc.BitSet;

/**
 * !!! PROTOTYPICAL !!! This class implements algorithms for generating coloured
 * graphs according to a given set of features
 * 
 * @author Jonas Hanselle (jmhansel@campus.uni-paderborn.de)
 *
 */
public class FeatureGraphGenerator {

	public FeatureGraphGenerator() {

	}

	/**
	 * 
	 * @param vertexColourDistribution
	 *            Desired colour distribution over vertices
	 * @param colouredInDegreeDistribution
	 *            Indegree distribution for each colour, MUST MATCH WITH
	 *            vertexColourDistribution AND edgeColourDistribution!
	 * @param edgeColourDistribution
	 *            Desired colour distribution over edges
	 * @return {@link ColouredGraph} object with the desired properties
	 */
	public ColouredGraph generateGraphColouredInDegree(ObjectDistribution<BitSet> vertexColourDistribution,
			Map<BitSet, IntDistribution> colouredInDegreeDistribution,
			ObjectDistribution<BitSet> edgeColourDistribution) {

		ColouredGraph cGraph = new ColouredGraph();
		// add vertices of desired colours to the graph
		for (int i = 0; i < vertexColourDistribution.getSampleSpace().length; i++) {
			int currentCount = (int) vertexColourDistribution.getValues()[i];
			for (int j = 0; j < currentCount; j++) {
				cGraph.addVertex(vertexColourDistribution.getSampleSpace()[i]);
			}
		}
		System.out.println("Size: " + cGraph.getVertices().size());
		// add edges to the graph
		if (colouredInDegreeDistribution != null) {
			BitSet[] inColours = vertexColourDistribution.getSampleSpace();
			// System.out.println("colours: " + inColours.length);
			int currentNode = 0;
			System.out.println("colors: " + inColours.length);
			for (BitSet col : inColours) {
				IntDistribution currentDist = colouredInDegreeDistribution.get(col);
				for (int i = 0; i < currentDist.getValues().length; i++) {
					for (int k = 0; k < (int) currentDist.getValues()[i]; k++) {
						for (int j = 0; j < (int) currentDist.getSampleSpace()[i]; j++) {
							// System.out.println("currentDist value: " + (int)currentDist.getValues()[i]);
							int candidate = 0;
							if (candidate == currentNode)
								candidate++;
							cGraph.addEdge(candidate, currentNode);
							candidate++;
						}
					}
					currentNode++;
				}
			}
		}
		// TODO apply edge colours according to distribution
		return cGraph;
	}

	/**
	 * 
	 * @param vertexColourDistribution
	 *            Desired colour distribution over vertices
	 * @param colouredOutDegreeDistribution
	 *            Indegree distribution for each colour, MUST MATCH WITH
	 *            vertexColourDistribution AND edgeColourDistribution!
	 * @param edgeColourDistribution
	 *            Desired colour distribution over edges colour distribution over
	 *            edges
	 * @return {@link ColouredGraph} object with the desired properties
	 */
	public ColouredGraph generateGraphColouredOutDegree(ObjectDistribution<BitSet> vertexColourDistribution,
			Map<BitSet, IntDistribution> colouredOutdegreeDistribution,
			ObjectDistribution<BitSet> edgeColourDistribution) {

		ColouredGraph cGraph = new ColouredGraph();
		// add vertices of desired colours to the graph
		for (int i = 0; i < vertexColourDistribution.getSampleSpace().length; i++) {
			int currentCount = (int) vertexColourDistribution.getValues()[i];
			for (int j = 0; j < currentCount; j++) {
				cGraph.addVertex(vertexColourDistribution.getSampleSpace()[i]);
			}
		}
		System.out.println("Size: " + cGraph.getVertices().size());
		// add edges to the graph
		if (colouredOutdegreeDistribution != null) {
			BitSet[] inColours = vertexColourDistribution.getSampleSpace();
			// System.out.println("colours: " + inColours.length);
			int currentNode = 0;
			System.out.println("colors: " + inColours.length);
			for (BitSet col : inColours) {
				IntDistribution currentDist = colouredOutdegreeDistribution.get(col);
				for (int i = 0; i < currentDist.getValues().length; i++) {
					for (int k = 0; k < (int) currentDist.getValues()[i]; k++) {
						for (int j = 0; j < (int) currentDist.getSampleSpace()[i]; j++) {
							// System.out.println("currentDist value: " + (int)currentDist.getValues()[i]);
							int candidate = 0;
							if (candidate == currentNode)
								candidate++;
							cGraph.addEdge(currentNode, candidate);
							candidate++;
						}
					}
					currentNode++;
				}
			}
		}
		// TODO apply edge colours according to distribution
		return cGraph;
	}

	// this entire function is only used for debugging
	public static void main(String[] args) {
		FeatureGraphGenerator g = new FeatureGraphGenerator();
		int colourCount = 10;
		BitSet[] colours = new BitSet[colourCount];
		double[] colorValues = new double[colourCount];
		for (int i = 0; i < colourCount; i++) {
			BitSet colour = new BitSet(i);
			colour.set(i);
			colours[i] = colour;
			// add some appropriate values for testing here
			// colorValues[i] = ThreadLocalRandom.current().nextInt(3, 5);
			colorValues[i] = 3;
		}
		Map<BitSet, IntDistribution> cIndegreeDistribution = new HashMap<BitSet, IntDistribution>();
		for (int i = 0; i < colourCount; i++) {
			int[] sampleSpace = { 1, 2, 3 };
			double[] degreeValues = { 1., 1., 1. };
			IntDistribution dist = new IntDistribution(sampleSpace, degreeValues);
			cIndegreeDistribution.put(colours[i], dist);
		}
		// System.out.println("size here: " + cIndegreeDistribution.size());
		ObjectDistribution<BitSet> vertexColourDistribution = new ObjectDistribution<BitSet>(colours, colorValues);
		// System.out.println(vertexColourDistribution);
		ColouredGraph cGraph = g.generateGraphColouredInDegree(vertexColourDistribution, cIndegreeDistribution, null);
		// analyze whether cGraph fulfills the desired properties
		// VertexColourDistributionMetric vcdm = new VertexColourDistributionMetric();
		// System.out.println(vcdm.apply(cGraph));
		ColouredInDegreeDistributionMetric cidm = new ColouredInDegreeDistributionMetric();
		System.out.println("" + cIndegreeDistribution);
		System.out.println("" + cidm.apply(cGraph));

		ColouredGraph cGraph2 = g.generateGraphColouredOutDegree(vertexColourDistribution, cIndegreeDistribution, null);
		ColouredOutDegreeDistributionMetric codm = new ColouredOutDegreeDistributionMetric();
		System.out.println(codm.apply(cGraph2));
	}
}
