package by.bsu.rfe.clustering.algorithm.genetic;

public interface CrossoverStrategy {

  public void crossover(Chromosome mother, Chromosome father, double probability);

  public Chromosome fistChild();

  public Chromosome secondChild();

}
