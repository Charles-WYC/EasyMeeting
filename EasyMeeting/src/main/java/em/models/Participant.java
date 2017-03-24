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
@Table(name = "participant")
//@ConfigurationProperties(prefix = "testuser")

public class Participant{

    @Id
    @Column(name="participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int participantId;
  
    @Column(name="user_id")
    @NotNull
    private int userId;
    
    @Column(name="meeting_id")
    @NotNull
    private int meetingId;
  
    public int getParticipantId(){
    	return participantId;
    }
    
    public void setParticipantId(int participantId){
    	this.participantId = participantId;
    }
    
    public int getMeetingId(){
    	return meetingId;
    }
    
    public void setMeetingId(int meetingId){
    	this.meetingId = meetingId;
    }
    
    public int getUserId(){
    	return userId;
    }
    
    public void setUserId(int userId){
    	this.userId = userId;
    }
  
    
} // class User