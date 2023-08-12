package accepted.vasilakakis.config;

import accepted.vasilakakis.domain.MatchOdd;
import accepted.vasilakakis.model.response.MatchOddResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class MatchOddConverter implements Converter<MatchOdd, MatchOddResponse> {

    @Override
    public MatchOddResponse convert(MatchOdd matchOdd) {
        return MatchOddResponse.builder()
                .id(matchOdd.getId())
                .matchId(matchOdd.getMatchId().getId())
                .specifier(matchOdd.getSpecifier())
                .odd(matchOdd.getOdd())
                .build();
    }

}
