package by.bsu.rfe.clustering.algorithm.genetic;

import java.util.List;

public interface MutationStrategy {

  public void mutate(List<Chromosome> population);

}
