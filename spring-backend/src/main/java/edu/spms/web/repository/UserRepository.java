package edu.spms.web.repository;
import edu.spms.web.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByUsernameIgnoreCaseAndActiveTrue(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
