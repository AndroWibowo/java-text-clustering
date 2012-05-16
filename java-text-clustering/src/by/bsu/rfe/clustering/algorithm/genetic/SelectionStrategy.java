package by.bsu.rfe.clustering.algorithm.genetic;

import java.util.List;

public interface SelectionStrategy {

  Chromosome select(List<Chromosome> population);

}
