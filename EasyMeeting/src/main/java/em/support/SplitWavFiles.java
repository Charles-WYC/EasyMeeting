package em.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SplitWavFiles{
	
	public static byte[] GetContent(String filePath) throws IOException{  
		 byte[] buffer = null;  
	        try 
	        {  
	            File file = new File(filePath);  
	            FileInputStream fis = new FileInputStream(file);  
	            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
	            byte[] b = new byte[1000];  
	            int n;  
	            while ((n = fis.read(b)) != -1) {  
	                bos.write(b, 0, n);  
	            }  
	            fis.close();  
	            bos.close();  
	            buffer = bos.toByteArray();  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return buffer;  
    }  
	
	public static int CalHeaderSize(String filePath) throws IOException{
		try{
		    byte[] buffer = GetContent(filePath);	
		    InputStream inputStream = new ByteArrayInputStream(buffer); 
		    int singleByte = 0;
		    int headerSize = 0;
		    for(int i = 0; i < buffer.length; ++i){
			    singleByte = inputStream.read();
			    headerSize++;
			    if(singleByte == 100){
		            int c1 = inputStream.read();
		            int c2 = inputStream.read();
		            int c3 = inputStream.read();
		            headerSize += 3;
		            if(c1==97 && c2==116 && c3==97){
		        	    return headerSize+4;
		            }
			    }
		    }
		}
		catch(FileNotFoundException e){  
            e.printStackTrace();  
        } 
		catch (IOException e){  
            e.printStackTrace();  
        }  
		return -1;
	}
	
	public static int SplitWavFile(String filePath, int secondPerFile, int audioId) 
			throws LineUnavailableException, IOException, UnsupportedAudioFileException{
		int number = 1;
		try{
		    /* calculate the header size of wav file */
		    int headerSize = CalHeaderSize(filePath);
		
		    /* get total time of wav */
		    File wav = new File(filePath);
		    Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(wav);
            clip.open(ais);
		    double totalTime = clip.getMicrosecondLength() / 1000000;
		
		    /* caculate necessary attributes */
	        int totalSize = (int)wav.length() - headerSize;
		    int bytesPerSecond = (int)(totalSize / totalTime);
		
		    /* algain to 8 */
		    while(bytesPerSecond % 8 != 0){
			    bytesPerSecond--;
		    }
		
		    int bytesPerFile = bytesPerSecond * secondPerFile;
		
		    /* split into several files according to secondPerFile */
		    byte[] buffer = GetContent(filePath);	
		    InputStream inputStream = new ByteArrayInputStream(buffer); 
		
		    /* get header context */
		    byte[] header = new byte[headerSize];
		    inputStream.read(header);
		
	        /* create splitting files */
		    byte[] b = new byte[bytesPerFile];
		    int readBytes = 0;
		    while(true){
			    readBytes = inputStream.read(b);
			
			    /* get the length of wav file */
			    byte[] targets = new byte[4];  
	            targets[3] = (byte)(readBytes & 0xFF);  
	            targets[2] = (byte)(readBytes >> 8 & 0xFF);  
	            targets[1] = (byte)(readBytes >> 16 & 0xFF);  
	            targets[0] = (byte)(readBytes >> 24 & 0xFF);
	        
	            /* modify header information */
	            byte[] tempHeader = header;
	            tempHeader[tempHeader.length-1] = targets[0];
	            tempHeader[tempHeader.length-2] = targets[1];
	            tempHeader[tempHeader.length-3] = targets[2];
	            tempHeader[tempHeader.length-4] = targets[3];
	        
	            /* create files */
	            String splitFilePath = "AudioId" + String.valueOf(audioId) + "split" + String.valueOf(number) + ".wav";
		        FileOutputStream fos = new FileOutputStream(splitFilePath);
		        fos.write(tempHeader);
		        fos.write(b, 0, readBytes);
		        fos.close();
		        if(readBytes != bytesPerFile){
		    	    return number;
		        }
		        number++;
		    }
		}
		catch(FileNotFoundException e){  
            e.printStackTrace();  
        } 
		catch (IOException e){  
            e.printStackTrace();  
        }  
		return number;
	}
	
	public static void SplitWavFileCovering(String filePath, int secondPerFile, int audioId) 
			throws LineUnavailableException, IOException, UnsupportedAudioFileException{
		try{
		    int fileNumber = SplitWavFile(filePath, secondPerFile/2, audioId);   
		    for(int i = 1; i < fileNumber; ++i){
			    mergeWavFile(i, i+1, audioId);
		    }
		}
		catch(FileNotFoundException e){  
            e.printStackTrace();  
        } 
		catch (IOException e){  
            e.printStackTrace();  
        } 
	}
	
	public static void mergeWavFile(int fileNumberA, int fileNumberB, int audioId) throws IOException{
		try{
		    String filePathA = "AudioId" + String.valueOf(audioId) + "split" + String.valueOf(fileNumberA) + ".wav";
		    String filePathB = "AudioId" + String.valueOf(audioId) + "split" + String.valueOf(fileNumberB) + ".wav";
		
		    /* get two files */
		    File fileA = new File(filePathA);
		    File fileB = new File(filePathB);
		
		    /* convert into byte array */
		    byte[] bufferA = GetContent(filePathA);	
		    byte[] bufferB = GetContent(filePathB);
		
		    /* get headerSize */
		    int headerSize = CalHeaderSize(filePathA);
		
		    /* get header context */
		    InputStream inputStream = new ByteArrayInputStream(bufferA); 
		    byte[] header = new byte[headerSize];
		    inputStream.read(header);
		    inputStream.close();
		
		    /* get audio context from A and B */
		    byte[] tempheader = new byte[headerSize];
		    byte[] audioA = new byte[bufferA.length - headerSize];
	        byte[] audioB = new byte[bufferB.length - headerSize];
	        InputStream inputA = new ByteArrayInputStream(bufferA);
	        InputStream inputB = new ByteArrayInputStream(bufferB);
	        inputA.read(tempheader);
	        inputA.read(audioA);
	        inputB.read(tempheader);
	        inputB.read(audioB);
	        inputA.close();
	        inputB.close();
	    
	        /* modify header */
	        int totalSize = bufferA.length + bufferB.length - 2*headerSize;
		    byte[] targets = new byte[4];  
            targets[3] = (byte)(totalSize & 0xFF);  
            targets[2] = (byte)(totalSize >> 8 & 0xFF);  
            targets[1] = (byte)(totalSize >> 16 & 0xFF);  
            targets[0] = (byte)(totalSize >> 24 & 0xFF);
        
            header[header.length-1] = targets[0];
            header[header.length-2] = targets[1];
            header[header.length-3] = targets[2];
            header[header.length-4] = targets[3];
	    
	        /* create a new wav file that merges A and B */
		    fileA.delete();
		    FileOutputStream fos = new FileOutputStream(filePathA);
		    fos.write(header);
		    fos.write(audioA);
		    fos.write(audioB);
		    fos.close();
		}
		catch(FileNotFoundException e){  
            e.printStackTrace();  
        } 
		catch (IOException e){  
            e.printStackTrace();  
        } 
	}
	
    public static void SplitWavFirstPart(String filePath, int second, int audioId) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
    	try{
    	    /* get header size */
    	    int headerSize = CalHeaderSize(filePath);
    	
		    /* get total time of wav */
		    File wav = new File(filePath);
		    Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(wav);
            clip.open(ais);
		    double totalTime = clip.getMicrosecondLength() / 1000000;
		
		    /* caculate necessary attributes */
	        int totalSize = (int)wav.length() - headerSize;
		    int bytesPerSecond = (int)(totalSize / totalTime);
		
		    /* algain to 8 */
		    while(bytesPerSecond % 8 != 0){
			    bytesPerSecond--;
		    }
		
		    int totalBytes = bytesPerSecond * second;
    	
		    /* split the first {second} seconds into anthor file */ 
		    byte[] buffer = GetContent(filePath);
		    InputStream inputStream = new ByteArrayInputStream(buffer); 
		    byte[] tempB = new byte[headerSize + totalBytes]; 
		    inputStream.read(tempB);
		    
		    String tempFilePath = "AudioId" + String.valueOf(audioId) + String.valueOf(second) + "seconds.wav";
		    FileOutputStream fos = new FileOutputStream(tempFilePath);
		    fos.write(tempB);
    	}
		catch(FileNotFoundException e){  
            e.printStackTrace();  
        } 
		catch (IOException e){  
            e.printStackTrace();  
        } 
		
    }
	
	public static void SplitWavFirstFewSecond(String filePathA, String filePathB) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
		try{
    	    /* get header size */
    	    int headerSize = CalHeaderSize(filePathA);
    
    	    byte[] bufferA = GetContent(filePathA);	
		    byte[] bufferB = GetContent(filePathB);
		    byte[] tempheader = new byte[headerSize];
		    InputStream inputStream = new ByteArrayInputStream(bufferA); 
		
		    /* get header */
		    inputStream.read(tempheader);
		
		    /* read first part */
		    byte[] tempBuffer = new byte[bufferB.length - headerSize];
		    inputStream.read(tempBuffer);
		
		    /* write the splitted wav file */
		    byte[] resultHeader = new byte[headerSize];
		    byte[] resultBody = new byte[bufferA.length - bufferB.length];
		    InputStream is = new ByteArrayInputStream(tempheader);
		    FileOutputStream fos = new FileOutputStream(filePathA);
            is.read(resultHeader);
		    inputStream.read(resultBody);
		    fos.write(resultHeader);
		    fos.write(resultBody);
		    fos.close();
		}		
		catch(FileNotFoundException e){  
            e.printStackTrace();  
        } 
		catch (IOException e){  
            e.printStackTrace();  
        } 
		
	}
	
	public static void copyFile(String filePathA, String filePathB) throws IOException{
		byte[] bufferA = GetContent(filePathA);		
		FileOutputStream fos = new FileOutputStream(filePathB);
		fos.write(bufferA);
		fos.close();
		return;
	}
	
	
	
	
	
}
