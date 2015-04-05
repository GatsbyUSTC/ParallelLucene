/**
 * 
 */
package parallel;

import java.awt.List;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author Gatsby
 *
 */
public class ParallelIndexFiles implements Runnable {
	private int startDocNum, endDocNum;
	private String IndexPath;
	private static final String ParentIndexPath = "C:/Users/Gatsby/Documents/LuceneIndex/Thread";
	
	public ParallelIndexFiles(int startDocNum, int endDocNum, int threadNum) {
		// TODO Auto-generated constructor stub
		this.startDocNum = startDocNum;
		this.endDocNum = endDocNum;
		this.IndexPath = ParentIndexPath + threadNum;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int threadNum = 3;
		int totalDocNum = 3000;
		int eachDocNum = totalDocNum / threadNum;
		
		ArrayList<ParallelIndexFiles> threads = new ArrayList<ParallelIndexFiles>();
		for(int i=0, s = 1, e = eachDocNum;i < threadNum;i++){
			threads.add(new ParallelIndexFiles(s, e, i));
			s += eachDocNum;
			e += eachDocNum;
		}
		for(int i=0;i < threadNum;i++){
			threads.get(i).run();
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Directory dir = FSDirectory.open(Paths.get(IndexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter indexWriter = new IndexWriter(dir, iwc);
			BlogDealer blogDealer = new BlogDealer();

			for (int i = startDocNum; i < endDocNum; i++) {
				Blog blog = blogDealer.getBlog(i);
				if (blog != null) {
					Document document = new Document();
					TextField titleField = new TextField("title",
							blog.getTitle(), Store.NO);
					document.add(titleField);
					TextField contentField = new TextField("content",
							blog.getContent(), Store.NO);
					document.add(contentField);
					//System.out.println("indexing " + i + " blog");
					indexWriter.addDocument(document);
				}
			}
			indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
