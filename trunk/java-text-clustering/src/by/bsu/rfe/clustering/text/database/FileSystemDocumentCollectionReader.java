package by.bsu.rfe.clustering.text.database;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.google.common.base.Preconditions;

import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.document.Document;
import by.bsu.rfe.clustering.text.document.DocumentCollection;

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

        File[] files = _folder.listFiles(_fileFilter);
        for (File file : files) {
            Scanner scanner = null;
            Document newDoc = new Document();

            try {
                scanner = new Scanner(file);

                while (scanner.hasNext()) {
                    String term = scanner.next();
                    if ((stopWords == null) || !stopWords.contains(term)) {
                        newDoc.addTerm(term);
                    }
                }

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

        return collection;
    }
}
