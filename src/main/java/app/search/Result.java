package app.search;

import java.util.LinkedList;

public class Result {
    private Integer numComparisons = null;
    private Integer numDocs = null;
    private LinkedList<Integer> docsList = null;

    public Result() {
        this.docsList = new LinkedList<>();
        this.numDocs = 0;
        this.numComparisons = 0;
    }

    public void addDoc(Integer doc) {
        this.docsList.add(doc);
        numDocs++;
    }

    public LinkedList<Integer> getDocs() {
        return this.docsList;
    }

    public Integer getNumberOfDocuments() {
        return this.numDocs;
    }

    public Integer getNumberOfComparisons() {
        return this.numComparisons;
    }

    public void addComparison() {
        numComparisons++;
    }
}