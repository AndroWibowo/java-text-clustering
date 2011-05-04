package by.bsu.rfe.clustering.algorithm.cluster;

import static com.google.common.collect.Constraints.constrainedList;

import java.util.ArrayList;
import java.util.List;

import by.bsu.rfe.clustering.algorithm.data.DataElement;

import com.google.common.collect.Constraints;

/**
 * A group of one or more {@link DataElement} instances that are similar to each
 * other
 * 
 * @author Siarhei_Yarashevich
 * 
 * @param <E>
 *            type of {@link DataElement}
 */
public class Cluster<E extends DataElement> {

    private String _label;

    private List<E> _elements = constrainedList(new ArrayList<E>(), Constraints.notNull());

    public static <E extends DataElement> Cluster<E> create() {
        return new Cluster<E>();
    }

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        _label = label;
    }

    public List<E> getDataElements() {
        return _elements;
    }

    public void addDataElement(E dataElement) {
        _elements.add(dataElement);
    }

    public void addAll(Iterable<E> dataElements) {
        for (E elem : dataElements) {
            addDataElement(elem);
        }
    }

}
