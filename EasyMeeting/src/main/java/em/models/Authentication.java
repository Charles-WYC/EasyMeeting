package em.models;

import java.util.Timer;
import java.util.TimerTask;

import em.result.EmResult;
import em.result.HttpSuccess;
import em.result.Result;
import em.support.APIHelper;

public class Authentication extends EmResult{
	private String accessUri = "https://oxford-speech.cloudapp.net/token/issueToken";
    private String clientId;
    private String clientSecret;
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
    	try
        {
        	Result result = APIHelper.executeAPI("AuthenticationAPI", clientId, clientSecret);
        	
        	if(result.getType().equals("AuthenticationSuccess")){
        		HttpSuccess httpSuccess = (HttpSuccess)result;
        		this.token = (AccessTokenInfo)httpSuccess.getFirstParameter();
        	}
        	else
        	{
        		this.token = null;
        	}
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
        	Result result = APIHelper.executeAPI("AuthenticationAPI", clientId, clientSecret);
    		
        	if(result.getType().equals("AuthenticationSuccess")){
        		HttpSuccess httpSuccess = (HttpSuccess)result;
        		this.token = (AccessTokenInfo)httpSuccess.getFirstParameter();
        	}
        	else
        	{
        		this.token = null;
        	}
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    	
    	accessTokenRenewer = new Timer();
    	accessTokenRenewer.schedule(new RenewTask(),refreshTokenDuration,refreshTokenDuration);
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
