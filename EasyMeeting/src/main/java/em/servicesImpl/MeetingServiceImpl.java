package em.servicesImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import em.daos.FriendDao;
import em.daos.MeetingDao;
import em.daos.ParticipantDao;
import em.daos.UserDao;
import em.models.Friend;
import em.models.Meeting;
import em.models.Participant;
import em.models.User;
import em.result.HttpError;
import em.result.HttpSuccess;
import em.result.Result;
import em.services.MeetingService;

@Service
public class MeetingServiceImpl implements MeetingService{
    
	@Autowired
	MeetingDao meetingDao;
	
	@Autowired  
    HttpServletRequest request;
	
	@Autowired
	ParticipantDao participantDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	FriendDao friendDao;
	
	public Result create(String meetingName){
		Meeting meeting = new Meeting();
		String createDate;
		int userId;
		try{
			/* get current date */
			Date date = new Date();
		    SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd");
		    createDate = matter.format(date);
		    
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* create a new meeting */
			meeting.setCreateDate(createDate);
			meeting.setMeetingName(meetingName);
			meeting.setUserId(userId);
			
			/* save meeting */
			meetingDao.save(meeting);
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 400, "repeatable meeting name", "the client tend to create meeting whose name has been existed");
			return httpError;
	    }
	}
	
	public Result findMyMeetings(){
		List<Meeting> meetings = new ArrayList<Meeting>();
		int userId;
		try{		    
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
				
			/* find meetings */
			meetings = meetingDao.findByUserId(userId);
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, meetings);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in find meetings", "the server has error in MeetingService findMyMeetings");
			return httpError;
	    }
	}
	
	public Result addParticipant(String meetingName, List<String> userNames){
		Meeting meeting = new Meeting();
		int userId;
		int meetingId;
		try{		
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* find meetings */
			meeting = meetingDao.findByUserIdAndMeetingName(userId, meetingName);
			meetingId = meeting.getMeetingId();
			
			/* save (user,meeting) pair */
			for(int i = 0; i < userNames.size(); ++i){
				int parId = userDao.findByUserName(userNames.get(i)).getUserId();
				Participant participant = new Participant();
				participant.setMeetingId(meetingId);
				participant.setUserId(userId);
				participantDao.save(participant);		
			}
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in find meetings", "the server has error in MeetingService findMyMeetings");
			return httpError;
	    }
	}
	
	public Result deleteParticipant(String meetingName, List<String> userNames){
		Meeting meeting = new Meeting();
		int userId;
		int meetingId;
		List<User> users = new ArrayList<User>();
		try{		
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* find meetings */
			meeting = meetingDao.findByUserIdAndMeetingName(userId, meetingName);
			meetingId = meeting.getMeetingId();
			
			/* get users to delete */
			for(int i = 0; i < userNames.size(); ++i){
			    users.add(userDao.findByUserName(userNames.get(i)));
			}
			
			/* delete in mysql database */
			Participant participant = participantDao.findByMeetingIdAndUserId(meetingId, userId);
			participantDao.delete(participant);
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in delete participant", "the server has error in delete participant");
			return httpError;
	    }
	}
	
	public Result findAllParticipants(String meetingName){
		Meeting meeting = new Meeting();
		List<Participant> parts = new ArrayList<Participant>();
		List<User> participants = new ArrayList<User>();
		int userId;
		int meetingId;
		try{		
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* find meetings */
			meeting = meetingDao.findByUserIdAndMeetingName(userId, meetingName);
			meetingId = meeting.getMeetingId();
			
			/* find all participants */
			parts = participantDao.findByMeetingId(meetingId);
			for(int i = 0; i < parts.size(); ++i){
				User participantUser = userDao.findByUserId(parts.get(i).getUserId());
				participants.add(participantUser);
			}
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, participants);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in find All Participants", "the server has error in MeetingService findAllParticipants");
			return httpError;
	    }
	}
	
	public Result findFriendsNotInMeeting(String meetingName){
		Meeting meeting = new Meeting();
		List<Participant> parts = new ArrayList<Participant>();
		List<User> participants = new ArrayList<User>();
		List<Friend> friends = new ArrayList<Friend>();
		List<User> friendUsers = new ArrayList<User>();
		List<User> findResult = new ArrayList<User>();
		int userId;
		int meetingId;
		try{		
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* find meetings */
			meeting = meetingDao.findByUserIdAndMeetingName(userId, meetingName);
			meetingId = meeting.getMeetingId();
			
			/* find all participants */
			parts = participantDao.findByMeetingId(meetingId);
			for(int i = 0; i < parts.size(); ++i){
				User participantUser = userDao.findByUserId(parts.get(i).getUserId());
				participants.add(participantUser);
			}
			
			/* find all friends */
			friends = friendDao.findByUserId(userId);
			for(int i = 0; i < friends.size(); ++i){
				User friend = userDao.findByUserId(friends.get(i).getUserId());
				friendUsers.add(friend);
			}
			
			/* get all the friends that not in the meeting */
			boolean has_existed = false;
			for(int i = 0; i < friendUsers.size(); ++i){
				for(int j = 0; j < participants.size(); ++j){
					if(friendUsers.get(i) == participants.get(j)){
						has_existed = true;
						break;
					}
				}
				if(has_existed == false){
					findResult.add(friendUsers.get(i));
				}
				has_existed = false;
			}

			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, findResult);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "the server has error in findFriendsNotInMeeting", "the server has error in MeetingService findFriendsNotInMeeting");
			return httpError;
	    }
	}
	
}