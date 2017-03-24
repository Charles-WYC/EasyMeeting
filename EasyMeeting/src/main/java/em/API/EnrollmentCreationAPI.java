/*
 * FileName - EnrollmentCreationAPI.java
 * Purpose - create enrollment for a profile
 * Author - Yunchao Wang
 * Last Edit - 2016/7/13
 */
package em.API;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import em.result.EmResult;
import em.result.HttpSuccess;
import em.support.APIHelper;
import em.support.LoggerHelper;
import em.support.TimingKillThread;

/*
 * EnrollmentCreationAPI - Class
 * Purpose - Provide a API to create enrollment form Microsoft API
 * Tip - NULL
 */
public class EnrollmentCreationAPI extends Thread{
	/* ID for enrollment profile*/
	private String identificationProfileId;
	
	/* Audio file path for enrollment*/
	private String filePath;

	/* Subscription key for using Microsoft API*/
	private static String subscriptionKey = "21d0e251d8e146f0a93ae96888024f66";
	
    /*
     * EnrollmentCreationAPI - Constructor
	 * Purpose - For null parameter construction
	 * Parameter - NULL
	 * Tip - NULL
     */
	public EnrollmentCreationAPI(){
	}
	
    /*
     * EnrollmentCreationAPI - Constructor
	 * Purpose - For two parameters construction
	 * Parameter - filePath:String - Audio file path for enrollment
	 * 			 - identificationProfileId:String - ID for enrollment profile
	 * Tip - NULL
     */
	public EnrollmentCreationAPI(String filePath, String identificationProfileId){
		this.filePath = filePath;
		this.identificationProfileId = identificationProfileId;
	}
	
    /*
     * run - Method Overload@java.lang.Thread#run()
     * Purpose - Send request to Microsoft API for create enrollment
     * Parameter - NULL
     * Return - NULL
     * Tip - the result of request will be written in APIHelper.ResultMap
     * 	   - result class - HttpSuccess or HttpError or InternalError
     *     - result type for HttpSuccess - BeginEnrollmentSuccesss
     *     - result type for HttpError - httpError
     *     - result type for InternalError - unknowError
     *	   - result parameter for HttpSuccess - firstParameter - operationLocation:String
     *	    								  - secondParameter - NULL
     */
	public void run(){
		LoggerHelper.logger.info("EnrollmentCreationAPI-"+ identificationProfileId+ "-begin");

		/* create file stream*/
        FileInputStream audioInputStream = null;
        
		/* create success result*/
    	HttpSuccess result = new HttpSuccess("BeginEnrollmentSuccess", 200);
        try
        {    	
        	/* create timing thread for 30s*/
    		LoggerHelper.logger.info("EnrollmentCreationAPI-"+ identificationProfileId+ "-CreateTimingThread");
    		TimingKillThread thread = new TimingKillThread(this, 30000, APIHelper.ResultMap);
    		thread.start();
    		
    		/* initialize file stream*/
        	int bufferSize = 1<<19;
            byte[] tempBytes = new byte[bufferSize];
            int byteRead = 0;
        	File audio = new File(filePath);
        	audioInputStream = new FileInputStream(audio);

        	/* create connection*/
    		String requestUri = "https://api.projectoxford.ai/spid/v1.0/identificationProfiles/";
    		requestUri += identificationProfileId;
            requestUri += "/enroll";
        	URL url = new URL(requestUri);
        	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        	connection.setDoInput(true);
        	connection.setDoOutput(true);
        	connection.setUseCaches(false);
        	connection.setRequestMethod("POST");
        	connection.setRequestProperty("Content-Type", "multipart/form-data");
        	connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
        	connection.connect();
        	
        	/* write audio*/
            BufferedOutputStream out=new BufferedOutputStream(connection.getOutputStream());
        	while((byteRead = audioInputStream.read(tempBytes,0,bufferSize)) > 0){
        		out.write(tempBytes, 0, byteRead);
        	}
        	audioInputStream.close();
        	out.flush();
        	out.close();
        	
        	/* get responseCode*/
        	int responseCode = connection.getResponseCode();

        	/* cancel timing thread*/
    		LoggerHelper.logger.info("EnrollmentCreationAPI-GetResponseSuccess-CancelTimingThread");
            thread.setNeedRemove(false);

    		LoggerHelper.logger.info("EnrollmentCreationAPI-"+ identificationProfileId+ "-ResponseCode-"+responseCode);

    		/* write error result into APIHelper.ResultMap*/
            if(responseCode == 500){
            	if(APIHelper.ResultMap.containsKey(this.getId()))
            	{
            		APIHelper.ResultMap.replace(this.getId(),EmResult.CreateEnrollmentInternalServerError);
            	}
            	return;
            }
            else if(responseCode == 400){
            	if(APIHelper.ResultMap.containsKey(this.getId()))
            	{
            		APIHelper.ResultMap.replace(this.getId(),EmResult.CreateEnrollmentBadRequest);
            	}
            	return;
            }

    		/* write success result into APIHelper.ResultMap*/
            System.out.println("1111111");
            System.out.println(APIHelper.ResultMap.containsKey(this.getId()));
            System.out.println(this.getId());
            result.setFirstParameter(connection.getHeaderField("Operation-Location"));
        	if(APIHelper.ResultMap.containsKey(this.getId()))
        	{
        		System.out.println("2222222");
        		APIHelper.ResultMap.replace(this.getId(), result);
        	}
        	
        } catch (Exception e1) {
			e1.printStackTrace();
    		/* write unknowError result into APIHelper.ResultMap*/
        	if(APIHelper.ResultMap.containsKey(this.getId()))
        	{
        		APIHelper.ResultMap.replace(this.getId(),EmResult.UnknowError);
        	}
			return;
		} finally {
			/* close audioInputStream if not been closed*/
			if (audioInputStream != null) {
				try {
					audioInputStream.close();
				} catch (IOException e1) {
		        	e1.printStackTrace();
		        	
		    		/* write close file error result into APIHelper.ResultMap*/
		        	if(APIHelper.ResultMap.containsKey(this.getId()))
		        	{
		        		APIHelper.ResultMap.replace(this.getId(),EmResult.CloseFileError);
		        	}
					return;
				}
			}
		}
        return;
	}
}
