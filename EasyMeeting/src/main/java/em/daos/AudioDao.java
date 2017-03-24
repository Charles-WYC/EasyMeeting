package em.daos;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import em.models.Audio;

@Transactional
public interface AudioDao extends CrudRepository<Audio, Integer>{
    public Audio findByUserIdAndMeetingIdAndAudioName(int userId, int meetingId, String audioName);
}
