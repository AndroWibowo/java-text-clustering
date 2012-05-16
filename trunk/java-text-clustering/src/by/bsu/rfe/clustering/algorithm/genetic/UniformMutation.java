package by.bsu.rfe.clustering.algorithm.genetic;

import java.util.List;
import java.util.Random;

public final class UniformMutation implements MutationStrategy {

  private Random random = new Random();
  private int lowerBound;
  private int uppperBound;
  private double rate;

  public UniformMutation(int loweBound, int upperBound, double rate) {
    this.lowerBound = loweBound;
    this.uppperBound = upperBound;
    this.rate = rate;
  }

  @Override
  public void mutate(List<Chromosome> population) {
    for (Chromosome c : population) {

      final int[] genes = c.getGenes();

      for (int i = 0; i < genes.length; i++) {
        if (random.nextDouble() <= rate) {
          genes[i] = lowerBound + random.nextInt(uppperBound - lowerBound);

          System.out.printf("\tGene[%d] Mutated to %d%n", i, genes[i]);
        }
      }
    }
  }

}
