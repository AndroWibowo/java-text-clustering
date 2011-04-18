package by.bsu.rfe.clustering.text.vsm;

import java.util.Set;

import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.SparseVector;

import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;
import by.bsu.rfe.clustering.text.ir.Document;
import by.bsu.rfe.clustering.text.ir.DocumentCollection;

import com.google.common.base.Preconditions;

public class TFIDF implements DocumentVSMGenerator {

  @Override
  public DocumentDataSet createVSM(DocumentCollection documentCollection) {
    Preconditions.checkNotNull(documentCollection, "DocumentCollection is null");

    DocumentDataSet resultSet = new DocumentDataSet(documentCollection);

    for (Document document : documentCollection) {
      Vector vector = calculateTFIDF(document, documentCollection);
      DocumentDataElement dataElement = new DocumentDataElement(vector, document);
      resultSet.addElement(dataElement);
    }

    return resultSet;
  }

  private Vector calculateTFIDF(Document document, DocumentCollection collection) {
    Set<String> allTerms = collection.getAllTerms();
    Vector resultVector = new SparseVector(allTerms.size());

    int index = 0;
    for (String term : allTerms) {
      int termCount = document.getTermCount(term);

      if (termCount > 0) {
        double tf = ((double) termCount) / document.totalTerms();

        // number of documents in the collection
        double totalDocuments = collection.size();

        int documentsWithTerm = documentsWithTerm(term, collection);

        double tfIdf = tf * Math.log(1 + totalDocuments / documentsWithTerm);
        resultVector.set(index, tfIdf);
      }

      index++;
    }

    return resultVector;
  }

  // how many documents contain the specified term
  private int documentsWithTerm(String term, DocumentCollection collection) {
    int frequency = 0;

    for (Document document : collection) {
      if (document.getTermCount(term) > 0) {
        frequency++;
      }
    }

    return frequency;
  }

}
