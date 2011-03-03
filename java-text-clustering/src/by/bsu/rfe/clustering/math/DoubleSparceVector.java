package by.bsu.rfe.clustering.math;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

public class DoubleSparceVector {

  private final Map<Integer, Double> _data;
  private int _size = 0;

  public DoubleSparceVector(int size) {
    Preconditions.checkArgument(size >= 0, "Negative size: " + size);

    _data = new HashMap<Integer, Double>();
    _size = size;
  }

  public void set(int index, double value) {
    rangeCheck(index);
    _data.put(index, value);
  }

  public double get(int index) {
    rangeCheck(index);

    Double value = _data.get(index);
    return (value == null) ? 0 : value;
  }

  public int size() {
    return _size;
  }

  private void rangeCheck(int index) {
    Preconditions.checkElementIndex(index, size());
  }

}
