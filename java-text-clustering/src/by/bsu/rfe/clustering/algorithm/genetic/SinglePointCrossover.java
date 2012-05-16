package by.bsu.rfe.clustering.algorithm.genetic;

import java.util.Arrays;
import java.util.Random;

public class SinglePointCrossover implements CrossoverStrategy {

  private Random random = new Random();

  private Chromosome firstChild;
  private Chromosome secondChild;

  @Override
  public void crossover(Chromosome mother, Chromosome father, double probability) {
    int[] motherGenes = mother.getGenes();
    int[] fatherGenes = father.getGenes();
    int[] firstChildGenes = new int[motherGenes.length];
    int[] secondChildGenes = new int[fatherGenes.length];

    double r = random.nextDouble();

    if (r <= probability) {
      final int crossoverPoint = random.nextInt(firstChildGenes.length);
      final int secondPos = crossoverPoint + 1;

      // System.out.println("Crossover point: " + crossoverPoint);

      System.arraycopy(motherGenes, 0, firstChildGenes, 0, crossoverPoint + 1);
      System
          .arraycopy(motherGenes, secondPos, secondChildGenes, secondPos, firstChildGenes.length - crossoverPoint - 1);

      System.arraycopy(fatherGenes, 0, secondChildGenes, 0, crossoverPoint + 1);
      System.arraycopy(fatherGenes, secondPos, firstChildGenes, secondPos, firstChildGenes.length - crossoverPoint - 1);
    }
    else {
      System.arraycopy(motherGenes, 0, firstChildGenes, 0, motherGenes.length);
      System.arraycopy(fatherGenes, 0, secondChildGenes, 0, fatherGenes.length);
    }

    firstChild = new Chromosome(firstChildGenes);
    secondChild = new Chromosome(secondChildGenes);
  }

  @Override
  public Chromosome fistChild() {
    return firstChild;
  }

  @Override
  public Chromosome secondChild() {
    return secondChild;
  }

  public static void main(String[] args) {
    final Chromosome mom = new Chromosome(new int[] { 1, 2, 3, 4, 5 });
    final Chromosome dad = new Chromosome(new int[] { 6, 7, 8, 9, -1 });

    final CrossoverStrategy cross = new SinglePointCrossover();
    cross.crossover(mom, dad, 0.8);

    System.out.println(Arrays.toString(mom.getGenes()));
    System.out.println(Arrays.toString(dad.getGenes()));

    System.out.println();

    System.out.println(Arrays.toString(cross.fistChild().getGenes()));
    System.out.println(Arrays.toString(cross.secondChild().getGenes()));
  }

}
