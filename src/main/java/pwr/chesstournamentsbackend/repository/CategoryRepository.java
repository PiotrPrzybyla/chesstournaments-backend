package pwr.chesstournamentsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.chesstournamentsbackend.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
