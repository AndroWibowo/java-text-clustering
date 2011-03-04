package by.bsu.rfe.clustering.model.document;

import java.util.List;
import java.util.Set;

public interface DocumentCollection {

	public List<Document> getDocuments();

	public Set<String> getFeatureSet();

}
