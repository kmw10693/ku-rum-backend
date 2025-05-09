package ku_rum.backend.domain.friend.application;

import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.friend.dto.response.FriendFindResponse;
import ku_rum.backend.domain.friend.dto.response.FriendListResponse;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.domain.user.domain.repository.UserRepository;
import ku_rum.backend.global.exception.friend.NoFriendsException;
import ku_rum.backend.global.exception.user.NoSuchUserException;
import ku_rum.backend.global.utill.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static ku_rum.backend.domain.friend.domain.FriendStatus.*;
import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;
import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.NO_SUCH_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final FriendQueryService friendQueryService;

    public List<FriendListResponse> getMyLists() {
        User user = getUser();
        List<Friend> friends = friendQueryService.getFriendList(user, ACCEPT);

        Set<User> userLists = friendQueryService.getUserSet(friends, user);
        return friendQueryService.getFriendListResponses(userLists);
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
    public void acceptFriendRequest(Long requestId) {
        User fromUser = friendQueryService.getUserById(requestId);
        User touser = getUser();

        Friend friend = friendQueryService.findPendingFriend(fromUser, touser);
        friend.setStatus(ACCEPT);
    }

    @Transactional
    public void denyFriendRequest(Long requestId) {
        User fromUser = friendQueryService.getUserById(requestId);
        User touser = getUser();

        Friend friend = friendQueryService.findPendingFriend(fromUser, touser);
        friend.setStatus(REJECT);
    }

    @Transactional
    public void deleteFriendRequest(Long requestId) {
        User fromUser = friendQueryService.getUserById(requestId);
        User touser = getUser();

        List<Friend> friends = friendRepository.findOriginFriends(ACCEPT, fromUser.getId(), touser.getId());
        friends.forEach(friend -> friend.setStatus(REJECT));
    }

    User getUser() {
        Long memberId = UserUtil.getLongMemberId();
        User user = userRepository.findUserById(memberId).orElseThrow(() -> new NoSuchUserException(NO_SUCH_USER));
        return user;
    }
}
