package em.result;

public class InternalError extends Result{
	private String reason;
	private String tip;
	
	public InternalError(String type, String reason, String tip){
		this.type = type;
		this.reason = reason;
		this.tip = tip;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
