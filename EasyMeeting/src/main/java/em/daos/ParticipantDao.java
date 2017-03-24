package em.daos;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import em.models.Meeting;
import em.models.Participant;

@Transactional
public interface ParticipantDao extends CrudRepository<Participant, Integer>{
    public List<Participant> findByMeetingId(int meetingId);
    public Participant findByMeetingIdAndUserId(int meetingId, int userId);
}
