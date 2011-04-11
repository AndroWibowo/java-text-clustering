package by.bsu.rfe.clustering.text.algorithm;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import test.by.bsu.rfe.clustering.app.util.CSVDataSetExporter;
import by.bsu.rfe.clustering.algorithm.Clustering;
import by.bsu.rfe.clustering.algorithm.KMeansClustering;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.text.document.Document;
import by.bsu.rfe.clustering.text.document.DocumentCollection;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.TFIDF;

import com.google.common.collect.MinMaxPriorityQueue;

// TODO use builder pattern
public class TextKMeansClustering implements
        Clustering<DocumentDataElement, Cluster<DocumentDataElement>, DocumentDataSet> {

    private KMeansClustering<DocumentDataElement, DocumentDataSet> _kMeans;

    public TextKMeansClustering(Integer numberOfClusters) {
        _kMeans = new KMeansClustering<DocumentDataElement, DocumentDataSet>(numberOfClusters);
    }

    @Override
    public List<Cluster<DocumentDataElement>> cluster(DocumentDataSet dataSet) {
        List<Cluster<DocumentDataElement>> cluserList = _kMeans.cluster(dataSet);
        assignLabels(cluserList, dataSet);

        return cluserList;
    }

    public void setNumberOfClusters(Integer numberOfClusters) {
        _kMeans.setNumberOfClusters(numberOfClusters);
    }

    private void assignLabels(List<Cluster<DocumentDataElement>> clusterData, DocumentDataSet dataSet) {
        for (Cluster<DocumentDataElement> cluster : clusterData) {
            MinMaxPriorityQueue<TermEntry> queue = MinMaxPriorityQueue.orderedBy(new Comparator<TermEntry>() {

                @Override
                public int compare(TermEntry o1, TermEntry o2) {
                    return -Double.compare(o1.getScore(), o2.getScore());
                }
            }).maximumSize(5).create();

            DocumentCollection localCollection = new DocumentCollection();
            for (DocumentDataElement elem : cluster.getDataElements()) {
                localCollection.addDocument(elem.getDocument());
            }

            DocumentVSMGenerator docToVsm = new TFIDF();
            DocumentDataSet clusterDataSet = docToVsm.createVSM(localCollection);
            // TODO remove this
            try {
                CSVDataSetExporter.export(clusterDataSet, new File("tmp/" + cluster.getLabel() + ".csv"));
            }
            catch (IOException e) {

            }

            for (DocumentDataElement elem : clusterDataSet.elements()) {
                Document document = elem.getDocument();

                for (String term : document.getAllTerms()) {

                    double termWeight = clusterDataSet.getTermWeight(document.getId(), term);
                    queue.offer(new TermEntry(term, termWeight * getDocumentCount(term, cluster)));
                }
            }

            String label = "";
            StringBuilder labelBuilder = new StringBuilder();

            // TODO this is a debug version of labels
            for (TermEntry termEntry : queue) {
                labelBuilder.append(termEntry.getTerm()).append(":")
                        .append(String.format("%7.5f", termEntry.getScore())).append(";")
                        .append(getDocumentCount(termEntry.getTerm(), cluster)).append(",");
            }

            if (labelBuilder.length() > 0) {
                label = labelBuilder.substring(0, labelBuilder.length() - 1);
            }
            cluster.setLabel(label);
        }
    }

    private int getDocumentCount(String term, Cluster<DocumentDataElement> cluster) {
        int count = 0;

        for (DocumentDataElement elem : cluster.getDataElements()) {
            if (elem.getDocument().getTermCount(term) > 0) {
                count++;
            }
        }

        return count;
    }

    private static class TermEntry {
        private String _term;
        private double _score;

        private TermEntry(String term, double weight) {
            _term = term;
            _score = weight;
        }

        private double getScore() {
            return _score;
        }

        private String getTerm() {
            return _term;
        }
    }
}
