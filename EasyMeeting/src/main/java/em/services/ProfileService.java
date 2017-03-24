package em.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import em.result.Result;

@Service
public interface ProfileService{
	
	public Result createProfileId(String userName);
	public Result creatEnrollment(MultipartFile voice);
    public Result resetEnrollment();
    public Result deleteProfileId();
	
}
