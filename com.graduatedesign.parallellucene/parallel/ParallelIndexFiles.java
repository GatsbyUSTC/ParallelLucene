/**
 * 
 */
package parallel;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
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
import org.apache.lucene.store.RAMDirectory;


public class ParallelIndexFiles implements Runnable {

	private CountDownLatch startSig, endSig;
	private int threadId;
	private ConcurrentLinkedQueue<StringBuffer> queue;
	private RAMDirectory dir;
	private IndexWriter fsWriter;

	public ParallelIndexFiles(ConcurrentLinkedQueue<StringBuffer> queue, int threadId, IndexWriter fsWriter,
			CountDownLatch startSig, CountDownLatch endSig) {
		this.queue = queue;
		this.threadId = threadId;
		this.startSig = startSig;
		this.endSig = endSig;
		this.dir = new RAMDirectory();
		this.fsWriter = fsWriter;
	}

	@Override
	public void run() {
		try {
			startSig.await();
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter indexWriter = new IndexWriter(dir, iwc);
			StringBuffer stringBuffer;
			Blog blog;
			while(!queue.isEmpty()) {
				stringBuffer = queue.remove();
				if(stringBuffer.equals(new StringBuffer("End of Queue")))
					break;
				if ((blog = BlogDealer.dealblog(stringBuffer)) != null) {
					Document document = new Document();
					TextField titleField = new TextField("title",
							blog.getTitle(), Store.NO);
					document.add(titleField);
					TextField contentField = new TextField("content",
							blog.getContent(), Store.NO);
					document.add(contentField);
					// System.out.println("indexing " + i + " blog");
					indexWriter.addDocument(document);
				}
			}
			indexWriter.close();
			fsWriter.addIndexes(new Directory[]{dir});
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
