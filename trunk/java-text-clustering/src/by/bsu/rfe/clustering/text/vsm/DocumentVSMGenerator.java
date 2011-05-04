package by.bsu.rfe.clustering.text.vsm;

import by.bsu.rfe.clustering.text.data.DocumentDataSet;
import by.bsu.rfe.clustering.text.ir.DocumentCollection;

public interface DocumentVSMGenerator {

    public DocumentDataSet createVSM(DocumentCollection documentCollection);

}
