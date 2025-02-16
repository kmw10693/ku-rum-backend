package ku_rum.backend.domain.friend.domain.repository;

import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.FriendStatus;

import java.util.List;

public interface FriendRepositoryCustom {
    List<Friend> findFriends(FriendStatus status, Long userId);
    Boolean existFriends(FriendStatus status, Long fromUser, Long toUser);
    List<Friend> findOriginFriends(FriendStatus status, Long fromUser, Long toUser);
}
