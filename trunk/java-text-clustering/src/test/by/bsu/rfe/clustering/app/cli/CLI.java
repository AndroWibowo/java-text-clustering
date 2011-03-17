package test.by.bsu.rfe.clustering.app.cli;

import java.io.File;
import java.net.URL;
import java.util.List;

import com.google.common.base.Joiner;

import by.bsu.rfe.clustering.algorithm.ClusteringAlgorithm;
import by.bsu.rfe.clustering.algorithm.KMeansAlgorithm;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.EuclideanDistance;
import by.bsu.rfe.clustering.math.VectorDistanse;
import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.database.DocumentCollectionReader;
import by.bsu.rfe.clustering.text.database.RSSDocumentCollectionReader;
import by.bsu.rfe.clustering.text.document.DocumentCollection;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.TFIDF;

public class CLI {

    public static void main(String[] args) throws Exception {
        File stopWords = new File("dictionary\\stopwords.txt");
        WordList stopWordList = WordList.load(stopWords);

        DocumentCollectionReader reader = new RSSDocumentCollectionReader(stopWordList).addSource(
                new URL("http://feeds.bbci.co.uk/news/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/world/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/uk/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/business/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/politics/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/health/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/education/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/science_and_environment/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/technology/rss.xml")).addSource(
                new URL("http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml"));

        DocumentCollection docCollection = reader.readDocuments();
        DocumentVSMGenerator<DocumentDataElement> vsmGen = new TFIDF();
        DataSet<DocumentDataElement> dataSet = vsmGen.generateVSM(docCollection);

        VectorDistanse distanse = new EuclideanDistance();

        final int numberOfClusters = 20;

        ClusteringAlgorithm<DocumentDataElement, Cluster<DocumentDataElement>> clustering = new KMeansAlgorithm<DocumentDataElement>(
                distanse, numberOfClusters);

        List<Cluster<DocumentDataElement>> clusters = clustering.cluster(dataSet);

        for (Cluster<DocumentDataElement> cluster : clusters) {
            System.out.printf("%s:%n", cluster.getLabel());

            for (DocumentDataElement elem : cluster.getDataElements()) {
                System.out.printf("\t\t\t%s%n", Joiner.on(",").join(elem.getDocument().getAllTerms()));
            }
        }
    }

}
