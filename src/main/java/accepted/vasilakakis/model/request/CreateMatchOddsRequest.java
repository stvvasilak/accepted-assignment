package accepted.vasilakakis.model.request;

import accepted.vasilakakis.model.enums.Specifier;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateMatchOddsRequest {

    private long matchId;
    @NotNull
    private Specifier specifier;
    private double odd;
}
