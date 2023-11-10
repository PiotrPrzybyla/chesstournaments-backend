package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.Tournament;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    List<Tournament> findByDateAfter(LocalDateTime date);
    List<Tournament> findByDateBefore(LocalDateTime date);
    List<Tournament> findByUsers_userId(Integer userId);
    List<Tournament> findByOrganizer_OrganizerId(Integer organizerId);
}
