package by.bsu.rfe.clustering.text.ir;

import by.bsu.rfe.clustering.nlp.WordList;

public abstract class AbstractDocumentCollectionReader implements DocumentCollectionReader {

    private WordList _stopWords;

    public AbstractDocumentCollectionReader(WordList stopWords) {
        _stopWords = stopWords;
    }

    public WordList getStopWords() {
        return _stopWords;
    }

}
