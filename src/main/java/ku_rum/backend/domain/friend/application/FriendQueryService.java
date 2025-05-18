package ku_rum.backend.domain.friend.application;

import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.friend.domain.vo.FriendStatus;
import ku_rum.backend.domain.friend.dto.response.FriendListResponse;
import ku_rum.backend.domain.friend.dto.response.ReceivedFriendResponse;
import ku_rum.backend.domain.friend.dto.response.SentFriendResponse;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.utill.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendQueryService {

    private final FriendRepository friendRepository;
    private final UserUtil userUtil;

    // 친구 목록 조회
    public List<FriendListResponse> getFriendList() {
        User currentUser = userUtil.getUser();

        List<Friend> sent = friendRepository.findByFromUserAndStatus(currentUser, FriendStatus.ACCEPT);
        List<Friend> received = friendRepository.findByToUserAndStatus(currentUser, FriendStatus.ACCEPT);

        return Stream.concat(
                sent.stream().map(f -> FriendListResponse.from(f.getToUser())),
                received.stream().map(f -> FriendListResponse.from(f.getFromUser()))
        ).collect(Collectors.toList());
    }

    // 받은 요청 목록 조회
    public List<ReceivedFriendResponse> getReceivedPendingRequests() {
        User currentUser = userUtil.getUser();

        return friendRepository.findByToUserAndStatus(currentUser, FriendStatus.PENDING)
                .stream()
                .map(ReceivedFriendResponse::from)
                .collect(Collectors.toList());
    }

    public List<SentFriendResponse> getSentPendingRequests() {
            User currentUser = userUtil.getUser();
            List<Friend> sentRequests = friendRepository.findByFromUserAndStatus(currentUser, FriendStatus.PENDING);

            return sentRequests.stream()
                    .map(req -> new SentFriendResponse(
                            req.getId(),
                            req.getToUser().getId(),
                            req.getToUser().getNickname(),
                            req.getToUser().getImageUrl()
                    ))
                    .toList();
        }
}
