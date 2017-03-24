package em.API;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;

import em.result.EmResult;
import em.support.APIHelper;
import em.support.TimingKillThread;

public class ProfileDeletionAPI extends Thread{
	
	private static String subscriptionKey = "21d0e251d8e146f0a93ae96888024f66";
	private String identificationProfileId;
	
	public ProfileDeletionAPI(String identificationProfileId){
		this.identificationProfileId = identificationProfileId;
	}
	
	public ProfileDeletionAPI(){
	}
	
	public void run(){
		HttpClient httpclient = HttpClients.createDefault();

		System.out.println("before create timer");
		System.out.println("analyze thread: "+this.getId());
		TimingKillThread thread = new TimingKillThread(this, 30000, APIHelper.ResultMap);
		thread.start();
		
	    try
	    {
	    	URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/spid/v1.0/identificationProfiles/"+identificationProfileId);

	    	URI uri = builder.build();
	    	HttpDelete request = new HttpDelete(uri);
	    	request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

	    	HttpResponse response = httpclient.execute(request);

	    	thread.setNeedRemove(false);
	            
	    	if(response.getStatusLine().getStatusCode() == 500){
	    		if(APIHelper.ResultMap.containsKey(this.getId()))
	    		{
	    			APIHelper.ResultMap.replace(this.getId(),EmResult.DeleteProfileError);
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
