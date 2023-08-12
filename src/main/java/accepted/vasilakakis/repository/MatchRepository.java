package accepted.vasilakakis.repository;

import accepted.vasilakakis.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match,Long> {
}
