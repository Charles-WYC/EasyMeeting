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
import em.services.TextAnalyzationService;
import em.support.TimingKillThread;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TextAnalyzationAPI extends Thread{

	private static String subscriptionKey = "69b24b6ce2504c02b6d0432fce2da977";
	private String text;
	
	public TextAnalyzationAPI(){
	}
	
	public TextAnalyzationAPI(String text){
		this.text = text;
	}
	
	public void run(){
    	HttpSuccess result = new HttpSuccess("TextAnalyzationSuccess", 200 );
        try
        {
    		System.out.println("before create timer");
    		System.out.println("analyze thread: "+this.getId());
    		TimingKillThread thread = new TimingKillThread(this, 30000, TextAnalyzationService.textAnalyzeResultMap);
    		thread.start();
    		
            HttpClient httpclient = HttpClients.createDefault();
            
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/linguistics/v1.0/analyze");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            String body = "{";
            body += "\"language\" : \"en\",";
            body += "\"analyzerIds\" : [\"22a6b758-420f-4745-8a3c-46835a67c0d2\"],";
            body += "\"text\" : \"";
            body += text;
            body += "\"}";
            
            System.out.println("body: "+body);
            // Request body
            StringEntity reqEntity = new StringEntity(body);
            request.setEntity(reqEntity);

            System.out.println("before execute");
            HttpResponse response = httpclient.execute(request);
            System.out.println("after execute");
            
            thread.setNeedRemove(false);
            System.out.println("after stop timing");
            int responseCode = response.getStatusLine().getStatusCode();
            
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);
            
            System.out.println(responseCode);
            if(responseCode == 200){
            	
            }
            else if(responseCode == 400){
            	if(TextAnalyzationService.textAnalyzeResultMap.containsKey(this.getId()))
            	{
            		TextAnalyzationService.textAnalyzeResultMap.replace(this.getId(),EmResult.AnalyzeTextBadArgumentError);
            	}
            	return;
            }
            else if(responseCode == 401){
            	if(TextAnalyzationService.textAnalyzeResultMap.containsKey(this.getId()))
            	{
            		TextAnalyzationService.textAnalyzeResultMap.replace(this.getId(), EmResult.AnalyzeTextInvalidKeyError);
            	}
            	return;
            }
            else if(responseCode == 403){
            	if(TextAnalyzationService.textAnalyzeResultMap.containsKey(this.getId()))
            	{
            		TextAnalyzationService.textAnalyzeResultMap.replace(this.getId(), EmResult.AnalyzeTextOutOfQuotaError);
            	}
            	return;
            }
            else if(responseCode == 415){
            	if(TextAnalyzationService.textAnalyzeResultMap.containsKey(this.getId()))
            	{
            		TextAnalyzationService.textAnalyzeResultMap.replace(this.getId(), EmResult.AnalyzeTextInvalidMediaTypeError);
            	}
            	return;
            }
            else if(responseCode == 429){
            	if(TextAnalyzationService.textAnalyzeResultMap.containsKey(this.getId()))
            	{
            		TextAnalyzationService.textAnalyzeResultMap.replace(this.getId(), EmResult.AnalyzeTextRateLimitError);
            	}
            	return;
            }
            else if(responseCode == 500){
            	if(TextAnalyzationService.textAnalyzeResultMap.containsKey(this.getId()))
            	{
            		TextAnalyzationService.textAnalyzeResultMap.replace(this.getId(), EmResult.AnalyzeTextInternalServerError);
            	}
            	return;
            }
        	JSONArray responseJsonArray = JSONArray.fromObject(content);
        	JSONObject responseJsonObject = responseJsonArray.getJSONObject(0);
        	String resultString = responseJsonObject.getString("result");
        	
			result.setFirstParameter(resultString.substring(2, resultString.length()-2));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return;
        }
        
    	if(TextAnalyzationService.textAnalyzeResultMap.containsKey(this.getId()))
    	{
    		TextAnalyzationService.textAnalyzeResultMap.replace(this.getId(), result);
    	}
        return;
	}
}
