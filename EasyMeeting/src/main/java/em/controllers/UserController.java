package em.controllers;

import em.models.User;
import em.result.HttpError;
import em.result.HttpSuccess;
import em.result.Result;
import em.services.ProfileService;
import em.services.UserService;
import em.daos.UserDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.filters.RequestFilter;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@RestController 
@RequestMapping("/user")
public class UserController {
	
    @RequestMapping("/sessioncheck")
	@ResponseBody
	public Result sessionCheck(){	  
	    Result result;
		try{
		    result = userService.sessionCheck();
		    if(result.getType().equals("error")){
		        System.out.println(((HttpError)result).getTip());
		    }
		    return result;
		}
		catch (Exception ex){
		    System.out.println("the server has error in UserController sessioncheck");
			HttpError httpError = new HttpError("error", 500, "the server has error in UserController sessioncheck", "");
			return httpError;
		}
    }
    
    @RequestMapping("/voicecheck")
	@ResponseBody
	public Result voiceCheck(){	  
	    Result result;
		try{
		    result = userService.voiceCheck();
		    if(result.getType().equals("error")){
		        System.out.println(((HttpError)result).getTip());
		    }
		    return result;
		}
		catch (Exception ex){
		    System.out.println("the server has error in UserController voicecheck");
			HttpError httpError = new HttpError("error", 500, "the server has error in UserController voicecheck", "");
			return httpError;
		}
    }		
    
    @RequestMapping("/updatedeviceid")
	@ResponseBody
	public Result updateDeviceId(@RequestParam("deviceId") String deviceId){	  
	    Result result;
		try{
		    result = userService.updateDeviceId(deviceId);
		    if(result.getType().equals("error")){
		        System.out.println(((HttpError)result).getTip());
		    }
		    return result;
		}
		catch (Exception ex){
		    System.out.println("the server has error in UserController updateDeviceId");
			HttpError httpError = new HttpError("error", 500, "the server has error in UserController updateDeviceId", "");
			return httpError;
		}
    }	
	
  @RequestMapping("/login")
  @ResponseBody
  public Result login(@RequestParam("userName") String userName, @RequestParam("userPwd") String userPwd,
		              @RequestParam("deviceId") String deviceId){	  
    Result loginResult;
    Result updateDeviceIdResult;
	try{
	    loginResult = userService.login(userName, userPwd);
	    if(loginResult.getType().equals("error")){
	        System.out.println(((HttpError)loginResult).getTip());
	        return loginResult;
	    }
	    updateDeviceIdResult = userService.updateDeviceId(deviceId);
	    if(updateDeviceIdResult.getType().equals("error")){
	        System.out.println(((HttpError)updateDeviceIdResult).getTip());
	        return updateDeviceIdResult;
	    }
	    return loginResult;
	}
	catch (Exception ex){
		System.out.println("the server has error in UserController login");
		HttpError httpError = new HttpError("error", 500, "the server has error in UserController login", "");
		return httpError;	
	}
  }	
  
  @RequestMapping("/logout")
  @ResponseBody
  public Result logout(){	  
	try{
	    request.getSession().invalidate();   
	    HttpSuccess httpSuccess = new HttpSuccess("success", 200);
		return httpSuccess;
	}
	catch (Exception ex){
		System.out.println("the server has error in UserController login");
		HttpError httpError = new HttpError("error", 500, "the server has error in UserController login", "");
		return httpError;	
	}
  }	
	
  @RequestMapping("/create")
  @ResponseBody
  public Result create(@RequestParam("userName") String userName,@RequestParam("userPwd") String userPwd){	  
      Result createUserResult;
      Result createProfileResult;
      try{
    	  createUserResult = userService.create(userName, userPwd);
  	      if(createUserResult.getType().equals("error")){
    	      System.out.println(((HttpError)createUserResult).getTip());
    	      return createUserResult; 
	      }
  	      
  	      createProfileResult = profileService.createProfileId(userName);
	      if(createProfileResult.getType().equals("error")){
  	          System.out.println(((HttpError)createProfileResult).getTip());
  	          return createProfileResult;
	      }
          return createUserResult;
      }
      catch (Exception ex){
    	  System.out.println("the server has error in UserControllr create");
    	  HttpError httpError = new HttpError("error", 500, "the server has error in UserControllr create", "");
    	  return httpError;
      }
  }
  
  @RequestMapping("/findinfo")
  @ResponseBody
  public Result findinfo(){	  
      Result result;
      try{
          result = userService.findInfo();
  	      if(result.getType().equals("error")){
    	      System.out.println(((HttpError)result).getTip());
	      }
          return result;
      }
      catch (Exception ex){
    	  System.out.println("the server has error in UserControllr findinfo");
    	  HttpError httpError = new HttpError("error", 500, "the server has error in UserControllr findinfo", "");
    	  return httpError;
      }
  }
  
  @RequestMapping("/modify")
  @ResponseBody
  public Result modify(@RequestParam("userName") String userName,@RequestParam("userPwd") String userPwd){	  
      Result result;
      try{
          result = userService.modify(userName, userPwd);
          if(result.getType().equals("error")){
    	      System.out.println(((HttpError)result).getTip());
	      }
          return result;
      }
      catch (Exception ex){
    	  System.out.println("the server has error in UserControllr modify");
    	  HttpError httpError = new HttpError("error", 500, "the server has error in UserControllr modify", "");
    	  return httpError;
      }
  }
  
  @RequestMapping("/findbyusernamelike")
  @ResponseBody
  public Result modify(@RequestParam("userName") String userName){	  
      Result result;
      try{
          result = userService.findByUserNameLike(userName);
          if(result.getType().equals("error")){
    	      System.out.println(((HttpError)result).getTip());
	      }
          return result;
      }
      catch (Exception ex){
    	  System.out.println("the server has error in UserControllr findbyusername");
    	  HttpError httpError = new HttpError("error", 500, "the server has error in UserControllr findbyusername", "");
    	  return httpError;
      }
  }
 

  @RequestMapping("/delete/{userId}")
  @ResponseBody
  public String delete(@PathVariable("userId") int userId){
    try {
      User user = new User(userId);
      userDao.delete(user);
    }
    catch (Exception ex) {
      return "Error deleting the user: " + ex.toString();
    }
    return "User succesfully deleted!";
  }
  
  @RequestMapping("/getallusers")
  @ResponseBody
  public List<User> getAllUsers() {
	List<User> users = new ArrayList<User>();
    try {
      users = userDao.findAll();
    }
    catch (Exception ex) {
      return users;
    }
    return users;
  }
  
  @RequestMapping("/saveaudio")
  @ResponseBody
  public String saveAudio(){
	try{
		 DB db = mongoTemplate.getDb();
         GridFS gridFS = new GridFS(db, "test");
         gridFS.remove("wangjiahui");
         
         /* save into mongodb */
         File ff = new File("wangjiahui.wav");
         GridFSInputFile gfs = gridFS.createFile(ff);
         gfs.put("userName", "wangjiahui");
         gfs.put("contentType", "wav");
         gfs.put("filename", "wangjiahui");
         gfs.save();
         
	}
    catch (Exception ex) {
      return "something wrong";
    }
    return "save success";
  }
  
  @RequestMapping("/downloadaudio")
  @ResponseBody
  public String downloadAudio(){
	  //List<GridFSDBFile> gdbfs = new ArrayList<GridFSDBFile>();
	  try{
		  /*gdbfs= gridFsTemplate.find(new Query(Criteria.where("userName").is("wangjiahui")));
		  System.out.println("2");
		  File test = new File("aaa.txt");
		  gdbfs.get(0).writeTo(test);
		  */
		  DB db = mongoTemplate.getDb();
          GridFS gridFS = new GridFS(db, "test");
          GridFSDBFile dbfile = gridFS.findOne("wangjiahui");
		  dbfile.writeTo(new File("wangjiahui123.wav"));
	  }
	  catch (Exception ex) {
	      return "something wrong";
	    }
	  return "download success";
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private UserDao userDao;
  
  @Autowired
  private ProfileService profileService;
  
  //@Autowired
  //private GridFsTemplate gridFsTemplate;
  
  @Autowired  
  private HttpServletRequest request;
  
  @Autowired
  private MongoTemplate mongoTemplate;

  
} // class UserController