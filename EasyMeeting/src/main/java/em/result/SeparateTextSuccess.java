package em.result;

import java.util.ArrayList;

public class SeparateTextSuccess extends Result{
	private ArrayList<String> resultList;
	
	public SeparateTextSuccess(){
		this.type = "separateTextSuccess";
	}

	public ArrayList<String> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<String> resultList) {
		this.resultList = resultList;
	}
}
