package org.aksw.simba.lemming.creation;

import org.aksw.simba.lemming.ColouredGraph;
import org.aksw.simba.lemming.colour.ColourPalette;
import org.aksw.simba.lemming.metrics.dist.IntDistribution;

import grph.Grph;
import grph.in_memory.InMemoryGrph;

/**
 * This class implements algorithms for generating coloured 
 * graphs according to a given set of features
 * @author Jonas Hanselle (jmhansel@campus.uni-paderborn.de)
 *
 */
public class FeatureGraphGenerator {

	public FeatureGraphGenerator() {
		
	}
	
	public ColouredGraph generateGraph(IntDistribution inDegreeDistrib){
		ColouredGraph cGraph = null;
		Grph graph = new InMemoryGrph();
		graph.addNVertices(12);
		graph.clique();
		ColourPalette vertexPalette = null;
		ColourPalette edgePalette = null;
		cGraph = new ColouredGraph(graph, vertexPalette, edgePalette);
		System.out.println(""+inDegreeDistrib);
		return cGraph;
	}
	
	public static void main(String[] args) {
		FeatureGraphGenerator g = new FeatureGraphGenerator();
		int[] sampleSpace = {1,2,3};
		double[] values = {1.0/3.0,1.0/3.0,1.0/3.0};
		IntDistribution inDegreeDistrib = new IntDistribution(sampleSpace, values);
		g.generateGraph(inDegreeDistrib);
	}
}
