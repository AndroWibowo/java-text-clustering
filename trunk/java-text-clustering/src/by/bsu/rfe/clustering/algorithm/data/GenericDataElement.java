package by.bsu.rfe.clustering.algorithm.data;

import static com.google.common.base.Preconditions.checkNotNull;
import no.uib.cipr.matrix.Vector;

public class GenericDataElement implements DataElement {

  private Vector _vector;

  public GenericDataElement(Vector vector) {
    _vector = checkNotNull(vector, "Vector is null");
  }

  @Override
  public Vector asVector() {
    return _vector;
  }

}
