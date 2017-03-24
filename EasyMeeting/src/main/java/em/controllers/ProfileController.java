package em.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import em.result.HttpError;
import em.result.Result;
import em.services.ProfileService;

@RestController 
@RequestMapping("/profile")
public class ProfileController{
	
	  @Autowired
	  ProfileService profileService;
	
	  @RequestMapping("/deleteprofileid")
	  @ResponseBody
	  public Result deleteProfileId(){	  
	      Result result;
	      try{
	          result = profileService.deleteProfileId();
	  	      if(result.getType().equals("error")){
	    	      System.out.println(((HttpError)result).getTip());
		      }
	          return result;
	      }
	      catch (Exception ex){
	    	  System.out.println("the server has error in ProfileControllr deleteProfileId");
	    	  HttpError httpError = new HttpError("error", 500, "the server has error in ProfilleControllr deleteProfileId", "");
	    	  return httpError;
	      }
	  }
	  
	  @RequestMapping(value = "/createnrollment", method = RequestMethod.POST)
	  @ResponseBody
	  public Result creatEnrollment(@RequestParam("voice") MultipartFile voice){	  
	      Result result;
	      try{
	          result = profileService.creatEnrollment(voice);
	  	      if(result.getType().equals("error")){
	    	      System.out.println(((HttpError)result).getTip());
		      }
	          return result;
	      }
	      catch (Exception ex){
	    	  System.out.println("the server has error in ProfileControllr creatEnrollment");
	    	  HttpError httpError = new HttpError("error", 500, "the server has error in ProfilleControllr creatEnrollment", "");
	    	  return httpError;
	      }
	  }
	  
	  @RequestMapping("/resetenrollment")
	  @ResponseBody
	  public Result resetEnrollment(){	  
	      Result result;
	      try{
	          result = profileService.resetEnrollment();
	  	      if(result.getType().equals("error")){
	    	      System.out.println(((HttpError)result).getTip());
		      }
	          return result;
	      }
	      catch (Exception ex){
	    	  System.out.println("the server has error in ProfileControllr resetEnrollment");
	    	  HttpError httpError = new HttpError("error", 500, "the server has error in ProfilleControllr resetEnrollment", "");
	    	  return httpError;
	      }
	  }
	

}
