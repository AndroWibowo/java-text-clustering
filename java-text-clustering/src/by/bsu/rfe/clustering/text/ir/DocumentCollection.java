package by.bsu.rfe.clustering.text.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;

public class DocumentCollection implements Iterable<Document> {

    private List<Document> _documents;

    private Map<String, Integer> _terms;

    public DocumentCollection() {
        _documents = new ArrayList<Document>();
        _terms = new HashMap<String, Integer>();
    }

    public void addDocument(Document document) {
        Preconditions.checkNotNull(document, "Document is null");

        _documents.add(document);

        for (String term : document.getAllTerms()) {
            Integer termCount = _terms.get(term);
            termCount = (termCount == null) ? 0 : termCount;
            _terms.put(term, termCount + document.getTermCount(term));
        }
    }

    public Set<String> getAllTerms() {
        return Collections.unmodifiableSet(_terms.keySet());
    }

    public int getTermCount(String term) {
        Integer termCount = _terms.get(term);
        return (termCount == null) ? 0 : termCount;
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
            throw new UnsupportedOperationException("remove() is not supported");
        }

    }

}
