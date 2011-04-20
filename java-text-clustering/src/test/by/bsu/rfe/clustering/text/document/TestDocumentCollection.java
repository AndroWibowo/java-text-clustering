package test.by.bsu.rfe.clustering.text.document;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

import test.by.bsu.rfe.clustering.app.util.CSVDataSetExporter;
import by.bsu.rfe.clustering.text.data.DocumentDataSet;
import by.bsu.rfe.clustering.text.ir.Document;
import by.bsu.rfe.clustering.text.ir.DocumentCollection;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.TFIDF;

public class TestDocumentCollection extends TestCase {

  @Test
  public void testTermCount() {
    String[][] terms = { { "document", "collection", "data", "text" }, { "clustering", "text" },
        { "text", "data", "point", "java", "java" } };

    DocumentCollection collection = new DocumentCollection();
    int ordinal = 1;

    for (String[] document : terms) {
      Document newDoc = new Document("doc_" + ordinal++);

      for (String term : document) {
        newDoc.addTerm(term);
      }

      collection.addDocument(newDoc);
    }

    assertEquals(collection.getTermCount("document"), 1);
    assertEquals(collection.getTermCount("java"), 2);
    assertEquals(collection.getTermCount("text"), 3);
    assertEquals(collection.getTermCount("dummy"), 0);

    assertEquals(collection.getAllTerms().size(), 7);

    DocumentVSMGenerator vsm = new TFIDF();
    DocumentDataSet dataSet = vsm.createVSM(collection);

    try {
      CSVDataSetExporter.export(dataSet, new File("tmp/testDS.csv"));
    }
    catch (IOException e) {
      // nobody cares
    }
  }
}
