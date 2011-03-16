package by.bsu.rfe.clustering.math;

public interface DoubleVector {

    public double get(int index);

    public Iterable<Integer> indices();

    public void set(int index, double value);

    public int size();
}
