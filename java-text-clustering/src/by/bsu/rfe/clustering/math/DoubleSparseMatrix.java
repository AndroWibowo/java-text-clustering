package by.bsu.rfe.clustering.math;

import com.google.common.base.Preconditions;

public class DoubleSparseMatrix {

	private final DoubleSparceVector[] _rows;

	public DoubleSparseMatrix(int rows, int cols) {
		Preconditions.checkArgument(rows >= 0, "Negtive row number: " + rows);
		Preconditions.checkArgument(cols >= 0, "Negative column number: " + cols);

		_rows = new DoubleSparceVector[rows];
		for (int rowIndex = 0; rowIndex < _rows.length; rowIndex++) {
			_rows[rowIndex] = new DoubleSparceVector(cols);
		}
	}

}
