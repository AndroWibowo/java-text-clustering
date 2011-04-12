package by.bsu.rfe.clustering.nlp;

public class PorterStemmer implements Stemmer {

  private PorterStemmerImpl _porterStemmer;

  public PorterStemmer() {
    _porterStemmer = new PorterStemmerImpl();
  }

  @Override
  public String stem(String token) {
    _porterStemmer.add(token.toCharArray(), token.length());
    _porterStemmer.stem();
    return _porterStemmer.toString();
  }
}
