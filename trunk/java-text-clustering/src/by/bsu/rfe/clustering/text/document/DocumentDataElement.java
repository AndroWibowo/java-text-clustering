package by.bsu.rfe.clustering.text.document;

import no.uib.cipr.matrix.Vector;
import by.bsu.rfe.clustering.algorithm.data.GenericDataElement;
import by.bsu.rfe.clustering.text.ir.Document;

import com.google.common.base.Preconditions;

public class DocumentDataElement extends GenericDataElement {

  private Document _document;

  public DocumentDataElement(Vector vector, Document document) {
    super(vector);
    _document = Preconditions.checkNotNull(document, "Document is null");
  }

  public Document getDocument() {
    return _document;
  }
}
