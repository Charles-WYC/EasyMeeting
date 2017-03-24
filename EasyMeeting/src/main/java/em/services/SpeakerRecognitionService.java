package em.services;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import em.API.SpeakerRecognitionAPI;
import em.result.EmResult;
import em.result.Result;

public class SpeakerRecognitionService extends EmResult{
	public static Map<Long, Result> speakerRecognitionResultMap = new Hashtable<Long, Result>();

	public static Result DoSpeakerRecognition(String filePath, ArrayList<String> identificationProfileIds){
		SpeakerRecognitionAPI speakerRecognitionAPI = new SpeakerRecognitionAPI(filePath, identificationProfileIds);
		long threadId = speakerRecognitionAPI.getId();
		speakerRecognitionResultMap.put(threadId, InitialResult);
		speakerRecognitionAPI.start();
		boolean finished = false;
		while(!finished){
			Result tempResult = speakerRecognitionResultMap.get(threadId);
			if(tempResult != null){
				if(!tempResult.equals(InitialResult)){
					System.out.println("finished");
					finished = true;
				}
				System.out.println("outer id : " + String.valueOf(threadId));
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
				speakerRecognitionAPI = new SpeakerRecognitionAPI(filePath, identificationProfileIds);
        		System.out.println("1");
        		threadId = speakerRecognitionAPI.getId();
        		System.out.println("2");
        		speakerRecognitionResultMap.put(threadId, InitialResult);
        		System.out.println("3");
        		speakerRecognitionAPI.start();
        		System.out.println("4");
			}
		}
		System.out.println("after analyze");
    	Result result = speakerRecognitionResultMap.get(threadId);
    	speakerRecognitionResultMap.remove(threadId);
    	
    	return result;
	}
}
