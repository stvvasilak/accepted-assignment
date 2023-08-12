package accepted.vasilakakis.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MatchResponse {
    private long id;

    private String description;

    private String matchDate;

    private String matchTime;

    private String teamA;

    private String teamB;

    private String sport;
}
