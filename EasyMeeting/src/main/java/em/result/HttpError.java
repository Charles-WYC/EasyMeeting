package em.result;

public class HttpError extends Result{
	private int responseCode;
	private String reason;
	private String tip;
	
	public HttpError(String type,int responseCode, String reason, String tip){
		this.type = type;
		this.responseCode = responseCode;
		this.reason = reason;
		this.tip = tip;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	
}
