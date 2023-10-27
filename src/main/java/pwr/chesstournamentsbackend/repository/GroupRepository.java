package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}
