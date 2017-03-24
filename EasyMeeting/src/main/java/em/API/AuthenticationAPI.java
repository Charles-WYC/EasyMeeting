/*
 * FileName - AuthenticationAPI.java
 * 
 */
package em.API;

import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import em.models.AccessTokenInfo;
import em.result.HttpSuccess;
import em.support.APIHelper;
import em.support.TimingKillThread;
import net.sf.json.JSONObject;

public class AuthenticationAPI extends Thread{
	private String accessUri = "https://oxford-speech.cloudapp.net/token/issueToken";
    private String clientId;
    private String clientSecret;
    
    public AuthenticationAPI(){
    }
    
    public AuthenticationAPI(String clientId, String clientSecret){
    	this.clientId = clientId;
    	this.clientSecret = clientSecret;
    }
    
    public void run(){
    	HttpSuccess result = new HttpSuccess("AuthenticationSuccess", 200);
    	
        try
        {
    		System.out.println("before create timer");
    		System.out.println("analyze thread: "+this.getId());
    		TimingKillThread thread = new TimingKillThread(this, 30000, APIHelper.ResultMap);
    		thread.start();
    		
	        String body = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s&scope=%s",
	        		URLEncoder.encode(clientId,"utf-8"),
	        		URLEncoder.encode(clientSecret,"utf-8"),
	        		URLEncoder.encode("https://speech.platform.bing.com","utf-8"));

			System.out.println("auth body: " + body);
			
	        HttpClient httpclient = HttpClients.createDefault();
        	URIBuilder builder = new URIBuilder(accessUri);
        	URI uri = builder.build();
        	HttpPost request = new HttpPost(uri);
        	
        	System.out.println("Request: " + request);
        	System.out.println("RequestHeader: " + request.getHeaders("Content-Type"));
        	System.out.println("RequestEntity: " + request.getEntity());
        	
        	StringEntity reqEntity = new StringEntity(body);
        	reqEntity.setContentType("application/x-www-form-urlencoded");
        	request.setEntity(reqEntity);

        	System.out.println("Request: " + request);
        	System.out.println("RequestHeader: " + request.getHeaders("Content-Type"));
        	System.out.println("RequestEntity: " + request.getEntity());
        	
        	HttpResponse response = httpclient.execute(request);

    		thread.setNeedRemove(false);
    		
        	if(response.getStatusLine().getStatusCode()==200){
        		
	        	HttpEntity entity = response.getEntity();
	    		
	    		String content = EntityUtils.toString(entity);
	    		
	        	JSONObject responseJsonObject = JSONObject.fromObject(content);
	
	    		System.out.println("json object: "+ responseJsonObject);
	    		
	    		
	        	AccessTokenInfo token = new AccessTokenInfo();
	        	token.setAccessToken(responseJsonObject.getString("access_token"));
	        	token.setTokenType(responseJsonObject.getString("token_type"));
	        	token.setExpiresIn(responseJsonObject.getString("expires_in"));
	        	token.setScope(responseJsonObject.getString("scope"));
	        	
	        	System.out.println("token.access_token: " + token.getAccessToken());
	        	System.out.println("token.token_type: " + token.getTokenType());
	        	System.out.println("token.expires_in: " + token.getExpiresIn());
	        	System.out.println("token.scope: " + token.getScope());
	        	result.setFirstParameter(token);
        	}
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    	if(APIHelper.ResultMap.containsKey(this.getId())){
    		APIHelper.ResultMap.replace(this.getId(), result);
    	}
        return;
    }
}
