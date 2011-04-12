package by.bsu.rfe.clustering.text.ir;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import by.bsu.rfe.clustering.nlp.WordList;

import com.google.common.base.Preconditions;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSDocumentCollectionReader extends AbstractDocumentCollectionReader {

  private List<URL> _feedSourceList = new LinkedList<URL>();

  public RSSDocumentCollectionReader() {
    this(null);
  }

  public RSSDocumentCollectionReader(WordList stopWords) {
    super(stopWords);
  }

  @Override
  public DocumentCollection readDocuments() throws InformationRetrievalException {
    DocumentCollection result = new DocumentCollection();

    SyndFeedInput input = new SyndFeedInput();
    SyndFeed feed = null;

    int documentIdSuffix = 0;
    for (URL feedSource : _feedSourceList) {
      try {
        feed = input.build(new XmlReader(feedSource));

        for (Object entryObj : feed.getEntries()) {
          SyndEntry entry = (SyndEntry) entryObj;
          Document document = createDocument(entry, "doc_" + documentIdSuffix);
          result.addDocument(document);

          documentIdSuffix++;
        }
      }
      catch (Exception e) {
        throw new InformationRetrievalException(e);
      }
    }

    return result;
  }

  public RSSDocumentCollectionReader addSource(URL feedSource) {
    _feedSourceList.add(Preconditions.checkNotNull(feedSource, "URL is null"));

    return this;
  }

  private Document createDocument(SyndEntry entry, String documentId) {
    Document document = new Document(documentId);
    WordList stopWords = getStopWords();

    String entryText = entry.getDescription().getValue();
    Matcher wordMatcher = WordList.WORD_PATTERN.matcher(entryText);

    while (wordMatcher.find()) {
      String word = wordMatcher.group();

      if ((stopWords == null) || !stopWords.contains(word)) {
        document.addTerm(word);
      }
    }

    document.setOriginalText(entryText);
    document.setTitle(entry.getTitle());

    return document;
  }

}
