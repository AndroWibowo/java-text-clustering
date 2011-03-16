package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataSet;

public interface ClusteringAlgorithm<C extends Cluster> {

    public List<C> cluster(DataSet dataSet);

}
