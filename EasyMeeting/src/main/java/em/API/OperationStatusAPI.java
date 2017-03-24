/*
 * FileName - OperationStatusAPI.java
 * Purpose - get non-real time operation's status
 * Author - Yunchao Wang
 * Last Edit - 2016/7/13
 */
package em.API;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import em.result.EmResult;
import em.result.HttpSuccess;
import em.support.APIHelper;
import em.support.LoggerHelper;
import em.support.TimingKillThread;
import net.sf.json.JSONObject;

/*
 * OperationStatusAPI - Class
 * Purpose - Provide a API to get non-real time operation's status form Microsoft API
 * Tip - NULL
 */
public class OperationStatusAPI extends Thread{
	/* the location of operation status*/
	private String operationLocation;

	/* Subscription key for using Microsoft API*/
	private static String subscriptionKey = "21d0e251d8e146f0a93ae96888024f66";
	
    /*
     * OperationStatusAPI - Constructor
	 * Purpose - For null parameter construction
	 * Parameter - NULL
	 * Tip - NULL
     */
	public OperationStatusAPI(){
	}
	
    /*
     * OperationStatusAPI - Constructor
	 * Purpose - For one parameters construction
	 * Parameter - the location of operation status
	 * Tip - NULL
     */
	public OperationStatusAPI(String operationLocation){
		this.operationLocation = operationLocation;
	}
    
    /*
     * run - Method Overload@java.lang.Thread#run()
     * Purpose - Send request to Microsoft API to get operation status
     * Parameter - NULL
     * Return - NULL
     * Tip - the result of request will be written in APIHelper.ResultMap
     * 	   - result class - HttpSuccess or HttpError or InternalError
     *     - result type for HttpSuccess - getOperationStatusSuccess
     *     - result type for HttpError - httpError
     *     - result type for InternalError - unknowError
     *	   - result parameter for HttpSuccess - firstParameter - status:String
     *	    								  - secondParameter - NULL
     */
	public void run(){
		LoggerHelper.logger.info("OperationStatusAPI-"+ operationLocation+ "-begin");
    	String isValid = "valid";
		
        try
        {
        	/* create timing thread for 30s*/
    		LoggerHelper.logger.info("OperationStatusAPI-"+ operationLocation+ "-CreateTimingThread");
    		TimingKillThread thread = new TimingKillThread(this, 30000, APIHelper.ResultMap);
    		thread.start();

    		/* create success result*/
    		HttpSuccess result = new HttpSuccess("getOperationStatusSuccess", 200);
    		
	        /* create request*/
    		HttpClient httpclient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(operationLocation);
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        	/* execute request and get response*/
            HttpResponse response = httpclient.execute(request);

        	/* cancel timing thread*/
        	if(APIHelper.ResultMap.containsKey(this.getId())){
        		LoggerHelper.logger.info("OperationStatusAPI-"+ operationLocation+ "-GetResponseSuccess-CancelTimingThread");
        		thread.setNeedRemove(false);
        	}
        	else{
        		LoggerHelper.logger.info("OperationStatusAPI-"+ operationLocation+ "-GetResponseSuccess-TimingThreadHasFinishedAlready");
        		isValid = "invalid";
        	}

    		/* if response code is 200, write success result into APIHelper.ResultMap*/
        	int responseCode = response.getStatusLine().getStatusCode();
        	if(responseCode == 200){
        		LoggerHelper.logger.info("OperationStatusAPI-"+isValid+"-"+ operationLocation+ "-ResponseCode-"+responseCode);

        		/* analyze response*/
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity);
            	JSONObject responseJsonObject = JSONObject.fromObject(content);
            	String status = responseJsonObject.getString("status");
            	result.setFirstParameter(status);
            	System.out.println("status : " + status);
            	
            	if(status.equals("succeeded")){
	            	JSONObject processingResult = responseJsonObject.getJSONObject("processingResult");
	            	String enrollmentStatus = processingResult.getString("enrollmentStatus");
	                if(enrollmentStatus != null){
	                	result.setSecondParameter(enrollmentStatus);
	                }
	                else{
	                	String identifiedProfileId = processingResult.getString("identifiedProfileId");
	                	result.setSecondParameter(identifiedProfileId);
	                }
            	}
                System.out.println(APIHelper.ResultMap.containsKey(this.getId()));
                System.out.println("id : " + this.getId());
	    		/* write success result into APIHelper.ResultMap*/
            	if(APIHelper.ResultMap.containsKey(this.getId())){
            		APIHelper.ResultMap.replace(this.getId(), result);
            	}
        	}

    		/* write error result into APIHelper.ResultMap*/
        	else{
        		LoggerHelper.logger.warn("OperationStatusAPI-"+isValid+"-"+ operationLocation+ "-ResponseCode-"+responseCode);

	            if(responseCode == 404){
		        	if(APIHelper.ResultMap.containsKey(this.getId())){
		        		APIHelper.ResultMap.replace(this.getId(), EmResult.GetOperationStatusError);
		        	}
	            }
	            else{
	            	if(APIHelper.ResultMap.containsKey(this.getId()))
	            	{
	            		APIHelper.ResultMap.replace(this.getId(),EmResult.UnknowHttpError);
	            	}
	            }
        	}
        }
        catch (Exception e)
        {
    		LoggerHelper.logger.error("OperationStatusAPI-"+isValid+"-"+ operationLocation+"-error-"+e.getMessage());
           
    		/* write unknowError result into APIHelper.ResultMap*/
        	if(APIHelper.ResultMap.containsKey(this.getId())){
        		APIHelper.ResultMap.replace(this.getId(), EmResult.UnknowError);
        	}
        }

		LoggerHelper.logger.info("OperationStatusAPI-"+isValid+"-"+ operationLocation+ "-end");
    	return;
	}
	
	public String getOperationLocation() {
		return operationLocation;
	}

	public void setOperationLocation(String operationLocation) {
		this.operationLocation = operationLocation;
	}
	
}
