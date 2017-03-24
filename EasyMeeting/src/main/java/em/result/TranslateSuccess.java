package em.result;

public class TranslateSuccess extends Result {
	private int ResponseCode = 200;
	private String result = null;
	private String status = null;
	
	public TranslateSuccess(){
		this.type = "translateSuccess";
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public int getResponseCode() {
		return ResponseCode;
	}
	public void setResponseCode(int responseCode) {
		ResponseCode = responseCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
