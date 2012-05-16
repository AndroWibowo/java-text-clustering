package by.bsu.rfe.clustering.algorithm.genetic;

/**
 * Gene array is available for modification outside.
 * 
 * @author Siarhei_Yarashevich
 * 
 */
public class Chromosome implements Comparable<Chromosome> {

  private int[] genes;
  private double fitness;

  public Chromosome(int[] genes) {
    this.genes = genes;
  }

  public double getFitness() {
    return fitness;
  }

  public void setFitness(double fitness) {
    this.fitness = fitness;
  }

  public int[] getGenes() {
    return genes;
  }

  @Override
  public int compareTo(Chromosome c) {
    if (this.fitness == c.fitness) {
      return 0;
    }

    return (this.fitness < c.fitness) ? -1 : 0;
  }

}
