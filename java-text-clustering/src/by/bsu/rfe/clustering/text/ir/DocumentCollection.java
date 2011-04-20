package by.bsu.rfe.clustering.text.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class DocumentCollection implements Iterable<Document> {

  private List<Document> _documents;

  private Multiset<String> _terms = HashMultiset.create();

  public DocumentCollection() {
    _documents = new ArrayList<Document>();
  }

  public void addDocument(Document document) {
    Preconditions.checkNotNull(document, "Document is null");

    _documents.add(document);

    for (String term : document.getAllTerms()) {
      _terms.add(term, document.getTermCount(term));
    }
  }

  public Set<String> getAllTerms() {
    return Collections.unmodifiableSet(_terms.elementSet());
  }

  public int getTermCount(String term) {
    return _terms.count(term);
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
