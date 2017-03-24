package em.support;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import em.result.EmResult;
import em.result.GetOperationStatusSuccess;
import em.result.Result;
import net.sf.json.JSONObject;

public class OperationStatus extends EmResult{
	private String operationLocation;
	private static String subscriptionKey = "21d0e251d8e146f0a93ae96888024f66";
	
	public OperationStatus(String operationLocation){
		this.operationLocation = operationLocation;
	}
	
	public Result Get(){
		HttpClient httpclient = HttpClients.createDefault();
		GetOperationStatusSuccess result = new GetOperationStatusSuccess();
		
        try
        {
            URIBuilder builder = new URIBuilder(operationLocation);


            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            HttpResponse response = httpclient.execute(request);
            
            if(response.getStatusLine().getStatusCode() == 404){
            	return GetOperationStatusError;
            }
            
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);
    		
        	JSONObject responseJsonObject = JSONObject.fromObject(content);
        	
        	result.setStatus(responseJsonObject.getString("status"));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
			return UnknowError;
        }
        return result;
	}
	
	public String getOperationLocation() {
		return operationLocation;
	}

	public void setOperationLocation(String operationLocation) {
		this.operationLocation = operationLocation;
	}
	
}
