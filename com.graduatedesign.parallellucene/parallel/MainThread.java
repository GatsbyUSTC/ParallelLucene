package parallel;

import java.util.concurrent.CountDownLatch;

public class MainThread {

	public static final int THREADNUM = 3;
	public static final int DOCNUM = 10000;
	public static final String ParentIndexPath = "C:/Users/Gatsby/Documents/LuceneIndex";
	public static final String BlogPath = "C:/Users/Gatsby/Documents/LoalaSave/blog.sina.com.cn/";
	private final static CountDownLatch startSig = new CountDownLatch(1);
	private final static CountDownLatch endSig = new CountDownLatch(MainThread.THREADNUM);
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index begin @" +startTime+ "!!!!!!!!!!");
		BlogReader blogDealer = new BlogReader();
		for(int i=0 ;i < THREADNUM;i++)
			new Thread(new ParallelIndexFiles(blogDealer, i, startSig, endSig)).start();
		startSig.countDown();
		try {
			endSig.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MergeIndexFiles mergeIndexFiles = new MergeIndexFiles();
		mergeIndexFiles.merge();
		long endTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index complete @" +endTime+ "!!!!!!!!!!!");
		System.out.println("total time is :" +(endTime-startTime)+ "miliseconds");
	}
}
