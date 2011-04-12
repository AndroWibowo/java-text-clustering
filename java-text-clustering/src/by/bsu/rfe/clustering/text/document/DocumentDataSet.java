package by.bsu.rfe.clustering.text.document;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.algorithm.data.GenericDataSet;
import by.bsu.rfe.clustering.text.ir.DocumentCollection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class DocumentDataSet implements DataSet<DocumentDataElement> {

  private Map<String, Integer> _documentIndexMap = new HashMap<String, Integer>();

  private Map<String, Integer> _termIndexMap = Maps.newHashMap();

  private DataSet<DocumentDataElement> _delegateDataSet = new GenericDataSet<DocumentDataElement>();

  public DocumentDataSet(DocumentCollection collection) {
    int termIndex = 0;
    for (String term : collection.getAllTerms()) {
      _termIndexMap.put(term, termIndex++);
    }
  }

  // TODO set a more descriptive method name
  public double getTermWeight(String documentId, String term) {
    Integer documentIndex = _documentIndexMap.get(documentId);
    DocumentDataElement element = _delegateDataSet.get(documentIndex);

    Integer termIndexInVector = _termIndexMap.get(term);
    return element.asVector().get(termIndexInVector);
  }

  public void addElement(DocumentDataElement element) {
    Preconditions.checkNotNull(element, "Element is null");

    String documentId = element.getDocument().getId();
    if (_documentIndexMap.containsKey(documentId)) {
      throw new IllegalArgumentException("Document with duplicate id");
    }

    _delegateDataSet.addElement(element);
    _documentIndexMap.put(documentId, _delegateDataSet.size() - 1);
  }

  public Set<String> getAllTerms() {
    return Collections.unmodifiableSet(_termIndexMap.keySet());
  }

  public DocumentDataElement get(int index) {
    return _delegateDataSet.get(index);
  }

  public List<DocumentDataElement> elements() {
    return _delegateDataSet.elements();
  }

  public boolean isEmpty() {
    return _delegateDataSet.isEmpty();
  }

  public int size() {
    return _delegateDataSet.size();
  }

}
