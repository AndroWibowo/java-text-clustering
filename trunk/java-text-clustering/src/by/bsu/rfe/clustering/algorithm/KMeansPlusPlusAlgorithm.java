package by.bsu.rfe.clustering.algorithm;

import java.util.List;
import java.util.Random;
import java.util.Set;

import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;
import by.bsu.rfe.clustering.math.VectorDistanseMeasure;
import by.bsu.rfe.clustering.math.WeightedValue;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class KMeansPlusPlusAlgorithm<E extends DataElement, D extends DataSet<E>> extends KMeansAlgorithm<E, D> {

    public KMeansPlusPlusAlgorithm(VectorDistanseMeasure vectorDistanse) {
        super(vectorDistanse);
    }

    @Override
    protected List<Bin> selectInitialCenters(D dataSet, final int numberOfClusters) {
        final List<Bin> clusterList = Lists.newArrayListWithCapacity(numberOfClusters);
        final List<E> chosenCenters = Lists.newArrayListWithCapacity(numberOfClusters);
        Random random = new Random();

        List<E> probableClusterCenters = Lists.newArrayList(dataSet.elements());

        // there MUST be at least one bin in the list
        int firstCenterIndex = random.nextInt(dataSet.size());

        // first step of KMeans++
        E firstCenter = dataSet.get(firstCenterIndex);
        chosenCenters.add(firstCenter);
        probableClusterCenters.remove(firstCenter);

        VectorDistanseMeasure distanceMeasure = new EuclideanDistanceMeasure();

        // we'll reuse this array
        double[] clusterProbabilities = new double[numberOfClusters - 1];

        // choose next centers
        for (int remainingClusters = (numberOfClusters - 1); remainingClusters > 0; remainingClusters--) {
            double totalSquaredDistance = 0;

            for (int index = 0; index < probableClusterCenters.size(); index++) {
                E probableCenter = probableClusterCenters.get(index);
                double distanceToNearest = distanceToNearest(probableCenter, chosenCenters, distanceMeasure);

                clusterProbabilities[index] = (distanceToNearest * distanceToNearest);
                totalSquaredDistance += clusterProbabilities[index];
            }

            Set<WeightedValue<E>> probableCenters = Sets.newTreeSet();

            // final probability values
            for (int index = 0; index < remainingClusters; index++) {
                clusterProbabilities[index] /= totalSquaredDistance;

                probableCenters.add(WeightedValue.of(probableClusterCenters.get(index), clusterProbabilities[index]));
            }

        }

        return clusterList;
    }

    private double distanceToNearest(E probableCenter, List<E> chosenCenters, VectorDistanseMeasure distanceMeasure) {
        double minDistance = Double.MIN_VALUE;

        for (E center : chosenCenters) {
            double distance = distanceMeasure.compute(probableCenter.asVector(), center.asVector());
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    private WeightedValue<E> chooseNextCluster(Set<WeightedValue<E>> probableCenters) {
        // TODO implement me!!!
        return null;
    }

}
