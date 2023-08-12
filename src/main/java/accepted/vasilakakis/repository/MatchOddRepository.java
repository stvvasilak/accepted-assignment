package accepted.vasilakakis.repository;

import accepted.vasilakakis.domain.Match;
import accepted.vasilakakis.domain.MatchOdd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchOddRepository extends JpaRepository<MatchOdd,Long> {

    Optional<MatchOdd> findByMatchIdAndSpecifier(Match match, String specifier);

    List<MatchOdd> findAllByMatchId(Match match);
}
