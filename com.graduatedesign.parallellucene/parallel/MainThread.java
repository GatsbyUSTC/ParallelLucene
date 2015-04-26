package parallel;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.index.IndexWriter;

public class MainThread {

	public static final int THREADNUM = 4;
	public static final int DOCNUM = 8000;
	public static final String ParentIndexPath = "C:/Users/Gatsby/Documents/LuceneIndex";
	public static final String BlogPath = "C:/Users/Gatsby/Documents/LoalaSave/blog.sina.com.cn/";
	private static CountDownLatch startSig = new CountDownLatch(2);
	private static CountDownLatch endSig = new CountDownLatch(THREADNUM);
	private static ConcurrentLinkedQueue<StringBuffer> queue = new ConcurrentLinkedQueue<StringBuffer>(); 

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index begin @" + startTime
				+ "!!!!!!!!!!");
		IndexWriter fsWriter = FSWriterFactory.getFSWriter();
		new Thread(new CacheQueue(queue, startSig, DOCNUM)).start();
		for (int i = 0; i < THREADNUM; i++)
			new Thread(new ParallelIndexFiles(queue, i, fsWriter, startSig, endSig))
					.start();
		try {
			startSig.countDown();
			endSig.await();
			fsWriter.close();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		queue.clear();
		// for(int i=0 ;i < THREADNUM;i++)
		// new Thread(new ParallelIndexFiles(blogDealer, linkedBlockingQueue, i,
		// startSig, endSig)).start();
		// try {
		// //endSig.await();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// MergeIndexFiles mergeIndexFiles = new MergeIndexFiles();
		// mergeIndexFiles.merge();
		long endTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index complete @" + endTime
				+ "!!!!!!!!!!!");
		System.out.println("total time is :" + (endTime - startTime)
				+ "miliseconds");
	}
}
