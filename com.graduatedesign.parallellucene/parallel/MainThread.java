package parallel;

import java.util.concurrent.CountDownLatch;


public class MainThread {

	public static final int ThreadNum = 3;
	public static final int DocumentNum = 10000;
	public static final String ParentIndexPath = "C:/Users/Gatsby/Documents/LuceneIndex";
	private final static CountDownLatch startSig = new CountDownLatch(1);
	private final static CountDownLatch endSig = new CountDownLatch(MainThread.ThreadNum);
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index begin @" +startTime+ "!!!!!!!!!!");
		int eachDocNum = DocumentNum / ThreadNum;
		for(int i=0, s = 1, e = eachDocNum;i < ThreadNum;i++){
			new Thread(new ParallelIndexFiles(s, e, i, startSig, endSig)).start();
			s += eachDocNum;
			e += eachDocNum;
		}
		startSig.countDown();
		try {
			endSig.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MergeIndexFiles mergeIndexFiles = new MergeIndexFiles();
		mergeIndexFiles.merge();long endTime = System.currentTimeMillis();
		System.out.println("!!!!!!!!!!!!!!index complete @" +endTime+ "!!!!!!!!!!!");
		System.out.println("total time is :" +(endTime-startTime)+ "miliseconds");
	}
}
