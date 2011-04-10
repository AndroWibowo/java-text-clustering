package by.bsu.rfe.clustering.math;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.base.Preconditions;

/**
 * This implementation of {@link DoubleVector} is used for vectors where most of
 * values are zeros.
 * 
 * @author Siarhei Yarashevich
 * 
 */
public class DoubleSparceVector implements DoubleVector {

  private final Map<Integer, Double> _values;
  private int _size = 0;

  public DoubleSparceVector(int size) {
    Preconditions.checkArgument(size >= 0, "Negative size: " + size);

    _values = new HashMap<Integer, Double>();
    _size = size;
  }

  public double get(int index) {
    rangeCheck(index);

    Double value = _values.get(index);
    return (value == null) ? 0 : value;
  }

  @Override
  public Iterable<Integer> indices() {

    return new Iterable<Integer>() {
      @Override
      public Iterator<Integer> iterator() {
        return new IndicesIterator(_size);
      }
    };
  }

  public void set(int index, double value) {
    rangeCheck(index);

    if (value != 0) {
      _values.put(index, value);
    }
    else {
      _values.remove(index);
    }
  }

  public int size() {
    return _size;
  }

  private void rangeCheck(int index) {
    Preconditions.checkElementIndex(index, size());
  }

  private static class IndicesIterator implements Iterator<Integer> {

    private int _size;
    private int _currentIndex;

    private IndicesIterator(int size) {
      _size = size;
      _currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
      return _currentIndex < _size;
    }

    @Override
    public Integer next() {
      if (!hasNext()) {
        throw new NoSuchElementException("No more elements are available");
      }

      return _currentIndex++;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove() is not supported");
    }
  }

  @Override
  public double vectorLength() {
    double sum = 0;

    for (Integer index : _values.keySet()) {
      double val = get(index);
      sum += (val * val);
    }

    return Math.sqrt(sum);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof DoubleVector)) {
      return false;
    }

    DoubleVector v = (DoubleVector) obj;
    if (v.size() != size()) {
      return false;
    }

    for (Integer index : indices()) {
      if (Double.compare(get(index), v.get(index)) != 0) {
        return false;
      }
    }
    return true;
  }
}
