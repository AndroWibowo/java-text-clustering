package by.bsu.rfe.clustering.text.vsm;

import java.util.Set;

import by.bsu.rfe.clustering.math.DoubleSparceVector;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.text.document.Document;
import by.bsu.rfe.clustering.text.document.DocumentCollection;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;

import com.google.common.base.Preconditions;

public class TFIDF implements DocumentVSMGenerator {

    @Override
    public DocumentDataSet generateVSM(DocumentCollection documentCollection) {
        Preconditions.checkNotNull(documentCollection, "DocumentCollection is null");

        DocumentDataSet resultSet = new DocumentDataSet(documentCollection);

        for (Document document : documentCollection) {
            DoubleVector vector = calculateTFIDF(document, documentCollection);
            DocumentDataElement dataElement = new DocumentDataElement(vector, document);
            resultSet.addElement(dataElement);
        }

        return resultSet;
    }

    private DoubleVector calculateTFIDF(Document document, DocumentCollection collection) {
        Set<String> allTerms = collection.getAllTerms();
        DoubleVector resultVector = new DoubleSparceVector(allTerms.size());

        int index = 0;
        for (String term : allTerms) {
            int termFrequency = document.getTermCount(term);

            if (termFrequency > 0) {
                int documentFrequency = documentFrequency(term, collection);
                double cardinality = collection.size();
                double tfIdf = termFrequency * Math.log(cardinality / (1 + documentFrequency));
                resultVector.set(index, tfIdf);
            }

            index++;
        }

        return resultVector;
    }

    // how many documents contain the specified term
    private int documentFrequency(String term, DocumentCollection collection) {
        int frequency = 0;

        for (Document document : collection) {
            if (document.getTermCount(term) > 0) {
                frequency++;
            }
        }

        return frequency;
    }

}
