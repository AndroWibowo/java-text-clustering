package by.bsu.rfe.clustering.algorithm.data;

import by.bsu.rfe.clustering.math.DoubleVector;

public interface DataElement {

    /**
     * Returns vector representation of this element
     * 
     * @return an implementation of {@link DoubleVector} - vector representation
     *         of this element
     */
    public DoubleVector asVector();

}
