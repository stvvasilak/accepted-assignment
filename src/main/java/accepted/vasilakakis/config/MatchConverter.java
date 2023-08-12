package accepted.vasilakakis.config;

import accepted.vasilakakis.domain.Match;
import accepted.vasilakakis.model.enums.Sport;
import accepted.vasilakakis.model.response.MatchResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class MatchConverter implements Converter<Match, MatchResponse> {

    @Override
    public MatchResponse convert(Match match) {
        return MatchResponse.builder()
                .id(match.getId())
                .matchTime(match.getMatchTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .matchDate(match.getMatchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .sport(Sport.fromValue(match.getSport()).name())
                .description(match.getDescription())
                .teamA(match.getTeamA())
                .teamB(match.getTeamB())
                .build();
    }

}
