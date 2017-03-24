package em.servicesImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import em.daos.AudioDao;
import em.daos.MeetingDao;
import em.models.Audio;
import em.models.AudioAnalysis;
import em.models.Meeting;
import em.result.HttpError;
import em.result.HttpSuccess;
import em.result.Result;
import em.services.AudioService;

@Service
public class AudioServiceImpl implements AudioService{
    
	@Autowired
	AudioDao audioDao;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	MeetingDao meetingDao;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Result saveAudio(MultipartFile audioFile, String meetingName){
		Audio audio = new Audio();
		int userId;
		int meetingId;
		String uploadDate;
		String audioName;
		try{
		  	/* get current date */
			Date date = new Date();
		    SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd");
		    uploadDate = matter.format(date);;
		    
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
			
			/* get audio name */
			audioName = audioFile.getOriginalFilename();
			
			/* find the meeting that the audio belongs to */
			Meeting meeting = meetingDao.findByMeetingName(meetingName);
			meetingId = meeting.getMeetingId();
			
			/* save into mysql database */
			System.out.println(audioName);
			System.out.println(uploadDate);
			System.out.println(userId);
			System.out.println(meetingId);
			
			audio.setAudioName(audioName);
			audio.setUploadDate(uploadDate);
			audio.setUserId(userId);
			audio.setMeetingId(meetingId);
			audio = audioDao.save(audio);
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, audio);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "repeatable audio name", "the client tend to save an audio whose name has been existed");
			return httpError;
	    }
	}
	
	public Result saveTranslateText(int audioId, String translateText){
		try{
			/* save translate text into database*/
			Audio audio = audioDao.findOne(audioId);
			audio.setTranslateText(translateText);
			audioDao.save(audio);
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, audio);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "save translate text error", "save translate text error");
			return httpError;
	    }	
	}
	
	public Result deleteAudio(String meetingName, String audioName){
		Audio audio = new Audio();
		int userId;
		int meetingId;
		try{		    
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
					
			/* find the meeting that the audio belongs to */
			Meeting meeting = meetingDao.findByMeetingName(meetingName);
			meetingId = meeting.getMeetingId();
			
			/* find the audio in database */
			audio = audioDao.findByUserIdAndMeetingIdAndAudioName(userId, meetingId, audioName);
			audioDao.delete(audio.getAudioId());
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, audio);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "cannot find theaudio in such meeting", "the client tend to delete an unexisted audio");
			return httpError;
	    }
	}
	
	public Result modifyAudioName(String meetingName, String audioName, String newAudioName){
		int userId;
		int meetingId;
		try{		    
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
					
			/* find the meeting that the audio belongs to */
			Meeting meeting = meetingDao.findByUserIdAndMeetingName(userId, meetingName);
			meetingId = meeting.getMeetingId();
			
			/* find the audio in database */
			Audio audio = audioDao.findByUserIdAndMeetingIdAndAudioName(userId, meetingId, audioName);
			audio.setAudioName(newAudioName);
			audioDao.save(audio);
			
			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, audio);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "duplicate audio name in this meeting", "the client tend to modify audio's name with "
					                                                                                  + "a duplicate audio name in this meeting");
			return httpError;
	    }
	}
	
	public Result getAnalysis(String meetingName, String audioName){
		int userId;
		int meetingId;
		int audioId;
		List<GridFSDBFile> gridfsdbfiles = new ArrayList<GridFSDBFile>();
		List<AudioAnalysis> audioAnalysises = new ArrayList<AudioAnalysis>();
		try{		    
		    /* get current userId */
			if(request.getSession().getAttribute("userId") == null){
		    	HttpError httpError = new HttpError("error", 401, "session is out of date", "the session client visited is out of date");
				return httpError;	
			}			
			userId = (int)request.getSession().getAttribute("userId");
					
			/* find the meeting that the audio belongs to */
			Meeting meeting = meetingDao.findByUserIdAndMeetingName(userId, meetingName);
			meetingId = meeting.getMeetingId();
			System.out.println("find the meeting that the audio belongs to success");
			
			/* find the audio in mysql database */
			Audio audio = audioDao.findByUserIdAndMeetingIdAndAudioName(userId, meetingId, audioName);
			audioId = audio.getAudioId();
			System.out.println("find the audio in mysql database success");
			
			/* find audio file in mongodb database */
			DB db = mongoTemplate.getDb();
	        GridFS gridFS = new GridFS(db, "test");
	        
	        for(int i = 1; i < (1<<19); ++i){
	            String sentenceFilePath = "AudioId" + String.valueOf(audioId) + "sentence" + String.valueOf(i) + ".wav";
	            GridFSDBFile dbf = gridFS.findOne(sentenceFilePath);
	            if(dbf != null){
	            	gridfsdbfiles.add(dbf);
	            }
	            else{
	            	break;
	            }
	        }
	  
	        /* get file list */
	        System.out.println("get file list in mongodb database success");
	        for(int i = 0; i < gridfsdbfiles.size(); ++i){
	        	GridFSDBFile gridfsdbfile = gridfsdbfiles.get(i);
	        	AudioAnalysis audioAnalysis = new AudioAnalysis();
	        	String speakerName;
	        	String sentenceContent;
	        	String fileName;
	        	File sentenceFile;
	        	
	        	/* get speakerName */
	        	speakerName = (String)gridfsdbfile.getMetaData().get("speakerName");
	        	
	        	/* get sentenceContent */
	        	sentenceContent = (String)gridfsdbfile.getMetaData().get("sentenceContent");
	        	
	        	/* get sentenceFile */
	        	fileName = gridfsdbfile.getFilename();
	        	gridfsdbfile.writeTo(new File(fileName));
	        	sentenceFile = new File(fileName);
	        	
	        	/* set audioAnalysis */
	        	audioAnalysis.setSentenceContent(sentenceContent);
	        	audioAnalysis.setSentenceFile(sentenceFile);
	        	audioAnalysis.setSpeakerName(speakerName);
	        	
	        	audioAnalysises.add(audioAnalysis);
	        }
	        System.out.println("get all attributes needed success");

			/* return success result */
	    	HttpSuccess httpSuccess = new HttpSuccess("success", 200, audioAnalysises);
			return httpSuccess;
		}
		catch(Exception ex){
			HttpError httpError = new HttpError("error", 500, "duplicate audio name in this meeting", "the client tend to modify audio's name with "
					                                                                                  + "a duplicate audio name in this meeting");
			return httpError;
	    }
	}
	
	
}
