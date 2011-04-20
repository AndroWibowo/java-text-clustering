package by.bsu.rfe.clustering.algorithm.cluster;

import static com.google.common.base.Preconditions.checkNotNull;
import no.uib.cipr.matrix.Vector;
import by.bsu.rfe.clustering.algorithm.data.DataElement;

public class CentroidCluster<E extends DataElement> extends Cluster<E> {

  private Vector _centroid;

  public static <E extends DataElement> CentroidCluster<E> create() {
    return new CentroidCluster<E>();
  }

  public Vector getCentroid() {
    return _centroid;
  }

  public void setCentroid(Vector centroid) {
    _centroid = checkNotNull(centroid, "Centroid is null");
  }
}
