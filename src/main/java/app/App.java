package app;

import java.io.IOException;

import app.index.IndexBuilder;
import app.index.InvertedIndex;
import app.io.InputHandler;
import app.io.OutputHandler;
import app.search.Operation;
import app.search.Result;
import app.search.Searcher;
import app.search.Strategy;

public class App 
{
    public static void main( String[] args ) throws IOException {

        IndexBuilder maker = new IndexBuilder(args[0]);
        OutputHandler oHandler = new OutputHandler(args[1]);
        InputHandler iHandler = new InputHandler(args[2]);

        InvertedIndex index = maker.build();

        Searcher engine = new Searcher(index);

        while(iHandler.hasNextQuery()) {
            iHandler.advancePosition();
            // GetPostings
            for (String term : iHandler.getCurrentQueryTerms()) {
                oHandler.addLine("GetPostings");
                oHandler.addLine(term);
                String postingList = "";
                for (Integer posting : index.getPostingsForTerm(term)) {
                    postingList = postingList + " " + posting;
                }
                oHandler.addLine("Postings list:" + postingList);
            }

            // prepare for search
            engine.setTerms(iHandler.getCurrentQueryTerms());
            Result result;

            // TAAT
            oHandler.addLine("TaatAnd");
            oHandler.addLine(iHandler.getCurrentQuery());
            engine.setMode(Strategy.TAAT, Operation.AND);
            result = engine.search();
            String resultList = "";
            for (Integer docID : result.getDocs()) {
                resultList = resultList + " " + docID;
            }
            oHandler.addLine("Results:" + (resultList.equals("")?" empty":resultList));
            oHandler.addLine("Number of documents in results: " + result.getNumberOfDocuments());
            oHandler.addLine("Number of comparisons: " + result.getNumberOfComparisons());

            oHandler.addLine("TaatOr");
            oHandler.addLine(iHandler.getCurrentQuery());
            engine.setMode(Strategy.TAAT, Operation.OR);
            result = engine.search();
            resultList = "";
            for (Integer docID : result.getDocs()) {
                resultList = resultList + " " + docID;
            }
            oHandler.addLine("Results:" + (resultList.equals("")?" empty":resultList));
            oHandler.addLine("Number of documents in results: " + result.getNumberOfDocuments());
            oHandler.addLine("Number of comparisons: " + result.getNumberOfComparisons());

            // DAAT - stripped
            oHandler.addLine("DaatAnd");
            oHandler.addLine(iHandler.getCurrentQuery());
            engine.setMode(Strategy.DAAT, Operation.AND);
            result = engine.search();
            resultList = "";
            for (Integer docID : result.getDocs()) {
                resultList = resultList + " " + docID;
            }
            oHandler.addLine("Results:" + (resultList.equals("")?" empty":resultList));
            oHandler.addLine("Number of documents in results: " + result.getNumberOfDocuments());
            oHandler.addLine("Number of comparisons: " + result.getNumberOfComparisons());

            oHandler.addLine("DaatOr");
            oHandler.addLine(iHandler.getCurrentQuery());
            engine.setMode(Strategy.DAAT, Operation.OR);
            result = engine.search();
            resultList = "";
            for (Integer docID : result.getDocs()) {
                resultList = resultList + " " + docID;
            }
            oHandler.addLine("Results:" + (resultList.equals("")?" empty":resultList));
            oHandler.addLine("Number of documents in results: " + result.getNumberOfDocuments());
            oHandler.addLine("Number of comparisons: " + result.getNumberOfComparisons());
        }
        oHandler.writeAllLines();
    }
}
