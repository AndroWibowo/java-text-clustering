package by.bsu.rfe.clustering.algorithm.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;

public class DataSet implements Iterable<DataElement> {

	private final List<DataElement> _elements;

	public DataSet() {
		_elements = new ArrayList<DataElement>();
	}

	public void addElement(DataElement element) {
		Preconditions.checkNotNull(element, "Data Element is null");
		_elements.add(element);
	}

	public List<DataElement> getElements() {
		return Collections.unmodifiableList(_elements);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<DataElement> iterator() {
		return new ElementIterator();
	}

	public int size() {
		return _elements.size();
	}

	private class ElementIterator implements Iterator<DataElement> {

		private final Iterator<DataElement> _delegate;

		private ElementIterator() {
			_delegate = _elements.iterator();
		}

		@Override
		public boolean hasNext() {
			return _delegate.hasNext();
		}

		@Override
		public DataElement next() {
			return _delegate.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove() is not supported");
		}
	}
}
