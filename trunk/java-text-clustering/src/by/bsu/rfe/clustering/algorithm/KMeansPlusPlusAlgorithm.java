package by.bsu.rfe.clustering.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;
import by.bsu.rfe.clustering.math.DistanseMeasure;
import by.bsu.rfe.clustering.math.WeightedValue;

import com.google.common.collect.Lists;

public class KMeansPlusPlusAlgorithm<E extends DataElement, D extends DataSet<E>> extends KMeansAlgorithm<E, D> {

    public KMeansPlusPlusAlgorithm(DistanseMeasure vectorDistanse) {
        super(vectorDistanse);
    }

    // TODO split into multiples methods
    @Override
    protected List<Bin> selectInitialCenters(D dataSet, final int numberOfClusters) {
        final List<Bin> clusterList = Lists.newArrayListWithCapacity(numberOfClusters);
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
            WeightedValue<E>[] probableCenters = (WeightedValue<E>[]) new WeightedValue[remainingClusters];
            // final probability values
            for (int index = 0; index < remainingClusters; index++) {
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
            Bin bin = new Bin();
            bin.elements().add(center);
            clusterList.add(bin);
        }

        return clusterList;
    }

    private double distanceToNearest(E probableCenter, List<E> chosenCenters, DistanseMeasure distanceMeasure) {
        double minDistance = Double.MIN_VALUE;

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

        double randomValue = random.nextDouble() * sum;

        for (int index = 0; index < probableCentersSorted.length; index++) {
            if (randomValue <= probableCentersSorted[index].weight()) {
                return probableCentersSorted[index];
            }
        }

        return probableCentersSorted[probableCentersSorted.length - 1];
    }

}
