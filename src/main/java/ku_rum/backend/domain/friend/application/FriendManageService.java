package ku_rum.backend.domain.friend.application;

import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.friend.domain.vo.FriendStatus;
import ku_rum.backend.domain.user.application.UserQueryService;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.exception.global.GlobalException;
import ku_rum.backend.global.utill.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendManageService {

    private final FriendRepository friendRepository;
    private final UserUtil userUtil;
    private final UserQueryService userQueryService;

    // 친구 요청
    public void requestFriend(Long toUserId) {
        User fromUser = userUtil.getUser();
        User toUser = userQueryService.getUserById(toUserId);

        if (friendRepository.findByFromUserAndToUser(fromUser, toUser).isPresent()) {
            throw new GlobalException(DUPLICATE_FRIENDS);
        }
        friendRepository.save(Friend.of(fromUser, toUser, FriendStatus.PENDING));
    }

    // 친구 수락 및 거절
    public void respondToFriend(Long requestId, boolean accept) {
        User currentUser = userUtil.getUser();
        Friend friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new GlobalException(NO_PENDING_LIST));

        if (!friend.getToUser().equals(currentUser)) {
            throw new GlobalException(NOT_EQUAL_TO_USER);
        }
        friend.setStatus(accept ? FriendStatus.ACCEPT : FriendStatus.REJECT);
    }

    // 보낸 친구 요청 삭제
    public void deleteSentRequest(Long requestId) {
        User currentUser = userUtil.getUser();
        Friend friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new GlobalException(NO_PENDING_LIST));

        if (!friend.getFromUser().equals(currentUser)) {
            throw new GlobalException(NOT_EQUAL_TO_USER);
        }

        if (friend.getStatus() != FriendStatus.PENDING) {
            throw new GlobalException(DUPLICATE_RESPONSE);
        }

        friendRepository.delete(friend);
    }

}