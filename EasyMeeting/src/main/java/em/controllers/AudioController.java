package em.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
import com.mongodb.gridfs.GridFSInputFile;

import em.models.Audio;
import em.models.User;
import em.result.HttpError;
import em.result.HttpSuccess;
import em.result.Result;
import em.services.ArticleSeparationService;
import em.services.AudioService;
import em.services.MeetingService;
import em.services.OperationStatusService;
import em.services.SpeakerRecognitionService;
import em.services.TranslationService;
import em.services.UserService;
import em.support.MergeSaparateResult;

@RestController 
@RequestMapping("/audio")
public class AudioController{
	 
	@Autowired
	UserService userService;
	
	@Autowired
	AudioService audioService;
	
	@Autowired
	TranslationService translationService;
	
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
    @RequestMapping(value = "/uploadaudio", method = RequestMethod.POST)
	@ResponseBody
	public Result uploadAudio(@RequestParam("audio") MultipartFile audio, @RequestParam("meetingName") String meetingName){
		Result result;
		try{
		    result = audioService.saveAudio(audio, meetingName);
		    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    if(result.getType().equals("success")){
		    	HttpSuccess httpSuccess = (HttpSuccess)result;
		    	Audio aud = (Audio)httpSuccess.getResponseTip();
		    	System.out.println("upload success");
		    	System.out.println("audio name : " + aud.getAudioName());
		    	System.out.println("upload date : " + aud.getUploadDate());
		    	System.out.println("upload user : " + aud.getUserId());
		    	
		    	/* create thread doing translation */		    	
		    	TranslateThread translateThread = new TranslateThread(aud.getAudioId(), audio, meetingName);
		    	translateThread.start();
		    }
		    return result;
		}
		catch(Exception ex) {
	        System.out.println("the server has error in AudioController uploadAudio");
	        HttpError httpError = new HttpError("error", 500, "the server has error in AudioController uploadAudio", "");
			return httpError;
		}
	}
    
    @RequestMapping(value = "/delete")
	@ResponseBody
	public Result deleteAudio(@RequestParam("meetingName") String meetingName, @RequestParam("audioName") String audioName){
		Result result;
		try{
			result = audioService.deleteAudio(meetingName, audioName);
		    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    if(result.getType().equals("success")){
		    	System.out.println("delete audio success");
		    }
		    return result;
		}
		catch(Exception ex) {
	        System.out.println("the server has error in AudioController deleteAudio");
	        HttpError httpError = new HttpError("error", 500, "the server has error in AudioController deleteAudio", "");
			return httpError;
		}
	}
    
    @RequestMapping(value = "/modifyname")
	@ResponseBody
	public Result modifyAudioName(@RequestParam("meetingName") String meetingName, 
			                  @RequestParam("audioName") String audioName,
			                  @RequestParam("newAudioName") String newAudioName){
		Result result;
		System.out.println("newAudioName : " + newAudioName);
		try{
			result = audioService.modifyAudioName(meetingName, audioName, newAudioName);
		    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    if(result.getType().equals("success")){
		    	System.out.println("modify audio name success");
		    }
		    return result;
		}
		catch(Exception ex) {
	        System.out.println("the server has error in AudioController modifyAudioName");
	        HttpError httpError = new HttpError("error", 500, "the server has error in AudioController modifyAudioName", "");
			return httpError;
		}
	}
    
    @RequestMapping(value = "/getanalysis", method = RequestMethod.POST)
	@ResponseBody
	public Result getAnalysis(@RequestParam("meetingName") String meetingName, 
			                  @RequestParam("audioName") String audioName){
		Result result;
		try{
			result = audioService.getAnalysis(meetingName, audioName);
		    if(result.getType().equals("error")){
	    	    System.out.println(((HttpError)result).getTip());
		    }
		    if(result.getType().equals("success")){
		    	System.out.println("get audio Analysis success");
		    }
		    return result;
		}
		catch(Exception ex) {
	        System.out.println("the server has error in AudioController getAnalysis");
	        HttpError httpError = new HttpError("error", 500, "the server has error in AudioController getAnalysis", "");
			return httpError;
		}
	}
    
    
	public class TranslateThread extends Thread{
		
		private MultipartFile audioFile;
		
		private int audioId;
		
		private String meetingName;
		
		public TranslateThread(int audioId, MultipartFile audio, String meetingName){
		    this.audioId = audioId;
		    this.audioFile = audio;
		    this.meetingName = meetingName;
		}
		
        public void run(){
        	String translateText;
        	Result splitResult;
        	Result findResult;
        	ArrayList<String> as = new ArrayList<String>();
        	try{
        		String filePath = "audioIdMain" + String.valueOf(audioId) + ".wav";
        		
                /* write the upload audio to disk */
        		File f = new File(filePath);
                InputStream is = audioFile.getInputStream();
                FileOutputStream fos = new FileOutputStream(filePath);
                byte[] b = new byte[1024];
                while((is.read(b)) != -1){
                     fos.write(b);
                }
                is.close();
                fos.close();
            
                /* translate audio */
                translateText = translationService.TranslateWavFile(filePath, 12, audioId);
                System.out.println("translate whole text success");
                
                /* write translation result into mysql database */
                audioService.saveTranslateText(audioId, translateText);
                System.out.println("save whole text into mysql success");
                
                /* split text into sentences */
                splitResult = ArticleSeparationService.separate(translateText);
    		    if(splitResult.getType().equals("error")){
    	    	    System.out.println(((HttpError)splitResult).getTip());
    	    	    return;
    		    }
            	HttpSuccess httpSuccess = (HttpSuccess)splitResult;
            	as = (ArrayList<String>)httpSuccess.getFirstParameter();
            	as = MergeSaparateResult.mergeSaparate(as);
            	String[] sentences = new String[as.size()];
            	for(int i = 0; i < as.size(); ++i){
            		sentences[i] = as.get(i);
            		System.out.println(sentences[i]);
            	}
            	
            	/* split audio into sentences */
            	translationService.SplitWavBySentence(sentences, filePath, audioId);
            	System.out.println("split audio into sentences success");
            	
            	/* find all participants and get their voice profileId */
            	ArrayList<User> participants = new ArrayList<User>();
            	ArrayList<String> profileIds = new ArrayList<String>();
            	
            	findResult = meetingService.findAllParticipants(meetingName);
    		    if(findResult.getType().equals("error")){
    	    	    System.out.println(((HttpError)findResult).getTip());
    	    	    return;
    		    }
    		    participants = (ArrayList<User>) ((HttpSuccess)findResult).getResponseTip();
    		    for(int i = 0; i < participants.size(); ++i){
    		    	profileIds.add(participants.get(i).getVoiceId());
    		    }
    		    System.out.println("get all voice of meeting success");
    		    
    		    /* identify the speaker of each sentence */
    		    Result recognitionResult;
    		    Result statusResult;
    		    Result speakerResult;
    		    String operationLocation;
    		    String speakerProfileId;
    		    User speaker = new User();
    		    ArrayList<String> speakerNames = new ArrayList<String>();
    		    for(int i = 1; i <= sentences.length; ++i){
    		        /* send recoginiton request */
    		    	String sentenceFilePath = "AudioId" + String.valueOf(audioId) + "sentence" + String.valueOf(i) + ".wav";
    		        recognitionResult = SpeakerRecognitionService.DoSpeakerRecognition(sentenceFilePath, profileIds);  
    		        operationLocation = (String)((HttpSuccess)recognitionResult).getFirstParameter();
    		        
    		        /* get speaker's profileId */
    		        statusResult = OperationStatusService.getOperationStatus(operationLocation);
    		        speakerProfileId = (String)((HttpSuccess)recognitionResult).getSecondParameter();
    		        
    		        /* find user according to profileId */
    		        speakerResult = userService.findByVoiceId(speakerProfileId);
    		        speaker = (User)((HttpSuccess)speakerResult).getResponseTip();
    		        speakerNames.add(speaker.getUserName());
    		    }
    		    System.out.println("speaker recognition success");
    		    
            	/* save splitted audio pieces into mongoDB database */
    		    for(int i = 1; i <= sentences.length; ++i){
    		        String sentenceFilePath = "AudioId" + String.valueOf(audioId) + "sentence" + String.valueOf(i) + ".wav";
    		        DB db = mongoTemplate.getDb();
    		        GridFS gridFS = new GridFS(db, "test");
    		        
    		        /* delete if there is any same */
    		        gridFS.remove(sentenceFilePath); 
    		         
    		        /* save into mongodb */
    		        File tempAudio = new File(sentenceFilePath);
    		        GridFSInputFile gfs = gridFS.createFile(tempAudio);
    		        
    		        /* create metadata */
    		        DBObject query = new BasicDBObject();
    		        query.put("audioId", audioId);
    		        query.put("speakerName", speakerNames.get(i-1));
    		        query.put("sequence", i);
    		        query.put("sentenceContent", sentences[i-1]);
  
    		        gfs.setMetaData(query);
    		        gfs.setContentType("audio/wav");
    		        gfs.setFilename(sentenceFilePath);
    		        gfs.save(); 
    		    }
    		    System.out.println("save sentence audio into mongodb success");
                
        	}
    		catch(Exception ex) {
    			System.out.println("error in translation and split thread");
    		}
        	
        	
		}

		public MultipartFile getAudioFile() {
			return audioFile;
		}

		public void setAudioFile(MultipartFile audioFile) {
			this.audioFile = audioFile;
		}

		public int getAudioId() {
			return audioId;
		}

		public void setAudioId(int audioId) {
			this.audioId = audioId;
		}

		public String getMeetingName() {
			return meetingName;
		}

		public void setMeetingName(String meetingName) {
			this.meetingName = meetingName;
		}
		
    }

    
    
    
    
    
}
