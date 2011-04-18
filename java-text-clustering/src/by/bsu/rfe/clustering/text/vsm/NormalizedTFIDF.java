package by.bsu.rfe.clustering.text.vsm;

import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.Vector.Norm;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;
import by.bsu.rfe.clustering.text.ir.DocumentCollection;

public class NormalizedTFIDF implements DocumentVSMGenerator {

  private TFIDF _tfIdf = new TFIDF();

  @Override
  public DocumentDataSet createVSM(DocumentCollection documentCollection) {
    DocumentDataSet dataSet = _tfIdf.createVSM(documentCollection);

    return normalize(dataSet);
  }

  // normalize vectors in all elements to vector length and return the dataset
  private static DocumentDataSet normalize(DocumentDataSet dataSet) {
    for (DocumentDataElement elem : dataSet.elements()) {
      Vector documentVector = elem.asVector();
      double vectorLength = documentVector.norm(Norm.Two);

      for (VectorEntry entry : documentVector) {
        entry.set(entry.get() / vectorLength);
      }
    }

    return dataSet;
  }

}
