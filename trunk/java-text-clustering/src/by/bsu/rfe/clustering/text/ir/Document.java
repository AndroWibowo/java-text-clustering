package by.bsu.rfe.clustering.text.ir;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class Document {

    private String _id;

    private Map<String, Integer> _terms;

    private String _title;

    private String _originalText;

    public Document(String id) {
        _id = Preconditions.checkNotNull(id, "Id is null");
        _terms = new HashMap<String, Integer>();
    }

    public void addTerm(String term) {
        Preconditions.checkNotNull(term, "Term is null");

        final String normalizedTerm = term.toLowerCase().trim();

        Integer count = _terms.get(normalizedTerm);
        Integer newTermCount = (count == null) ? 1 : count + 1;

        _terms.put(normalizedTerm, newTermCount);
    }

    public Set<String> getAllTerms() {
        return Collections.unmodifiableSet(_terms.keySet());
    }

    public String getId() {
        return _id;
    }

    public int getTermCount(String term) {
        Integer count = _terms.get(term);
        return (count == null) ? 0 : count;
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
    
    @Override
    public String toString() {
        return Strings.nullToEmpty(getTitle());
    }

}
