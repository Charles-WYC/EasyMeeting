package em.services;

import java.util.Hashtable;
import java.util.Map;

import em.API.TextAnalyzationAPI;
import em.result.EmResult;
import em.result.Result;

public class TextAnalyzationService extends EmResult{

	public static Map<Long, Result> textAnalyzeResultMap = new Hashtable<Long, Result>();
	
	public static Result getTextAnalyzation(String text){
		TextAnalyzationAPI textAnalyzationAPI = new TextAnalyzationAPI(text);
		long threadId = textAnalyzationAPI.getId();
		textAnalyzeResultMap.put(threadId, InitialResult);
		textAnalyzationAPI.start();
		boolean finished = false;
		while(!finished){
			Result tempResult = textAnalyzeResultMap.get(threadId);
			if(tempResult != null){
				if(!tempResult.equals(InitialResult)){
					System.out.println("finished");
					finished = true;
				}
				System.out.println("wating");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("in restart");
				textAnalyzationAPI = new TextAnalyzationAPI(text);
        		System.out.println("1");
        		threadId = textAnalyzationAPI.getId();
        		System.out.println("2");
        		textAnalyzeResultMap.put(threadId, InitialResult);
        		System.out.println("3");
        		textAnalyzationAPI.start();
        		System.out.println("4");
			}
		}
		System.out.println("after analyze");
    	Result result = textAnalyzeResultMap.get(threadId);
    	textAnalyzeResultMap.remove(threadId);
    	
    	return result;
	}
}
