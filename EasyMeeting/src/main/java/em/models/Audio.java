package em.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "audio")
public class Audio{
	
    @Id
    @Column(name="audio_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int audioId;
  
    @Column(name="user_id")
    @NotNull
    private int userId;
    
    @Column(name="meeting_id")
    @NotNull
    private int meetingId;
  
    @Column(name="audio_name")
    @NotNull
    private String audioName;
    
    @Column(name="upload_date")
    @NotNull
    private String uploadDate;

    @Column(name="translate_text")
    private String translateText;
    
    public int getAudioId(){
    	return audioId; 
    }
    
    public void setAudioId(int audioId){
    	this.audioId = audioId;
    }
    
    public int getUserId(){
    	return userId; 
    }
    
    public void setUserId(int userId){
    	this.userId = userId;
    }
    
    public int getMeetingId(){
    	return meetingId; 
    }
    
    public void setMeetingId(int meetingId){
    	this.meetingId = meetingId;
    }
    
    public String getAudioName(){
    	return audioName; 
    }
    
    public void setAudioName(String audioName){
    	this.audioName = audioName;
    }
    
    public String getUploadDate(){
    	return uploadDate; 
    }
    
    public void setUploadDate(String uploadDate){
    	this.uploadDate = uploadDate;
    }
    
    public String getTranslateText(){
    	return translateText; 
    }
    
    public void setTranslateText(String translateText){
    	this.translateText = translateText;
    }
    
}
