package test.by.bsu.rfe.clustering.app.cli.testapache;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;

import test.by.bsu.rfe.clustering.app.util.CSVDataSetExporter;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;
import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;
import by.bsu.rfe.clustering.text.ir.DocumentCollection;
import by.bsu.rfe.clustering.text.ir.DocumentCollectionReader;
import by.bsu.rfe.clustering.text.ir.FileSystemDocumentCollectionReader;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.TFIDF;

import com.google.common.collect.Lists;

public class CLI {

  private static transient Log _log = LogFactory.getLog(CLI.class);

  public static void main(String[] args) throws Exception {
    File stopWords = new File("dictionary\\stopwords.txt");
    WordList stopWordList = WordList.load(stopWords);

    DocumentCollectionReader reader = new FileSystemDocumentCollectionReader(new File("samples"), stopWordList);

    DocumentCollection docCollection = reader.readDocuments();

    /*
     * for (String term : new TreeSet<String>(docCollection.getAllTerms())) {
     * System.out.println(term); }
     */

    System.out.println("\r\n\r\n\r\n");

    DocumentVSMGenerator vsmGen = new TFIDF();
    DocumentDataSet dataSet = vsmGen.createVSM(docCollection);

    CSVDataSetExporter.export(dataSet, new File("tmp\\dataset.csv"));

    DistanseMeasure distanse = new EuclideanDistanceMeasure();

    final int numberOfClusters = 20;

    Collection<DocumentClusterable> points = Lists.newArrayListWithCapacity(dataSet.size());
    for (DocumentDataElement elem : dataSet.elements()) {
      points.add(new DocumentClusterable(elem.asVector(), elem.getDocument()));
    }

    KMeansPlusPlusClusterer<DocumentClusterable> clust = new KMeansPlusPlusClusterer<DocumentClusterable>(new Random(),
        KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);

    List<org.apache.commons.math.stat.clustering.Cluster<DocumentClusterable>> clusters = clust.cluster(points,
        numberOfClusters, 100);

    for (org.apache.commons.math.stat.clustering.Cluster<DocumentClusterable> cluster : clusters) {
      System.out.printf("%n%s:%n%n", "ololo! I am the next cluster!");

      for (DocumentDataElement elem : cluster.getPoints()) {
        System.out.printf("\t%s%n", elem.getDocument().getTitle());
      }
    }

  }

}
