package by.bsu.rfe.clustering.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.DoubleSparceVector;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.math.VectorDistanse;

import com.google.common.base.Preconditions;

public class KMeansAlgorithm implements ClusteringAlgorithm<Cluster> {

    private VectorDistanse _vectorDistanse;

    public KMeansAlgorithm(VectorDistanse vectorDistanse) {
        setVectorDistanse(vectorDistanse);
    }

    @Override
    public List<Cluster> cluster(DataSet dataSet) {
        Preconditions.checkNotNull(dataSet, "DataSet is null");

        if (dataSet.isEmpty()) {
            return new LinkedList<Cluster>();
        }

        final int numberOfBins = computeNumberOfBins(dataSet);
        final List<Centroid> bins = new ArrayList<Centroid>(numberOfBins);

        prepareCentroids(dataSet, bins);

        final List<Cluster> clusters = new ArrayList<Cluster>(numberOfBins);

        return clusters;
    }

    public void setVectorDistanse(VectorDistanse vectorDistanse) {
        _vectorDistanse = Preconditions.checkNotNull(vectorDistanse, "Vector distance is null");
    }

    private int computeNumberOfBins(DataSet dataSet) {
        // TODO Auto-generated method stub
        return 4;
    }

    // distribute data vectors among bins
    private void prepareCentroids(DataSet dataSet, List<Centroid> bins) {
        final int totalElems = dataSet.getElements().size();

        Random random = new Random();

        for (int elemIndex = 0; elemIndex < totalElems; elemIndex++) {
            int binIndex = random.nextInt(bins.size());

            Centroid bin = bins.get(binIndex);
            bin.addDataElement(dataSet.getElements().get(elemIndex));
        }
    }

    private static class Centroid {

        private List<DataElement> _elements = new ArrayList<DataElement>();

        private void addDataElement(DataElement elem) {
            _elements.add(elem);
        }

        private DoubleVector computeMeanVector() {
            final int size = _elements.get(0).getVectorModel().size();

            DoubleSparceVector resultVector = new DoubleSparceVector(size);

            for (int vectorIndex : resultVector.indices()) {
                double nextValue = 0;

                for (int elemIndex = 0; elemIndex < _elements.size(); elemIndex++) {
                    nextValue += _elements.get(elemIndex).getVectorModel().get(vectorIndex);
                }

                nextValue /= _elements.size();
                resultVector.set(vectorIndex, nextValue);
            }

            return resultVector;
        }
    }

}
