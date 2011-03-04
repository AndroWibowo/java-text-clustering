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

public class StopWordList {

	private final Set<String> _stopWords = new HashSet<String>();

	public boolean contains(String word) {
		final String normalizedWord = Strings.nullToEmpty(word).toLowerCase().trim();
		return _stopWords.contains(normalizedWord);
	}

	public static StopWordList load(File file) throws IOException {
		Preconditions.checkNotNull(file, "File is null.");

		StopWordList result = new StopWordList();
		result.parse(new FileInputStream(file));

		return result;
	}

	private void parse(InputStream stream) throws IOException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(stream);

			Splitter splitter = Splitter.onPattern("[\\s]+").omitEmptyStrings().trimResults();
			final String wordRegex = "\\w+'\\w+";

			while (scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				Iterable<String> possibleWords = splitter.split(nextLine);

				for (String word : possibleWords) {
					if (word.matches(wordRegex)) {
						_stopWords.add(word);
					}
				}
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	public static void main(String[] args) {
		Pattern stopWordPattern = Pattern.compile("\\w+'\\w+");
		String input = "can't , wouldn't";
		Matcher matcher = stopWordPattern.matcher(input);

		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}

}
