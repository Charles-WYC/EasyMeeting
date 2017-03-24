package em.API;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import em.models.Profile;
import em.result.EmResult;
import em.result.HttpSuccess;
import em.support.APIHelper;
import em.support.TimingKillThread;
import net.sf.json.JSONObject;

public class ProfileObtainmentAPI extends Thread{
	private String identificationProfileId;
	private static String subscriptionKey = "21d0e251d8e146f0a93ae96888024f66";
	
	public ProfileObtainmentAPI(){
	}
	
	public ProfileObtainmentAPI(String identificationProfileId){
		this.setIdentificationProfileId(identificationProfileId);
	}

	public void run(){
		System.out.println("before create timer");
		System.out.println("analyze thread: "+this.getId());
		TimingKillThread thread = new TimingKillThread(this, 30000, APIHelper.ResultMap);
		thread.start();
		
		HttpClient httpclient = HttpClients.createDefault();

		HttpSuccess result = new HttpSuccess("ProfileObtainmentSuccess", 200);
        try
        {
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/spid/v1.0/identificationProfiles/"+identificationProfileId);

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            HttpResponse response = httpclient.execute(request);

            thread.setNeedRemove(false);
            
            if(response.getStatusLine().getStatusCode() == 500){
            	if(APIHelper.ResultMap.containsKey(this.getId()))
            	{
            		APIHelper.ResultMap.replace(this.getId(),EmResult.GetProfileError);
            	}
            	return;
            }
            
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);
    		
        	JSONObject responseJsonObject = JSONObject.fromObject(content);
        	
        	Profile tempProfile = new Profile(identificationProfileId);
        	tempProfile.setLocale(responseJsonObject.getString("locale"));
        	tempProfile.setEnrollmentSpeechTime(responseJsonObject.getDouble("enrollmentSpeechTime"));
        	tempProfile.setRemainingEnrollmentSpeechTime(responseJsonObject.getDouble("remainingEnrollmentSpeechTime"));
        	tempProfile.setCreatedDateTime(responseJsonObject.getString("createdDateTime"));
        	tempProfile.setLastActionDateTime(responseJsonObject.getString("lastActionDateTime"));
        	tempProfile.setEnrollmentStatus(responseJsonObject.getString("enrollmentStatus"));
        	result.setFirstParameter(tempProfile);
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
    		APIHelper.ResultMap.replace(this.getId(), result);
    	}
        return;
	}
	
	public String getIdentificationProfileId() {
		return identificationProfileId;
	}

	public void setIdentificationProfileId(String identificationProfileId) {
		this.identificationProfileId = identificationProfileId;
	}
}
