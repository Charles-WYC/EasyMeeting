package em.services;

import org.springframework.web.multipart.MultipartFile;

import em.result.Result;

public interface AudioService{
	
	public Result saveAudio(MultipartFile audio, String MeetingName);
    public Result saveTranslateText(int audioId, String translateText);
    public Result deleteAudio(String meetingName, String audioName);
    public Result modifyAudioName(String meetingName, String audioName, String newAudioName);
    public Result getAnalysis(String meetingName, String audioName);
}
