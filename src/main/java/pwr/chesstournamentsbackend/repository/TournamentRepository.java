package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
}
