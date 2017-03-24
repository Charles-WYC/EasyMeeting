package em.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import em.result.Result;

@Service
public interface UserService{
    public Result login(String userName, String userPwd);
    public Result create(String userName, String userPwd);
    public Result sessionCheck();
    public Result modify(String userName, String userPwd);
    public Result findInfo();
    //public Result checkVoiceRegister();
    public Result findByUserNameLike(String userName);
    public Result updateVoiceStatus();
    public Result updateDeviceId(String deviceId);
    public Result voiceCheck();
    public Result findByVoiceId(String profileId);
}