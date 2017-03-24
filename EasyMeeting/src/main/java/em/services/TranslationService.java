package em.services;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.stereotype.Service;

import em.API.TranslationAPI;
import em.result.EmResult;
import em.result.HttpError;
import em.result.HttpSuccess;
import em.result.Result;
import em.result.TranslateSuccess;
import em.support.APIHelper;
import em.support.CalStringSimilarity;
import em.support.MergeTranslateResult;
import em.support.SplitWavFiles;

@Service
public class TranslationService extends EmResult{
	public static Map<Long, Result> translationResultMap = new Hashtable<Long, Result>();
	
	public static Result getTranslation(String filePath){
		boolean succeeded = false;
		Result result = null;
		while(!succeeded){
			result = APIHelper.executeAPI("TranslationAPI",filePath, null);
			
	    	if(result.getType().equals("translationSuccess")){
	    		succeeded = true;
	    	}
	    	else
	    	{
	    		HttpError error = (HttpError)result;
	    		int responseCode = error.getResponseCode();
	    		if(responseCode == 408 || responseCode == 502){
	    			
	    		}
	    		else if (responseCode == 401 || responseCode == 403){
	    			AuthenticationService.renewAuthenticationInstance();
	    		}
	    		else
	    		{
	    			return result;
	    		}
	    	}
		}
		
    	return result;
	}
	
	public String TranslateWavFile(String filePath, int secondPerFile, int audioId) throws LineUnavailableException, UnsupportedAudioFileException, IOException{
		/* calculate the number of files that split */
		/* get total time of wav */
		File wav = new File(filePath);
		Clip clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(wav);
        clip.open(ais);
		double totalTime = clip.getMicrosecondLength() / 1000000;
		
		double tempSecond = (double)secondPerFile / 2.0;
		int fileNumber = (int)(totalTime / tempSecond) - 1;
		System.out.println("fileNumber : "+ fileNumber);
		
        String[] results = new String[fileNumber];
	    try{
	    	SplitWavFiles.SplitWavFileCovering(filePath, secondPerFile, audioId);
	    	for(int i = 1; i <= fileNumber; ++i){
	    		String splitFilePath = "AudioId" + String.valueOf(audioId) + "split" + String.valueOf(i) + ".wav";
	    		HttpSuccess result = (HttpSuccess)getTranslation(splitFilePath);
	    		results[i-1] = (String)result.getFirstParameter();
	    	}    	
	    	for(int i = 1; i < fileNumber; ++i){
	    		System.out.println("merging audio : " + String.valueOf(i));
	    		results[0] = MergeTranslateResult.MergeSentence(results[0], results[i]);
	    	}    	
	    }
	    catch (Exception ex){
	    	System.out.println("Error in TranslateWavFile: " + ex.toString());
	    	return null;
	    }
	    return results[0];			
	}
	
	public void SplitWavBySentence(String[] sentences, String filePath, int audioId)throws IOException, LineUnavailableException, UnsupportedAudioFileException{
		String result;
		int[] sim = new int[10];
		int seq = 1;
		
		try{
            for(int i = 0; i < sentences.length; ++i){
        	    for(int j = 3; j < 13; j++){
        	        /* split first 3s/4s/5s/6s/7s/8s/9s/10s/11s/12s audio */
        	        SplitWavFiles.SplitWavFirstPart(filePath, j, audioId);
        	    
        	        /* translate the splitted audio */
        	        String tempAudioPath = "AudioId" + String.valueOf(audioId) + String.valueOf(j) + "seconds.wav";
        	        result = (String)((HttpSuccess)getTranslation(tempAudioPath)).getFirstParameter();
                    System.out.println("result : " + result);
        	    
        	        /* calculate the similar rate between sentences[i] and result */
        	        sim[j-3] = CalStringSimilarity.getLevenshteinDistance(sentences[i], result);
        	        System.out.println("similarity : " + String.valueOf(sim[j-3]));
        	     }
        	
        	    /* get the most similar one */
        	    int minIndex = 0;
        	    int minSim = 99999;
        	    for(int k = 0; k < sim.length; ++k){
                    if(sim[k] < minSim){
                	    minIndex = k;
                	    minSim = sim[k];
                    }
        	    }
        	    int second = minIndex + 3;
        	    System.out.println("the most similar version :" + String.valueOf(second) + " seconds");
        	
                /* split the main wav file */
        	    String simAudioPath = "AudioId" + String.valueOf(audioId) + String.valueOf(second) + "seconds.wav";
        	    SplitWavFiles.SplitWavFirstFewSecond(filePath, simAudioPath);    	
        	
        	    /* copy the sentence audio to another file */
        	    String resultAudioPath = "AudioId" + String.valueOf(audioId) + "sentence" + String.valueOf(seq) + ".wav";
        	    SplitWavFiles.copyFile(simAudioPath, resultAudioPath);
        	    seq++;
            }
	    }
	    catch (Exception ex){
	    	System.out.println("Error in SplitWavBySentence: " + ex.toString());
	    }
	}
}
