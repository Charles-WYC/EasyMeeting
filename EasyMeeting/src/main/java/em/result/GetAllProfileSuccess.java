package em.result;

import java.util.ArrayList;

import em.models.Profile;

public class GetAllProfileSuccess extends Result{
	private int ResponseCode = 200;
	
	private ArrayList<Profile> profiles;

	public GetAllProfileSuccess(){
		this.type = "getAllProfileSuccess";
		this.profiles = new ArrayList<Profile>();
	}
	public ArrayList<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(ArrayList<Profile> profiles) {
		this.profiles = profiles;
	}

	public int getResponseCode() {
		return ResponseCode;
	}

	public void setResponseCode(int responseCode) {
		ResponseCode = responseCode;
	}
}
