package em.result;

import java.util.List;

import em.models.User;

public class HttpSuccess extends Result{
	private int responseCode;
	private Object responseTip;
	private Object firstParameter;
	private Object secondParameter;
	
	public HttpSuccess(String type, int responseCode){
		this.type = type;
		this.setResponseCode(responseCode);
	}

	public HttpSuccess(String type, int responseCode, Object responseTip) {
		this.type = type;
		this.setResponseCode(responseCode);
		this.responseTip = responseTip;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public Object getSecondParameter() {
		return secondParameter;
	}

	public void setSecondParameter(Object secondParameter) {
		this.secondParameter = secondParameter;
	}

	public Object getFirstParameter() {
		return firstParameter;
	}

	public void setFirstParameter(Object firstParameter) {
		this.firstParameter = firstParameter;
	}

	public Object getResponseTip() {
		return responseTip;
	}

	public void setResponseTip(Object responseTip) {
		this.responseTip = responseTip;
	}
}
