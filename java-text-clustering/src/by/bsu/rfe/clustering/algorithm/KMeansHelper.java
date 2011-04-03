package by.bsu.rfe.clustering.algorithm;

import static com.google.common.collect.Constraints.constrainedList;
import static com.google.common.collect.Constraints.notNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import by.bsu.rfe.clustering.algorithm.cluster.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

@Deprecated
// This class exists only for test purposes
public class KMeansHelper<E extends DataElement, D extends DataSet<E>> {

    private D _dataSet;
    private int _numberOfClusters;
    private boolean _hasNextStep;
    private List<CentroidCluster<E>> _clusterList;
    private DistanseMeasure _vectorDistanse = new EuclideanDistanceMeasure();

    final Map<E, CentroidCluster<E>> elementClusterMap = Maps.newIdentityHashMap();
    final Map<CentroidCluster<E>, DoubleVector> meanVectors = Maps.newIdentityHashMap();
    final Multimap<CentroidCluster<E>, E> tempElementStorage = HashMultimap.create();

    private List<StepCompleteListener<E>> _stepCompleteListeners = constrainedList(
            new LinkedList<StepCompleteListener<E>>(), notNull());

    public static <E extends DataElement, D extends DataSet<E>> KMeansHelper<E, D> initialize(D dataSet,
            int numberOfClusters) {
        KMeansHelper<E, D> helper = new KMeansHelper<E, D>();

        helper._dataSet = dataSet;
        helper._numberOfClusters = numberOfClusters;
        helper._clusterList = helper.createInitialClusters(dataSet, numberOfClusters);
        helper._hasNextStep = true;

        return helper;
    }

    public boolean hasNextStep() {
        return _hasNextStep;
    }

    public void runNextStep() {

        long movedDocuments = 0;

        for (CentroidCluster<E> cluster : _clusterList) {
            meanVectors.put(cluster, cluster.computeCentroid());
        }

        for (E elem : _dataSet.elements()) {
            double minDistance = Double.MAX_VALUE;
            CentroidCluster<E> assignTo = null;

            for (CentroidCluster<E> cluster : _clusterList) {
                DoubleVector meanVector = meanVectors.get(cluster);
                DoubleVector elemVector = elem.asVector();

                double diffDistance = _vectorDistanse.compute(meanVector, elemVector);

                if (diffDistance < minDistance) {
                    minDistance = diffDistance;
                    assignTo = cluster;
                }
            }

            tempElementStorage.put(assignTo, elem);

            // check if the document was moved to another cluster
            if (elementClusterMap.get(elem) != assignTo) {
                movedDocuments++;
                elementClusterMap.put(elem, assignTo);
            }
        }

        _hasNextStep = (movedDocuments > 0);

        for (CentroidCluster<E> cluster : _clusterList) {
            cluster.getDataElements().clear();
            cluster.getDataElements().addAll(tempElementStorage.get(cluster));
        }

        tempElementStorage.clear();

        fireEvent();
    }

    public void addStepCompleteListener(StepCompleteListener<E> listener) {
        _stepCompleteListeners.add(listener);
    }

    public void removeStepCompleteListener(StepCompleteListener<E> listener) {
        _stepCompleteListeners.remove(listener);
    }

    public static interface StepCompleteListener<E extends DataElement> {
        public void onStepComplete(StepCompleteEvent<E> event);
    }

    public static class StepCompleteEvent<E extends DataElement> {

        private List<CentroidCluster<E>> _currentDistribution;

        private StepCompleteEvent(List<CentroidCluster<E>> distribution) {
            _currentDistribution = distribution;
        }

        public List<CentroidCluster<E>> getCurrentDistribution() {
            return _currentDistribution;
        }
    }

    private void dispatchEvent(StepCompleteEvent<E> event) {
        for (StepCompleteListener<E> listener : _stepCompleteListeners) {
            listener.onStepComplete(event);
        }
    }

    protected List<CentroidCluster<E>> createInitialClusters(D dataSet, int numberOfClusters) {
        List<CentroidCluster<E>> clusters = createClusterList(numberOfClusters);
        final int totalElems = dataSet.elements().size();

        Random random = new Random();
        /*
         * Set<Integer> i = Sets.newTreeSet(); for (int index = 0; index <
         * numberOfClusters; index++) {
         * 
         * int elemIndex; do { elemIndex = random.nextInt(dataSet.size()); }
         * while (i.contains(elemIndex));
         * 
         * i.add(elemIndex);
         * clusters.get(index).getDataElements().add(dataSet.get(elemIndex)); }
         * 
         * System.out.println(i.size()); System.out.println(i);
         */

        for (int elemIndex = 0; elemIndex < totalElems; elemIndex++) {
            int binIndex = random.nextInt(clusters.size());

            Cluster<E> bin = clusters.get(binIndex);
            bin.getDataElements().add(dataSet.elements().get(elemIndex));
        }

        return clusters;
    }

    private List<CentroidCluster<E>> createClusterList(int numberOfclusters) {
        List<CentroidCluster<E>> clusterList = Lists.newArrayListWithCapacity(numberOfclusters);
        int nextOrdinal = 0;

        while (nextOrdinal < numberOfclusters) {
            clusterList.add(new CentroidCluster<E>());
            nextOrdinal++;
        }

        return clusterList;
    }

    public void fireEvent() {
        StepCompleteEvent<E> event = new StepCompleteEvent<E>(_clusterList);
        dispatchEvent(event);
    }
}
