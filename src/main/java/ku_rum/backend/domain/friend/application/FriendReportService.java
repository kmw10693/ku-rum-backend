package ku_rum.backend.domain.friend.application;

import ku_rum.backend.domain.friend.domain.FriendBlock;
import ku_rum.backend.domain.friend.domain.FriendReport;
import ku_rum.backend.domain.friend.domain.repository.FriendBlockRepository;
import ku_rum.backend.domain.friend.domain.repository.FriendReportRepository;
import ku_rum.backend.domain.friend.domain.repository.FriendRepository;
import ku_rum.backend.domain.friend.dto.request.FriendBlockRequest;
import ku_rum.backend.domain.friend.dto.request.FriendReportRequest;
import ku_rum.backend.domain.user.application.UserQueryService;
import ku_rum.backend.domain.user.domain.User;
import ku_rum.backend.global.exception.global.GlobalException;
import ku_rum.backend.global.utill.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.DUPLICATE_BLOCK;
import static ku_rum.backend.global.support.status.BaseExceptionResponseStatus.NO_BLOCK_MYSELF;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FriendReportService {
    private final FriendBlockRepository friendBlockRepository;
    private final FriendRepository friendRepository;
    private final UserQueryService userQueryService;
    private final UserUtil userUtil;
    private final FriendReportRepository friendReportRepository;

    @Transactional
    public void reportFriend(final FriendReportRequest friendReportRequest) {
        User fromUser = userUtil.getUser();
        User toUser = userQueryService.getUserById(friendReportRequest.reportId());

        if (fromUser.getId().equals(toUser.getId())) {
            throw new GlobalException(NO_BLOCK_MYSELF);
        }

        boolean alreadyReported = friendReportRepository
                .existsByFromUserAndToUser(fromUser, toUser);
        if (alreadyReported) {
            throw new GlobalException(DUPLICATE_BLOCK);
        }

        FriendReport report = FriendReport.of(fromUser, toUser, friendReportRequest.reason());
        friendReportRepository.save(report);
    }

    @Transactional
    public void blockFriend(final FriendBlockRequest friendBlockRequest) {
        User fromUser = userUtil.getUser();
        User toUser = userQueryService.getUserById(friendBlockRequest.reportId());

        if (fromUser.getId().equals(toUser.getId())) {
            throw new GlobalException(NO_BLOCK_MYSELF);
        }

        boolean alreadyBlocked = friendBlockRepository.existsByFromUserAndToUser(fromUser, toUser);
        if (alreadyBlocked) {
            throw new GlobalException(DUPLICATE_BLOCK);
        }

        friendRepository.deleteByFromUserAndToUser(fromUser, toUser);
        friendRepository.deleteByFromUserAndToUser(toUser, fromUser);

        FriendBlock block = FriendBlock.of(fromUser, toUser);
        friendBlockRepository.save(block);
    }
}
