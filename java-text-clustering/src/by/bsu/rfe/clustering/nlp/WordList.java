package by.bsu.rfe.clustering.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

public class WordList {

    public static final Pattern WORD_PATTERN = Pattern.compile("[a-zA-Z]+'?[a-zA-Z]*");

    public static WordList load(File file) throws IOException {
        Preconditions.checkNotNull(file, "File is null.");

        WordList result = new WordList();
        result.parse(new FileInputStream(file));

        return result;
    }

    // TODO remove the method
    public static void main(String[] args) throws Exception {
        File stopWords = new File("dictionary\\stopwords.txt");
        System.out.println(stopWords.getCanonicalPath());
        System.out.println(stopWords.getAbsolutePath());
        WordList stopWordList = WordList.load(stopWords);

        for (String stopWord : stopWordList._words) {
            System.out.println(stopWord);
        }

        System.out.println("Total: " + stopWordList._words.size());
    }

    private final Set<String> _words = new HashSet<String>();

    public boolean contains(String word) {
        final String normalizedWord = Strings.nullToEmpty(word).toLowerCase().trim();
        return _words.contains(normalizedWord);
    }

    private void parse(InputStream stream) throws IOException {
        Scanner scanner = null;

        try {
            scanner = new Scanner(stream);

            final Splitter splitter = Splitter.onPattern("[\\s]+").omitEmptyStrings().trimResults();

            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                Iterable<String> possibleWords = splitter.split(nextLine);

                for (String word : possibleWords) {
                    Matcher matcher = WORD_PATTERN.matcher(word);

                    if (matcher.matches()) {
                        _words.add(word.toLowerCase());
                    }
                }
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

}
