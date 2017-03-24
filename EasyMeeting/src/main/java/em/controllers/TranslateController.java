package em.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import em.models.AudioAnalysis;
import em.models.TranslateResult;
import em.result.HttpSuccess;
import em.result.Result;
import em.result.SeparateTextSuccess;
import em.services.ArticleSeparationService;
import em.services.OperationStatusService;
import em.services.SpeakerRecognitionService;
import em.services.TranslationService;
import em.support.*;

@RestController 
@RequestMapping("/test")
public class TranslateController{
	
	  @Autowired
	  TranslationService translationService;
	  
	  @Autowired
	  MongoTemplate mongoTemplate;
	
	  @RequestMapping("/splitbysentencetest")
	  @ResponseBody
	  public String[] splitBySentencetest() throws LineUnavailableException, UnsupportedAudioFileException, IOException{
        Result result;
        try{
		    String filePath = "globalwarming.wav";
            String text = "One problem of global warming global warming may or not be the great environmental crisis up the 21st century but regardless of whether it is or isn't we won't do much about it We will argue over it and maybe even as a nation make some fairly salams sounding commitments to avoid it But the more dramatic and meaning of these commitments in the less likely they are to be observed elmcor cause global warming in the Inconvenient Truth as it merely recognizing it to put in on a path to a solution with the real truth is that we don't know enough to believe global warming and without major technological breakthroughs We can't do much about it No government will adopt region restrictions on economic growth and personal freedom limits on electricity usage driving in trouble It might cut back global warming and still politicians want to show their doing something considered the Kyoto Protocol It allowed countries that joined to punish those that didn't But it has introduced AC 02 emissions up about 25% since 1990 Endumeni signatories did not adopt tough enough policies to hit their 2008 to 2012 targets the practical conclusion is that global warming is a potential desastre The only solution is new technology only an aggressive research and development program might find ways of breaking our dependence on fossil fuels or dealing with it but trouble with the global warming debate is that it has become a more a problem when it's really engineering one being mean in truth is that if we don't solve the engineering problem or help.";    	
		    /* analyize article */
		    result = ArticleSeparationService.separate(text);
    	    HttpSuccess httpSuccess = (HttpSuccess)result;
    	
    	    ArrayList<String> as = (ArrayList<String>)httpSuccess.getFirstParameter();
    	
    	    /* merge saparate result */
    	    as = MergeSaparateResult.mergeSaparate(as);
    	    String[] sentences = new String[as.size()];
    	    for(int i = 0; i < as.size(); ++i){
    		    sentences[i] = as.get(i);
    		    System.out.println(sentences[i]);
    	    }
    	
    	    /* split wav into sentences */
	    	translationService.SplitWavBySentence(sentences, filePath, 0);
	    	return null;
	    }
	    catch (Exception ex){
	    	System.out.println("Error in splitbysentencetest: " + ex.toString());
	    	return null;
	    }
	    //return result;
	  }
	  
	  @RequestMapping("/translatetest")
	  @ResponseBody
	  public String translateTest(){
		String filePath = "globalwarming.wav";	
		String translateResult;
	    try{
	        translateResult = translationService.TranslateWavFile(filePath, 12, 0);
	    }
	    catch (Exception ex){
	    	System.out.println("Error in translatetest: " + ex.toString());
	    	return null;
	    }
	    return translateResult;
	  }
	  
	  @RequestMapping("/recognitiontest")
	  @ResponseBody
	  public void recognitionTest(){
		String filePath = "batman.wav";
		String operationLocation;
		ArrayList<String> identificationProfileIds = new ArrayList<String>();
		identificationProfileIds.add("72b2ea2f-1f23-413a-ae47-46e3909e9270");
		identificationProfileIds.add("91ad6249-66df-4eaf-a561-a34eaa7b6fef");
        Result result;
	    try{
	    	
	        result = SpeakerRecognitionService.DoSpeakerRecognition(filePath, identificationProfileIds);
	        operationLocation = (String)((HttpSuccess)result).getFirstParameter();	        
	 	
	        System.out.println("operationLocation : " + operationLocation);
	        
	        result = OperationStatusService.getOperationStatus("https://api.projectoxford.ai/spid/v1.0/operations/cf87809a-129b-46ab-a0fb-dc266038b036");
	        System.out.println("speaker : " + (String)((HttpSuccess)result).getSecondParameter());
	        
	    } 
	    catch (Exception ex){
	    	System.out.println("Error in recognitiontest: " + ex.toString());
	    }
	    //return result;
	  }
	  
	  @RequestMapping("/mongodbsavetest")
	  @ResponseBody
	  public void mongodbSaveTest(){
		DB db = mongoTemplate.getDb();
	    GridFS gridFS = new GridFS(db, "easymeeting");
	    try{
		    for(int i = 1; i <= 17; ++i){
		        String sentenceFilePath = "AudioId" + String.valueOf(0) + "sentence" + String.valueOf(i) + ".wav";
		        gridFS.remove(sentenceFilePath); 
		        
		        /* save into mongodb */
		        File tempAudio = new File(sentenceFilePath);
		        GridFSInputFile gfs = gridFS.createFile(tempAudio);
		
		        /* create metadata */
		        DBObject query = new BasicDBObject();
		        query.put("audioId", 0);
		        query.put("speakerName", "wangjiahui");
		        query.put("sequence", i);
		        query.put("sentenceContent", String.valueOf(i) + String.valueOf(i));

		        gfs.setMetaData(query);
		        gfs.setContentType("audio/wav");
		        gfs.setFilename(sentenceFilePath);
		        gfs.save(); 
		        System.out.println("save success : " + sentenceFilePath);
		    }	
	    } 
	    catch (Exception ex){
	    	System.out.println("Error in mongodbsavetest: " + ex.toString());
	    }
	  }
	  
	  @RequestMapping("/mongodbfindtest")
	  @ResponseBody
	  public List<AudioAnalysis> mongodbFindTest(){
		List<GridFSDBFile> gridfsdbfiles = new ArrayList<GridFSDBFile>();
		List<AudioAnalysis> audioAnalysises = new ArrayList<AudioAnalysis>();
		DB db = mongoTemplate.getDb();
        GridFS gridFS = new GridFS(db, "easymeeting");
	    try{
	        for(int i = 1; i < (1<<19); ++i){
	            String sentenceFilePath = "AudioId" + String.valueOf(0) + "sentence" + String.valueOf(i) + ".wav";
	            GridFSDBFile dbf = gridFS.findOne(sentenceFilePath);
	            if(dbf != null){
	            	gridfsdbfiles.add(dbf);
	            }
	            else{
	            	break;
	            }
	        }
	        System.out.println(gridfsdbfiles.size());
	        System.out.println("get file list in mongodb database success");
	        for(int i = 0; i < gridfsdbfiles.size(); ++i){
	        	GridFSDBFile gridfsdbfile = gridfsdbfiles.get(i);
	        	AudioAnalysis audioAnalysis = new AudioAnalysis();
	        	String speakerName;
	        	String sentenceContent;
	        	String fileName;
	        	File sentenceFile;
	        	
	        	/* get speakerName */
	        	speakerName = (String)gridfsdbfile.getMetaData().get("speakerName");
	        	
	        	/* get sentenceContent */
	        	sentenceContent = (String)gridfsdbfile.getMetaData().get("sentenceContent");
	        	
	        	/* get sentenceFile */
	        	fileName = gridfsdbfile.getFilename();
	        	gridfsdbfile.writeTo(new File(fileName));
	        	sentenceFile = new File(fileName);
	        	
	        	/* set audioAnalysis */
	        	audioAnalysis.setSentenceContent(sentenceContent);
	        	audioAnalysis.setSentenceFile(sentenceFile);
	        	audioAnalysis.setSpeakerName(speakerName);
	        	
	        	audioAnalysises.add(audioAnalysis);
	        }
	        /* test */       
	        //System.out.println(gridFS.find("AudioId0sentence1.wav").size());
	        //GridFSDBFile dbfile = gridFS.findOne("AudioId0sentence1.wav");
	        
			//dbfile.writeTo(new File("cccccc.wav"));
	        System.out.println("get all attributes needed success");
		    return audioAnalysises;
	    } 
	    catch (Exception ex){
	    	System.out.println("Error in mongodbfindtest: " + ex.toString());
	    	return null;
	    }
	  }
	  
	  @RequestMapping("/filetest")
	  @ResponseBody
	  public File fileTest(){
        try{
		    File ff = new File("wangjiahui.wav");
		    return ff;
	    }
	    catch (Exception ex){
	    	System.out.println("Error in splitbysentencetest: " + ex.toString());
	    	return null;
	    }
	    //return result;
	  }
}
