package by.bsu.rfe.clustering.algorithm.data;

import java.util.List;

public interface DataSet<E extends DataElement> {

    public void addElement(E element);

    public List<E> elements();

    public E getElement(int index);

    public boolean isEmpty();

    public int size();
}
