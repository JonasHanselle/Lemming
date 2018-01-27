package org.aksw.simba.lemming.creation;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.BitSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.aksw.simba.lemming.ColouredGraph;
import org.aksw.simba.lemming.metrics.dist.IntDistribution;
import org.aksw.simba.lemming.metrics.dist.ObjectDistribution;
//import org.aksw.simba.lemming.metrics.dist.VertexColourDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredInDegreeDistributionMetric;
import org.aksw.simba.lemming.metrics.dist.multi.ColouredOutDegreeDistributionMetric;

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
		ArrayList<Integer> vertexIDs = new ArrayList<Integer>();
		ArrayList<Integer> edgeIDs = new ArrayList<Integer>();
		for (int i = 0; i < vertexColourDistribution.getSampleSpace().length; i++) {
			int currentCount = (int) vertexColourDistribution.getValues()[i];
			for (int j = 0; j < currentCount; j++) {
				vertexIDs.add(cGraph.addVertex(vertexColourDistribution.getSampleSpace()[i]));
			}
		}

		// shuffle vertices for randomness
		Collections.shuffle(vertexIDs);

		System.out.println("Size: " + cGraph.getVertices().size());
		// add edges to the graph
		if (colouredInDegreeDistribution != null) {
			BitSet[] inColours = vertexColourDistribution.getSampleSpace();
			// System.out.println("colours: " + inColours.length);
			int currentNode = 0;
			System.out.println("colors: " + inColours.length);
			// iterate over all colours
			for (BitSet col : inColours) {
				IntDistribution currentDist = colouredInDegreeDistribution.get(col);
				// iterate over all elements of the values of the distribution
				for (int i = 0; i < currentDist.getValues().length; i++) {
					// until all enough edges has been generated
					for (int k = 0; k < (int) currentDist.getValues()[i]; k++) {
						// generate edges according to fit the desired node degree
						for (int j = 0; j < (int) currentDist.getSampleSpace()[i]; j++) {
							Collections.shuffle(vertexIDs);
							int candidate = 0;
							if (vertexIDs.get(candidate) == currentNode)
								candidate++;
							cGraph.addEdge(vertexIDs.get(candidate), currentNode);
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
			// iterate over all colours
			for (BitSet col : inColours) {
				IntDistribution currentDist = colouredOutdegreeDistribution.get(col);
				// iterate over all elements of the values of the distribution
				for (int i = 0; i < currentDist.getValues().length; i++) {
					// until all enough edges has been generated
					for (int k = 0; k < (int) currentDist.getValues()[i]; k++) {
						// generate edges according to fit the desired node degree
						for (int j = 0; j < (int) currentDist.getSampleSpace()[i]; j++) {
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

}
