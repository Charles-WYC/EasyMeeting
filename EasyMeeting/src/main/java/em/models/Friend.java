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
@Table(name = "friend")

public class Friend{
  
    @Id
    @Column(name="_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
	
    @Column(name="user_id")
    @NotNull
    private int userId;
    
    @Column(name="friend_id")
    @NotNull
    private int friendId;
    
    public int getUserId(){
    	return userId;
    }
    
    public void setUserId(int userId){
    	this.userId = userId;
    }
  
    public int getFriendId(){
    	return friendId;
    }
    
    public void setFriendId(int friendId){
    	this.friendId = friendId;
    }

    
} // class User