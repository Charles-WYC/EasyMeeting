package em.support;

import java.util.Map;

import em.result.Result;

public class TimingKillThread extends Thread{
	private Thread thread;
	private int waitTime;
	private boolean needRemove = true;
	private Map<Long, Result> editMap;
	
	public TimingKillThread(Thread thread, int waitTime, Map<Long, Result> editMap){
		this.setThread(thread);
		this.waitTime = waitTime;
		this.editMap = editMap;
	}

	public void run(){
		try {
            System.out.println("begin timing");
			Thread.sleep(waitTime);
            System.out.println("after timing");
            if(needRemove){
				long threadId = thread.getId();
				editMap.remove(threadId);
				System.out.println("after remove: "+ editMap);
	            System.out.println("end timing");
            }
            System.out.println("end timing");
			return;
		} catch (InterruptedException e) {
			return;
		}
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public boolean isNeedRemove() {
		return needRemove;
	}

	public void setNeedRemove(boolean needRemove) {
		this.needRemove = needRemove;
	}

	public Map<Long, Result> getEditMap() {
		return editMap;
	}

	public void setEditMap(Map<Long, Result> editMap) {
		this.editMap = editMap;
	}
}
