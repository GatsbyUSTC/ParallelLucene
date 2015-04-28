package parallel;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.index.IndexWriter;

public class MainThread {

	public static final int THREADNUM = 4;
	public static final int DOCNUM = 8000;
	public static final String ParentIndexPath = "C:/Users/Gatsby/Documents/LuceneIndex";
	public static final String BlogPath = "C:/Users/Gatsby/Documents/LoalaSave/blog.sina.com.cn/";
	private static CountDownLatch startSig = new CountDownLatch(1);
	private static CountDownLatch endSig = new CountDownLatch(THREADNUM);

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index begin @" + startTime
				+ "!!!!!!!!!!");
		IndexWriter fsWriter = FSWriterFactory.getFSWriter();
		int docForEach = DOCNUM / THREADNUM;
		for (int i = 0; i < THREADNUM; i++)
			new Thread(new ParallelIndexFiles(docForEach, i, fsWriter, startSig, endSig))
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
		long endTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index complete @" + endTime
				+ "!!!!!!!!!!!");
		System.out.println("total time is :" + (endTime - startTime)
				+ "miliseconds");
	}
}
