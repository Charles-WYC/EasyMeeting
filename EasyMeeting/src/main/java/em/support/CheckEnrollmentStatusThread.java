package em.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import em.daos.UserDao;
import em.models.User;
import em.result.HttpSuccess;
import em.result.Result;
import em.services.OperationStatusService;
import em.services.UserService;

@Component
public class CheckEnrollmentStatusThread extends Thread{
	
	@Autowired
	private UserDao userDao;
	
	private String operationLocation;
	
	private String deviceId;
	
	private int userId;
	
	public CheckEnrollmentStatusThread(){};
		
	public CheckEnrollmentStatusThread(String operationLocation, String deviceId, int userId){
		this.setOperationLocation(operationLocation);
		this.setDeviceId(deviceId);
		this.setUserId(userId);
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Result result = OperationStatusService.getOperationStatus(operationLocation);
			if(result.getType().equals("getOperationStatusSuccess")){
				HttpSuccess successResult = (HttpSuccess) result;
				if(successResult.getFirstParameter().equals("succeeded")){
					if(successResult.getSecondParameter().equals("Enrolled")){
						/* send message to user device */
						PushExample.sendSuccessPush(deviceId);
						
						/* update voice_status in database */
						User user = userDao.findByUserId(userId);
						user.setVoiceStatus(1);
						userDao.save(user);
					}
					else{
						System.out.println("enrollment "+(String)successResult.getSecondParameter());
					}
					return;
				}
				else if(successResult.getFirstParameter().equals("failed")){
					System.out.println("enrollment failed");
					return;
				}
				else{
					System.out.println("enrollment "+(String)successResult.getFirstParameter());
				}
			}
		}
	}

	public String getOperationLocation() {
		return operationLocation;
	}

	public void setOperationLocation(String operationLocation) {
		this.operationLocation = operationLocation;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
