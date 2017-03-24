package em.daos;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import em.models.User;

@Transactional
public interface UserDao extends CrudRepository<User, Integer> {

  public User findByUserName(String userName);
  public List<User> findAll();
  public User findByUserNameAndUserPwd(String userName, String userPwd);
  public User findByUserId(int userId);

  public List<User> findByUserNameLike(String userName);
  public User findByVoiceId(String voiceId);

} // class UserDaoeger