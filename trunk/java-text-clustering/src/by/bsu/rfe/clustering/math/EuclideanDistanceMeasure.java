package by.bsu.rfe.clustering.math;

import com.google.common.base.Preconditions;

public class EuclideanDistanceMeasure implements DistanseMeasure {

    @Override
    public double compute(DoubleVector vector1, DoubleVector vector2) {
        Preconditions.checkArgument(vector1.size() == vector2.size(), "Vectors must have the same size");

        double result = 0;

        for (int index : vector1.indices()) {
            double diff = vector1.get(index) - vector2.get(index);
            result += (diff * diff);
        }

        return Math.sqrt(result);
    }

}
