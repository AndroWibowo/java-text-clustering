package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.algorithm.data.FuzzyDataElement;

public interface FuzzyClustering<E extends DataElement, D extends DataSet<E>> {

  List<Cluster<FuzzyDataElement<E>>> cluster(D dataSet);

}
