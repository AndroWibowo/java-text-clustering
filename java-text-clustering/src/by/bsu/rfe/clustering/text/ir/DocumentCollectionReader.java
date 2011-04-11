package by.bsu.rfe.clustering.text.ir;

import by.bsu.rfe.clustering.text.document.DocumentCollection;

public interface DocumentCollectionReader {

    public DocumentCollection readDocuments() throws InformationRetrievalException;

}
