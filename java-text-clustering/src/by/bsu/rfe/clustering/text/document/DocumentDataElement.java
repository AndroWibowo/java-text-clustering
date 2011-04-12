package by.bsu.rfe.clustering.text.document;

import by.bsu.rfe.clustering.algorithm.data.GenericDataElement;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.text.ir.Document;

import com.google.common.base.Preconditions;

public class DocumentDataElement extends GenericDataElement {

  private Document _document;

  public DocumentDataElement(DoubleVector vector, Document document) {
    super(vector);
    _document = Preconditions.checkNotNull(document, "Document is null");
  }

  public Document getDocument() {
    return _document;
  }
}
