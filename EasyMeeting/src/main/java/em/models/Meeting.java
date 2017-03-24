package em.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Entity
@Table(name = "meeting")
//@ConfigurationProperties(prefix = "testuser")

public class Meeting{

    @Id
    @Column(name="meeting_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meetingId;
  
    @Column(name="user_id")
    @NotNull
    private int userId;
    
    @Column(name="meeting_name")
    @NotNull
    private String meetingName;
  
    @Column(name="create_date")
    @NotNull
    private String createDate;
    
    public int getMeetingId(){
    	return meetingId;
    }
    
    public void setMeetingId(int meetingId){
    	this.meetingId = meetingId;
    }
    
    public String getMeetingName(){
    	return meetingName;
    }
  
    public void setMeetingName(String meetingName){
    	this.meetingName = meetingName;
    }
    
    public int getUserId(){
    	return userId;
    }
    
    public void setUserId(int userId){
    	this.userId = userId;
    }
  
    public String getCreateDate(){
    	return createDate;
    }
    
    public void setCreateDate(String createDate){
    	this.createDate = createDate;
    }
    
} // class User