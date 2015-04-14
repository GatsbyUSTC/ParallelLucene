package parallel;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class MergeIndexFiles {

	private String TotalIndexPath;
	public MergeIndexFiles() {
		// TODO Auto-generated constructor stub
		TotalIndexPath = MainThread.ParentIndexPath + "/TotalIndex";
	}
	
	public void merge(){
		Directory dir;
		try {
			dir = FSDirectory.open(Paths.get(TotalIndexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter indexWriter = new IndexWriter(dir, iwc);
			Directory[] directorys = new Directory[MainThread.THREADNUM]; 
			for(int i=0;i < MainThread.THREADNUM;i++){
				String tempPath = MainThread.ParentIndexPath + "/Thread" + i;
				Directory tempDir = FSDirectory.open(Paths.get(tempPath));
				directorys[i] = tempDir;
			}
			indexWriter.addIndexes(directorys);
			indexWriter.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
