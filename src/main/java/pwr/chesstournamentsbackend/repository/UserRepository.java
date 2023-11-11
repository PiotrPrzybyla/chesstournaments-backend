package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUid(String uid);
    Set<User> findByTournaments_TournamentId(Integer tournamentId);
}
