package test.by.bsu.rfe.clustering.app.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import by.bsu.rfe.clustering.text.document.DocumentDataElement;
import by.bsu.rfe.clustering.text.document.DocumentDataSet;

public class CSVDataSetExporter {

  public static void export(DocumentDataSet ds, File saveTo) throws IOException {
    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(saveTo)));

      Set<String> allTerms = new TreeSet<String>(ds.getAllTerms());
      int termIndex = 0;

      out.print("\"\"");

      // export terms
      for (String term : allTerms) {
        out.print(',');
        out.printf("\"%s\"", term.replace("\"", "\"\""));
        termIndex++;
      }
      out.print("\r\n");

      // export features
      for (DocumentDataElement elem : ds.elements()) {
        String title = elem.getDocument().getTitle();

        out.printf("\"%s\"", (title == null) ? "" : title.replace("\"", "\"\""));
        int row = 0;

        for (String term : allTerms) {
          out.print(',');

          double weight = ds.getTermWeight(elem.getDocument().getId(), term);
          int count = elem.getDocument().getTermCount(term);

          out.printf("\"%5.2f:%d\"", weight, count);
          row++;
        }
        out.print("\r\n");
      }
    }
    finally {
      if (out != null) {
        out.close();
      }
    }
  }

}
