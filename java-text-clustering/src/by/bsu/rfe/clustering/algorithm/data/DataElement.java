package by.bsu.rfe.clustering.algorithm.data;

import no.uib.cipr.matrix.Vector;

public interface DataElement {

  /**
   * Returns vector representation of this element
   * 
   * @return an implementation of {@link Vector} - vector representation of this
   *         element
   */
  public Vector asVector();

}
