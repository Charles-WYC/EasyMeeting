package em.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import em.result.EmResult;
import em.result.HttpError;
import em.result.Result;
import em.result.HttpSuccess;
public class ArticleSeparationService extends EmResult{
	private static List<String> activeTags = Arrays.asList("S", "SBARQ", "SINV", "SQ");
	private static List<String> ignoreTags = Arrays.asList("ADJP", "ADVP", "CONJP", "FRAG", "INTJ", "LST", "NAC", "NP",
														   "NX", "PP", "PRN", "PRT", "QP", "RRC", "SBAR", "UCP", "VP",
														   "WHADJP", "WHADVP", "WHNP", "WHPP","X", "TOP", "JJ", "CC", 
														   "NNP", "VBZ", "DT", "NNS", "PRP$", "DT", "PRP", "IN", "NN",
														   "MD", "VB", "VBN", "CD", "POS", "JJS", "RB", "VBG", "VBP",
														   "TO", "RBS", ".");
	
	public static Result separate(String text){
		//System.out.println("begin separate");
        int head = 0;
        int tail = 0;
        ArrayList<String> resultList = new ArrayList<String>();
        HttpSuccess result = new HttpSuccess("ArticleSeparationSuccess", 200);
        while(head < text.length()){
            tail = head+200;
        	if(tail > text.length()){
        		tail = text.length();
        	}
        	boolean succeeded = false;
        	while(!succeeded){
        		System.out.println("separation thread: "+ Thread.currentThread().getId());
        		System.out.println("analyze text: " + text.substring(head, tail));
        		Result textAnalyzationResult = TextAnalyzationService.getTextAnalyzation(text.substring(head, tail));
        		
	        	if(textAnalyzationResult.getType().equals("httpError")){
	        		//System.out.println("analyze failed");
	        		HttpError httpError = (HttpError)textAnalyzationResult;
	        		//System.out.println(httpError.getResponseCode());
	        		if(httpError.getResponseCode() == 429){
	        			try {
							Thread.sleep(30000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	        		}
	        		else
	        		{
	        			return httpError;
	        		}
	        	}
	        	else
	        	{
	        		//System.out.println("analyze succeeded");
	        		succeeded = true;
	        		HttpSuccess httpSuccess = (HttpSuccess)textAnalyzationResult;
	        		int totalLength = getSentence((String)httpSuccess.getFirstParameter(), resultList);
	        		head += (totalLength);
	        		//System.out.println("after get sentence");
	        	}
        		
        	}
        }
        result.setFirstParameter(resultList);
        return result;
	}
	
	private static int getSentence(String resultText, ArrayList<String> resultList){
		//System.out.println("begin get sentence");
		resultText = resultText.replace("(","");
		resultText = resultText.replace(")","");
		//System.out.println("result text: "+resultText);
		String words[] = resultText.split(" ");
		int size = words.length;
		//System.out.println(size);
		String sentence = "";
		for(int i =0; i<size; i++){
			if(activeTags.contains(words[i])){
				sentence = sentence.trim();
				if(!sentence.equals("")){
					System.out.println("####sentence: " + sentence+"||");
					resultList.add(sentence);
					//System.out.println("end get sentence");
					return sentence.length()+1;
				}
			}
			else if(ignoreTags.contains(words[i])){
			}
			else
			{
				if(words[i].equals("'s")){
					sentence = sentence.substring(0, sentence.length()-1);
				}
				sentence +=(words[i]+" ");
			}
		}
		sentence = sentence.trim();
		if(!sentence.equals("")){
			resultList.add(sentence);
		}
		//System.out.println("end get sentence");
		return sentence.length()+1;
	}
}
