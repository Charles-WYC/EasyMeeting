package em.models;

import java.io.File;

public class AudioAnalysis{
	
	private File sentenceFile;

	private String sentenceContent;
	
	private String speakerName;

	public File getSentenceFile() {
		return sentenceFile;
	}

	public void setSentenceFile(File sentenceFile) {
		this.sentenceFile = sentenceFile;
	}

	public String getSentenceContent() {
		return sentenceContent;
	}

	public void setSentenceContent(String sentenceContent) {
		this.sentenceContent = sentenceContent;
	}

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}

}
