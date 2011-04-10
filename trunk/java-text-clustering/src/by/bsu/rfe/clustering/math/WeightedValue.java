package by.bsu.rfe.clustering.math;

public class WeightedValue<V> implements Comparable<WeightedValue<V>> {
  private V _value;
  private double _weight;

  public WeightedValue(V value, double weight) {
    _value = value;
    _weight = weight;
  }

  public static <V> WeightedValue<V> of(V value, double weight) {
    return new WeightedValue<V>(value, weight);
  }

  public double weight() {
    return _weight;
  }

  public void setWeight(double weight) {
    _weight = weight;
  }

  public V value() {
    return _value;
  }

  @Override
  public int compareTo(WeightedValue<V> anotherValue) {
    return Double.compare(weight(), anotherValue.weight());
  }

  @Override
  public String toString() {
    return _value + ":" + _weight;
  }
}