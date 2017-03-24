package em.support;

import java.util.Timer;
import java.util.TimerTask;
import java.net.URI;
import java.net.URLEncoder;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import em.models.AccessTokenInfo;
import em.result.EmResult;

public class Authentication{
	private String accessUri = "https://oxford-speech.cloudapp.net/token/issueToken";
    private String clientId;
    private String clientSecret;
    private URI uri;
    private String body;
    private AccessTokenInfo token;
    private Timer accessTokenRenewer;
    private int refreshTokenDuration = 500000;
    
    class RenewTask extends TimerTask{
    	public void run(){
    		try{
    			RenewAccessToken();
    		}
            catch (Exception ex)
            {
                System.out.println(String.format("Failed renewing access token. Details: %s", ex.getMessage()));
            }
            finally
            {
                try
                {
                	accessTokenRenewer.cancel();
                }
                catch (Exception ex)
                {
                	System.out.println(String.format("Failed to reschedule the timer to renew access token. Details: %s", ex.getMessage()));
                }
            }
    		
    	}
    }
    
    private void RenewAccessToken(){
    	
        HttpClient httpclient = HttpClients.createDefault();
    	try
        {
        	URIBuilder builder = new URIBuilder(accessUri);
        	uri = builder.build();
        	HttpPost request = new HttpPost(uri);
        	StringEntity reqEntity = new StringEntity(body);
        	reqEntity.setContentType("application/x-www-form-urlencoded");
        	request.setEntity(reqEntity);
        	
        	HttpResponse response = httpclient.execute(request);
        	HttpEntity entity = response.getEntity();

    		System.out.println("In renew Auth entity: " + entity);

    		
    		String content = EntityUtils.toString(entity);
    		
    		System.out.println("Auth entity: " + content);
    		
        	JSONObject responseJsonObject = JSONObject.fromObject(content);

    		System.out.println("In renew json object: "+ responseJsonObject);
    		
        	token.setAccessToken(responseJsonObject.getString("access_token"));
        	token.setTokenType(responseJsonObject.getString("token_type"));
        	token.setExpiresIn(responseJsonObject.getString("expires_in"));
        	token.setScope(responseJsonObject.getString("scope"));
        	
        	System.out.println("In renew token.access_token: " + token.getAccessToken());
        	System.out.println("In renew token.token_type: " + token.getTokenType());
        	System.out.println("In renew token.expires_in: " + token.getExpiresIn());
        	System.out.println("In renew token.scope: " + token.getScope());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public Authentication(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        try
        {
	        body = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s&scope=%s",
	        		URLEncoder.encode(clientId,"utf-8"),
	        		URLEncoder.encode(clientSecret,"utf-8"),
	        		URLEncoder.encode("https://speech.platform.bing.com","utf-8"));

			System.out.println("auth body: " + body);
			
	        HttpClient httpclient = HttpClients.createDefault();
        	URIBuilder builder = new URIBuilder(accessUri);
        	uri = builder.build();
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
        	
        	if(response.getStatusLine().getStatusCode()==200){
	        	HttpEntity entity = response.getEntity();
	    		
	    		String content = EntityUtils.toString(entity);
	    		
	        	JSONObject responseJsonObject = JSONObject.fromObject(content);
	
	    		System.out.println("json object: "+ responseJsonObject);
	    		
	    		
	        	token = new AccessTokenInfo();
	        	token.setAccessToken(responseJsonObject.getString("access_token"));
	        	token.setTokenType(responseJsonObject.getString("token_type"));
	        	token.setExpiresIn(responseJsonObject.getString("expires_in"));
	        	token.setScope(responseJsonObject.getString("scope"));
	        	
	        	System.out.println("token.access_token: " + token.getAccessToken());
	        	System.out.println("token.token_type: " + token.getTokenType());
	        	System.out.println("token.expires_in: " + token.getExpiresIn());
	        	System.out.println("token.scope: " + token.getScope());
	        	accessTokenRenewer = new Timer();
	        	accessTokenRenewer.schedule(new RenewTask(),refreshTokenDuration,refreshTokenDuration);
        	}
        	else
        	{
        		token = null;
        	}
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
	public String getAccessUri() {
		return accessUri;
	}
	public void setAccessUri(String accessUri) {
		this.accessUri = accessUri;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public AccessTokenInfo getToken() {
		return token;
	}
	public void setToken(AccessTokenInfo token) {
		this.token = token;
	}
	public Timer getAccessTokenRenewer() {
		return accessTokenRenewer;
	}
	public void setAccessTokenRenewer(Timer accessTokenRenewer) {
		this.accessTokenRenewer = accessTokenRenewer;
	}
	public int getRefreshTokenDuration() {
		return refreshTokenDuration;
	}
	public void setRefreshTokenDuration(int refreshTokenDuration) {
		this.refreshTokenDuration = refreshTokenDuration;
	}
}
