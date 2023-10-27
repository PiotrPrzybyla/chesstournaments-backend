package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.Organizer;


public interface OrganizerRepository extends JpaRepository<Organizer, Integer> {
}
