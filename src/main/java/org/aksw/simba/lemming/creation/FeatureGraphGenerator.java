package org.aksw.simba.lemming.creation;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.carrotsearch.hppc.BitSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	 * @param colouredInDegreeDistribution
	 *            Indegree distribution for each colour
	 * @param edgeColourDistribution
	 *            Desired colour distribution over edges
	 * @return {@link ColouredGraph} object with the desired properties
	 */
	public ColouredGraph generateGraphColouredInDegree(Map<BitSet, IntDistribution> colouredInDegreeDistribution,
			ObjectDistribution<BitSet> edgeColourDistribution) {

		ColouredGraph cGraph = new ColouredGraph();
		// add vertices of desired colours to the graph
		ArrayList<Integer> vertexIDs = new ArrayList<Integer>();
		ArrayList<BitSet> edgeColours = new ArrayList<BitSet>();

		// Create object distribution for vertex colours for convenient access
		Set<BitSet> sampleSpaceSet = colouredInDegreeDistribution.keySet();
		BitSet[] sampleSpace = sampleSpaceSet.toArray(new BitSet[sampleSpaceSet.size()]);
		double[] values = new double[sampleSpaceSet.size()];

		for (int i = 0; i < sampleSpace.length; i++) {
			IntDistribution currentDistribution = colouredInDegreeDistribution.get(sampleSpace[i]);
			double currentCount = 0.;
			for (int j = 0; j < currentDistribution.values.length; j++) {
				currentCount += currentDistribution.values[j];
			}
			values[i] = currentCount;
		}

		ObjectDistribution<BitSet> vertexColourDistributionInside = new ObjectDistribution<>(sampleSpace, values);

		// create vertices
		for (int i = 0; i < vertexColourDistributionInside.getSampleSpace().length; i++) {
			int currentCount = (int) vertexColourDistributionInside.getValues()[i];
			for (int j = 0; j < currentCount; j++) {
				vertexIDs.add(cGraph.addVertex(vertexColourDistributionInside.getSampleSpace()[i]));
			}
		}

		// create edge colours
		for (int i = 0; i < edgeColourDistribution.getSampleSpace().length; i++) {
			int currentCount = (int) edgeColourDistribution.getValues()[i];
			for (int j = 0; j < currentCount; j++) {
				edgeColours.add(edgeColourDistribution.getSampleSpace()[i]);
			}
		}
		// shuffle vertices for randomness
		Collections.shuffle(vertexIDs);
		Collections.shuffle(edgeColours);
		// add edges to the graph
		if (colouredInDegreeDistribution != null) {
			BitSet[] inColours = vertexColourDistributionInside.getSampleSpace();
			ObjectArrayList<BitSet> vColours = cGraph.getVertexColours();
			int currentNode = 0;
			int currentEdge = 0;
			// iterate over all colours
			for (BitSet col : inColours) {
				IntDistribution currentDist = colouredInDegreeDistribution.get(col);
				// iterate over all elements of the values of the distribution
				for (int i = 0; i < currentDist.getValues().length; i++) {
					// until enough edges have been generated
					for (int k = 0; k < (int) currentDist.getValues()[i]; k++) {
						// generate edges according to fit the desired node degree
						for (int j = 0; j < (int) currentDist.getSampleSpace()[i]; j++) {
							int candidate = 0;
							if (vertexIDs.get(candidate) == currentNode)
								candidate++;
							cGraph.addEdge(vertexIDs.get(candidate), currentNode, edgeColours.get(currentEdge));
							candidate++;
							currentEdge++;
						}
						currentNode++;
					}
				}
			}
		}
		return cGraph;
	}

	/**
	 * @param colouredInDegreeDistribution
	 *            Outdegree distribution for each colour
	 * @param edgeColourDistribution
	 *            Desired colour distribution over edges
	 * @return {@link ColouredGraph} object with the desired properties
	 */
	public ColouredGraph generateGraphColouredOutDegree(Map<BitSet, IntDistribution> colouredOutDegreeDistribution,
			ObjectDistribution<BitSet> edgeColourDistribution) {

		ColouredGraph cGraph = new ColouredGraph();
		// add vertices of desired colours to the graph
		ArrayList<Integer> vertexIDs = new ArrayList<Integer>();
		ArrayList<BitSet> edgeColours = new ArrayList<BitSet>();

		// Create object distribution for vertex colours for convenient access
		Set<BitSet> sampleSpaceSet = colouredOutDegreeDistribution.keySet();
		BitSet[] sampleSpace = sampleSpaceSet.toArray(new BitSet[sampleSpaceSet.size()]);
		double[] values = new double[sampleSpaceSet.size()];

		for (int i = 0; i < sampleSpace.length; i++) {
			IntDistribution currentDistribution = colouredOutDegreeDistribution.get(sampleSpace[i]);
			double currentCount = 0.;
			for (int j = 0; j < currentDistribution.values.length; j++) {
				currentCount += currentDistribution.values[j];
			}
			values[i] = currentCount;
		}

		ObjectDistribution<BitSet> vertexColourDistributionInside = new ObjectDistribution<>(sampleSpace, values);

		// create vertices
		for (int i = 0; i < vertexColourDistributionInside.getSampleSpace().length; i++) {
			int currentCount = (int) vertexColourDistributionInside.getValues()[i];
			for (int j = 0; j < currentCount; j++) {
				vertexIDs.add(cGraph.addVertex(vertexColourDistributionInside.getSampleSpace()[i]));
			}
		}

		// create edge colours
		for (int i = 0; i < edgeColourDistribution.getSampleSpace().length; i++) {
			int currentCount = (int) edgeColourDistribution.getValues()[i];
			for (int j = 0; j < currentCount; j++) {
				edgeColours.add(edgeColourDistribution.getSampleSpace()[i]);
			}
		}
		// shuffle vertices for randomness
		Collections.shuffle(vertexIDs);
		Collections.shuffle(edgeColours);
		// add edges to the graph
		if (colouredOutDegreeDistribution != null) {
			BitSet[] inColours = vertexColourDistributionInside.getSampleSpace();
			ObjectArrayList<BitSet> vColours = cGraph.getVertexColours();
			int currentNode = 0;
			int currentEdge = 0;
			// iterate over all colours
			for (BitSet col : inColours) {
				IntDistribution currentDist = colouredOutDegreeDistribution.get(col);
				// iterate over all elements of the values of the distribution
				for (int i = 0; i < currentDist.getValues().length; i++) {
					// until enough edges have been generated
					for (int k = 0; k < (int) currentDist.getValues()[i]; k++) {
						// generate edges according to fit the desired node degree
						for (int j = 0; j < (int) currentDist.getSampleSpace()[i]; j++) {
							int candidate = 0;
							if (vertexIDs.get(candidate) == currentNode)
								candidate++;
							cGraph.addEdge(currentNode, vertexIDs.get(candidate), edgeColours.get(currentEdge));
							candidate++;
							currentEdge++;
						}
						currentNode++;
					}
				}
			}
		}
		return cGraph;
	}

	/**
	 * @param colouredInDegreeDistribution
	 *            Outdegree distribution for each colour normalized
	 * @param edgeColourDistribution
	 *            Desired colour distribution over edges normalized
	 * @return {@link ColouredGraph} object with the desired properties
	 */
	public ColouredGraph generateGraphColouredOutDegreeNormalized(Map<BitSet, IntDistribution> colouredOutDegreeDistribution,
			ObjectDistribution<BitSet> edgeColourDistribution, int numberNodes) {
		Map<BitSet, IntDistribution> colouredOutDegreeDistributionDiscrete = new HashMap<BitSet, IntDistribution>();
		ObjectDistribution<BitSet> edgeColourDistributionDiscrete;
		int edges = 0;
		for (BitSet col : colouredOutDegreeDistribution.keySet()) {
			IntDistribution currentDist = colouredOutDegreeDistribution.get(col);
			int[] sampleSpace = currentDist.sampleSpace;
			double[] degreeValues = new double[currentDist.values.length];
			for (int i = 0; i < degreeValues.length; i++) {
				// compute discrete value for the normalized distribution, smoothed by adding 1
				degreeValues[i] = (currentDist.values[i] * numberNodes) + 1;
				edges += sampleSpace[i] * degreeValues[i];
			}
			IntDistribution discreteDist = new IntDistribution(sampleSpace, degreeValues);
			colouredOutDegreeDistributionDiscrete.put(col, discreteDist);
		}
		double[] edgeColourValues = new double[edgeColourDistribution.values.length];
		for(int i = 0; i < edgeColourDistribution.values.length; i++) {
			edgeColourValues[i] = edges * edgeColourDistribution.values[i];
		}
		edgeColourDistributionDiscrete = new ObjectDistribution<>(edgeColourDistribution.sampleSpace, edgeColourValues);
		return generateGraphColouredOutDegree(colouredOutDegreeDistributionDiscrete, edgeColourDistributionDiscrete);
	}
	
	/**
	 * @param colouredInDegreeDistribution
	 *            Outdegree distribution for each colour normalized
	 * @param edgeColourDistribution
	 *            Desired colour distribution over edges normalized
	 * @return {@link ColouredGraph} object with the desired properties
	 */
	public ColouredGraph generateGraphColouredInDegreeNormalized(Map<BitSet, IntDistribution> colouredInDegreeDistribution,
			ObjectDistribution<BitSet> edgeColourDistribution, int numberNodes) {
		Map<BitSet, IntDistribution> colouredOutDegreeDistributionDiscrete = new HashMap<BitSet, IntDistribution>();
		ObjectDistribution<BitSet> edgeColourDistributionDiscrete;
		int edges = 0;
		for (BitSet col : colouredInDegreeDistribution.keySet()) {
			IntDistribution currentDist = colouredInDegreeDistribution.get(col);
			int[] sampleSpace = currentDist.sampleSpace;
			double[] degreeValues = new double[currentDist.values.length];
			for (int i = 0; i < degreeValues.length; i++) {
				// compute discrete value for the normalized distribution, smoothed by adding 1
				degreeValues[i] = (currentDist.values[i] * numberNodes) + 1;
				edges += sampleSpace[i] * degreeValues[i];
			}
			IntDistribution discreteDist = new IntDistribution(sampleSpace, degreeValues);
			colouredOutDegreeDistributionDiscrete.put(col, discreteDist);
		}
		double[] edgeColourValues = new double[edgeColourDistribution.values.length];
		for(int i = 0; i < edgeColourDistribution.values.length; i++) {
			edgeColourValues[i] = edges * edgeColourDistribution.values[i];
		}
		edgeColourDistributionDiscrete = new ObjectDistribution<>(edgeColourDistribution.sampleSpace, edgeColourValues);
		return generateGraphColouredInDegree(colouredOutDegreeDistributionDiscrete, edgeColourDistributionDiscrete);
	}
}
