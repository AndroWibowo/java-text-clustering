package test.by.bsu.rfe.clustering.app.cli;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.by.bsu.rfe.clustering.app.util.CSVDataSetExporter;
import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.data.DocumentDataSet;
import by.bsu.rfe.clustering.text.ir.DocumentCollection;
import by.bsu.rfe.clustering.text.ir.DocumentCollectionReader;
import by.bsu.rfe.clustering.text.ir.FileSystemDocumentCollectionReader;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.TFIDF;

public class TestClass {

  private static Log log = LogFactory.getLog(TestClass.class);

  public static void main(String[] args) throws Exception {
    File stopWords = new File("dictionary\\stopwords.txt");
    WordList stopWordList = WordList.load(stopWords);

    DocumentCollectionReader reader = new FileSystemDocumentCollectionReader(new File("samples"), stopWordList);

    DocumentCollection docCollection = reader.readDocuments();

    DocumentVSMGenerator vsmGen = new TFIDF();
    DocumentDataSet dataSet = vsmGen.createVSM(docCollection);

    CSVDataSetExporter.export(dataSet, new File("tmp\\dataset.csv"));

    log.info("CSV export finished");
  }

}
