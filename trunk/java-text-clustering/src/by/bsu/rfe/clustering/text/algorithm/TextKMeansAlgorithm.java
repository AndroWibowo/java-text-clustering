package by.bsu.rfe.clustering.text.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import by.bsu.rfe.clustering.algorithm.ClusteringAlgorithm;
import by.bsu.rfe.clustering.algorithm.KMeansAlgorithm;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.VectorDistanse;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;

import com.google.common.collect.Maps;
import com.google.common.collect.MinMaxPriorityQueue;

public class TextKMeansAlgorithm implements ClusteringAlgorithm<DocumentDataElement, Cluster<DocumentDataElement>> {

    private KMeansAlgorithm<DocumentDataElement> _kMeans;

    public TextKMeansAlgorithm(VectorDistanse vectorDistanse) {
        this(vectorDistanse, null);
    }

    public TextKMeansAlgorithm(VectorDistanse vectorDistanse, Integer numberOfClusters) {
        _kMeans = new KMeansAlgorithm<DocumentDataElement>(vectorDistanse);

        setNumberOfClusters(numberOfClusters);
        setVectorDistanse(vectorDistanse);
    }

    @Override
    public List<Cluster<DocumentDataElement>> cluster(DataSet<DocumentDataElement> dataSet) {
        List<Cluster<DocumentDataElement>> cluserList = _kMeans.cluster(dataSet);
        assignLabels(cluserList);

        return cluserList;
    }

    public void setNumberOfClusters(Integer numberOfClusters) {
        _kMeans.setNumberOfClusters(numberOfClusters);
    }

    public void setVectorDistanse(VectorDistanse vectorDistanse) {
        _kMeans.setVectorDistanse(vectorDistanse);
    }

    private void assignLabels(List<Cluster<DocumentDataElement>> clusterData) {
        for (Cluster<DocumentDataElement> cluster : clusterData) {

            Map<String, Integer> wordCount = Maps.newHashMap();
            for (DocumentDataElement elem : cluster.getDataElements()) {
                for (String term : elem.getDocument().getAllTerms()) {
                    int count = wordCount.containsKey(term) ? wordCount.get(term) : 0;
                    wordCount.put(term, count + elem.getDocument().getTermCount(term));
                }
            }

            MinMaxPriorityQueue<TermEntry> queue = MinMaxPriorityQueue.orderedBy(new Comparator<TermEntry>() {

                @Override
                public int compare(TermEntry o1, TermEntry o2) {
                    return o2.getCount() - o1.getCount();
                }
            }).maximumSize(7).create();

            for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                queue.offer(new TermEntry(entry.getKey(), entry.getValue()));
            }

            StringBuilder labelBuilder = new StringBuilder();
            for (TermEntry termEntry : queue) {
                labelBuilder.append(termEntry.getTerm()).append(":").append(termEntry.getCount()).append(",");
            }

            String label = labelBuilder.substring(0, labelBuilder.length() - 1);
            cluster.setLabel(label);
            // System.out.printf("\t%s %d times%n", label, count);
        }
    }

    private static class TermEntry {
        private String _term;
        private int _count;

        private TermEntry(String term, int count) {
            _term = term;
            _count = count;
        }

        private int getCount() {
            return _count;
        }

        private String getTerm() {
            return _term;
        }
    }
}
