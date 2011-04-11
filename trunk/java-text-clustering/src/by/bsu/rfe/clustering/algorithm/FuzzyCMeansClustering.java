package by.bsu.rfe.clustering.algorithm;

import java.io.File;
import java.util.List;
import java.util.Random;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.by.bsu.rfe.clustering.app.util.CSVDataSetExporter;
import by.bsu.rfe.clustering.algorithm.cluster.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.algorithm.data.FuzzyDataElement;
import by.bsu.rfe.clustering.algorithm.data.GenericDataSet;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.math.DoubleSparceVector;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;
import by.bsu.rfe.clustering.nlp.PorterStemmer;
import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.document.DocumentCollection;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;
import by.bsu.rfe.clustering.text.ir.DocumentCollectionReader;
import by.bsu.rfe.clustering.text.ir.FileSystemDocumentCollectionReader;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.NormalizedTFIDF;

import com.google.common.collect.Lists;

public class FuzzyCMeansClustering<E extends FuzzyDataElement<? extends DataElement>, D extends DataSet<E>> implements
    Clustering<E, Cluster<E>, D> {

  private static Log log = LogFactory.getLog(FuzzyCMeansClustering.class);

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
  public FuzzyCMeansClustering(int numberOfClusters) {
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
  public FuzzyCMeansClustering(int numberOfClusters, double weightThreshold) {
    _weightThreshold = weightThreshold;
    _numberOfClusters = numberOfClusters;
  }

  @Override
  public List<Cluster<E>> cluster(D dataSet) {
    Matrix weights = createRandomProbabilityMatrix(dataSet);
    Matrix prev = null;

    System.out.println(weights);
    print(weights);

    // TODO introduce instance variable for this
    final double eps = 1e-5;

    List<CentroidCluster<E>> tempClusters = createClusters(dataSet);
    List<Cluster<E>> result = Lists.newArrayListWithCapacity(_numberOfClusters);

    // double nextTargetValue = 0;

    for (int it = 0; it < _maxIterations; it++) {
      computeCentroids(tempClusters, dataSet, weights);
      updateWeights(tempClusters, dataSet, weights);

      System.out.println();
      print(weights);
      System.out.println();

      // nextTargetValue = evalObjectiveFunction(tempClusters, dataSet,
      // weights);

      if (prev != null) {
        if (eval(prev, weights) < eps) {
          break;
        }
      }
      else {
        prev = new DenseMatrix(_numberOfClusters, dataSet.size());
      }

      prev.set(weights);

    }

    for (int i = 0; i < tempClusters.size(); i++) {
      Cluster<E> newCluster = new Cluster<E>();

      for (int j = 0; j < dataSet.size(); j++) {
        double weight = weights.get(i, j);

        if (weight > _weightThreshold) {
          System.out.println("\t\t" + weight);
          E elem = dataSet.get(j);
          FuzzyDataElement<E> newElem = FuzzyDataElement.newInstance(elem, weight);
          newCluster.addDataElement((E) newElem);
        }
      }
      result.add(newCluster);
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
        // System.out.printf("weight: %f%n", weight);
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
        // System.out.printf("sumUij2: %f, newCentroidValue: %f%n",
        // sumUijSquared, newCentroidValue);

        centroid.set(j, newCentroidValue);
      }

      cluster.setCentroid(centroid);
    }
  }

  private void updateWeights(List<CentroidCluster<E>> clusterList, D dataSet, Matrix weights) {
    for (int i = 0; i < weights.numRows(); i++) {
      for (int j = 0; j < weights.numColumns(); j++) {
        DoubleVector centroid = clusterList.get(i).getCentroid();
        DoubleVector elemVector = dataSet.get(j).asVector();

        double distanceToThis = _distanseMeasure.compute(centroid, elemVector);
        double newWeight = 0;

        for (int k = 0; k < _numberOfClusters; k++) {
          DoubleVector kCentroid = clusterList.get(k).getCentroid();
          double distanceToThat = _distanseMeasure.compute(kCentroid, elemVector);
          // System.out.println("\t\t" + distanceToThat);
          double inc = distanceToThis / distanceToThat;
          newWeight += inc * inc;
        }
        // System.out.printf("new weight %f%n", 1/newWeight);
        weights.set(i, j, 1 / newWeight);
      }
    }
  }

  private double eval(Matrix prev, Matrix next) {
    double max = Double.MIN_VALUE;

    for (int i = 0; i < prev.numRows(); i++) {
      for (int j = 0; j < prev.numColumns(); j++) {
        double diff = prev.get(i, j) - next.get(i, j);
        double diffSquared = diff * diff;

        if (diffSquared > max) {
          max = diffSquared;
        }
      }
    }

    return max;
  }

  private double evalObjectiveFunction(List<CentroidCluster<E>> clusterList, D dataSet, Matrix weights) {
    double result = 0;

    for (int row = 0; row < weights.numRows(); row++) {
      for (int col = 0; col < weights.numColumns(); col++) {
        DoubleVector centroidVector = clusterList.get(row).getCentroid();
        DoubleVector elementVector = dataSet.get(col).asVector();

        double distance = _distanseMeasure.compute(centroidVector, elementVector);
        result += distance * distance * weights.get(row, col);
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

    for (int j = 0; j < matrix.numColumns(); j++) {
      double sum = 0;
      for (int i = 0; i < matrix.numRows(); i++) {
        sum += matrix.get(i, j);
      }
      System.out.println("sum " + sum);
    }
    return matrix;
  }

  private List<CentroidCluster<E>> createClusters(D dataSet) {
    List<CentroidCluster<E>> clusters = Lists.newArrayListWithCapacity(_numberOfClusters);
    for (int i = 0; i < _numberOfClusters; i++) {
      clusters.add(new CentroidCluster<E>());
    }

    return clusters;
  }

  private static void print(Matrix m) {
    for (int j = 0; j < m.numRows(); j++) {
      System.out.print(j + "\t");
      for (int i = 0; i < m.numColumns(); i++) {
        System.out.printf("%f\t", m.get(j, i));
      }
      System.out.println();
    }
  }

  public static void main(String[] args) throws Exception {
    //pointCMeans();
     textCMeans();
  }

  private static class Point implements DataElement {

    private DoubleSparceVector _vector = new DoubleSparceVector(2);

    public Point(double x, double y) {
      setX(x);
      setY(y);
    }

    void setX(double x) {
      _vector.set(0, x);
    }

    void setY(double y) {
      _vector.set(1, y);
    }

    double getX() {
      return _vector.get(0);
    }

    double getY() {
      return _vector.get(1);
    }

    @Override
    public DoubleVector asVector() {
      return _vector;
    }

    @Override
    public String toString() {
      return String.format("[%f, %f]", getX(), getY());
    }
  }

  private static void pointCMeans() {
    // butterfly dataset
    DataSet<FuzzyDataElement<Point>> dataSet = new GenericDataSet<FuzzyDataElement<Point>>();
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0, 0.0), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0, 0.2), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0, 0.4), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.1, 0.1), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.1, 0.2), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.1, 0.3), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.2, 0.2), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.3, 0.2), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.4, 0.2), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.5, 0.1), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.5, 0.2), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.5, 0.3), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.6, 0.0), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.6, 0.2), 0));
    dataSet.addElement(FuzzyDataElement.newInstance(new Point(0.6, 0.4), 0));

    final int numberOfClusters = 2;

    Clustering<FuzzyDataElement<Point>, Cluster<FuzzyDataElement<Point>>, DataSet<FuzzyDataElement<Point>>> alg = new FuzzyCMeansClustering<FuzzyDataElement<Point>, DataSet<FuzzyDataElement<Point>>>(
        numberOfClusters, 0.2);

    List<Cluster<FuzzyDataElement<Point>>> result = alg.cluster(dataSet);
    for (Cluster<FuzzyDataElement<Point>> cluster : result) {
      System.out.printf("%n(%d) %s:%n%n", cluster.getDataElements().size(), cluster.getLabel());

      for (FuzzyDataElement elem : cluster.getDataElements()) {
        FuzzyDataElement<Point> doc = ((FuzzyDataElement<Point>) elem.getDataElement());
        System.out.printf("\t (%f) %s%n", elem.getWeight(), doc.getDataElement());
      }
    }
  }

  private static void textCMeans() throws Exception {
    File stopWords = new File("dictionary\\stopwords.txt");
    WordList stopWordList = WordList.load(stopWords);

    DocumentCollectionReader reader = new FileSystemDocumentCollectionReader(new File("c:\\samples"), stopWordList).useStemmer(new PorterStemmer());

    DocumentCollection docCollection = reader.readDocuments();

    /*
     * for (String term : new TreeSet<String>(docCollection.getAllTerms())) {
     * System.out.println(term); }
     */

    System.out.println("\r\n\r\n\r\n");

    DocumentVSMGenerator vsmGen = new NormalizedTFIDF();
    DocumentDataSet dataSet = vsmGen.createVSM(docCollection);

    CSVDataSetExporter.export(dataSet, new File("tmp\\dataset.csv"));

    final int numberOfClusters = 3;

    Clustering<FuzzyDataElement<DocumentDataElement>, Cluster<FuzzyDataElement<DocumentDataElement>>, DataSet<FuzzyDataElement<DocumentDataElement>>> clustering = new FuzzyCMeansClustering<FuzzyDataElement<DocumentDataElement>, DataSet<FuzzyDataElement<DocumentDataElement>>>(
        numberOfClusters, 0.25);// new TextKMeansAlgorithm(

    DataSet<FuzzyDataElement<DocumentDataElement>> toCLuster = new GenericDataSet<FuzzyDataElement<DocumentDataElement>>();
    for (DocumentDataElement e : dataSet.elements()) {
      toCLuster.addElement(FuzzyDataElement.newInstance(e, 0));
    }

    List<Cluster<FuzzyDataElement<DocumentDataElement>>> clusters = clustering.cluster(toCLuster);

    for (Cluster<FuzzyDataElement<DocumentDataElement>> cluster : clusters) {
      System.out.printf("%n(%d) %s:%n%n", cluster.getDataElements().size(), cluster.getLabel());

      for (FuzzyDataElement elem : cluster.getDataElements()) {
        DocumentDataElement doc = ((FuzzyDataElement<DocumentDataElement>) elem.getDataElement()).getDataElement();
        System.out.printf("\t(%20.15f)%s%n", elem.getWeight(), doc.getDocument().getTitle());
      }
    }
  }

}
