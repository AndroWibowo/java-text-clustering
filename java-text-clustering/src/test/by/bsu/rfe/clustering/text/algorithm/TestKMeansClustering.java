package test.by.bsu.rfe.clustering.text.algorithm;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import test.by.bsu.rfe.clustering.app.util.CSVDataSetExporter;

import by.bsu.rfe.clustering.algorithm.ClusteringAlgorithm;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;
import by.bsu.rfe.clustering.text.algorithm.TextKMeansAlgorithm;
import by.bsu.rfe.clustering.text.document.Document;
import by.bsu.rfe.clustering.text.document.DocumentCollection;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.TFIDF;


public class TestKMeansClustering extends TestCase {

    @Test
    public void testKMeans() {
        String[][] terms = { 
                // cluster 1
                { "datamining", "classification", "clustering", "clustering" ,"kmeans"},
                { "datamining", "classification", "clustering", "clustering" ,"machine"},
                { "datamining", "classification", "clustering", "clustering" ,"coffee"},
                { "datamining", "classification", "clustering", "clustering" ,"beer"},
                // cluster 2
                { "programming", "language", "language", "job", "java"},
                { "programming", "language", "language", "job" ,"book"},
                { "programming", "language", "language", "job" ,"tdd"},
                { "programming", "language", "language", "job" ,"bitwise"}
        };

        DocumentCollection collection = new DocumentCollection();
        int ordinal = 1;

        for (String[] document : terms) {
            String title = "doc_" + ordinal++;
            
            Document newDoc = new Document(title);
            newDoc.setTitle(title);
            newDoc.setOriginalText(Arrays.toString(document));

            for (String term : document) {
                newDoc.addTerm(term);
            }

            collection.addDocument(newDoc);
        }

        final int numberOfClusters = 2;
        DocumentVSMGenerator vsm = new TFIDF();
        DocumentDataSet dataSet = vsm.createVSM(collection);
        
        try {
            CSVDataSetExporter.export(dataSet, new File("tmp/testDS.csv"));
        }
        catch (IOException e) {
            // nobody cares
        }

        DistanseMeasure distanse = new EuclideanDistanceMeasure();
        ClusteringAlgorithm<DocumentDataElement, Cluster<DocumentDataElement>, DocumentDataSet> clustering = 
            new TextKMeansAlgorithm(distanse, numberOfClusters);

        List<Cluster<DocumentDataElement>> clusters = clustering.cluster(dataSet);

        for (Cluster<DocumentDataElement> cluster : clusters) {
            System.out.printf("%n%s:%n%n", cluster.getLabel());

            for (DocumentDataElement elem : cluster.getDataElements()) {
                System.out.printf("\t%s%n", elem.getDocument().getOriginalText());
            }
        }
        
    }
}
