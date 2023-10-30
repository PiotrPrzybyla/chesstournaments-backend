package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.Organizer;

import java.util.Optional;


public interface OrganizerRepository extends JpaRepository<Organizer, Integer> {
    Optional<Organizer> findByUserUserId(Integer userUserId);
}
