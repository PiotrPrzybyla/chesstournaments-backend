package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUid(String uid);
}
