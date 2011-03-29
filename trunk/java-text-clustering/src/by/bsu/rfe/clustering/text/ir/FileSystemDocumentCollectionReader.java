package by.bsu.rfe.clustering.text.ir;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.document.Document;
import by.bsu.rfe.clustering.text.document.DocumentCollection;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class FileSystemDocumentCollectionReader extends AbstractDocumentCollectionReader {

    private File _folder;

    private FileFilter _fileFilter;

    public FileSystemDocumentCollectionReader(File folder, WordList stopWordList) {
        super(stopWordList);
        _folder = Preconditions.checkNotNull(folder, "Folder is null");
    }

    public FileSystemDocumentCollectionReader useFilter(FileFilter filter) {
        _fileFilter = filter;
        return this;
    }

    @Override
    public DocumentCollection readDocuments() throws DocumentReadException {
        if (!_folder.exists()) {
            String msg = "Detination folder doese not exist";
            throw new DocumentReadException(new FileNotFoundException(msg));
        }

        if (!_folder.isDirectory()) {
            String msg = "Destination is not a directory";
            throw new DocumentReadException(msg);
        }

        try {
            return read();
        }
        catch (IOException e) {
            throw new DocumentReadException(e);
        }
    }

    private DocumentCollection read() throws IOException {
        DocumentCollection collection = new DocumentCollection();
        WordList stopWords = getStopWords();

        // TODO add more punctuation characters
        Pattern delimiter = Pattern.compile("[\\s.,;:()_\\[\\]{}!]+");

        File[] files = _folder.

        listFiles(_fileFilter);
        int idSuffix = 0;
        for (File file : files) {
            if (!file.isDirectory()) {
                Scanner scanner = null;
                Document newDoc = new Document("doc_" + idSuffix);

                try {
                    scanner = new Scanner(file).useDelimiter(delimiter);

                    while (scanner.hasNext()) {
                        String term = scanner.next();
                        if (((stopWords == null) || !stopWords.contains(term)) && (term.length() > 2)) {
                            Matcher matcher = WordList.WORD_PATTERN.matcher(term);

                            if (matcher.matches()) {
                                newDoc.addTerm(term);
                            }
                        }
                    }

                    newDoc.setOriginalText(Files.toString(file, Charset.defaultCharset()));
                    newDoc.setTitle(file.getName());

                    collection.addDocument(newDoc);
                }
                catch (FileNotFoundException e) {
                    throw e;
                }
                finally {
                    if (scanner != null) {
                        scanner.close();
                    }
                }
            }

            idSuffix++;
        }

        return collection;
    }
}
