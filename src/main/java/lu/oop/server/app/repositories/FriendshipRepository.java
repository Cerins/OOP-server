package lu.oop.server.app.repositories;

import lu.oop.server.app.models.users.FriendshipModel;
import lu.oop.server.app.models.users.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipModel, Long> {

    List<FriendshipModel> findByRequestor(UserModel requestor);

    List<FriendshipModel> findByResponder(UserModel responder);

    Optional<FriendshipModel> findByRequestorAndResponder(UserModel requestor, UserModel responder);

    @Query("SELECT f FROM FriendshipModel f WHERE (f.requestor = :user OR f.responder = :user) AND f.status = :status")
    List<FriendshipModel> findByUserAndStatus(@Param("user") UserModel user, @Param("status") int status);

    @Query("SELECT f FROM FriendshipModel f WHERE f.requestor = :user AND f.status = :status")
    List<FriendshipModel> findByRequestorAndStatus(@Param("user") UserModel user, @Param("status") int status);

    @Query("SELECT f FROM FriendshipModel f WHERE f.responder = :user AND f.status = :status")
    List<FriendshipModel> findByResponderAndStatus(@Param("user") UserModel user, @Param("status") int status);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FriendshipModel f WHERE (f.requestor = :user1 AND f.responder = :user2) OR (f.requestor = :user2 AND f.responder = :user1)")
    boolean existsByUsers(@Param("user1") UserModel user1, @Param("user2") UserModel user2);
}
