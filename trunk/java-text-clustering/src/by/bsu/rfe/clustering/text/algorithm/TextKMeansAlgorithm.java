package by.bsu.rfe.clustering.text.algorithm;

import java.util.Comparator;
import java.util.List;

import by.bsu.rfe.clustering.algorithm.ClusteringAlgorithm;
import by.bsu.rfe.clustering.algorithm.KMeansAlgorithm;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.math.VectorDistanse;
import by.bsu.rfe.clustering.text.document.Document;
import by.bsu.rfe.clustering.text.document.DocumentCollection;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;

import com.google.common.collect.MinMaxPriorityQueue;

public class TextKMeansAlgorithm implements
        ClusteringAlgorithm<DocumentDataElement, Cluster<DocumentDataElement>, DocumentDataSet> {

    private KMeansAlgorithm<DocumentDataElement, DocumentDataSet> _kMeans;

    public TextKMeansAlgorithm(VectorDistanse vectorDistanse) {
        this(vectorDistanse, null);
    }

    public TextKMeansAlgorithm(VectorDistanse vectorDistanse, Integer numberOfClusters) {
        _kMeans = new KMeansAlgorithm<DocumentDataElement, DocumentDataSet>(vectorDistanse);

        setNumberOfClusters(numberOfClusters);
        setVectorDistanse(vectorDistanse);
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

    public void setVectorDistanse(VectorDistanse vectorDistanse) {
        _kMeans.setVectorDistanse(vectorDistanse);
    }

    private void assignLabels(List<Cluster<DocumentDataElement>> clusterData, DocumentDataSet dataSet) {
        for (Cluster<DocumentDataElement> cluster : clusterData) {
            MinMaxPriorityQueue<TermEntry> queue = MinMaxPriorityQueue.orderedBy(new Comparator<TermEntry>() {

                @Override
                public int compare(TermEntry o1, TermEntry o2) {
                    return -Double.compare(o1.getWeight(), o2.getWeight());
                }
            }).maximumSize(3).create();

            for (DocumentDataElement elem : cluster.getDataElements()) {
                Document document = elem.getDocument();

                for (String term : document.getAllTerms()) {
                    
                    if (getDocumentCount(term, cluster) > 1) {
                        double termWeight = dataSet.getTermWeight(document.getId(), term);
                        queue.offer(new TermEntry(term, termWeight));
                    }
                }
            }

            String label = "";
            StringBuilder labelBuilder = new StringBuilder();
            for (TermEntry termEntry : queue) {
                labelBuilder
                    .append(termEntry.getTerm())
                    .append(":")
                    .append(String.format("%5.2f",termEntry.getWeight()))
                    .append(";")
                    .append(getDocumentCount(termEntry.getTerm(), cluster))
                    .append(",");
            }

            if (labelBuilder.length() > 0) {
                label = labelBuilder.substring(0, labelBuilder.length() - 1);
            }
            cluster.setLabel(label);
        }
    }
    
    private int getDocumentCount(String term, Cluster<DocumentDataElement> cluster) {
        int count = 0;
        
        for(DocumentDataElement elem: cluster.getDataElements()) {
            if(elem.getDocument().getTermCount(term) > 0) {
                count++;
            }
        }
        
        return count;
    }

    private static class TermEntry {
        private String _term;
        private double _weight;

        private TermEntry(String term, double weight) {
            _term = term;
            _weight = weight;
        }

        private double getWeight() {
            return _weight;
        }

        private String getTerm() {
            return _term;
        }
    }
}
