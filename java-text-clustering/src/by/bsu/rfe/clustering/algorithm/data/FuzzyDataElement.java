package by.bsu.rfe.clustering.algorithm.data;

import com.google.common.base.Preconditions;

import by.bsu.rfe.clustering.math.DoubleVector;

/**
 * 
 * @author Siarhei Yarashevich
 * 
 * @param <E>
 *            an implementation of {@link DataElement} to be wrapped
 */
public class FuzzyDataElement<E extends DataElement> implements DataElement {

    private E _dataElement;
    private double _weight;

    public FuzzyDataElement(E dataElement, double weight) {
        _dataElement = Preconditions.checkNotNull(dataElement, "Data Element is null");
        _weight = weight;
    }

    public FuzzyDataElement(E dataElement) {
        this(dataElement, 1);
    }

    public static <E extends DataElement> FuzzyDataElement<E> newInstance(E elem, double weight) {
        return new FuzzyDataElement<E>(elem, weight);
    }

    /**
     * 
     * @return degree of belonging to cluster
     */
    public double getWeight() {
        return _weight;
    }

    public void setWeight(double weight) {
        _weight = weight;
    }

    @Override
    public DoubleVector asVector() {
        return _dataElement.asVector();
    }

}
