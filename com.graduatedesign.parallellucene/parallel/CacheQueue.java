package parallel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class CacheQueue implements Runnable {
	private LinkedBlockingQueue<StringBuffer> cacheQueue;
	private CountDownLatch startSig;
	public CacheQueue(LinkedBlockingQueue<StringBuffer> queue, CountDownLatch startSig){
		cacheQueue = queue;
		this.startSig = startSig;
	}
	@Override
	public void run() {
		BlogReader blogReader = new BlogReader();
		StringBuffer buffer;
		while((buffer = blogReader.getNextBuffer()) != null ){ 
			try {
				cacheQueue.put(buffer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		startSig.countDown();
		System.out.println("read in");
	}
}
