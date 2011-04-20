package by.bsu.rfe.clustering.math;

import java.util.Iterator;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * Contains utility methods for working with vectors.
 * 
 * @author Siarhei_Yarashevich
 */
public final class Vectors {

  private Vectors() {
    // utility class needs not to be instantiated
  };

  public static Vector computeMeanVectorAsSparce(Iterable<? extends Vector> vectors) {
    Iterator<? extends Vector> itr = vectors.iterator();

    if (!itr.hasNext()) {
      throw new MathException("No vectors available");
    }

    Vector first = itr.next();
    int total = 1;

    Vector mean = new SparseVector(first);

    return finishMeanComputation(itr, total, mean);
  }

  public static Vector computerMeanVectorAsDense(Iterable<? extends Vector> vectors) {
    Iterator<? extends Vector> itr = vectors.iterator();

    if (!itr.hasNext()) {
      throw new MathException("No vectors available");
    }

    Vector first = itr.next();
    int total = 1;

    Vector mean = new DenseVector(first);

    return finishMeanComputation(itr, total, mean);
  }

  private static Vector finishMeanComputation(Iterator<? extends Vector> rest, int total, Vector mean) {
    while (rest.hasNext()) {
      Vector next = rest.next();
      total++;

      // hope there won't be an overflow
      mean.add(next);
    }
    mean.scale(1D / total);

    return mean;
  }
}
