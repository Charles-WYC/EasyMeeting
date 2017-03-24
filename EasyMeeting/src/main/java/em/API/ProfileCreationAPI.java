package em.API;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import em.result.EmResult;
import em.result.HttpSuccess;
import em.support.APIHelper;
import em.support.TimingKillThread;
import net.sf.json.JSONObject;

public class ProfileCreationAPI extends Thread{
	private static String subscriptionKey = "21d0e251d8e146f0a93ae96888024f66";
	
	public void run(){

   	 	HttpClient httpclient = HttpClients.createDefault();
   	 	HttpSuccess httpSuccess = new HttpSuccess("profileCreationSuccess", 200);
   	 	
        try
        {
    		System.out.println("before create timer");
    		System.out.println("analyze thread: "+this.getId());
    		TimingKillThread thread = new TimingKillThread(this, 30000, APIHelper.ResultMap);
    		thread.start();
    		
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/spid/v1.0/identificationProfiles");


            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            
            // Request body
            StringEntity reqEntity = new StringEntity("{\"locale\":\"en-us\",}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);

            thread.setNeedRemove(false);
            
            if(response.getStatusLine().getStatusCode() == 500){
            	if(APIHelper.ResultMap.containsKey(this.getId()))
            	{
            		APIHelper.ResultMap.replace(this.getId(),EmResult.CreateProfileError);
            	}
            	return;
            }
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            
        	JSONObject responseJsonObject = JSONObject.fromObject(content);
        	 
        	httpSuccess.setFirstParameter(responseJsonObject.getString("identificationProfileId"));
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
    		APIHelper.ResultMap.replace(this.getId(),httpSuccess);
    	}
        return;
	}
}
