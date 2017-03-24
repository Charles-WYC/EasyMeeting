package em.result;

public class BeginRecognitionSuccess extends Result{
	private String operationLocation;
	
	public BeginRecognitionSuccess(){
		this.type = "beginRecognitionSuccess";
	}

	public String getOperationLocation() {
		return operationLocation;
	}

	public void setOperationLocation(String operationLocation) {
		this.operationLocation = operationLocation;
	}
}
