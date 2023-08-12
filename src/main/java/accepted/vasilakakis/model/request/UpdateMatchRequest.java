package accepted.vasilakakis.model.request;

import accepted.vasilakakis.model.enums.Sport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateMatchRequest {
    @NotBlank
    private String teamA;
    @NotBlank
    private String teamB;
    @NotBlank
    private String matchDate;
    @NotBlank
    private String matchTime;
    @NotNull
    private Sport sport;
}
