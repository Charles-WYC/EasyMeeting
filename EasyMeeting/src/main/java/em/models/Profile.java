package em.models;

import em.result.EmResult;
import em.result.HttpSuccess;
import em.result.Result;
import em.support.APIHelper;

public class Profile extends EmResult{
	private String identificationProfileId;
	private String locale;
	private double enrollmentSpeechTime;
	private double remainingEnrollmentSpeechTime;
	private String createdDateTime;
	private String lastActionDateTime;
	private String enrollmentStatus;
	
	public Profile(String identificationProfileId){
		this.identificationProfileId = identificationProfileId;
	}
	public Profile(){
		
	}
	
	public static Result GetAllProfiles(){
    	Result result = APIHelper.executeAPI("ProfilesObtainmentAPI", null, null);
    	return result;
	}
	
	public Result Get(){
    	Result result = APIHelper.executeAPI("ProfileObtainmentAPI", identificationProfileId, null);
    	
    	if(result.getType().equals("profileObtainmentSuccess")){
    		HttpSuccess httpSuccess = (HttpSuccess) result;
    		Profile tempProfile = (Profile)httpSuccess.getFirstParameter();
    		this.createdDateTime = tempProfile.getCreatedDateTime();
    		this.enrollmentSpeechTime = tempProfile.getEnrollmentSpeechTime();
    		this.enrollmentStatus = tempProfile.getEnrollmentStatus();
    		this.lastActionDateTime = tempProfile.getLastActionDateTime();
    		this.locale = tempProfile.getLocale();
    		this.remainingEnrollmentSpeechTime = tempProfile.getRemainingEnrollmentSpeechTime();
    		
    		return HttpSuccessWithoutParameter;
    	}
    	return result;
	}

	public Result create(){
    	Result result = APIHelper.executeAPI("ProfileCreationAPI", null, null);
    	
    	if(result.getType().equals("profileCreationSuccess")){
    		HttpSuccess httpSuccess = (HttpSuccess) result;
    		this.identificationProfileId = (String)httpSuccess.getFirstParameter();
    		return HttpSuccessWithoutParameter;
    	}
    	return result;
	}

	public Result delete(){
    	Result result = APIHelper.executeAPI("ProfileDeletionAPI", identificationProfileId, null);
    	
    	return result;
	}
	
	public Result ResetEnrollment(){
    	Result result = APIHelper.executeAPI("EnrollmentReplacementAPI", identificationProfileId, null);
    	
    	return result;
	}
	
	public Result createEnrollment(String filePath){
    	Result result = APIHelper.executeAPI("EnrollmentCreationAPI", filePath, identificationProfileId);
    	
    	return result;
	}

	public String getIdentificationProfileId() {
		return identificationProfileId;
	}
	public void setIdentificationProfileId(String identificationProfileId) {
		this.identificationProfileId = identificationProfileId;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public double getEnrollmentSpeechTime() {
		return enrollmentSpeechTime;
	}
	public void setEnrollmentSpeechTime(double enrollmentSpeechTime) {
		this.enrollmentSpeechTime = enrollmentSpeechTime;
	}
	public double getRemainingEnrollmentSpeechTime() {
		return remainingEnrollmentSpeechTime;
	}
	public void setRemainingEnrollmentSpeechTime(double remainingEnrollmentSpeechTime) {
		this.remainingEnrollmentSpeechTime = remainingEnrollmentSpeechTime;
	}
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getLastActionDateTime() {
		return lastActionDateTime;
	}
	public void setLastActionDateTime(String lastActionDateTime) {
		this.lastActionDateTime = lastActionDateTime;
	}
	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}
	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}
	
	
}
