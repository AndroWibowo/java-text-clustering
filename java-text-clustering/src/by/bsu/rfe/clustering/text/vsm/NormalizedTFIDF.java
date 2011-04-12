package by.bsu.rfe.clustering.text.vsm;

import by.bsu.rfe.clustering.math.DoubleVector;
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
      DoubleVector documentVector = elem.asVector();
      double vectorLength = documentVector.vectorLength();

      for (Integer index : documentVector.indices()) {
        double value = documentVector.get(index);
        documentVector.set(index, value / vectorLength);
      }
    }

    return dataSet;
  }

}
