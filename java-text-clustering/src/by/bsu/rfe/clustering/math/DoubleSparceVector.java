package by.bsu.rfe.clustering.math;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.base.Preconditions;

public class DoubleSparceVector implements DoubleVector {

    private final Map<Integer, Double> _data;
    private int _size = 0;

    public DoubleSparceVector(int size) {
        Preconditions.checkArgument(size >= 0, "Negative size: " + size);

        _data = new HashMap<Integer, Double>();
        _size = size;
    }

    public double get(int index) {
        rangeCheck(index);

        Double value = _data.get(index);
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
            _data.put(index, value);
        }
        else {
            _data.remove(index);
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
}
