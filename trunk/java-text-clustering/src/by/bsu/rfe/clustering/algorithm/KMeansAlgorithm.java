package by.bsu.rfe.clustering.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.DoubleSparceVector;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.math.VectorDistanse;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class KMeansAlgorithm implements ClusteringAlgorithm<Cluster> {

    private Integer _numberOfClusters = null;

    private VectorDistanse _vectorDistanse;

    public KMeansAlgorithm(VectorDistanse vectorDistanse) {
        this(vectorDistanse, null);
    }

    public KMeansAlgorithm(VectorDistanse vectorDistanse, Integer numberOfClusters) {
        setNumberOfClusters(numberOfClusters);
        setVectorDistanse(vectorDistanse);
    }

    @Override
    public List<Cluster> cluster(DataSet dataSet) {
        Preconditions.checkNotNull(dataSet, "DataSet is null");

        if (dataSet.isEmpty()) {
            return new LinkedList<Cluster>();
        }

        final int numberOfBins = (_numberOfClusters != null) ? _numberOfClusters : computeNumberOfBins(dataSet);
        final List<Bin> bins = createBinList(numberOfBins);

        prepareCentroids(dataSet, bins);

        final Map<DataElement, Bin> elementBinMap = Maps.newIdentityHashMap();
        final Map<Bin, DoubleVector> meanVectors = Maps.newIdentityHashMap();
        final Multimap<Bin, DataElement> tempElementStorage = HashMultimap.create();

        for (Bin bin : bins) {
            for (DataElement elem : bin.elements()) {
                elementBinMap.put(elem, bin);
            }
        }

        boolean proceed = true;
        while (proceed) {
            long movedDocuments = 0;

            for (Bin bin : bins) {
                meanVectors.put(bin, bin.computeMeanVector());
            }

            for (DataElement elem : dataSet) {
                double minDistance = Double.MAX_VALUE;
                Bin assignTo = null;

                for (Bin bin : bins) {
                    DoubleVector meanVector = meanVectors.get(bin);
                    DoubleVector elemVector = elem.getVectorModel();

                    double diffDistance = _vectorDistanse.compute(meanVector, elemVector);

                    if (diffDistance < minDistance) {
                        minDistance = diffDistance;
                        assignTo = bin;
                    }
                }

                tempElementStorage.put(assignTo, elem);

                // check if the document was moved to another bin
                if (elementBinMap.get(elem) != assignTo) {
                    movedDocuments++;
                    elementBinMap.put(elem, assignTo);
                }
            }

            proceed = (movedDocuments > 0);

            for (Bin bin : bins) {
                bin.elements().clear();
                bin.elements().addAll(tempElementStorage.get(bin));
            }

            tempElementStorage.clear();
        }

        final List<Cluster> clusters = new ArrayList<Cluster>(numberOfBins);

        return clusters;
    }

    public void setVectorDistanse(VectorDistanse vectorDistanse) {
        _vectorDistanse = Preconditions.checkNotNull(vectorDistanse, "Vector distance is null");
    }

    public void setNumberOfClusters(Integer numberOfClusters) {
        if (numberOfClusters != null) {
            Preconditions.checkArgument(numberOfClusters > 0, "Non-positive number of clusters");
        }

        _numberOfClusters = numberOfClusters;
    }

    private int computeNumberOfBins(DataSet dataSet) {
        // TODO Auto-generated method stub
        return 4;
    }

    private List<Bin> createBinList(int numberOfBins) {
        List<Bin> centroidList = new ArrayList<Bin>(numberOfBins);
        int nextOrdinal = 0;

        while (nextOrdinal < numberOfBins) {
            centroidList.add(new Bin());
        }

        return centroidList;
    }

    // distribute data vectors among bins
    private void prepareCentroids(DataSet dataSet, List<Bin> bins) {
        final int totalElems = dataSet.getElements().size();

        Random random = new Random();

        for (int elemIndex = 0; elemIndex < totalElems; elemIndex++) {
            int binIndex = random.nextInt(bins.size());

            Bin bin = bins.get(binIndex);
            bin.elements().add(dataSet.getElements().get(elemIndex));
        }
    }

    private static class Bin {

        private final List<DataElement> _elements = new ArrayList<DataElement>();

        private Bin() {
        }

        private List<DataElement> elements() {
            return _elements;
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
