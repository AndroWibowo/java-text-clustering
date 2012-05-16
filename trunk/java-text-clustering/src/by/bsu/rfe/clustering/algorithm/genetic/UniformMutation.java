package by.bsu.rfe.clustering.algorithm.genetic;

import java.util.List;
import java.util.Random;

public final class UniformMutation implements MutationStrategy {

  private Random random = new Random();
  private int lowerBound;
  private int uppperBound;
  private double rate;
  private double geneRate;

  public UniformMutation(int loweBound, int upperBound, double rate, double geneRate) {
    this.lowerBound = loweBound;
    this.uppperBound = upperBound;
    this.rate = rate;
    this.geneRate = geneRate;
  }

  @Override
  public void mutate(List<Chromosome> population) {
    for (Chromosome c : population) {
      final double r = random.nextDouble();

      if (r <= rate) {
        final int[] genes = c.getGenes();

        for (int i = 0; i < genes.length; i++) {
          if (random.nextDouble() <= geneRate) {
            genes[i] = lowerBound + random.nextInt(uppperBound - lowerBound);
          }
        }
      }
    }
  }

}
