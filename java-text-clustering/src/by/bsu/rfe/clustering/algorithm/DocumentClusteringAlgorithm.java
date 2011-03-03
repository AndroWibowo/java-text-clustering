package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.model.cluster.Cluster;
import by.bsu.rfe.clustering.model.document.DocumentCollection;

public interface DocumentClusteringAlgorithm {

  public List<Cluster> cluster(DocumentCollection documentCollection);

}
