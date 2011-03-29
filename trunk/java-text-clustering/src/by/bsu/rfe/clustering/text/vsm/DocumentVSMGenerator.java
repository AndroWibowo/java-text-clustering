package by.bsu.rfe.clustering.text.vsm;

import by.bsu.rfe.clustering.text.document.DocumentCollection;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;

public interface DocumentVSMGenerator {

    public DocumentDataSet generateVSM(DocumentCollection documentCollection);

}
