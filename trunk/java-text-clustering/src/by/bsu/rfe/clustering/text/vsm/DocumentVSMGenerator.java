package by.bsu.rfe.clustering.text.vsm;

import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.text.document.DocumentCollection;

public interface DocumentVSMGenerator<E extends DataElement> {

    public DataSet<E> generateVSM(DocumentCollection documentCollection);

}
