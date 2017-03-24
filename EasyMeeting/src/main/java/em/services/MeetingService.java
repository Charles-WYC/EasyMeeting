package em.services;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import em.models.Meeting;
import em.result.Result;

@Service
public interface MeetingService{
    public Result create(String meetingName);
    public Result findMyMeetings();
    public Result addParticipant(String meetingName, List<String> userNames);
    public Result findAllParticipants(String MeetingName);
    public Result findFriendsNotInMeeting(String MeetingName);
    public Result deleteParticipant(String meetingName, List<String> userNames);
    
    
}