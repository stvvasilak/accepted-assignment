package accepted.vasilakakis.controller;

import accepted.vasilakakis.model.request.CreateMatchRequest;
import accepted.vasilakakis.model.request.UpdateMatchRequest;
import accepted.vasilakakis.service.MatchOddsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchOddsService matchOddsService;

    @Operation(summary = "Retrieve match data by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of match data!"),
            @ApiResponse(responseCode = "404", description = "Match data not found!")
    })
    @GetMapping(value = "/{matchId}")
    public ResponseEntity<Object> getMatchData(@PathVariable(value = "matchId") long matchId) {
        return matchOddsService.getMatchData(matchId);
    }

    @Operation(summary = "Create match data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful creation of match data!"),
            @ApiResponse(responseCode = "400", description = "Bad Request for creation of match data!")
    })
    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createMatchData(@Valid @RequestBody CreateMatchRequest createMatchRequest) {
        return matchOddsService.createMatchData(createMatchRequest);
    }

    @Operation(summary = "Update match data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful update of match data!"),
            @ApiResponse(responseCode = "404", description = "Match data to be updated not found!")
    })
    @PutMapping(value = "{matchId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMatchData(@PathVariable(value = "matchId") long matchId, @Valid @RequestBody UpdateMatchRequest updateMatchRequest) {
        return matchOddsService.updateMatchData(matchId, updateMatchRequest);
    }

    @Operation(summary = "Delete match data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful deletion of match data!"),
            @ApiResponse(responseCode = "404", description = "Match data to be deleted not found!")
    })
    @DeleteMapping(value = "/{matchId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteMatchData(@PathVariable long matchId) {
        return matchOddsService.deleteMatchData(matchId);
    }
}
