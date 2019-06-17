package app.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class InputHandler {
    private Path filePath; // = Paths.get("./input.txt");
    private LinkedList<String> queries = new LinkedList<String>();              // list of all queries
    private LinkedList<String[]> termLists = new LinkedList<>();                // list of all terms extracted from those queries
    private Integer queryListIndex = -1;                                         // common index pointer for both the lists

    public InputHandler (String filePath) {
        this.filePath = Paths.get(filePath);
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(this.filePath, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                queries.add(line);
                termLists.add(line.split(" "));
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }    
    }

    public void resetPosition() {
        queryListIndex = -1;
    }

    public void advancePosition() {
        queryListIndex += 1;
    }

    public Boolean hasNextQuery() {
        // if (listIndex < queries.size()) return true;
        // else return false;
        return ((queryListIndex + 1) < queries.size())? true : false;
    }

    public String getNextQuery() throws IndexOutOfBoundsException {
        if (queryListIndex < queries.size()) {
            return queries.get(++queryListIndex);
        } else throw new IndexOutOfBoundsException();
    }

    public String[] getNextQueryTerms() throws IndexOutOfBoundsException {
        if (queryListIndex < queries.size()) {
            return termLists.get(++queryListIndex);
        } else throw new IndexOutOfBoundsException();
    }

    public String getCurrentQuery() throws IndexOutOfBoundsException {
        if (queryListIndex < queries.size()) {
            return queries.get(queryListIndex);
        } else throw new IndexOutOfBoundsException();
    }

    public String[] getCurrentQueryTerms() throws IndexOutOfBoundsException {
        // System.out.println(queryListIndex + " : " + queries.size());
        if (queryListIndex < queries.size()) {
            return termLists.get(queryListIndex);
        } else throw new IndexOutOfBoundsException();
    }
}