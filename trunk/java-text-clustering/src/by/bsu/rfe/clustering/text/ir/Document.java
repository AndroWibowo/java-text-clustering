package by.bsu.rfe.clustering.text.ir;

import java.util.Collections;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Document {

  private String _id;

  private Multiset<String> _terms = HashMultiset.create();

  private String _title;

  private String _originalText;

  public Document(String id) {
    _id = Preconditions.checkNotNull(id, "Id is null");
  }

  public void addTerm(String term) {
    Preconditions.checkNotNull(term, "Term is null");

    final String normalizedTerm = term.toLowerCase().trim();

    _terms.add(normalizedTerm);
  }

  public Set<String> getAllTerms() {
    return Collections.unmodifiableSet(_terms.elementSet());
  }

  public String getId() {
    return _id;
  }

  public int getTermCount(String term) {
    return _terms.count(term);
  }

  public String getTitle() {
    return _title;
  }

  public void setTitle(String title) {
    _title = title;
  }

  public String getOriginalText() {
    return _originalText;
  }

  public void setOriginalText(String originalText) {
    _originalText = originalText;
  }

  public int totalTerms() {
    int total = 0;
    for (String term : getAllTerms()) {
      total += getTermCount(term);
    }

    return total;
  }

}
