package by.bsu.rfe.clustering.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;

import com.google.common.base.Preconditions;

public class KMeansAlgorithm implements ClusteringAlgorithm<Cluster> {

	private static class Bin {
		private List<DataElement> _elements = new ArrayList<DataElement>();

		void addDataElement(DataElement elem) {
			_elements.add(elem);
		}

	}

	@Override
	public List<Cluster> cluster(DataSet dataSet) {
		Preconditions.checkNotNull(dataSet, "DataSet is null");

		final int numberOfBins = computeNumberOfBins(dataSet);
		final List<Bin> bins = new ArrayList<Bin>(numberOfBins);

		prepareBins(dataSet, bins);

		final List<Cluster> clusters = new ArrayList<Cluster>(numberOfBins);

		return clusters;
	}

	private int computeNumberOfBins(DataSet dataSet) {
		// TODO Auto-generated method stub
		return 4;
	}

	// distribute data vectors among bins
	private void prepareBins(DataSet dataSet, List<Bin> bins) {
		final int totalElems = dataSet.getElements().size();

		Random random = new Random();

		for (int elemIndex = 0; elemIndex < totalElems; elemIndex++) {
			int binIndex = random.nextInt(bins.size());

			Bin bin = bins.get(binIndex);
			bin.addDataElement(dataSet.getElements().get(elemIndex));
		}
	}

}
