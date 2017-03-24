package em.API;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;

import em.result.EmResult;
import em.support.APIHelper;
import em.support.TimingKillThread;

public class EnrollmentReplacementAPI extends Thread{

	private String identificationProfileId;

	private static String subscriptionKey = "21d0e251d8e146f0a93ae96888024f66";
	
	public EnrollmentReplacementAPI(){
	}
	
	public EnrollmentReplacementAPI(String identificationProfileId){
		this.identificationProfileId = identificationProfileId;
	}
	
	public void run(){
		System.out.println("before create timer");
		System.out.println("analyze thread: "+this.getId());
		TimingKillThread thread = new TimingKillThread(this, 30000, APIHelper.ResultMap);
		thread.start();
		
        HttpClient httpclient = HttpClients.createDefault();
        
		try{
			String requestUri = "https://api.projectoxford.ai/spid/v1.0/identificationProfiles/";
			requestUri += identificationProfileId;
	        requestUri += "/reset";
	        
			URIBuilder builder = new URIBuilder(requestUri);
	
	
	        URI uri = builder.build();
	        HttpPost request = new HttpPost(uri);
	        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            thread.setNeedRemove(false);
            
	        HttpResponse response = httpclient.execute(request);
	        if(response.getStatusLine().getStatusCode() == 400){
            	if(APIHelper.ResultMap.containsKey(this.getId()))
            	{
            		APIHelper.ResultMap.replace(this.getId(),EmResult.ResetEnrollmentBadRequestError);
            	}
	        	return;
	        }
	        else if(response.getStatusLine().getStatusCode() == 500){
            	if(APIHelper.ResultMap.containsKey(this.getId()))
            	{
            		APIHelper.ResultMap.replace(this.getId(),EmResult.ResetEnrollmentInternalServerError);
            	}
	        	return;
	        }
	    }
	    catch (Exception e)
	    {
	        System.out.println(e.getMessage());
        	if(APIHelper.ResultMap.containsKey(this.getId()))
        	{
        		APIHelper.ResultMap.replace(this.getId(),EmResult.UnknowError);
        	}
			return;
	    }
    	if(APIHelper.ResultMap.containsKey(this.getId()))
    	{
    		APIHelper.ResultMap.replace(this.getId(),EmResult.HttpSuccessWithoutParameter);
    	}
		return;
	}
}
