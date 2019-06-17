package app.index;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;

public class IndexFetcher {

    private Path indexPath; // = Paths.get("./index");
    private IndexReader reader;

    public IndexFetcher(String indexPath) throws IOException {
        this.indexPath = Paths.get(indexPath);
        FSDirectory directory = FSDirectory.open(this.indexPath);
        reader = StandardDirectoryReader.open(directory);
    }

    public IndexReader getReader() {
        return this.reader;
    }
}