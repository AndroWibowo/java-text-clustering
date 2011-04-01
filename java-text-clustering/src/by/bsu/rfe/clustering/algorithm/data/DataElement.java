package by.bsu.rfe.clustering.algorithm.data;

import static com.google.common.base.Preconditions.checkNotNull;
import by.bsu.rfe.clustering.math.DoubleVector;

public class DataElement {

    private DoubleVector _vector;

    public DataElement(DoubleVector vector) {
        _vector = checkNotNull(vector, "Vector is null");
    }

    public DoubleVector asVector() {
        return _vector;
    }

}
