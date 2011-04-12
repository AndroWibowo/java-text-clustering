package test.by.bsu.rfe.clustering.text.document;

import junit.framework.TestCase;

import org.junit.Test;

import by.bsu.rfe.clustering.text.ir.Document;

public class TestDocument extends TestCase {

  @Test
  public void testTermCount() {
    Document testDoc = new Document("testDoc");
    String[] terms = { "data", "data", "clustering", "kmeans", "kmeans", "kmeans" };

    for (String term : terms) {
      testDoc.addTerm(term);
    }

    assertEquals(testDoc.getTermCount("data"), 2);
    assertEquals(testDoc.getTermCount("clustering"), 1);
    assertEquals(testDoc.getTermCount("kmeans"), 3);
    assertEquals(testDoc.getTermCount("dummy"), 0);

    assertEquals(testDoc.totalTerms(), 6);
    assertEquals(testDoc.getAllTerms().size(), 3);
  }

}
