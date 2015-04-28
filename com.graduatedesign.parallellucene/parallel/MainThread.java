package parallel;

import java.util.concurrent.CountDownLatch;

public class MainThread {

	public static final int THREADNUM = 3;
	public static final int DOCNUM = 3000;
	public static final String ParentIndexPath = "C:/Users/Gatsby/Documents/LuceneIndex";
	private final static CountDownLatch startSig = new CountDownLatch(1);
	private final static CountDownLatch endSig = new CountDownLatch(MainThread.THREADNUM);
	
	public static void main(String[] args) {
		int eachDocNum = DOCNUM / THREADNUM;
		for(int i=0, s = 1, e = eachDocNum;i < THREADNUM;i++){
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
		mergeIndexFiles.merge();
	}
}
