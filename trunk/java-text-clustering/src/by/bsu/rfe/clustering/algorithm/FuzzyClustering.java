package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.algorithm.datamodel.Cluster;
import by.bsu.rfe.clustering.algorithm.datamodel.DataElement;
import by.bsu.rfe.clustering.algorithm.datamodel.DataSet;
import by.bsu.rfe.clustering.algorithm.datamodel.FuzzyDataElement;

public interface FuzzyClustering<E extends DataElement, D extends DataSet<E>> {

    List<Cluster<FuzzyDataElement<E>>> cluster(D dataSet);

}
