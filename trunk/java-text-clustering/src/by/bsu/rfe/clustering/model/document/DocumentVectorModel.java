package by.bsu.rfe.clustering.model.document;

import by.bsu.rfe.clustering.math.DoubleSparceVector;

public interface DocumentVectorModel {

	public DoubleSparceVector getVector();

	public String getDocumentId();

}
