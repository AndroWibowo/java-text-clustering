package by.bsu.rfe.clustering.algorithm.cluster;

import java.util.Collection;
import java.util.LinkedList;

import com.google.common.base.Preconditions;

import by.bsu.rfe.clustering.algorithm.data.DataElement;

public class Cluster<E extends DataElement> {

    private String _label;

    private Collection<E> _data = new LinkedList<E>();

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        _label = label;
    }

    public Collection<E> getDataElements() {
        return _data;
    }

    public void addDataElement(E dataElement) {
        _data.add(Preconditions.checkNotNull(dataElement, "Data element is null."));
    }

    public void addDataElements(Iterable<E> dataElements) {
        for (E elem : dataElements) {
            _data.add(elem);
        }
    }

}
