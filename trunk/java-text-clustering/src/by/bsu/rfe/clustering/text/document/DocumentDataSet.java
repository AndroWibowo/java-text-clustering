package by.bsu.rfe.clustering.text.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.algorithm.data.GenericDataSet;

import com.google.common.base.Preconditions;

public class DocumentDataSet implements DataSet<DocumentDataElement> {

    private Map<String, Integer> _documentIndexMap = new HashMap<String, Integer>();

    private Map<String, Integer> _termIndexMap = new HashMap<String, Integer>();

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
        DocumentDataElement element = _delegateDataSet.getElement(documentIndex);

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

    public DocumentDataElement getElement(int index) {
        return _delegateDataSet.getElement(index);
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
