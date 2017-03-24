package em.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * An entity User composed by three fields (id, email, name).
 * The Entity annotation indicates that this class is a JPA entity.
 * The Table annotation specifies the name for the table in the db.
 *
 * @author netgloo
 */
@Entity
@Table(name = "user")
//@ConfigurationProperties(prefix = "testuser")

public class User{

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
  
    @Column(name="user_name")
    @NotNull
    private String userName;
  
    @Column(name="user_pwd")
    @NotNull
    private String userPwd;
    
    @Column(name="voice_id")
    private String voiceId;
    
    @Column(name="voice_status")
    @NotNull
    private int voiceStatus;
    
    @Column(name="device_id")
    private String deviceId;
    
    @Column(name="upload_voice")
    @NotNull
    private int uploadVoice;
    
  
    public User(){}
    
    public User(int userId){ 
        this.userId = userId;
    }
  
    public User(String userName, String userPwd){
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public User(int userId, String userName, String userPwd){
		this.userId = userId;
		this.userName = userName;
        this.userPwd = userPwd;
	}

	public User(User user) {
		this.userId = user.getUserId();
		this.userName = user.getUserName();
		this.userPwd = user.getUserPwd();
	}

	public int getUserId(){
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
  
    public void setUserName(String userName){
        this.userName = userName;
    }
  
    public String getUserPwd(){
        return userPwd;
    }

    public void setUserPwd(String userPwd){
        this.userPwd = userPwd;
    }
    
    public String getVoiceId(){
        return voiceId;
    }

    public void setVoiceId(String voiceId){
        this.voiceId = voiceId;
    }
    
	public int getVoiceStatus(){
        return voiceStatus;
    }

    public void setVoiceStatus(int voiceStatus){
        this.voiceStatus = voiceStatus;
    }
    
    public String getDeviceId(){
        return deviceId;
    }

    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }
    
	public int getUploadVoice(){
        return uploadVoice;
    }

    public void setUploadVoice(int uploadVoice){
        this.uploadVoice = uploadVoice;
    }
  
} // class User