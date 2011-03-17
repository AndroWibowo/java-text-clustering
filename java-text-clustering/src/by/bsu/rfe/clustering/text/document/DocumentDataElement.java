package by.bsu.rfe.clustering.text.document;

import com.google.common.base.Preconditions;

import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.math.DoubleVector;

public class DocumentDataElement extends DataElement {

    private Document _document;

    public DocumentDataElement(DoubleVector vector, Document document) {
        super(vector);
        _document = Preconditions.checkNotNull(document, "Document is null");
    }

    public Document getDocument() {
        return _document;
    }
}
