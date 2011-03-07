package by.bsu.rfe.clustering.text.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;

public class DocumentCollection implements Iterable<Document> {

	private class DocumentIterator implements Iterator<Document> {

		private Iterator<Document> _delegate;

		private DocumentIterator() {
			_delegate = _documents.iterator();
		}

		@Override
		public boolean hasNext() {
			return _delegate.hasNext();
		}

		@Override
		public Document next() {
			return _delegate.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Operation is not supported");
		}

	}

	private List<Document> _documents;

	private Set<String> _terms;

	public DocumentCollection() {
		_documents = new ArrayList<Document>();
		_terms = new HashSet<String>();
	}

	public void addDocument(Document document) {
		Preconditions.checkNotNull(document, "Document is null");

		_documents.add(document);
		_terms.addAll(document.getAllTerms());
	}

	public Set<String> getAllTerms() {
		return Collections.unmodifiableSet(_terms);
	}

	public List<Document> getDocuments() {
		return Collections.unmodifiableList(_documents);
	}

	@Override
	public Iterator<Document> iterator() {
		return new DocumentIterator();
	}

	public int size() {
		return _documents.size();
	}

}
