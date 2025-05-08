package ku_rum.backend.domain.friend.application;

import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.FriendStatus;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.friend.dto.response.FriendListResponse;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.global.exception.friend.NoFriendsException;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ku_rum.backend.domain.friend.domain.FriendStatus.*;
import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;
import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.NO_SUCH_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FriendQueryService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public List<FriendListResponse> getFriendListResponses(Set<User> userLists) {
        return userLists.stream()
                .map(userList -> new FriendListResponse(userList.getId(), userList.getNickname()))
                .collect(Collectors.toList());
    }

    public Set<User> getUserSet(List<Friend> friends, User user) {
        Set<User> userLists = friends.stream()
                .flatMap(friend -> Stream.of(friend.getFromUser(), friend.getToUser()))
                .filter(users -> !users.equals(user))
                .collect(Collectors.toSet());
        return userLists;
    }

    public List<Friend> getFriendList(User user, FriendStatus friendStatus) {
        List<Friend> friends = friendRepository.findFriends(friendStatus, user.getId());
        if (friends.isEmpty()) {
            throw new NoFriendsException(NO_FRIENDS_FOUND);
        }
        return friends;
    }

    public User getUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
    }

    public Friend findPendingFriend(User fromUser, User toUser) {
        return friendRepository.findFirstByFromUserAndToUserAndStatus(fromUser, toUser, PENDING)
                .orElseThrow(() -> new NoFriendsException(NO_FRIENDS_FOUND));
    }
}
