package em.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import em.daos.FriendDao;
import em.daos.MeetingDao;
import em.daos.UserDao;
import em.models.Friend;
import em.models.User;
import em.result.HttpError;
import em.result.HttpSuccess;
import em.result.Result;
import em.services.FriendService;

@Service
public class FriendServiceImpl implements FriendService{
	
	@Autowired
	FriendDao friendDao;
	
	@Autowired  
    HttpServletRequest request;
	
	@Autowired
	UserDao userDao;
	
	public Result findMyFriends(){
		int userId;
		List<Friend> friends = new ArrayList<Friend>();
		List<User> friendUsers = new ArrayList<User>();
		try{
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* find all friends' id */
			friends = friendDao.findByUserId(userId);
			
			/* find all friend users */
			for(int i = 0; i < friends.size(); ++i){
				User friendUser = userDao.findByUserId(friends.get(i).getFriendId());
				friendUsers.add(friendUser);
			}
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, friendUsers);
			return httpSuccess;	
		}		
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 400, "find friends has error in server", "find friends has error in server");
			return httpError;
	    }
	}
		
	public Result create(String userName){
		int userId;
		User friend = new User();
		Friend friendPair = new Friend();
		try{
			/* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* create friend pair */
			friend = userDao.findByUserName(userName);
			friendPair.setUserId(userId);
			friendPair.setFriendId(friend.getUserId());
			System.out.println("userId : " + userId);
			System.out.println("friendId : " + friend.getUserId());
			friendDao.save(friendPair);
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200);
			return httpSuccess;		
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 400, "create friendPair has error in server", "create friends has error in server");
			return httpError;
	    }
	}
	
	public Result delete(String userName){
		int userId;
		User friend = new User();
		try{
			/* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* create friend pair */
			friend = userDao.findByUserName(userName);
			Friend friendPair = friendDao.findByUserIdAndFriendId(userId, friend.getUserId());
			friendDao.delete(friendPair);
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200);
			return httpSuccess;		
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 400, "delete friendPair has error in server", "delete friends has error in server");
			return httpError;
	    }
	}
}
