package em.result;

public class GetOperationStatusSuccess extends Result{
	private String status;

	public GetOperationStatusSuccess(){
		this.type = "getOperationStatusSuccess";
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
