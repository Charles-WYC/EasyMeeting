package em.API;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import em.result.EmResult;
import em.result.HttpSuccess;
import em.services.SpeakerRecognitionService;
import em.support.TimingKillThread;

public class SpeakerRecognitionAPI extends Thread{
	private String filePath;
	private ArrayList<String> identificationProfileIds;
	private static String subscriptionKey = "21d0e251d8e146f0a93ae96888024f66";
	
	public SpeakerRecognitionAPI(){
	}
	
	public SpeakerRecognitionAPI(String filePath, ArrayList<String> identificationProfileIds){
		this.filePath = filePath;
		this.identificationProfileIds = identificationProfileIds;
	}

	public void run(){
		System.out.println("before create timer");
		System.out.println("analyze thread: "+this.getId());
		TimingKillThread thread = new TimingKillThread(this, 60000, SpeakerRecognitionService.speakerRecognitionResultMap);
		thread.start();
		
		int bufferSize = 1<<19;

		String requestUri = "https://api.projectoxford.ai/spid/v1.0/identify?identificationProfileIds=";
		if(identificationProfileIds.size() == 0){
        	if(SpeakerRecognitionService.speakerRecognitionResultMap.containsKey(this.getId()))
        	{
        		SpeakerRecognitionService.speakerRecognitionResultMap.replace(this.getId(),EmResult.NoIdentificationProfileIdError);
        	}
        	return;
		}
		requestUri += identificationProfileIds.get(0);
		for(int i = 1; i<identificationProfileIds.size(); i++){
			requestUri +=(","+identificationProfileIds.get(i));
		}
		HttpSuccess result = new HttpSuccess("BeginSpeakerRecognitionSuccess", 200);
		
        byte[] tempBytes = new byte[bufferSize];
        int byteRead = 0;
        FileInputStream audioInputStream = null;
        try
        {
        	File audio = new File(filePath);
        	audioInputStream = new FileInputStream(audio);

    		System.out.println(requestUri);
    		
        	URL url = new URL(requestUri);
        	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        	connection.setDoInput(true);
        	connection.setDoOutput(true);
        	connection.setUseCaches(false);
        	connection.setRequestMethod("POST");
        	connection.setRequestProperty("Content-Type", "application/octet-stream");
        	connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
        	connection.connect();
            BufferedOutputStream out=new BufferedOutputStream(connection.getOutputStream());

    		System.out.println("4");
    		
        	while((byteRead = audioInputStream.read(tempBytes,0,bufferSize)) > 0){
        		out.write(tempBytes, 0, byteRead);
        	}

        	audioInputStream.close();
        	out.flush();
        	out.close();

    		System.out.println("5");
    		
    		int responseCode = connection.getResponseCode();

            thread.setNeedRemove(false);
            
            if(responseCode == 500){
            	System.out.println("5.1");
            	if(SpeakerRecognitionService.speakerRecognitionResultMap.containsKey(this.getId()))
            	{
            		SpeakerRecognitionService.speakerRecognitionResultMap.replace(this.getId(),EmResult.SpeakerRecognitionInternalServerError);
            	}
            	return;
            }
            else if(responseCode == 400){
            	System.out.println("5.2");
            	if(SpeakerRecognitionService.speakerRecognitionResultMap.containsKey(this.getId()))
            	{
            		SpeakerRecognitionService.speakerRecognitionResultMap.replace(this.getId(),EmResult.SpeakerRecognitionBadRequest);
            	}
            	return;
            }

    		System.out.println("6");
    		
            result.setFirstParameter(connection.getHeaderField("Operation-Location"));
        } catch (Exception e1) {
			e1.printStackTrace();
        	if(SpeakerRecognitionService.speakerRecognitionResultMap.containsKey(this.getId()))
        	{
        		SpeakerRecognitionService.speakerRecognitionResultMap.replace(this.getId(),EmResult.UnknowError);
        	}
			return;
		} finally {
			if (audioInputStream != null) {
				try {
					audioInputStream.close();
				} catch (IOException e1) {
		        	e1.printStackTrace();
		        	if(SpeakerRecognitionService.speakerRecognitionResultMap.containsKey(this.getId()))
		        	{
		        		SpeakerRecognitionService.speakerRecognitionResultMap.replace(this.getId(),EmResult.CloseFileError);
		        	}
					return;
				}
			}
		}
    	if(SpeakerRecognitionService.speakerRecognitionResultMap.containsKey(this.getId()))
    	{
    		SpeakerRecognitionService.speakerRecognitionResultMap.replace(this.getId(), result);
    	}
        return;
	}
}
