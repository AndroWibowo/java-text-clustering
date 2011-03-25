package by.bsu.rfe.clustering.text.algorithm;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import by.bsu.rfe.clustering.algorithm.ClusteringAlgorithm;
import by.bsu.rfe.clustering.algorithm.KMeansAlgorithm;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.VectorDistanse;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;

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

            String label = null;
            int count = 0;
            for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                if (entry.getValue() > count) {
                    count = entry.getValue();
                    label = entry.getKey();
                }
            }

            cluster.setLabel(label);
            System.out.printf("\t%s %d times%n", label, count);
        }
    }
}
