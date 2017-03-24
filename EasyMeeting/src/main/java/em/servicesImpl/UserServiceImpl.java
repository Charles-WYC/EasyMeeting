package em.servicesImpl;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import em.daos.UserDao;
import em.models.User;
import em.result.HttpError;
import em.result.HttpSuccess;
import em.result.Result;
import em.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	
	@Autowired  
    HttpServletRequest request;
	
	public Result sessionCheck(){
		int userId;
		try{
		    userId = (int)request.getSession().getAttribute("userId");
		    System.out.println("userId : " + userId);
		    if(userId == 0){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
		    }
		    else{
		    	User user = userDao.findByUserId(userId);
		    	if(user.getVoiceId() == null){
		    	    HttpSuccess httpSuccess = new HttpSuccess("success", 200, "false");
				    return httpSuccess;
		    	}
		    	else{
		    		HttpSuccess httpSuccess = new HttpSuccess("success", 200, "false");
			        return httpSuccess;
		    	}
		    }
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 401, "the server has error in session check", "the server has error in UserService sessioncheck");
			return httpError;
	    }
	}
	
	public Result login(String userName, String userPwd){
	    User user = null;
	    String sessionId = "";
		try{
		   user = userDao.findByUserNameAndUserPwd(userName, userPwd);
		   if(user == null){
			   HttpError httpError = new HttpError("error", 401, "username/password error", "the client sent wrong username/password");
			   return httpError;
		   }
		   else{
			   sessionId = request.getSession().getId();
			   request.getSession().setAttribute("userId", user.getUserId());
			   System.out.println("userName : " + user.getUserName());
			   System.out.println("userPwd : " + user.getUserPwd());
			   System.out.println("sessionId : " + sessionId);
			   
			   HttpSuccess httpSuccess = new HttpSuccess("success", 200, new Integer(user.getUserId()));
			   return httpSuccess;
		   }
		}
		catch(Exception ex){
	        HttpError httpError = new HttpError("error", 401, "the server has error in login", "the server has error in UserService login");
			return httpError;
		}
	}
	
	public Result create(String userName, String userPwd){
        User user = null;
		try{
		    user = new User(userName, userPwd);
		    user = userDao.save(user);
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200);
		    return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 401, "the server has error in create user", "the server has has error in UserService create");
		    return httpError;
		}
	}
	
	public Result modify(String userName, String userPwd){
		int userId;
		try{
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* set new user information*/
		    User user = userDao.findOne(userId);		    
		    user.setUserName(userName);
		    user.setUserPwd(userPwd);
		    userDao.save(user);
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200);
		    return httpSuccess;
		}	 
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in modify user information", "the server has error in UserService modify");
		    return httpError;
		}
	}
	
	public Result findInfo(){
		User user = null;
		int userId;
		try{
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			System.out.println(userId);
			
			/* get user information */
		    user = userDao.findByUserId(userId);
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200, new User(user));
		    return httpSuccess;
		}	 
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in find user information", "the server has error in UserService findInfo");
		    return httpError;
		}
	}
	
	public Result findByUserNameLike(String userName){
		int userId;
		List<User> users = new ArrayList<User>();
		try{
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			System.out.println(userId);
			
			/* if the userName is null */
			if(userName.equals("")){
			    HttpSuccess httpSuccess = new HttpSuccess("success", 200, users);
			    return httpSuccess;
			}		
			/* find users */
		    users = userDao.findByUserNameLike("%" + userName + "%");
		    
		    /* delete the customer himself */
		    for(int i = 0; i < users.size(); ++i){
		    	if(users.get(i).getUserId() == userId){
		    		users.remove(i);
		    	}
		    }
		    
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200, users);
		    return httpSuccess;
		}	 
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in find users", "the server has error in UserService findByUserName");
		    return httpError;
		}
	}
	
	public Result findByVoiceId(String profileId){
	    User user = new User();
		try{	
			/* find users */
		    user = userDao.findByVoiceId(profileId);
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200, user);
		    return httpSuccess;
		}	 
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in find user by profileId", "the server has error in UserService findByVoiceId");
		    return httpError;
		}
	}
	
	public Result voiceCheck(){
		User user = null;
		int userId;
		try{
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* check user voice register */
		    user = userDao.findByUserId(userId);
		    if(user.getUploadVoice() == 0){
		        HttpSuccess httpSuccess = new HttpSuccess("success", 200, new String("haven't upload voice"));
		        return httpSuccess;
		    }
		    else{
		    	if(user.getVoiceStatus() == 0){
		            HttpSuccess httpSuccess = new HttpSuccess("success", 200, new String("enrollment still building"));
		            return httpSuccess;
		    	}
		    	else{
		    		HttpSuccess httpSuccess = new HttpSuccess("success", 200, new String("voice register success"));
		            return httpSuccess;
		    	}
		    }
		}	 
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in voicecheck", "the server has error in UserService voiceCheck");
		    return httpError;
		}
	}
	
	public Result updateVoiceStatus(){
		int userId;
		try{
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* update voice status */
		    User user = userDao.findOne(userId);
		    user.setVoiceStatus(1);
		    userDao.save(user);
			
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200);
	        return httpSuccess;
		}	 
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in updateVoiceStatusr", "the server has error in UserService updateVoiceStatus");
		    return httpError;
		}
	}
	
	public Result updateDeviceId(String deviceId){
		int userId;
		try{
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* update voice status */
		    User user = userDao.findOne(userId);
		    user.setDeviceId(deviceId);
		    userDao.save(user);
			
		    HttpSuccess httpSuccess = new HttpSuccess("success", 200);
	        return httpSuccess;
		}	 
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in update current device id", "the server has error in UserService updateDeviceId");
		    return httpError;
		}
	}
	
	
}
