package by.bsu.rfe.clustering.text.database;

import by.bsu.rfe.clustering.text.document.DocumentCollection;

public interface DocumentCollectionReader {

	public DocumentCollection readDocuments() throws DocumentReadException;

}
