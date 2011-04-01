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
import by.bsu.rfe.clustering.math.DistanseMeasure;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class KMeansAlgorithm<E extends DataElement, D extends DataSet<E>> implements
        ClusteringAlgorithm<E, Cluster<E>, D> {

    private Integer _numberOfClusters = null;

    private DistanseMeasure _vectorDistanse;

    private int _rows = 0;

    public KMeansAlgorithm(DistanseMeasure vectorDistanse) {
        this(vectorDistanse, null);
    }

    public KMeansAlgorithm(DistanseMeasure vectorDistanse, Integer numberOfClusters) {
        setNumberOfClusters(numberOfClusters);
        setVectorDistanse(vectorDistanse);
    }

    @Override
    public List<Cluster<E>> cluster(D dataSet) {
        Preconditions.checkNotNull(dataSet, "DataSet is null");

        if (dataSet.isEmpty()) {
            return new LinkedList<Cluster<E>>();
        }

        _rows = dataSet.elements().get(0).asVector().size();

        final int numberOfBins = (_numberOfClusters != null) ? _numberOfClusters : computeNumberOfBins(dataSet);
        final List<Bin> bins = selectInitialCenters(dataSet, numberOfBins);

        List<Cluster<E>> clusterList = runKMeans(dataSet, bins);

        postProcess(clusterList, dataSet);

        return clusterList;
    }

    public void setVectorDistanse(DistanseMeasure vectorDistanse) {
        _vectorDistanse = Preconditions.checkNotNull(vectorDistanse, "Vector distance is null");
    }

    public void setNumberOfClusters(Integer numberOfClusters) {
        if (numberOfClusters != null) {
            Preconditions.checkArgument(numberOfClusters > 0, "Non-positive number of clusters");
        }

        _numberOfClusters = numberOfClusters;
    }

    private List<Cluster<E>> runKMeans(D dataSet, List<Bin> bins) {
        final Map<E, Bin> elementBinMap = Maps.newIdentityHashMap();
        final Map<Bin, DoubleVector> meanVectors = Maps.newIdentityHashMap();
        final Multimap<Bin, E> tempElementStorage = HashMultimap.create();

        for (Bin bin : bins) {
            for (E elem : bin.elements()) {
                elementBinMap.put(elem, bin);
            }
        }

        boolean proceed = true;
        while (proceed) {
            long movedDocuments = 0;

            for (Bin bin : bins) {
                meanVectors.put(bin, bin.computeMeanVector());
            }

            for (E elem : dataSet.elements()) {
                double minDistance = Double.MAX_VALUE;
                Bin assignTo = null;

                for (Bin bin : bins) {
                    DoubleVector meanVector = meanVectors.get(bin);
                    DoubleVector elemVector = elem.asVector();

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

        final List<Cluster<E>> clusterList = new ArrayList<Cluster<E>>(bins.size());

        for (Bin bin : bins) {
            if (!bin.elements().isEmpty()) {
                Cluster<E> cluster = new Cluster<E>();
                cluster.addAll(bin.elements());
                clusterList.add(cluster);
            }
        }

        return clusterList;
    }

    private int computeNumberOfBins(D dataSet) {
        // TODO Auto-generated method stub
        return 4;
    }

    private List<Bin> createBinList(int numberOfBins) {
        List<Bin> centroidList = new ArrayList<Bin>(numberOfBins);
        int nextOrdinal = 0;

        while (nextOrdinal < numberOfBins) {
            centroidList.add(new Bin());
            nextOrdinal++;
        }

        return centroidList;
    }

    // distribute data vectors among bins
    protected List<Bin> selectInitialCenters(D dataSet, int numberOfBins) {
        List<Bin> bins = createBinList(numberOfBins);
        final int totalElems = dataSet.elements().size();

        Random random = new Random();

        for (int elemIndex = 0; elemIndex < totalElems; elemIndex++) {
            int binIndex = random.nextInt(bins.size());

            Bin bin = bins.get(binIndex);
            bin.elements().add(dataSet.elements().get(elemIndex));
        }

        return bins;
    }

    protected void postProcess(List<Cluster<E>> clusterList, D initialDataSet) {
        int clusterOrdinal = 0;
        for (Cluster<E> cluster : clusterList) {
            cluster.setLabel("cluster_" + clusterOrdinal);
            clusterOrdinal++;
        }
    }

    // TODO replace this with Cluster
    protected class Bin {

        private final List<E> _elements = new ArrayList<E>();

        protected Bin() {
        }

        protected List<E> elements() {
            return _elements;
        }

        protected DoubleVector computeMeanVector() {

            if (_elements.isEmpty()) {
                return new DoubleSparceVector(_rows);
            }

            final int size = _elements.get(0).asVector().size();

            DoubleSparceVector resultVector = new DoubleSparceVector(size);

            for (int vectorIndex : resultVector.indices()) {
                double nextValue = 0;

                for (int elemIndex = 0; elemIndex < _elements.size(); elemIndex++) {
                    nextValue += (_elements.get(elemIndex).asVector().get(vectorIndex) / _elements.size());
                }

                resultVector.set(vectorIndex, nextValue);
            }

            return resultVector;
        }
    }

}
