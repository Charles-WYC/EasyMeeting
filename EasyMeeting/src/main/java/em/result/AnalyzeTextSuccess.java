package em.result;

public class AnalyzeTextSuccess extends Result{
	private String resultText;
	
	public AnalyzeTextSuccess(){
		this.type = "analyzeTextSuccess";
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
}
