/**
 * 
 */
package parallel;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

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



public class ParallelIndexFiles implements Runnable {
	
	private CountDownLatch startSig, endSig;
	private int startDocNum, endDocNum;
	private int threadId;
	private String IndexPath;
	
	
	public ParallelIndexFiles(int startDocNum, int endDocNum, int threadId, CountDownLatch startSig, CountDownLatch endSig) {
		this.startDocNum = startDocNum;
		this.endDocNum = endDocNum;
		this.threadId = threadId;
		this.startSig = startSig;
		this.endSig = endSig;
		this.IndexPath = MainThread.ParentIndexPath + "/Thread" + threadId;
	}
	
	@Override
	public void run() {
		try {
			startSig.await();
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
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		endSig.countDown();
	}
}
