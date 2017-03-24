package em.support;

public class MergeTranslateResult {
	public static String MergeSentence(String sentenceA, String sentenceB){
		System.out.println(sentenceA);
		System.out.println(sentenceB);
		
	    String[] wordsA = sentenceA.split(" ");	
	    String[] wordsB = sentenceB.split(" ");
	    
	    int mergeIndexA = wordsA.length-1;
	    int mergeIndexB = 0;
	    boolean findMerge = false;
	    
	    /* caculate the merge index of both two sentences */
	    for(int i = wordsA.length-2; i >= 2; --i){
	    	String aim = wordsA[i];
	    	for(int j = 0; j < wordsB.length; ++j){
	    		if(aim.equals(wordsB[j])){
	    			if(i-2>=0 && j-2>=0 && wordsA[i-1].equals(wordsB[j-1]) && wordsA[i-2].equals(wordsB[j-2])){
	    				mergeIndexA = i;
	    				mergeIndexB = j;
	    				findMerge = true;
	    				break;
	    			}
	    		}
	    	}
	    	if(findMerge == true){
	    		break;
	    	}
	    }
	    
	    /* if not find such accurate mergeIndex */
	    if(findMerge == false){
	    	for(int i = wordsA.length-1; i >= 0; --i){
	    		for(int j = 0; j < wordsB.length; ++j){
	    			if(wordsA[i].equals(wordsB[j])){
	    				mergeIndexA = i;
	    				mergeIndexB = j;
	    				findMerge = true;
	    				break;
	    			}
	    		}
	    		if(findMerge == true){
		    		break;
	    		}
	    	}
	    }
	      
	    System.out.println(mergeIndexA);
	    System.out.println(mergeIndexB);
	    /* merge into one String array */
	    String[] mergeResult = new String[mergeIndexA + wordsB.length - mergeIndexB];
	    int tempIndex = mergeIndexB + 1;
	    for(int i = 0; i < mergeResult.length; ++i){
	    	if(i <= mergeIndexA){
	    		mergeResult[i] = wordsA[i];
	    	}
	    	else{
	    		mergeResult[i] = wordsB[tempIndex];
	    		tempIndex++;
	    	}
	    }
		
	    /* get result string */
	    String result = "";
	    for(int i = 0; i < mergeResult.length; ++i){
	    	if(i != mergeResult.length - 1){
	    	    result = result + mergeResult[i] + ' ';
	    	}
	    	else{
	    		result = result + mergeResult[i];
	    	}
	    }	    
	    return result;
	}
}
