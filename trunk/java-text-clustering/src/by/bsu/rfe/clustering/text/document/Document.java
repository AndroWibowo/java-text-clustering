package by.bsu.rfe.clustering.text.document;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;

public class Document {

    private Map<String, Integer> _terms;
    
    private String _originalText;

    public Document() {
        _terms = new HashMap<String, Integer>();
    }

    public void addTerm(String term) {
        Preconditions.checkNotNull(term, "Term is null");

        final String normalizedTerm = term.toLowerCase().trim();

        Integer count = _terms.get(normalizedTerm);
        Integer incCount = (count == null) ? 1 : count + 1;

        _terms.put(normalizedTerm, incCount);
    }

    public Set<String> getAllTerms() {
        return Collections.unmodifiableSet(_terms.keySet());
    }

    public int getTermCount(String term) {
        Integer count = _terms.get(term);
        return (count == null) ? 0 : count;
    }

    public String getOriginalText() {
        return _originalText;
    }

    public void setOriginalText(String originalText) {
        _originalText = originalText;
    }

}
