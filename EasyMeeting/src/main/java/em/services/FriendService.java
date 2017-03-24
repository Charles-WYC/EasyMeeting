package em.services;

import org.springframework.stereotype.Service;

import em.result.Result;

@Service
public interface FriendService {
    public Result findMyFriends();
    public Result create(String userName);
    public Result delete(String userName);
}
