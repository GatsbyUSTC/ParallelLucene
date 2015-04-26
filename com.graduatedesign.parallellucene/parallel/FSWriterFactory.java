package parallel;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class FSWriterFactory {

	public static IndexWriter getFSWriter() {
		Directory directory;
		IndexWriter fsWriter = null;
		try {
			directory = FSDirectory.open(Paths.get(MainThread.ParentIndexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig conf = new IndexWriterConfig(analyzer);
			fsWriter = new IndexWriter(directory, conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fsWriter;
	}

}
