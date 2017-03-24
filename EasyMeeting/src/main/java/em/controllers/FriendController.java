package em.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import em.result.HttpError;
import em.result.Result;
import em.services.FriendService;


@RestController 
@RequestMapping("/friend")
public class FriendController{
	
	@Autowired
	FriendService friendService;
	
	@RequestMapping("/findmyfriends")
	@ResponseBody
	public Result findMyFriends(){	  
		Result result;
		try{
		    result = friendService.findMyFriends();
	  	    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    return result;
		}
		catch(Exception ex){
	        System.out.println("the server has error in FriendController findmyfriends");
	        HttpError httpError = new HttpError("error", 500, "the server has error in FriendController findmyfriends", "");
			return httpError;
		}	
	}
	
	
	@RequestMapping("/create")
	@ResponseBody
	public Result create(@RequestParam("userName") String userName){	  
	    Result result;
	    try{
	        result = friendService.create(userName);
	  	    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
	        return result;
	    }
	    catch(Exception ex){
	    	System.out.println("the server has error in FriendController create");
	    	HttpError httpError = new HttpError("error", 500, "the server has error in FriendController create", "");
	    	return httpError;
	    }
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(@RequestParam("userName") String userName){	  
	    Result result;
	    try{
	        result = friendService.delete(userName);
	  	    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
	        return result;
	    }
	    catch(Exception ex){
	    	System.out.println("the server has error in FriendController delete");
	    	HttpError httpError = new HttpError("error", 500, "the server has error in FriendController delete", "");
	    	return httpError;
	    }
	}
	
}
