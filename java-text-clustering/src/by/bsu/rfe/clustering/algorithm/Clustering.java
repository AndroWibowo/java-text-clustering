package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;

public interface Clustering<E extends DataElement, C extends Cluster<E>, D extends DataSet<E>> {

  public List<C> cluster(D dataSet);

}
