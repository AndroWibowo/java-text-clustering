package by.bsu.rfe.clustering.algorithm.cluster;

import java.util.List;

import no.uib.cipr.matrix.Vector;

import by.bsu.rfe.clustering.algorithm.data.DataElement;

public class CentroidCluster<E extends DataElement> extends Cluster<E> {

  private Vector _centroid;

  // TODO move the method logic to utility class
  /**
   * Computes the center of this cluster.
   * 
   * @throws IllegalStateException
   *           if the cluster has no elements
   * @return a {@link DoubleVector} which is the center of this cluster
   */
/*  public DoubleVector computeCentroid() {

    List<E> elements = getDataElements();

    if (elements.isEmpty()) {
      throw new IllegalStateException("Cluster has no elements");
    }

    final int size = elements.get(0).asVector().size();

    DoubleSparceVector resultVector = new DoubleSparceVector(size);

    for (int vectorIndex : resultVector.indices()) {
      double nextValue = 0;

      for (int elemIndex = 0; elemIndex < elements.size(); elemIndex++) {
        nextValue += (elements.get(elemIndex).asVector().get(vectorIndex));
      }

      resultVector.set(vectorIndex, nextValue / elements.size());
    }

    return resultVector;
  }*/

  public Vector getCentroid() {
    return _centroid;
  }

  public void setCentroid(Vector centroid) {
    _centroid = centroid;
  }
}