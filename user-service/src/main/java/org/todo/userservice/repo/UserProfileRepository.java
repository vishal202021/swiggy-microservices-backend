package org.todo.userservice.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.todo.userservice.model.UserProfile;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findByEmail(String email);
    Optional<UserProfile> findByUserId(Long userId);
}
