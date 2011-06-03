package by.bsu.rfe.clustering.algorithm.datamodel;

import static com.google.common.base.Preconditions.checkNotNull;
import by.bsu.rfe.clustering.math.DoubleVector;

public class GenericDataElement implements DataElement {

    private DoubleVector _vector;

    public GenericDataElement(DoubleVector vector) {
        _vector = checkNotNull(vector, "Vector is null");
    }

    @Override
    public DoubleVector asVector() {
        return _vector;
    }

}
