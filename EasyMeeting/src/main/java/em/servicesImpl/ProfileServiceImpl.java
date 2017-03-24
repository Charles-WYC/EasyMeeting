package em.servicesImpl;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import em.controllers.AudioController.TranslateThread;
import em.daos.UserDao;
import em.models.Profile;
import em.models.User;
import em.result.HttpError;
import em.result.HttpSuccess;
import em.result.Result;
import em.services.ProfileService;
import em.support.*;

@Service
public class ProfileServiceImpl implements ProfileService{
	
	@Autowired
	UserDao userDao;
	
	@Autowired  
    HttpServletRequest request;
	
	public Result createProfileId(String userName){
		int userId;
		Profile profile = new Profile();
		try{
		    /* get userId */
			System.out.println("createProfileId");
			userId = userDao.findByUserName(userName).getUserId();
			System.out.println(userId);
			
			/* create identity Id for user */
			Result result = profile.create();
			System.out.println("result type : " + result.getType());
			System.out.println("IdentificationProfileId : " + profile.getIdentificationProfileId());
			if(!result.getType().equals("httpSuccess")){
				return result;
			}
			
			/* update voiceId in database */
			User user = userDao.findOne(userId);
			user.setVoiceId(profile.getIdentificationProfileId());
			userDao.save(user);
			
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200);
		    return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 400, "the server has error in create profileId", "the server has error in profileService create profileId");
			return httpError;
	    }
	}
	
	public Result deleteProfileId(){
		int userId;
		Profile profile = new Profile();
		User user = new User();
		String profileId;
		try{
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* find user profile id */
			user = userDao.findOne(userId);
			profileId = user.getVoiceId();
			profile.setIdentificationProfileId(profileId);
			
			/* delete identity Id for user */
			Result result = profile.delete();
			if(!result.getType().equals("httpSuccess")){
				return result;
			}
			
			/* update profileId in database */
			User tempUser = userDao.findOne(userId);
			tempUser.setVoiceId(null);
			userDao.save(tempUser);
			
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200);
		    return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 400, "the user hasn't registered voice", "the client tend to delete profileId that unexisted");
			return httpError;
	    }
	}
	
	public Result creatEnrollment(MultipartFile voice){
		int userId;
		String profileId;
		Profile profile = new Profile();
		User user = new User();
		try{
			 /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* find user profile id */
			user = userDao.findOne(userId);
			profileId = user.getVoiceId();
			profile.setIdentificationProfileId(profileId);
			
			/* write file to disk */
			String voiceFilePath = "VoiceUserId" + String.valueOf(userId) + ".wav";
			File f = new File(voiceFilePath);
            InputStream is = voice.getInputStream();
            FileOutputStream fos = new FileOutputStream(voiceFilePath);
            byte[] b = new byte[1024];
            while((is.read(b)) != -1){
                 fos.write(b);
            }
            is.close();
            fos.close();
			
			/* voice enrollment */
			Result result = profile.createEnrollment(voiceFilePath);
			if(!result.getType().equals("httpSuccess")){
				System.out.println("create enrollment error");
				return result;
			}	
		    
			/* update user's uploadVoice */
			User tempUser = userDao.findOne(userId);
			tempUser.setUploadVoice(1);
			userDao.save(tempUser);
			
			/* create a thread to check voice status */
			HttpSuccess httpSuccess = (HttpSuccess)result;
	    	CheckEnrollmentStatusThread checkEnrollmentStatusThread = new CheckEnrollmentStatusThread((String)httpSuccess.getFirstParameter(), 
	    			                                                                                  user.getDeviceId(), user.getUserId());
	    	checkEnrollmentStatusThread.start();
			
			
			HttpSuccess success = new HttpSuccess("success", 200);
		    return success;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 400, "the server has error in creatEnrollment", "the server has error in creatEnrollment");
			return httpError;
	    }
	}
	
	public Result resetEnrollment(){
		int userId;
		String profileId;
		Profile profile = new Profile();
		User user = new User();
		try{
			 /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* find user profile id */
			user = userDao.findOne(userId);
			profileId = user.getVoiceId();
			profile.setIdentificationProfileId(profileId);			
			
			/* reset voice enrollment */
			Result result = profile.ResetEnrollment();
			if(!result.getType().equals("httpSuccess")){
				System.out.println("create enrollment error");
				return result;
			}	
		    
			HttpSuccess httpSuccess = new HttpSuccess("success", 200);
		    return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 400, "the server has error in resetEnrollment", "the server has error in resetEnrollment");
			return httpError;
	    }
	}

	
}
