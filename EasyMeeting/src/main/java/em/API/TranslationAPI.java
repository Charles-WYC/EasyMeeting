package em.API;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import em.models.Authentication;
import em.result.EmResult;
import em.result.HttpSuccess;
import em.services.AuthenticationService;
import em.support.APIHelper;
import em.support.TimingKillThread;
import net.sf.json.JSONObject;

public class TranslationAPI extends Thread{
	private int bufferSize = 1<<19;
	private String filePath;
	
	public TranslationAPI(){
	}
	
	public TranslationAPI(String filePath){
		this.setFilePath(filePath);
	}
	
	public void run(){

		System.out.println("before create timer");
		System.out.println("analyze thread: "+this.getId());
		TimingKillThread thread = new TimingKillThread(this, 30000, APIHelper.ResultMap);
		thread.start();
		
		Authentication auth = AuthenticationService.getAuthenticationInstance();
		if(auth.getToken()==null){
        	if(APIHelper.ResultMap.containsKey(this.getId())){
        		APIHelper.ResultMap.replace(this.getId(), EmResult.AccessTokenError);
        	}
		}
		
		System.out.print(filePath+"\n");
		String requestUri = "https://speech.platform.bing.com/recognize";
        requestUri += "?scenarios=smd";                                  
        requestUri += "&appid=D4D52672-91D7-4C74-8AD8-42B1D98141A5";     
        requestUri += "&locale=en-US";                                   
        requestUri += "&device.os=wp7";
        requestUri += "&version=3.0";
        requestUri += "&format=json";
        requestUri += "&instanceid=565D69FF-E928-4B7E-87DA-9A750B96D9E3";
        requestUri += "&requestid="+ UUID.randomUUID().toString();
		
        byte[] tempBytes = new byte[bufferSize];
        int byteRead = 0;
        FileInputStream audioInputStream = null;
        HttpURLConnection connection = null;
        HttpSuccess result = new HttpSuccess("translationSuccess", 200);
        try
        {
        	File audio = new File(filePath);
        	audioInputStream = new FileInputStream(audio);
        	
        	while((byteRead = audioInputStream.read(tempBytes,0,bufferSize)) > 0){
            	URL url = new URL(requestUri);
            	connection = (HttpURLConnection)url.openConnection();
            	connection.setDoInput(true);
            	connection.setDoOutput(true);
            	connection.setUseCaches(false);
            	connection.setRequestMethod("POST");
            	connection.setRequestProperty("Content-Type", "audio/wav; codec=\"audio/pcm\"; samplerate=16000");
            	String authorization = "Bearer: " + auth.getToken().getAccessToken();
            	System.out.print(authorization);
            	connection.setRequestProperty("Authorization", authorization);
            	connection.connect();
                BufferedOutputStream out=new BufferedOutputStream(connection.getOutputStream());
        		out.write(tempBytes, 0, byteRead);
        		
            	out.flush();
            	out.close();
            	
            	int responseCode = connection.getResponseCode();

        		thread.setNeedRemove(false);
            	
            	if(responseCode == 403){
                	if(APIHelper.ResultMap.containsKey(this.getId())){
                		APIHelper.ResultMap.replace(this.getId(), EmResult.TranslateAccessTokenInvalidError);
                	}
                	return;
            	}
            	else if(responseCode == 400){
                	if(APIHelper.ResultMap.containsKey(this.getId())){
                		APIHelper.ResultMap.replace(this.getId(), EmResult.TranslateBadRequestError);
                	}
            		return;
            	}
            	else if(responseCode == 401){
                	if(APIHelper.ResultMap.containsKey(this.getId())){
                		APIHelper.ResultMap.replace(this.getId(), EmResult.TranslateRequestUnauthorizedError);
                	}
            		return;
            	}
            	else if(responseCode == 502)
            	{
                	if(APIHelper.ResultMap.containsKey(this.getId())){
                		APIHelper.ResultMap.replace(this.getId(), EmResult.TranslateBadGatewayError);
                	}
            		return;
            	}
            	else if(responseCode == 408)
            	{
                	if(APIHelper.ResultMap.containsKey(this.getId())){
                		APIHelper.ResultMap.replace(this.getId(), EmResult.TranslateRequestTimeout);
                	}
            		return;
            	}
            	BufferedReader inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            	String line = null;
            	
            	line = inReader.readLine();
            	if(line != null){
                	JSONObject responseJsonObject = JSONObject.fromObject(line);
                	JSONObject header = responseJsonObject.getJSONObject("header");
                	result.setSecondParameter(header.getString("status"));
                	if(result.getSecondParameter().equals("success"))
                	{
                		result.setFirstParameter(header.getString("name"));
                	}
                	else{
                		
                	}
                	System.out.print(result.getFirstParameter()+"\n");
            	}
            	connection.disconnect();
        	}
        	audioInputStream.close();
        	
        } catch (Exception e1) {
			e1.printStackTrace();
        	if(APIHelper.ResultMap.containsKey(this.getId())){
        		APIHelper.ResultMap.replace(this.getId(), EmResult.UnknowError);
        	}
			return;
		} finally {
			if (audioInputStream != null) {
				try {
					audioInputStream.close();
				} catch (IOException e1) {
		        	e1.printStackTrace();
		        	if(APIHelper.ResultMap.containsKey(this.getId())){
		        		APIHelper.ResultMap.replace(this.getId(), EmResult.CloseFileError);
		        	}
				}
			}
		}
    	if(APIHelper.ResultMap.containsKey(this.getId())){
    		APIHelper.ResultMap.replace(this.getId(), result);
    	}
        return;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
