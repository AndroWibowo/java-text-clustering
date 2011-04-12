package by.bsu.rfe.clustering.algorithm.data;

import static com.google.common.collect.Constraints.constrainedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Constraints;

public class GenericDataSet<E extends DataElement> implements DataSet<E>, Iterable<E> {

  private final List<E> _elements;

  public GenericDataSet() {
    _elements = constrainedList(new ArrayList<E>(), Constraints.notNull());
  }

  public void addElement(E element) {
    _elements.add(element);
  }

  public List<E> elements() {
    return _elements;
  }

  public E get(int index) {
    return _elements.get(index);
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public Iterator<E> iterator() {
    return new ElementIterator();
  }

  public int size() {
    return _elements.size();
  }

  private class ElementIterator implements Iterator<E> {

    private final Iterator<E> _delegate;

    private ElementIterator() {
      _delegate = _elements.iterator();
    }

    @Override
    public boolean hasNext() {
      return _delegate.hasNext();
    }

    @Override
    public E next() {
      return _delegate.next();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove() is not supported");
    }
  }
}
