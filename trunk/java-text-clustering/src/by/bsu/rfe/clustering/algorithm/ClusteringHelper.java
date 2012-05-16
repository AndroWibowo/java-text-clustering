package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.algorithm.datamodel.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.datamodel.DataElement;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.math.MathUtil;

public final class ClusteringHelper {

  private ClusteringHelper() {
  }

  public static <E extends DataElement> double computeSquareError(CentroidCluster<E> cluster) {
    List<E> elements = cluster.getDataElements();

    if (elements.isEmpty()) {
      throw new IllegalStateException("Empty cluster");
    }

    DoubleVector center = cluster.computeCentroid();

    double totalError = 0;
    final int vectorSize = center.size();

    for (DataElement e : elements) {
      DoubleVector elemVector = e.asVector();
      double errorWithinCluster = 0;

      for (int i = 0; i < vectorSize; i++) {
        errorWithinCluster += MathUtil.square(elemVector.get(i) - center.get(i));
      }

      totalError += errorWithinCluster;
    }

    return totalError;
  }
}
