/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.simba.lemming.algo.refinement;

import java.util.Map;
import java.util.Set;

import org.aksw.simba.lemming.algo.expression.Expression;

import com.carrotsearch.hppc.ObjectDoubleOpenHashMap;

/**
 * Interface for fitness functions
 * @author ngonga
 */
public interface FitnessFunction {
    /**
     * Computes the fitness function for a given node
     * @param tree
     * @param node
     * @param values
     * @return 
     */
    double getFitness(RefinementTree tree, RefinementNode node, Set<Map<String, Double>> values);

    /**
     * Computes the fitness function for a given node
     * 
     * @param expression
     * @param graphVectors
     * @return
     */
    public double getFitness(Expression expression, ObjectDoubleOpenHashMap<String>[] graphVectors);
}
