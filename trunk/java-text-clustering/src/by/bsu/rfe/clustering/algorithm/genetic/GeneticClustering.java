package by.bsu.rfe.clustering.algorithm.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.bsu.rfe.clustering.algorithm.ClusteringHelper;
import by.bsu.rfe.clustering.algorithm.FlatClustering;
import by.bsu.rfe.clustering.algorithm.datamodel.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.datamodel.Cluster;
import by.bsu.rfe.clustering.algorithm.datamodel.DataElement;
import by.bsu.rfe.clustering.algorithm.datamodel.DataSet;

import com.google.common.base.Preconditions;

public class GeneticClustering<E extends DataElement, D extends DataSet<E>> implements FlatClustering<E, Cluster<E>, D> {

  private static final int MAX_GENERATIONS = 10;
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
  public List<Cluster<E>> cluster(D dataSet) {

    List<CentroidCluster<E>> clusterList = initializeClusters();

    // TODO change
    final int populationSize = 1 << 5;

    List<Chromosome> currentPopulation = randomPopulation(dataSet.size(), populationSize);

    System.out.println("Initial Population Created");

    calculateFitnessValues(currentPopulation, clusterList, dataSet);

    int generation = 1;
    while (generation <= MAX_GENERATIONS) {
      System.out.println("Generation: " + generation);

      currentPopulation = nextPopulation(currentPopulation);

      System.out.println("\tPopulation Created");

      calculateFitnessValues(currentPopulation, clusterList, dataSet);

      generation++;
    }

    Chromosome best = findBestFitted(currentPopulation);
    populateClusters(clusterList, dataSet, best);

    List<Cluster<E>> result = new ArrayList<Cluster<E>>();
    result.addAll(clusterList);
    return result;
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

  private Chromosome findBestFitted(List<Chromosome> population) {
    Chromosome best = null;
    double bestFitness = 0;

    int index = 0;
    for (Chromosome c : population) {
      if (c.getFitness() > bestFitness) {
        best = c;
      }

      index++;
    }

    return best;
  }

  private void calculateFitnessValues(final List<Chromosome> population, List<CentroidCluster<E>> clusterList,
      DataSet<E> dataSet) {

    for (int i = 0; i < population.size(); i++) {
      Chromosome chromosome = population.get(i);
      populateClusters(clusterList, dataSet, chromosome);

      double fitness = 0;

      for (CentroidCluster<E> cluster : clusterList) {
        fitness += ClusteringHelper.computeSquareError(cluster);
      }

      fitness = 1 / fitness;
      chromosome.setFitness(fitness);

      // System.out.printf("Chromosome[%d] fitness: %f%n", i, fitness);
    }
  }

  // each gene of chromosome is a cluster index
  private void populateClusters(List<CentroidCluster<E>> clusterList, DataSet<E> dataSet, Chromosome c) {
    for (CentroidCluster<E> cluster : clusterList) {
      cluster.clear();
    }

    final int[] genes = c.getGenes();

    for (int elemIndex = 0; elemIndex < genes.length; elemIndex++) {
      final int clusterIndex = genes[elemIndex];
      final CentroidCluster<E> cluster = clusterList.get(clusterIndex);
      final E elem = dataSet.get(elemIndex);
      cluster.addDataElement(elem);
    }
  }

  private List<Chromosome> randomPopulation(final int dataSetSize, final int populationSize) {
    List<Chromosome> population = new ArrayList<Chromosome>(populationSize);

    Random random = new Random();

    for (int chromosome = 0; chromosome < populationSize; chromosome++) {
      int[] genes = new int[dataSetSize];

      for (int gene = 0; gene < genes.length; gene++) {

        genes[gene] = random.nextInt(_numberOfClusters);

      }

      population.add(new Chromosome(genes));
    }

    return population;
  }

  private List<CentroidCluster<E>> initializeClusters() {
    List<CentroidCluster<E>> clusterList = new ArrayList<CentroidCluster<E>>(_numberOfClusters);

    for (int i = 0; i < _numberOfClusters; i++) {
      clusterList.add(new CentroidCluster<E>());
    }

    return clusterList;
  }
}
