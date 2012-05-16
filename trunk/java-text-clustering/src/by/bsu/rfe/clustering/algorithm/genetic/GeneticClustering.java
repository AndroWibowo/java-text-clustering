package by.bsu.rfe.clustering.algorithm.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.bsu.rfe.clustering.algorithm.ClusteringHelper;
import by.bsu.rfe.clustering.algorithm.FlatClustering;
import by.bsu.rfe.clustering.algorithm.datamodel.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.datamodel.DataElement;
import by.bsu.rfe.clustering.algorithm.datamodel.DataSet;

import com.google.common.base.Preconditions;

public class GeneticClustering<E extends DataElement, D extends DataSet<E>> implements
    FlatClustering<E, CentroidCluster<E>, D> {

  private static final int MAX_GENERATIONS = 1 << 7;
  private static final double CROSSOVER_PROBABILITY = 0.9;
  private static final double MUTATION_RATE = 0.005;
  private static final double GENE_MUTATION_RATE = 0.001;

  private int _numberOfClusters;

  private SelectionStrategy selection = new TournamentSelection();
  private CrossoverStrategy crossover = new SinglePointCrossover();
  private MutationStrategy mutation;

  public GeneticClustering(final int numberOfClusters) {
    setNumberOfClusters(numberOfClusters);

    mutation = new UniformMutation(0, numberOfClusters, MUTATION_RATE, GENE_MUTATION_RATE);
  }

  public int getNumberOfClusters() {
    return _numberOfClusters;
  }

  public void setNumberOfClusters(int numberOfClusters) {
    Preconditions.checkArgument(numberOfClusters > 0);
    _numberOfClusters = numberOfClusters;
  }

  @Override
  public List<CentroidCluster<E>> cluster(D dataSet) {

    List<CentroidCluster<E>> clusterList = initializeClusters();

    // TODO change
    final int populationSize = 1 << 5;

    List<Chromosome> currentPopulation = randomPopulation(dataSet.size(), populationSize, _numberOfClusters);

    calculateFitnessValues(currentPopulation, clusterList, dataSet);

    int generation = 1;
    while (generation < MAX_GENERATIONS) {
      currentPopulation = nextPopulation(currentPopulation);

      generation++;
    }

    return clusterList;
  }

  private List<Chromosome> nextPopulation(List<Chromosome> previousPopulation) {

    List<Chromosome> nextPopulation = new ArrayList<Chromosome>(previousPopulation.size());

    while (nextPopulation.size() < previousPopulation.size()) {
      Chromosome mother = selection.select(previousPopulation);
      Chromosome father = selection.select(previousPopulation);

      crossover.crossover(mother, father, CROSSOVER_PROBABILITY);
      nextPopulation.add(crossover.fistChild());
      nextPopulation.add(crossover.secondChild());
    }

    mutation.mutate(nextPopulation);

    return nextPopulation;
  }

  private int findBestFitness(List<Chromosome> population) {
    int withBestFitness = 0;
    double bestFitness = 0;

    int index = 0;
    for (Chromosome c : population) {
      if (c.getFitness() > bestFitness) {
        withBestFitness = index;
      }

      index++;
    }

    return withBestFitness;
  }

  private void calculateFitnessValues(final List<Chromosome> population, List<CentroidCluster<E>> clusterList,
      DataSet<E> dataSet) {

    for (int i = 0; i < population.size(); i++) {
      Chromosome chromosome = population.get(i);
      populateClusters(clusterList, dataSet, chromosome.getGenes());

      double fitness = 0;

      for (CentroidCluster<E> cluster : clusterList) {
        fitness += ClusteringHelper.computeSquareError(cluster);
      }

      fitness = 1 / fitness;
      chromosome.setFitness(fitness);

      System.out.printf("Chromosome[%d] fitness: %f%n", i, fitness);
    }
  }

  // each gene of chromosome is a cluster index
  private void populateClusters(List<CentroidCluster<E>> clusterList, DataSet<E> dataSet, int[] chromosome) {
    for (CentroidCluster<E> cluster : clusterList) {
      cluster.clear();
    }

    for (int elemIndex = 0; elemIndex < chromosome.length; elemIndex++) {
      final int clusterIndex = chromosome[elemIndex];
      final CentroidCluster<E> cluster = clusterList.get(clusterIndex);
      final E elem = dataSet.get(elemIndex);
      cluster.addDataElement(elem);
    }
  }

  private List<Chromosome> randomPopulation(final int dataSetSize, final int populationSize, final int numOfClusters) {
    List<Chromosome> population = new ArrayList<Chromosome>(populationSize);

    Random random = new Random();

    for (int chromosome = 0; chromosome < populationSize; chromosome++) {
      int[] genes = new int[dataSetSize];

      for (int gene = 0; gene < genes.length; gene++) {

        genes[gene] = random.nextInt(numOfClusters);

        System.out.print(genes[gene]);
        System.out.print(" | ");
      }

      population.add(new Chromosome(genes));

      System.out.println();

    }

    return population;
  }

  private List<CentroidCluster<E>> initializeClusters() {
    List<CentroidCluster<E>> clusterList = new ArrayList<CentroidCluster<E>>(_numberOfClusters);

    for (int i = 0; i < clusterList.size(); i++) {
      clusterList.add(new CentroidCluster<E>());
    }

    return clusterList;
  }

}
