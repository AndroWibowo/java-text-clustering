package by.bsu.rfe.clustering.algorithm.genetic;

import java.util.List;
import java.util.Random;

import com.google.common.base.Preconditions;

public final class TournamentSelection implements SelectionStrategy {

  private Random random;
  private double k = 0.75;

  public TournamentSelection() {
    random = new Random();
  }

  public TournamentSelection(Random random) {
    this.random = random;
  }

  @Override
  public Chromosome select(List<Chromosome> population) {
    Preconditions.checkNotNull(population);
    Preconditions.checkArgument(!population.isEmpty());

    final Chromosome first = selectRandom(population);
    final Chromosome second = selectRandom(population);

    final double r = random.nextDouble();

    if (r < k) {
      return first.getFitness() > second.getFitness() ? first : second;
    }
    else {
      return first.getFitness() < second.getFitness() ? first : second;
    }
  }

  public TournamentSelection useK(double k) {
    this.k = k;
    return this;
  }

  private Chromosome selectRandom(List<Chromosome> population) {
    final int randomIndex = random.nextInt(population.size());

    return population.get(randomIndex);
  }

}
