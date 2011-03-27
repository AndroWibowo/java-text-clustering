package test.by.bsu.rfe.clustering.app.cli;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import by.bsu.rfe.clustering.algorithm.ClusteringAlgorithm;
import by.bsu.rfe.clustering.algorithm.KMeansAlgorithm;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.EuclideanDistance;
import by.bsu.rfe.clustering.math.VectorDistanse;
import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.algorithm.TextKMeansAlgorithm;
import by.bsu.rfe.clustering.text.document.DocumentCollection;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.ir.DocumentCollectionReader;
import by.bsu.rfe.clustering.text.ir.FileSystemDocumentCollectionReader;
import by.bsu.rfe.clustering.text.ir.RSSDocumentCollectionReader;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.TFIDF;

public class CLI {

    public static void main(String[] args) throws Exception {
        File stopWords = new File("dictionary\\stopwords.txt");
        WordList stopWordList = WordList.load(stopWords);


        DocumentCollectionReader reader = new FileSystemDocumentCollectionReader(new File("samples"),
                stopWordList);

        DocumentCollection docCollection = reader.readDocuments();

        for (String term : new TreeSet<String>(docCollection.getAllTerms())) {
            System.out.println(term);
        }

        System.out.println("\r\n\r\n\r\n");

        DocumentVSMGenerator<DocumentDataElement> vsmGen = new TFIDF();
        DataSet<DocumentDataElement> dataSet = vsmGen.generateVSM(docCollection);

        VectorDistanse distanse = new EuclideanDistance();

        final int numberOfClusters = 10;

        ClusteringAlgorithm<DocumentDataElement, Cluster<DocumentDataElement>> clustering = new TextKMeansAlgorithm(
                distanse, numberOfClusters);

        List<Cluster<DocumentDataElement>> clusters = clustering.cluster(dataSet);

        for (Cluster<DocumentDataElement> cluster : clusters) {
            System.out.printf("%n%s:%n%n", cluster.getLabel());

            for (DocumentDataElement elem : cluster.getDataElements()) {
                System.out.printf("\t%s%n", elem.getDocument().getTitle());
            }
        }
    }

}
