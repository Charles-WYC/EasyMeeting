package em.daos;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import em.models.Friend;
import em.models.Meeting;

@Transactional
public interface FriendDao extends CrudRepository<Friend, Integer>{
    public List<Friend> findByUserId(int userId);
    public Friend findByUserIdAndFriendId(int userId, int friendId);
}
