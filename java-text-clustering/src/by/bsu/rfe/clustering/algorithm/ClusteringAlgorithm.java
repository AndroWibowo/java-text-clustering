package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;

public interface ClusteringAlgorithm<E extends DataElement, C extends Cluster<E>> {

    public List<C> cluster(DataSet<E> dataSet);

}
