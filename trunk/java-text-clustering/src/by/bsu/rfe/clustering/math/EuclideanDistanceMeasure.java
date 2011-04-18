package by.bsu.rfe.clustering.math;

import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;

import com.google.common.base.Preconditions;

public class EuclideanDistanceMeasure implements DistanseMeasure {

  @Override
  public double compute(Vector vector1, Vector vector2) {
    Preconditions.checkArgument(vector1.size() == vector2.size(), "Vectors must have the same size");

    double result = 0;

    for (VectorEntry entry : vector1) {
      double diff = entry.get() - vector2.get(entry.index());
      result += (diff * diff);
    }

    return Math.sqrt(result);
  }

}
