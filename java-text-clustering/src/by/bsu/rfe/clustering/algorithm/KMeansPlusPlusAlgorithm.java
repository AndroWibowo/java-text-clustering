package by.bsu.rfe.clustering.algorithm;

import java.util.List;

import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.VectorDistanse;

public class KMeansPlusPlusAlgorithm<E extends DataElement, D extends DataSet<E>> extends KMeansAlgorithm<E, D> {

    public KMeansPlusPlusAlgorithm(VectorDistanse vectorDistanse) {
        super(vectorDistanse);
    }

    @Override
    protected void selectInitialCenters(D dataSet, List<Bin> bins) {
        super.selectInitialCenters(dataSet, bins);
    }

}
