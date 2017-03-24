package em.services;

import java.util.Hashtable;
import java.util.Map;

import em.API.OperationStatusAPI;
import em.result.EmResult;
import em.result.Result;

public class OperationStatusService extends EmResult{
	public static Map<Long, Result> operationStatusResultMap = new Hashtable<Long, Result>(); 
	
	public static Result getOperationStatus(String operationLocation){
		OperationStatusAPI operationStatusAPI = new OperationStatusAPI(operationLocation);
		long threadId = operationStatusAPI.getId();
		operationStatusResultMap.put(threadId, InitialResult);
		operationStatusAPI.start();
		boolean finished = false;
		while(!finished){
			Result tempResult = operationStatusResultMap.get(threadId);
			if(tempResult != null){
				if(!tempResult.equals(InitialResult)){
					System.out.println("finished");
					finished = true;
				}
				System.out.println("outer id : " + String.valueOf(threadId));
				System.out.println("wating");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("in restart");
				operationStatusAPI = new OperationStatusAPI(operationLocation);
        		System.out.println("1");
        		threadId = operationStatusAPI.getId();
        		System.out.println("2");
        		operationStatusResultMap.put(threadId, InitialResult);
        		System.out.println("3");
        		operationStatusAPI.start();
        		System.out.println("4");
			}
		}
		System.out.println("after analyze");
    	Result result = operationStatusResultMap.get(threadId);
    	operationStatusResultMap.remove(threadId);
    	return result;
	}
}
