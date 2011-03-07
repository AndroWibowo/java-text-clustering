package by.bsu.rfe.clustering.text.database;

import java.io.File;
import java.net.URL;
import java.util.TreeSet;
import java.util.regex.Matcher;

import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.document.Document;
import by.bsu.rfe.clustering.text.document.DocumentCollection;

import com.google.common.base.Preconditions;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSDocumentCollectionReader extends AbstractDocumentCollectionReader {

	// TODO remove the method
	public static void main(String[] args) throws Exception {
		URL feedSource = new URL("http://feeds.bbci.co.uk/news/world/europe/rss.xml");

		WordList stopWords = WordList.load(new File("dictionary\\stopwords.txt"));
		DocumentCollectionReader reader = new RSSDocumentCollectionReader(feedSource, stopWords);
		DocumentCollection documents = reader.readDocuments();

		for (String term : new TreeSet<String>(documents.getAllTerms())) {
			int count = 0;

			for (Document document : documents) {
				count += document.getTermCount(term);
			}
			System.out.printf("Term %s count is %d%n", term, count);
		}
	}

	private URL _feedSource;

	public RSSDocumentCollectionReader(URL feedSource) {
		this(feedSource, null);
	}

	public RSSDocumentCollectionReader(URL feedSource, WordList stopWords) {
		super(stopWords);
		setFeedSource(feedSource);
	}

	@Override
	public DocumentCollection readDocuments() throws DocumentReadException {
		DocumentCollection result = new DocumentCollection();

		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;

		try {
			feed = input.build(new XmlReader(_feedSource));

			for (Object entryObj : feed.getEntries()) {
				SyndEntry entry = (SyndEntry) entryObj;
				Document document = createDocument(entry);
				result.addDocument(document);
			}
		}
		catch (Exception e) {
			throw new DocumentReadException(e);
		}

		return result;
	}

	public void setFeedSource(URL feedSource) {
		Preconditions.checkNotNull(feedSource, "URL is null");
		_feedSource = feedSource;
	}

	private Document createDocument(SyndEntry entry) {
		Document document = new Document();
		WordList stopWords = getStopWords();

		String entryText = entry.getDescription().getValue();
		Matcher wordMatcher = WordList.WORD_PATTERN.matcher(entryText);

		while (wordMatcher.find()) {
			String word = wordMatcher.group();

			if ((stopWords == null) || !stopWords.contains(word)) {
				document.addTerm(word);
			}
		}

		return document;
	}

}
