package by.bsu.rfe.clustering.algorithm.cluster;

import static com.google.common.collect.Constraints.constrainedList;

import java.util.ArrayList;
import java.util.List;

import by.bsu.rfe.clustering.algorithm.data.DataElement;

import com.google.common.collect.Constraints;

public class Cluster<E extends DataElement> {

    private String _label;

    private List<E> _data = constrainedList(new ArrayList<E>(), Constraints.notNull());

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        _label = label;
    }

    public List<E> getDataElements() {
        return _data;
    }

    public void addDataElement(E dataElement) {
        _data.add(dataElement);
    }

    public void addAll(Iterable<E> dataElements) {
        for (E elem : dataElements) {
            addDataElement(elem);
        }
    }

}
