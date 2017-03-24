package em.daos;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import em.models.Meeting;

@Transactional
public interface MeetingDao extends CrudRepository<Meeting, Integer> {
  public Meeting findByMeetingName(String meetingName);
  public List<Meeting> findAll();
  public List<Meeting> findByUserId(int userId);
  public Meeting findByUserIdAndMeetingName(int userId, String meetingName);
} 