package by.bsu.rfe.clustering.model.document;

import java.util.Map;

public interface Document {

	public String getId();

	public Map<String, Integer> getFeatures();

	public void setFeatures(Map<String, Integer> features);

}
