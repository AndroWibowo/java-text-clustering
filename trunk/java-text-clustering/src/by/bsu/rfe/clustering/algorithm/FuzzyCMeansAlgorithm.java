package by.bsu.rfe.clustering.algorithm;

import java.util.List;
import java.util.Random;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;
import by.bsu.rfe.clustering.algorithm.cluster.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.algorithm.data.FuzzyDataElement;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.math.DoubleSparceVector;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;

import com.google.common.collect.Lists;

public class FuzzyCMeansAlgorithm<E extends FuzzyDataElement<? extends DataElement>, D extends DataSet<E>> implements
    ClusteringAlgorithm<E, Cluster<E>, D> {

  private double _weightThreshold;
  private int _numberOfClusters;
  private DistanseMeasure _distanseMeasure = new EuclideanDistanceMeasure();

  // TODO introduce getter/setter and for this field
  private int _maxIterations = 1000;

  /**
   * Creates a new instance of this class with specified number of clusters and
   * zero weight threshold.
   * 
   * @param numberOfClusters
   *          number of clusters
   */
  public FuzzyCMeansAlgorithm(int numberOfClusters) {
    this(numberOfClusters, 0);
  }

  /**
   * @param numberOfClusters
   *          number of clusters
   * @param weightThreshold
   *          threshold of degree of belonging to clusters, the value between 0
   *          and 1 inclusive. All elements with degree below this value will be
   *          removed from clusters after algorithm finishes if the threshold is
   *          greater than zero.
   */
  public FuzzyCMeansAlgorithm(int numberOfClusters, double weightThreshold) {
    _weightThreshold = weightThreshold;
    _numberOfClusters = numberOfClusters;
  }

  @Override
  public List<Cluster<E>> cluster(D dataSet) {
    Matrix weights = createRandomProbabilityMatrix(dataSet);

    // TODO introduce instance variable for this
    final double eps = 1e-5;

    List<CentroidCluster<E>> tempClusters = createClusters(dataSet);
    List<Cluster<E>> result = Lists.newArrayListWithCapacity(_numberOfClusters);

    double previousTargetValue = evalObjectiveFunction(tempClusters, dataSet, weights);
    double nextTargetValue = 0;

    for (int it = 0; it < _maxIterations; it++) {
      computeCentroids(tempClusters, dataSet, weights);
      updateWeights(tempClusters, dataSet, weights);
      nextTargetValue = evalObjectiveFunction(tempClusters, dataSet, weights);

      if (Math.abs(previousTargetValue - nextTargetValue) > eps) {
        break;
      }
    }

    return result;
  }

  // With fuzzy c-means, the centroid of a cluster is the mean of all points,
  // weighted by their degree of belonging to the cluster
  private void computeCentroids(List<CentroidCluster<E>> clusterList, D dataSet, Matrix weights) {
    final int dimSize = dataSet.get(0).asVector().size();

    for (int clusterOrdinal = 0; clusterOrdinal < clusterList.size(); clusterOrdinal++) {
      CentroidCluster<E> cluster = clusterList.get(clusterOrdinal);
      DoubleVector centroid = cluster.getCentroid();

      if (centroid == null) {
        centroid = new DoubleSparceVector(dimSize);
      }

      double sumUijSquared = 0;
      for (int elemIndex = 0; elemIndex < dataSet.size(); elemIndex++) {
        double weight = weights.get(clusterOrdinal, elemIndex);
        sumUijSquared += (weight * weight);
      }

      for (int j = 0; j < dimSize; j++) {
        double newCentroidValue = 0;

        // each cluster "contains" all elements from original set
        for (int elemIndex = 0; elemIndex < dataSet.size(); elemIndex++) {
          DoubleVector elemVector = dataSet.get(elemIndex).asVector();
          double val = elemVector.get(j);
          double weight = weights.get(clusterOrdinal, elemIndex);
          newCentroidValue += val * weight * weight;
        }

        newCentroidValue /= sumUijSquared;

        centroid.set(j, newCentroidValue);
      }

      cluster.setCentroid(centroid);
    }
  }

  private void updateWeights(List<CentroidCluster<E>> clusterList, D dataSet, Matrix weights) {
    // TODO implement
  }

  private double evalObjectiveFunction(List<CentroidCluster<E>> clusterList, D dataSet, Matrix weights) {
    double result = 0;

    for (int row = 0; row < weights.numRows(); row++) {
      for (int col = 0; col < weights.numColumns(); col++) {
        DoubleVector centroidVector = clusterList.get(row).getCentroid();
        DoubleVector elementVector = dataSet.get(col).asVector();

        double distance = _distanseMeasure.compute(centroidVector, elementVector);
        result += distance * weights.get(row, col);
      }
    }

    return result;
  }

  private Matrix createRandomProbabilityMatrix(D dataSet) {
    final int totalElements = dataSet.size();

    Matrix matrix = new DenseMatrix(_numberOfClusters, totalElements);
    Random random = new Random();

    double[] rand = new double[_numberOfClusters];

    for (int col = 0; col < totalElements; col++) {
      // assign probabilities for each element in such way that the sum of
      // probabilities of belonging to cluster is equal to 1 for each
      // element

      double sum = 0;
      for (int i = 0; i < rand.length; i++) {
        rand[i] = random.nextDouble();
        sum += rand[i];
      }

      for (int row = 0; row < _numberOfClusters; row++) {
        matrix.set(row, col, rand[row] / sum);
      }

    }

    return matrix;
  }

  private List<CentroidCluster<E>> createClusters(D dataSet) {
    List<CentroidCluster<E>> clusters = Lists.newArrayListWithCapacity(_numberOfClusters);
    for (int i = 0; i < dataSet.size(); i++) {
      clusters.add(new CentroidCluster<E>());
    }

    return clusters;
  }

}
