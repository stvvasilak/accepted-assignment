package accepted.vasilakakis.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MatchOddResponse {
    private long id;

    private long matchId;

    private String specifier;

    private double odd;
}
