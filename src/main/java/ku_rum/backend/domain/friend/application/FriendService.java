package ku_rum.backend.domain.friend.application;

import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.FriendStatus;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.friend.dto.response.FriendFindResponse;
import ku_rum.backend.domain.friend.dto.response.FriendListResponse;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.global.exception.friend.NoFriendsException;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import ku_rum.backend.global.security.jwt.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ku_rum.backend.domain.friend.domain.FriendStatus.*;
import static ku_rum.backend.global.response.status.BaseExceptionResponseStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    public List<FriendListResponse> getMyLists() {
        User user = getUser();
        List<Friend> friends = getFriendList(user, ACCEPT);

        Set<User> userLists = getUserSet(friends, user);
        return getFriendListResponses(userLists);
    }

    public FriendFindResponse findByNameInLists(String nickname) {
        User user = getUser();
        User toUser = userRepository.findUserByNickname(nickname)
                .orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));

        if (!friendRepository.existFriends(ACCEPT, user.getId(), toUser.getId()))
            throw new NoFriendsException(NO_FRIENDS_FOUND);

        return FriendFindResponse.from(toUser);
    }

    @Transactional
    public void requestFriends(Long requestId) {
        User fromUser = getUser();
        User toUser = userRepository.findUserById(requestId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));

        //이미 존재하면 거절
        if (friendRepository.existFriends(ACCEPT, fromUser.getId(), toUser.getId()))
            throw new NoFriendsException(DUPLICATE_FRIENDS);

        Friend friend = Friend.of(fromUser, toUser, PENDING);
        friendRepository.save(friend);
    }

    @Transactional
    public void accept(Long requestId) {
        User fromUser = userRepository.findUserById(requestId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
        User touser = getUser();

        Friend friend = friendRepository.findFirstByFromUserAndToUserAndStatus(fromUser, touser, PENDING).orElseThrow(() -> new NoFriendsException(NO_FRIENDS_FOUND));
        friend.setStatus(ACCEPT);
    }

    @Transactional
    public void deny(Long requestId) {
        User fromUser = userRepository.findUserById(requestId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
        User touser = getUser();

        Friend friend = friendRepository.findFirstByFromUserAndToUserAndStatus(fromUser, touser, PENDING).orElseThrow(() -> new NoFriendsException(NO_FRIENDS_FOUND));
        friend.setStatus(REJECT);
    }

    @Transactional
    public void delete(Long requestId) {
        User fromUser = userRepository.findUserById(requestId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
        User touser = getUser();

        List<Friend> friends = friendRepository.findOriginFriends(ACCEPT, fromUser.getId(), touser.getId());
        friends.forEach(friend -> friend.setStatus(REJECT));
    }

    private static List<FriendListResponse> getFriendListResponses(Set<User> userLists) {
        return userLists.stream()
                .map(userList -> new FriendListResponse(userList.getId(), userList.getNickname()))
                .collect(Collectors.toList());
    }

    private static Set<User> getUserSet(List<Friend> friends, User user) {
        Set<User> userLists = friends.stream()
                .flatMap(friend -> Stream.of(friend.getFromUser(), friend.getToUser()))
                .filter(users -> !users.equals(user))
                .collect(Collectors.toSet());
        return userLists;
    }

    private List<Friend> getFriendList(User user, FriendStatus friendStatus) {
        List<Friend> friends = friendRepository.findFriends(friendStatus, user.getId());
        if (friends.isEmpty()) {
            throw new NoFriendsException(NO_FRIENDS_FOUND);
        }
        return friends;
    }

    User getUser() {
        Long memberId = userUtils.getLongMemberId();
        User user = userRepository.findUserById(memberId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
        return user;
    }
}
