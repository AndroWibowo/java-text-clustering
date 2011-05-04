package by.bsu.rfe.clustering.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import by.bsu.rfe.clustering.algorithm.cluster.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class KMeansClustering<E extends DataElement, D extends DataSet<E>> implements FlatClustering<E, Cluster<E>, D> {

    private Integer _numberOfClusters = null;

    private DistanseMeasure _vectorDistanse = new EuclideanDistanceMeasure();

    public KMeansClustering(Integer numberOfClusters) {
        setNumberOfClusters(numberOfClusters);
    }

    @Override
    public List<Cluster<E>> cluster(D dataSet) {
        Preconditions.checkNotNull(dataSet, "DataSet is null");

        if (dataSet.isEmpty()) {
            return new LinkedList<Cluster<E>>();
        }

        final int numberOfClusters = (_numberOfClusters != null) ? _numberOfClusters : computeNumberOfClusters(dataSet);
        final List<CentroidCluster<E>> clusters = createInitialClusters(dataSet, numberOfClusters);

        List<Cluster<E>> clusterList = runKMeans(dataSet, clusters);

        postProcess(clusterList, dataSet);

        return clusterList;
    }

    public void setNumberOfClusters(Integer numberOfClusters) {
        if (numberOfClusters != null) {
            Preconditions.checkArgument(numberOfClusters > 0, "Non-positive number of clusters");
        }

        _numberOfClusters = numberOfClusters;
    }

    // TODO use arrays and indexes instead of maps
    private List<Cluster<E>> runKMeans(D dataSet, List<CentroidCluster<E>> clusters) {
        final Map<E, CentroidCluster<E>> elementClusterMap = Maps.newIdentityHashMap();
        final Map<CentroidCluster<E>, DoubleVector> meanVectors = Maps.newIdentityHashMap();
        final Multimap<CentroidCluster<E>, E> tempElementStorage = HashMultimap.create();

        for (CentroidCluster<E> cluster : clusters) {
            for (E elem : cluster.getDataElements()) {
                elementClusterMap.put(elem, cluster);
            }
        }

        boolean proceed = true;
        while (proceed) {
            long movedElements = 0;

            for (CentroidCluster<E> cluster : clusters) {
                meanVectors.put(cluster, cluster.computeCentroid());
            }

            for (E elem : dataSet.elements()) {
                double minDistance = Double.MAX_VALUE;
                CentroidCluster<E> assignTo = null;

                for (CentroidCluster<E> cluster : clusters) {
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
                    movedElements++;
                    elementClusterMap.put(elem, assignTo);
                }
            }

            proceed = (movedElements > 0);

            for (CentroidCluster<E> cluster : clusters) {
                cluster.getDataElements().clear();
                cluster.getDataElements().addAll(tempElementStorage.get(cluster));
            }

            tempElementStorage.clear();
        }

        final List<Cluster<E>> clusterList = new ArrayList<Cluster<E>>(clusters);

        return clusterList;
    }

    private int computeNumberOfClusters(D dataSet) {
        // TODO Auto-generated method stub
        return 4;
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

    // distribute data vectors among clusters
    protected List<CentroidCluster<E>> createInitialClusters(D dataSet, int numberOfClusters) {
        List<CentroidCluster<E>> clusters = createClusterList(numberOfClusters);

        Random random = new Random();

        // select some elements as initial cluster centers

        Set<Integer> indexSet = Sets.newTreeSet();
        for (int index = 0; index < numberOfClusters; index++) {

            int elemIndex;
            do {
                elemIndex = random.nextInt(dataSet.size());
            }
            while (indexSet.contains(elemIndex));

            indexSet.add(elemIndex);
            clusters.get(index).getDataElements().add(dataSet.get(elemIndex));
        }

        return clusters;
    }

    protected void postProcess(List<Cluster<E>> clusterList, D initialDataSet) {
        int clusterOrdinal = 0;
        for (Cluster<E> cluster : clusterList) {
            cluster.setLabel("cluster_" + clusterOrdinal);
            clusterOrdinal++;
        }
    }

}
