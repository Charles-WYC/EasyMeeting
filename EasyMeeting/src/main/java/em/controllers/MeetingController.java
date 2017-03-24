package em.controllers;

import em.models.Meeting;
import em.models.User;
import em.result.HttpError;
import em.result.Result;
import em.services.MeetingService;
import em.services.UserService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.filters.RequestFilter;
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


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@RestController 
@RequestMapping("/meeting")
public class MeetingController{
	
	@Autowired
	MeetingService meetingService;
	
    @RequestMapping("/create")
    @ResponseBody
    public Result create(@RequestParam("meetingName") String meetingName){	  
        Result result;
	    try{
	        result = meetingService.create(meetingName);
	  	    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
	        return result;
	    }
	    catch(Exception ex){
	        System.out.println("the server has error in MeetingController create");
	        HttpError httpError = new HttpError("error", 500, "the server has error in MeetingController create", "");
			return httpError;
	    }
    }
	    
	@RequestMapping("/findmymeetings")
	@ResponseBody
	public Result findMyMeetings(){	  
		Result result;
		try{
		    result = meetingService.findMyMeetings();
	  	    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    return result;
		}
		catch(Exception ex){
	        System.out.println("the server has error in MeetingController findmymeetings");
	        HttpError httpError = new HttpError("error", 500, "the server has error in MeetingController findmymeetings", "");
			return httpError;
		}	
	}
	
	@RequestMapping("/addparticipant")
	@ResponseBody
	public Result addParticipant(@RequestParam("meetingName") String meetingName, @RequestParam("userNames") List<String> userNames){	  
		Result result;
		try{
		    result = meetingService.addParticipant(meetingName, userNames);
	  	    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    return result;
		}
		catch(Exception ex){
	        System.out.println("the server has error in MeetingController addparticipant");
	        HttpError httpError = new HttpError("error", 500, "the server has error in MeetingController addparticipant", "");
			return httpError;
		}	
	}
	
	@RequestMapping("/deleteparticipant")
	@ResponseBody
	public Result deleteParticipant(@RequestParam("meetingName") String meetingName, @RequestParam("userNames") List<String> userNames){	  
		Result result;
		try{
		    result = meetingService.deleteParticipant(meetingName, userNames);
	  	    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    return result;
		}
		catch(Exception ex){
	        System.out.println("the server has error in MeetingController delete participant");
	        HttpError httpError = new HttpError("error", 500, "the server has error in MeetingController delete participant", "");
			return httpError;
		}	
	}
	
	@RequestMapping("/findfriendsnotinmeeting")
	@ResponseBody
	public Result findFriendsNotInMeeting(@RequestParam("meetingName") String meetingName){	  
		Result result;
		try{
		    result = meetingService.findFriendsNotInMeeting(meetingName);
	  	    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    return result;
		}
		catch(Exception ex){
	        System.out.println("the server has error in MeetingController findFriendsNotInMeeting");
	        HttpError httpError = new HttpError("error", 500, "the server has error in MeetingController findFriendsNotInMeeting", "");
			return httpError;
		}	
	}
	
	
	
}