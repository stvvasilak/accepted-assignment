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
public class CreateMatchRequest {

    @NotBlank
    @NotNull
    private String teamA;
    @NotBlank
    @NotNull
    private String teamB;
    @NotBlank
    @NotNull
    private String matchDate;
    @NotBlank
    @NotNull
    private String matchTime;
    @NotNull
    private Sport sport;
}
