package by.bsu.rfe.clustering.text.ir;

import by.bsu.rfe.clustering.nlp.Stemmer;
import by.bsu.rfe.clustering.nlp.WordList;

public abstract class AbstractDocumentCollectionReader implements DocumentCollectionReader {

    private WordList _stopWords;
    private Stemmer _stemmer;

    public AbstractDocumentCollectionReader(WordList stopWords) {
        this(stopWords, null);
    }

    public AbstractDocumentCollectionReader(WordList stopWords, Stemmer stemmer) {
        _stopWords = stopWords;
        _stemmer = stemmer;
    }

    public WordList getStopWords() {
        return _stopWords;
    }

    public DocumentCollectionReader useStemmer(Stemmer stemmer) {
        _stemmer = stemmer;
        return this;
    }

    public Stemmer getStemmer() {
        return _stemmer;
    }

}
