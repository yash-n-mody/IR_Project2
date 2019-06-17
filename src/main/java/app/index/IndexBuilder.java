package app.index;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;

public class IndexBuilder {
    private IndexFetcher fetcher = null;

    public IndexBuilder(IndexFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public IndexBuilder(String indexPath) throws IOException {
        this.fetcher = new IndexFetcher(indexPath);
    }

    public InvertedIndex build() throws IOException {
        IndexReader reader = fetcher.getReader();
        InvertedIndex index = new InvertedIndex();

        Terms terms_es = reader.leaves().get(0).reader().terms("text_es");
        int docBase_es = reader.leaves().get(0).docBase;

        Terms terms_fr = reader.leaves().get(1).reader().terms("text_fr");
        int docBase_fr = reader.leaves().get(1).docBase;

        Terms terms_en = reader.leaves().get(2).reader().terms("text_en");
        int docBase_en = reader.leaves().get(2).docBase;

        String indexTerm = null;
        LinkedList<Integer> indexPostings = null;
        BytesRef luceneTerm = null;
        PostingsEnum lucenePostings = null;
        TermsEnum luceneTerms = null;

        // for es terms
        luceneTerms = terms_es.iterator();
        while ((luceneTerm = luceneTerms.next()) != null) {
            indexTerm = luceneTerm.utf8ToString();
            indexPostings = new LinkedList<>();
            
            lucenePostings = luceneTerms.postings(lucenePostings);
            int i = 0;
            while ((i = lucenePostings.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                indexPostings.add(i + docBase_es);
            }
            index.addTermAndPostings(indexTerm, indexPostings);
        }

        // for fr terms
        luceneTerms = terms_fr.iterator();
        while ((luceneTerm = luceneTerms.next()) != null) {
            indexTerm = luceneTerm.utf8ToString();
            indexPostings = new LinkedList<>();
            
            lucenePostings = luceneTerms.postings(lucenePostings);
            int i = 0;
            while ((i = lucenePostings.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                indexPostings.add(i + docBase_fr);
            }
            index.addTermAndPostings(indexTerm, indexPostings);
        }

        // for en terms
        luceneTerms = terms_en.iterator();
        while ((luceneTerm = luceneTerms.next()) != null) {
            indexTerm = luceneTerm.utf8ToString();
            indexPostings = new LinkedList<>();
            
            lucenePostings = luceneTerms.postings(lucenePostings);
            int i = 0;
            while ((i = lucenePostings.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                indexPostings.add(i + docBase_en);
            }
            index.addTermAndPostings(indexTerm, indexPostings);
        }

        return index;
    }
}