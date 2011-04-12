package test.by.bsu.rfe.clustering.app.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSDownloader {

  private File _targetFolder;

  private Set<URL> _sources = new HashSet<URL>();

  private Set<URL> _processed = new HashSet<URL>();

  private Set<URL> _downloadedNews = new HashSet<URL>();

  public RSSDownloader(File saveTo) {
    _targetFolder = saveTo;
  }

  public RSSDownloader addSource(URL url) {
    _sources.add(url);

    return this;
  }

  public void download() throws IOException {
    SyndFeedInput input = new SyndFeedInput();

    if (!_targetFolder.exists()) {
      _targetFolder.mkdirs();
    }

    for (URL url : _sources) {
      if (!_processed.contains(url)) {
        SyndFeed feed = null;

        try {
          feed = input.build(new XmlReader(url));

          for (Object entryObj : feed.getEntries()) {
            SyndEntry entry = (SyndEntry) entryObj;
            URL newsURL = new URL(entry.getLink());

            if (!_downloadedNews.contains(newsURL)) {
              Parser parser = new Parser(newsURL.toString());
              CssSelectorNodeFilter filter = new CssSelectorNodeFilter(".story-body");
              NodeList nodes = parser.parse(filter);

              File file = new File(_targetFolder, entry.getTitle() + ".txt");
              PrintWriter out = null;

              if (nodes.size() > 0) {
                try {
                  out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));

                  SimpleNodeIterator it = nodes.elements();
                  while (it.hasMoreNodes()) {
                    Node node = it.nextNode();

                    NodeList children = node.getChildren();
                    SimpleNodeIterator childItr = children.elements();

                    while (childItr.hasMoreNodes()) {
                      Node child = childItr.nextNode();

                      if (child instanceof ParagraphTag) {
                        String text = child.toPlainTextString().replaceAll("&#039;", "\'").replaceAll("&quot;", "\"");
                        out.printf("%s%n%n", text);
                      }
                    }
                  }

                  _downloadedNews.add(newsURL);
                  System.out.println("Downloaded: " + newsURL);
                }
                catch (IOException e) {
                  System.err.println(e.getMessage());
                }
                finally {
                  if (out != null) {
                    out.close();
                  }
                }
              }
            }
          }

          _processed.add(url);
        }
        catch (Exception e) {
          throw new IOException(e);
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
    RSSDownloader downloader = new RSSDownloader(new File("samples")).addSource(
        new URL("http://feeds.bbci.co.uk/news/rss.xml")).addSource(
        new URL("http://feeds.bbci.co.uk/news/world/rss.xml")).addSource(
        new URL("http://feeds.bbci.co.uk/news/uk/rss.xml")).addSource(
        new URL("http://feeds.bbci.co.uk/news/business/rss.xml")).addSource(
        new URL("http://feeds.bbci.co.uk/news/politics/rss.xml")).addSource(
        new URL("http://feeds.bbci.co.uk/news/health/rss.xml")).addSource(
        new URL("http://feeds.bbci.co.uk/news/technology/rss.xml"));

    downloader.download();
  }

}
