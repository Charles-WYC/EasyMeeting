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
@Table(name = "voice")

public class Voice{
  
    @Id
    @Column(name="voice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voiceId;
	
    @Column(name="voice_name")
    @NotNull
    private String voiceName;
    
    @Column(name="profile_id")
    @NotNull
    private String profileId;
    
    public int getVoiceId(){
    	return voiceId;
    }
    
    public void setVoiceId(int voiceId){
    	this.voiceId = voiceId;
    }
  
    public String getVoiceName(){
    	return voiceName;
    }
    
    public void setVoiceName(String voiceName){
    	this.voiceName = voiceName;
    }
    
    public String getProfileId(){
    	return profileId;
    }
    
    public void setProfileId(String profileId){
    	this.profileId = profileId;
    }

    
} // class User