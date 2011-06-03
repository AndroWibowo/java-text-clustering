package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.algorithm.datamodel.Cluster;
import by.bsu.rfe.clustering.algorithm.datamodel.DataElement;
import by.bsu.rfe.clustering.algorithm.datamodel.DataSet;
import by.bsu.rfe.clustering.math.ComputationException;

public interface FlatClustering<E extends DataElement, C extends Cluster<E>, D extends DataSet<E>> {

  /**
   * @throws ComputationException
   */
  public List<C> cluster(D dataSet);

}
