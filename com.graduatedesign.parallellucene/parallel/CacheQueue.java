package parallel;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class CacheQueue implements Runnable {
	private ConcurrentLinkedQueue<StringBuffer> cacheQueue;
	private CountDownLatch startSig;
	private int cacheDocNum;
	
	public CacheQueue(ConcurrentLinkedQueue<StringBuffer> queue, CountDownLatch sig, int num){
		cacheQueue = queue;
		startSig = sig;
		cacheDocNum = num;
	}
	
	@Override
	public void run() {
		BlogReader blogReader = new BlogReader();
		for(int i=1;i <= cacheDocNum;i++){ 
			cacheQueue.add(blogReader.getBlog(i));
			System.out.println(i);
		}
		startSig.countDown();
	}
}
