package ku_rum.backend.domain.friend.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.FriendStatus;
import ku_rum.backend.domain.friend.domain.QFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ku_rum.backend.domain.friend.domain.QFriend.friend;

@Repository
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public FriendRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Friend> findFriends(FriendStatus status, Long userId) {
        QFriend friend = QFriend.friend;

        return queryFactory.selectFrom(friend)
                .leftJoin(friend.fromUser).fetchJoin()
                .leftJoin(friend.toUser).fetchJoin()
                .where(friend.status.eq(status)
                        .and(friend.fromUser.id.eq(userId).or(friend.toUser.id.eq(userId))))
                .fetch();
    }

    @Override
    public Boolean existFriends(FriendStatus status, Long fromUser, Long toUser) {
        Integer result = queryFactory
                .selectOne()
                .from(friend)
                .where(friend.status.eq(status)
                        .and(
                                (friend.fromUser.id.eq(fromUser).and(friend.toUser.id.eq(toUser)))  // `fromUser.id`가 `fromUser`와 일치하고, `toUser.id`가 `toUser`와 일치하는 데이터
                                        .or(friend.fromUser.id.eq(toUser).and(friend.toUser.id.eq(fromUser)))  // `fromUser.id`가 `toUser`와 일치하고, `toUser.id`가 `fromUser`와 일치하는 데이터
                        ))
                .fetchFirst();

        return result != null;
    }

    @Override
    public List<Friend> findOriginFriends(FriendStatus status, Long fromUser, Long toUser) {
        QFriend friend = QFriend.friend;

        List<Friend> fetch = queryFactory.selectFrom(friend)
                .leftJoin(friend.fromUser).fetchJoin()
                .leftJoin(friend.toUser).fetchJoin()
                .where(friend.status.eq(status)
                        .and(
                                (friend.fromUser.id.eq(fromUser).or(friend.toUser.id.eq(toUser)))
                                        .or(
                                                friend.fromUser.id.eq(toUser).or(friend.toUser.id.eq(fromUser))
                                        )
                        ))
                .fetch();
        return fetch;
    }

}
