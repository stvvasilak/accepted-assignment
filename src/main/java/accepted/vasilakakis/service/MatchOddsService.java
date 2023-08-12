package accepted.vasilakakis.service;

import accepted.vasilakakis.domain.Match;
import accepted.vasilakakis.domain.MatchOdd;
import accepted.vasilakakis.model.constants.Constants;
import accepted.vasilakakis.model.request.CreateMatchOddsRequest;
import accepted.vasilakakis.model.request.CreateMatchRequest;
import accepted.vasilakakis.model.request.UpdateMatchOddsRequest;
import accepted.vasilakakis.model.request.UpdateMatchRequest;
import accepted.vasilakakis.model.response.MatchOddResponse;
import accepted.vasilakakis.model.response.MatchResponse;
import accepted.vasilakakis.repository.MatchOddRepository;
import accepted.vasilakakis.repository.MatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MatchOddsService {

    private final MatchRepository matchRepository;
    private final MatchOddRepository matchOddRepository;
    private final ConversionService conversionService;

    public ResponseEntity<Object> getMatchData(long matchId) {
        var match = matchRepository.findById(matchId);
        return match.<ResponseEntity<Object>>map(value -> ResponseEntity.status(HttpStatus.OK).body(conversionService.convert(value, MatchResponse.class))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.MATCH_DATA_NOT_FOUND));
    }
    public ResponseEntity<Object> createMatchData(CreateMatchRequest createMatchRequest) {
        try {
            Match newMatch = Match.builder()
                    .matchDate(LocalDate.parse(createMatchRequest.getMatchDate(), DateTimeFormatter.ofPattern(Constants.LOCAL_DATE_PATTERN)))
                    .matchTime(LocalTime.parse(createMatchRequest.getMatchTime()))
                    .teamA(createMatchRequest.getTeamA())
                    .teamB(createMatchRequest.getTeamB())
                    .description(createMatchRequest.getTeamA() + "-" + createMatchRequest.getTeamB())
                    .sport(createMatchRequest.getSport().getValue())
                    .build();
            var savedMatch = matchRepository.save(newMatch);
            return ResponseEntity.status(HttpStatus.CREATED).body(conversionService.convert(savedMatch, MatchResponse.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ERROR_CREATING_MATCH_DATA);
        }
    }

    public ResponseEntity<Object> updateMatchData(long matchId, UpdateMatchRequest updateMatchRequest) {
        var match = matchRepository.findById(matchId);
        if (match.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MATCH_DATA_NOT_FOUND);
        }
        match.get().setMatchDate(LocalDate.parse(updateMatchRequest.getMatchDate(), DateTimeFormatter.ofPattern(Constants.LOCAL_DATE_PATTERN)));
        match.get().setMatchTime(LocalTime.parse(updateMatchRequest.getMatchTime()));
        match.get().setTeamA(updateMatchRequest.getTeamA());
        match.get().setTeamB(updateMatchRequest.getTeamB());
        match.get().setDescription(updateMatchRequest.getTeamA() + "-" + updateMatchRequest.getTeamB());
        match.get().setSport(updateMatchRequest.getSport().getValue());
        matchRepository.save(match.get());
        return ResponseEntity.status(HttpStatus.OK).body(conversionService.convert(match.get(), MatchResponse.class));
    }

    public ResponseEntity<Object> deleteMatchData(long matchId) {
        var match = matchRepository.findById(matchId);
        if (match.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MATCH_DATA_NOT_FOUND);
        }
        List<MatchOdd> matchOdds = matchOddRepository.findAllByMatchId(match.get());
        matchOdds.forEach(odd -> matchOddRepository.deleteById(odd.getId()));
        matchRepository.deleteById(matchId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    public ResponseEntity<Object> getMatchOdds(long matchId) {
        var match = matchRepository.findById(matchId);
        if (match.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ODDS_NOT_FOUND);
        }
        List<MatchOdd> matchOdds = matchOddRepository.findAllByMatchId(match.get());
        List<MatchOddResponse> matchOddsResponse = new ArrayList<>();
        matchOdds.forEach(matchOdd -> matchOddsResponse.add(conversionService.convert(matchOdd, MatchOddResponse.class)));
        return ResponseEntity.status(HttpStatus.OK).body(matchOddsResponse);
    }
    public ResponseEntity<Object> createMatchOdd(CreateMatchOddsRequest createMatchOddsRequest) {
        var match = matchRepository.findById(createMatchOddsRequest.getMatchId());
        if (match.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MATCH_DATA_NOT_FOUND);
        }
        var existingMatchOdd = matchOddRepository.findByMatchIdAndSpecifier(match.get(), createMatchOddsRequest.getSpecifier().getValue());
        if (existingMatchOdd.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ODD_ALREADY_PRESENT);
        }
        MatchOdd matchOdd = MatchOdd.builder()
                .matchId(match.get())
                .specifier(createMatchOddsRequest.getSpecifier().getValue())
                .odd(createMatchOddsRequest.getOdd())
                .build();
        matchOddRepository.save(matchOdd);
        return ResponseEntity.status(HttpStatus.CREATED).body(conversionService.convert(matchOdd, MatchOddResponse.class));
    }

    public ResponseEntity<Object> updateMatchOdd(long oddId, UpdateMatchOddsRequest updateMatchOddsRequest) {
        var matchOdd = matchOddRepository.findById(oddId);
        if (matchOdd.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ODD_NOT_FOUND);
        }
        matchOdd.get().setOdd(updateMatchOddsRequest.getUpdatedOdd());
        matchOddRepository.save(matchOdd.get());
        return ResponseEntity.status(HttpStatus.OK).body(conversionService.convert(matchOdd.get(), MatchOddResponse.class));
    }

    public ResponseEntity<Object> deleteMatchOdd(long oddId) {
        var matchOdd = matchOddRepository.findById(oddId);
        if (matchOdd.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ODD_NOT_FOUND);
        }
        matchOddRepository.deleteById(oddId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
