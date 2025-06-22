package ku_rum.backend.domain.friend.domain.repository;

import ku_rum.backend.domain.friend.domain.Friend;
import ku_rum.backend.domain.friend.domain.vo.FriendStatus;
import ku_rum.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    void deleteByFromUserAndToUser(User fromUser, User toUser);

    Optional<Friend> findByFromUserAndToUser(User fromUser, User toUser);
    List<Friend> findByToUserAndStatus(User toUser, FriendStatus status);
    List<Friend> findByFromUserAndStatus(User fromUser, FriendStatus status);

    @Query("SELECT f.toUser.id FROM Friend f " +
            "WHERE f.fromUser.id = :fromUserId AND f.toUser.id IN :targetUserIds AND f.status = :status")
    List<Long> findToUserIdsByFromUserAndStatus(@Param("fromUserId") Long fromUserId,
                                                @Param("targetUserIds") List<Long> targetUserIds,
                                                @Param("status") FriendStatus status);

    @Query("SELECT f.toUser.id FROM Friend f " +
            "WHERE f.fromUser.id = :currentUserId AND f.toUser.id IN :targetUserIds AND f.status = :status " +
            "UNION " +
            "SELECT f.fromUser.id FROM Friend f " +
            "WHERE f.toUser.id = :currentUserId AND f.fromUser.id IN :targetUserIds AND f.status = :status")
    List<Long> findFriendUserIds(@Param("currentUserId") Long currentUserId,
                                 @Param("targetUserIds") List<Long> targetUserIds,
                                 @Param("status") FriendStatus status);

    boolean existsByFromUserAndToUserAndStatus(User fromUser, User toUser, FriendStatus status);

    @Query(value = """
        SELECT u.* FROM users u
        JOIN friend f ON ( (f.from_user_id = :currentUserId AND f.to_user_id = u.id) 
                        OR (f.to_user_id = :currentUserId AND f.from_user_id = u.id) )
        WHERE u.active = true
          AND u.active_building_name = :name
          AND f.status = 'ACCEPT'
        """, nativeQuery = true)
    List<User> findActiveFriendsInPlace(@Param("name") String name, @Param("currentUserId") Long currentUserId);

    @Query(value = """
    SELECT u.* FROM users u
    JOIN friend f ON (
        (f.from_user_id = :currentUserId AND f.to_user_id = u.id) OR
        (f.to_user_id = :currentUserId AND f.from_user_id = u.id)
    )
    WHERE f.status = :status
    """, nativeQuery = true)
    List<User> findFriendsByUserIdAndStatus(@Param("currentUserId") Long currentUserId,
                                            @Param("status") FriendStatus status);

    @Query(value = """
    SELECT u.*
    FROM user u
    JOIN friend f ON (
        (f.from_user_id = :currentUserId AND f.to_user_id = u.id)
        OR (f.to_user_id = :currentUserId AND f.from_user_id = u.id)
    )
    WHERE f.status = 'ACCEPT'
      AND u.active_building_name = :buildingName
    """, nativeQuery = true)
    List<User> findActiveFriendsInPlaceByBuildingId(@Param("currentUserId") Long currentUserId, @Param("buildingName") String buildingName);
}