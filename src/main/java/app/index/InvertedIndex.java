package app.index;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class InvertedIndex {
    private HashMap<String, LinkedList<Integer>> index = new HashMap<>();
    
    public void addTermAndPostings(String term, LinkedList<Integer> postingsList) {
        index.put(term, postingsList);
    }

    public LinkedList<Integer> getPostingsForTerm(String term) {
        LinkedList<Integer> postingsList = index.get(term);
        Collections.sort(postingsList);
        return postingsList;
    }

    public Integer getVocabularySize() {
        return index.size();
    }
}