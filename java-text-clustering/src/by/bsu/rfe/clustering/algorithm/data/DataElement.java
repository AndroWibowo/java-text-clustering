package by.bsu.rfe.clustering.algorithm.data;

import by.bsu.rfe.clustering.math.DoubleVector;

import com.google.common.base.Preconditions;

public class DataElement {

    private DoubleVector _vector;

    public DataElement(DoubleVector vector) {
        Preconditions.checkNotNull(vector, "Vector is null");
        _vector = vector;
    }

    public DoubleVector getVectorModel() {
        return _vector;
    }

}
