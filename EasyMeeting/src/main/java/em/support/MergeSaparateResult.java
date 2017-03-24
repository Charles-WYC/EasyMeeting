package em.support;

import java.util.ArrayList;

public class MergeSaparateResult{
	private static int leastLength = 5;
    
	public static ArrayList<String> mergeSaparate(ArrayList<String> saparateResult){
		ArrayList<String> mergeResult = new ArrayList<String>();
		
		/* merge short sentence */
		for(int i = 0; i < saparateResult.size(); ++i){
			String tempSentence = saparateResult.get(i);
			String[] tempWords = tempSentence.split(" ");
			int length = tempWords.length;
			
			/* merge the next sentence until length > leastLength*/
			if(tempWords.length <= leastLength){
				for(int j = i+1; j < saparateResult.size(); ++j){
					String tempSentence2 = saparateResult.get(j);
					String[] tempWords2 = tempSentence.split(" ");
				    tempSentence = tempSentence + " " + tempSentence2;
				    length += tempWords2.length;
				    if(length > leastLength){
				    	i = j;
				    	break;
				    }
				}
			}
			mergeResult.add(tempSentence);
		}
		return mergeResult;
	}
}
