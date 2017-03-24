package em.support;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Map;

import em.result.EmResult;
import em.result.Result;

public class APIHelper extends EmResult{
	public static Map<Long, Result> ResultMap = new Hashtable<Long, Result>();
	
	public static Result executeAPI(String APIname, String firstParameter, String secondParameter){
		LoggerHelper.logger.info("APIHelper-executeAPI-" + APIname+"-begin");
        Thread API = null;
        Constructor<?> cons[] = null;
        try{
            Class<?> tempAPI=null;
	        tempAPI =Class.forName("em.API." + APIname);
	        cons=tempAPI.getConstructors();
	        if(APIname.equals("AuthenticationAPI") || APIname.equals("EnrollmentCreationAPI") || 
	        		APIname.equals("SpeakerRecognitionAPI")){
	        	if(cons[0].getParameterCount() == 2){
		        	API = (Thread)cons[0].newInstance(firstParameter, secondParameter);
	        	}
	        	else{
		        	API = (Thread)cons[1].newInstance(firstParameter, secondParameter);
	        	}
	        }
	        else if(APIname.equals("EnrollmentReplacementAPI") || APIname.equals("OperationStatusAPI") || 
	        		APIname.equals("ProfileDeletionAPI") || APIname.equals("ProfileObtainmentAPI") || 
	        		APIname.equals("TextAnalyzationAPI") || APIname.equals("TranslationAPI")){
	        	if(cons[0].getParameterCount() == 1){
		        	API = (Thread)cons[0].newInstance(firstParameter);
	        	}
	        	else{
		        	API = (Thread)cons[1].newInstance(firstParameter);
	        	}
	        }
	        else{
	        	API = (Thread)cons[0].newInstance();
	        }
        }catch (Exception e) {
            e.printStackTrace();
        }
       
		long threadId = API.getId();
		ResultMap.put(threadId, InitialResult);
		API.start();
		boolean finished = false;
		while(!finished){
			Result tempResult = ResultMap.get(threadId);
			if(tempResult != null){
				if(!tempResult.equals(InitialResult)){
					System.out.println("finished");
					finished = true;
				}
				System.out.println("waiting");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("in restart");
		        try {
			        if(APIname.equals("AuthenticationAPI") || APIname.equals("EnrollmentCreationAPI") || 
			        		APIname.equals("SpeakerRecognitionAPI")){
			        	if(cons[0].getParameterCount() == 2){
				        	API = (Thread)cons[0].newInstance(firstParameter, secondParameter);
			        	}
			        	else{
				        	API = (Thread)cons[1].newInstance(firstParameter, secondParameter);
			        	}
			        }
			        else if(APIname.equals("EnrollmentReplacementAPI") || APIname.equals("OperationStatusAPI") || 
			        		APIname.equals("ProfileDeletionAPI") || APIname.equals("ProfileObtainmentAPI") || 
			        		APIname.equals("TextAnalyzationAPI") || APIname.equals("TranslationAPI")){
			        	if(cons[0].getParameterCount() == 1){
				        	API = (Thread)cons[0].newInstance(firstParameter);
			        	}
			        	else{
				        	API = (Thread)cons[1].newInstance(firstParameter);
			        	}
			        }
			        else{
			        	API = (Thread)cons[0].newInstance();
			        }
				} catch (Exception e) {
					e.printStackTrace();
				} 
        		System.out.println("1");
        		threadId = API.getId();
        		System.out.println("2");
        		ResultMap.put(threadId, InitialResult);
        		System.out.println("3");
        		API.start();
        		System.out.println("4");
			}
		}
		System.out.println("after analyze");
    	Result result = ResultMap.get(threadId);
    	ResultMap.remove(threadId);
    	
    	return result;
	}
}
