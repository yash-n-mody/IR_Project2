package app.search;

import java.util.LinkedList;

import app.index.InvertedIndex;

public class Searcher {
    private String[] queryTerms = null;
    private Result result = null;
    private Strategy strategy = Strategy.TAAT;
    private Operation operation = Operation.AND;
    private InvertedIndex index = null;

    public Searcher(InvertedIndex index) {
        this.index = index;
    }

    public void setTerms(String[] terms) {
        this.queryTerms = terms;
    }

    public Result search() {
        result = new Result();
        if (strategy == Strategy.TAAT) {
            if (operation == Operation.AND) termAtATimeANDSearch(result);
            else if (operation == Operation.OR) termAtATimeORSearch(result);
            else;
        } else if (strategy == Strategy.DAAT) {
            if (operation == Operation.AND) documentAtATimeANDSearch(result);
            else if (operation == Operation.OR) documentAtATimeORSearch(result);
            else;
        } else;
        return result;
    }

    // this approach is not the best, but its the only way I could concieve parallely moving through
    // multiple linked lists and keeping track of all their pointers. I could have done this
    // by implementing something much more complicated that keeps track of multiple pointers and scales with 
    // the number of terms, but it would be prohibitively complex.
    public void documentAtATimeANDSearch(Result result) {
        Boolean[][] vectors = new Boolean[queryTerms.length][12000];

        for (int i = 0; i < 12000; i++) {
            for (int j = 0; j < queryTerms.length; j++) {
                vectors[j][i] = false;
            }
        }

        for (int i = 0; i < queryTerms.length; i++) {
            LinkedList<Integer> postings = index.getPostingsForTerm(queryTerms[i]);
            for (Integer docID : postings) vectors[i][docID] = true;
        }

        for (int docID = 0; docID < 12000; docID++) {
            Boolean bool = true;
            for (int term = 0; term < queryTerms.length; term++) {
                bool = bool && vectors[term][docID];
                // result.addComparison();
            }
            if (bool) result.addDoc(docID);
        }
    }

    public void documentAtATimeORSearch(Result result) {
        Boolean[][] vectors = new Boolean[queryTerms.length][12000];

        for (int i = 0; i < 12000; i++) {
            for (int j = 0; j < queryTerms.length; j++) {
                vectors[j][i] = false;
            }
        }

        for (int i = 0; i < queryTerms.length; i++) {
            LinkedList<Integer> postings = index.getPostingsForTerm(queryTerms[i]);
            for (Integer docID : postings) vectors[i][docID] = true;
        }

        for (int docID = 0; docID < 12000; docID++) {
            Boolean bool = false;
            for (int term = 0; term < queryTerms.length; term++) {
                bool = bool || vectors[term][docID];
                // result.addComparison();
            }
            if (bool) result.addDoc(docID);
        }
    }

    // need 3 linked lists - left, right and result
    // initially, left is postings[0], right is postings[1], result can be
    // un-initialised
    // go through both arrays, add all common values to result
    //// initialise two pointers to zero
    //// increment the one pointing to the lower value
    //// if both point to same value, add value to result set, increment both
    //// if you reach the end of one array, stop immediately
    // result becomes the new left, the next postings is the new right
    // repeat loop
    public void termAtATimeANDSearch(Result result) {
        LinkedList<Integer> resultList = new LinkedList<>();
        LinkedList<Integer> left, right;
        left = index.getPostingsForTerm(queryTerms[0]);
        Integer leftPointer = 0;
        Integer rightPointer = 0;
        Integer leftValue, rightValue;

        for (int numTerm = 1; numTerm < queryTerms.length; numTerm++) {
            right = index.getPostingsForTerm(queryTerms[numTerm]);

            while (leftPointer < left.size() && rightPointer < right.size()) {
                leftValue = left.get(leftPointer);
                rightValue = right.get(rightPointer);
                if (leftValue < rightValue) {
                    leftPointer++;
                } else if (leftValue > rightValue) {
                    rightPointer++;
                } else {
                    leftPointer++;
                    rightPointer++;
                    resultList.add(leftValue);
                }
                result.addComparison();
            }

            left = resultList;
            resultList = new LinkedList<>();
        }

        for (Integer docID : left)
            result.addDoc(docID);
    }

    // need 3 linked lists - left, right and result
    // initially, left is postings[0], right is postings[1], result can be
    // un-initialised
    // go through both arrays, add all values to result
    //// initialise two pointers to zero
    //// increment the one pointing to the lower value, add it to result
    //// if both point to same value, add value to result set, increment both
    //// if you reach the end of both arrays, stop
    // result becomes the new left, the next postings is the new right
    // repeat loop
    public void termAtATimeORSearch(Result result) {
        LinkedList<Integer> resultList = new LinkedList<>();
        LinkedList<Integer> left, right;
        left = index.getPostingsForTerm(queryTerms[0]);
        Integer leftPointer = 0, rightPointer = 0;
        Integer leftValue = 0, rightValue = 0;

        for (int numTerm = 1; numTerm < queryTerms.length; numTerm++) {
            right = index.getPostingsForTerm(queryTerms[numTerm]);
            leftPointer = 0;
            rightPointer = 0;

            while (leftPointer < left.size() && rightPointer < right.size()) {
                leftValue = left.get(leftPointer);
                rightValue = right.get(rightPointer);
                if (leftValue < rightValue) {
                    leftPointer++;
                    resultList.add(leftValue);
                } else if (leftValue > rightValue) {
                    rightPointer++;
                    resultList.add(rightValue);
                } else {
                    leftPointer++;
                    rightPointer++;
                    resultList.add(leftValue);
                }
                result.addComparison();
            }
            while (leftPointer < left.size()) {
                leftValue = left.get(leftPointer++);
                resultList.add(leftValue);
            }
            while (rightPointer < right.size()) {
                rightValue = right.get(rightPointer++);
                resultList.add(rightValue);
            }

            left = resultList;
            resultList = new LinkedList<>();
        }

        for (Integer docID : left)
            result.addDoc(docID);
    }

    public void setMode(Strategy strategy, Operation operation) {
        this.strategy = strategy;
        this.operation = operation;
    }
}