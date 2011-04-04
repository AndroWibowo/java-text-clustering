package test.by.bsu.rfe.clustering.app.cli.testapache;

import java.util.Collection;

import org.apache.commons.math.stat.clustering.Clusterable;

import by.bsu.rfe.clustering.math.DoubleSparceVector;
import by.bsu.rfe.clustering.math.DoubleVector;
import by.bsu.rfe.clustering.math.EuclideanDistanceMeasure;
import by.bsu.rfe.clustering.text.document.Document;
import by.bsu.rfe.clustering.text.document.DocumentDataElement;

public class DocumentClusterable extends DocumentDataElement implements Clusterable<DocumentClusterable> {

    public DocumentClusterable(DoubleVector vector, Document document) {
        super(vector, document);
        // TODO Auto-generated constructor stub
    }

    @Override
    public DocumentClusterable centroidOf(Collection<DocumentClusterable> p) {
        DoubleVector v = new DoubleSparceVector(asVector().size());

        for (int i = 0; i < asVector().size(); i++) {
            int newValue = 0;
            for (DocumentClusterable d : p) {
                newValue += d.asVector().get(i) / asVector().size();
            }
            v.set(i, newValue);
        }

        DocumentClusterable elem = new DocumentClusterable(v, new Document("centroidDoc"));
        return elem;
    }

    @Override
    public double distanceFrom(DocumentClusterable p) {
        return new EuclideanDistanceMeasure().compute(asVector(), p.asVector());
    }

}
