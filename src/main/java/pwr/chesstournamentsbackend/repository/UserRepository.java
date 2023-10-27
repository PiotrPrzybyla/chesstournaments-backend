package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
