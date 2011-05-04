package by.bsu.rfe.clustering.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import by.bsu.rfe.clustering.algorithm.cluster.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;
import by.bsu.rfe.clustering.math.WeightedValue;

import com.google.common.collect.Lists;

public class KMeansPlusPlusClustering<E extends DataElement, D extends DataSet<E>> extends KMeansClustering<E, D> {

    public KMeansPlusPlusClustering(Integer numberOfClusters) {
        super(numberOfClusters);
    }

    // TODO split into multiples methods
    @Override
    protected List<CentroidCluster<E>> createInitialClusters(D dataSet, final int numberOfClusters) {
        final List<CentroidCluster<E>> clusterList = Lists.newArrayListWithCapacity(numberOfClusters);
        final List<E> chosenCenters = Lists.newArrayListWithCapacity(numberOfClusters);
        Random random = new Random();

        List<E> probableClusterCenters = Lists.newArrayList(dataSet.elements());

        // there MUST be at least one cluster in the list
        int firstCenterIndex = random.nextInt(dataSet.size());

        // first step of KMeans++
        E firstCenter = dataSet.get(firstCenterIndex);
        chosenCenters.add(firstCenter);
        probableClusterCenters.remove(firstCenter);

        DistanseMeasure distanceMeasure = new EuclideanDistanceMeasure();

        // we'll reuse this array
        double[] clusterProbabilities = new double[probableClusterCenters.size()];

        // choose next centers
        for (int remainingClusters = (numberOfClusters - 1); remainingClusters > 0; remainingClusters--) {
            double totalSquaredDistance = 0;

            for (int index = 0; index < probableClusterCenters.size(); index++) {
                E probableCenter = probableClusterCenters.get(index);
                double distanceToNearest = distanceToNearest(probableCenter, chosenCenters, distanceMeasure);

                clusterProbabilities[index] = (distanceToNearest * distanceToNearest);
                totalSquaredDistance += clusterProbabilities[index];
            }

            @SuppressWarnings("unchecked")
            WeightedValue<E>[] probableCenters = (WeightedValue<E>[]) new WeightedValue[probableClusterCenters.size()];
            // final probability values
            for (int index = 0; index < probableClusterCenters.size(); index++) {
                clusterProbabilities[index] /= totalSquaredDistance;
                E probableCenter = probableClusterCenters.get(index);

                probableCenters[index] = WeightedValue.of(probableCenter, clusterProbabilities[index]);
            }

            Arrays.sort(probableCenters);
            E nextCenter = chooseNextCenter(probableCenters, random).value();

            chosenCenters.add(nextCenter);
            probableClusterCenters.remove(nextCenter);
        }

        for (E center : chosenCenters) {
            CentroidCluster<E> cluster = new CentroidCluster<E>();
            cluster.getDataElements().add(center);
            clusterList.add(cluster);
        }

        return clusterList;
    }

    private double distanceToNearest(E probableCenter, List<E> chosenCenters, DistanseMeasure distanceMeasure) {
        double minDistance = Double.MAX_VALUE;

        for (E center : chosenCenters) {
            double distance = distanceMeasure.compute(probableCenter.asVector(), center.asVector());
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    private WeightedValue<E> chooseNextCenter(WeightedValue<E>[] probableCentersSorted, Random random) {
        double sum = 0;
        for (int index = 0; index < probableCentersSorted.length; index++) {
            sum += probableCentersSorted[index].weight();
        }

        double rnd = random.nextDouble();
        double randomValue = rnd * sum;

        double lastSum = 0;

        for (int index = 0; index < probableCentersSorted.length; index++) {
            double weight = probableCentersSorted[index].weight();

            if ((randomValue > lastSum) && (randomValue <= lastSum + weight)) {
                return probableCentersSorted[index];
            }
            lastSum += weight;
        }

        return probableCentersSorted[probableCentersSorted.length - 1];
    }

}
