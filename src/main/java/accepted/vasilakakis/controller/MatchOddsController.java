package accepted.vasilakakis.controller;

import accepted.vasilakakis.model.request.CreateMatchOddsRequest;
import accepted.vasilakakis.model.request.UpdateMatchOddsRequest;
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
@RequestMapping("/matchOdd")
public class MatchOddsController {

    private final MatchOddsService matchOddsService;


    @Operation(summary = "Retrieve match odds by matchId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of match odds!")
    })
    @GetMapping(value = "/{matchId}")
    public ResponseEntity<Object> getMatchOdds(@PathVariable(value = "matchId") long matchId) {
        return matchOddsService.getMatchOdds(matchId);
    }

    @Operation(summary = "Create match odd")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful creation of match odds!"),
            @ApiResponse(responseCode = "400", description = "Bad Request for creation of match odds!")
    })
    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createMatchOdd(@Valid @RequestBody CreateMatchOddsRequest createMatchOddsRequest) {
        return matchOddsService.createMatchOdd(createMatchOddsRequest);
    }

    @Operation(summary = "Update match odd")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful update of match odd!"),
            @ApiResponse(responseCode = "404", description = "Match odd to be updated not found!")
    })
    @PutMapping(value = "/{oddId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMatchOdd(@PathVariable(value = "oddId") long oddId, @Valid @RequestBody UpdateMatchOddsRequest updateMatchOddsRequest) {
        return matchOddsService.updateMatchOdd(oddId, updateMatchOddsRequest);
    }

    @Operation(summary = "Delete match odd")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful deletion of match odd!"),
            @ApiResponse(responseCode = "404", description = "Match odd to be deleted not found!")
    })
    @DeleteMapping(value = "/{oddId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteMatchOdd(@PathVariable long oddId) {
        return matchOddsService.deleteMatchOdd(oddId);
    }
}
