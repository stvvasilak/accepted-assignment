package accepted.vasilakakis;

import accepted.vasilakakis.model.enums.Specifier;
import accepted.vasilakakis.model.enums.Sport;
import accepted.vasilakakis.model.request.CreateMatchOddsRequest;
import accepted.vasilakakis.model.request.CreateMatchRequest;
import accepted.vasilakakis.model.request.UpdateMatchOddsRequest;
import accepted.vasilakakis.model.request.UpdateMatchRequest;
import accepted.vasilakakis.repository.MatchOddRepository;
import accepted.vasilakakis.repository.MatchRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import static accepted.vasilakakis.model.constants.Constants.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppIntegrationTest {

    @Autowired
    protected MatchRepository matchRepository;
    @Autowired
    protected MatchOddRepository matchOddRepository;
    @Autowired
    protected ObjectMapper objectMapper;

    private static int createdMatchId;
    private static int createdHomeWinOddId;
    private static int createdAwayWinOddId;
    static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withPassword("postgres")
            .withUsername("postgres")
            .withDatabaseName("matchOdds");

    @DynamicPropertySource
    public static void setupThings(DynamicPropertyRegistry registry) {
        Startables.deepStart(postgreSQLContainer).join();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    protected RequestSpecification requestSpecification;

    @LocalServerPort
    private Integer localServerPort;

    @BeforeEach
    public void setUpAbstractIntegrationTest() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpecification = new RequestSpecBuilder()
                .setPort(localServerPort)
                .addHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }

    @Test
    @Order(-1)
    void cleanUp() {
        matchRepository.deleteAll();
        assertThat(matchRepository.findAll()).isEmpty();
    }

    @Test
    @Order(1)
    public void createMatchData() throws JsonProcessingException {
        given(requestSpecification)
                .body(objectMapper.writeValueAsString(CreateMatchRequest.builder()
                        .teamA("TestTeamA")
                        .teamB("TestTeamB")
                        .matchTime("22:00")
                        .matchDate("25/13/2023")
                        .sport(Sport.FOOTBALL)
                        .build()))
                .when()
                .post("match/create")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(ERROR_CREATING_MATCH_DATA));

        given(requestSpecification)
                .body(objectMapper.writeValueAsString(CreateMatchRequest.builder()
                        .teamA("TestTeamA")
                        .teamB("TestTeamB")
                        .matchTime("25:00")
                        .matchDate("25/08/2023")
                        .sport(Sport.FOOTBALL)
                        .build()))
                .when()
                .post("match/create")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(ERROR_CREATING_MATCH_DATA));

        var response =
                given(requestSpecification)
                        .body(objectMapper.writeValueAsString(CreateMatchRequest.builder()
                                .teamA("TestTeamA")
                                .teamB("TestTeamB")
                                .matchTime("22:00")
                                .matchDate("20/08/2023")
                                .sport(Sport.FOOTBALL)
                                .build()))
                        .when()
                        .post("match/create")
                        .then()
                        .statusCode(201)
                        .extract();
        createdMatchId = response.jsonPath().get("id");
    }

    @Test
    @Order(2)
    public void getMatchData() {
        given(requestSpecification)
                .pathParam("matchId", createdMatchId)
                .when()
                .get("match/{matchId}")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(createdMatchId));

        given(requestSpecification)
                .pathParam("matchId", 9999)
                .when()
                .get("match/{matchId}")
                .then()
                .statusCode(404)
                .body(Matchers.equalTo(MATCH_DATA_NOT_FOUND));
    }

    @Test
    @Order(3)
    public void createMatchOdd() throws JsonProcessingException {
        given(requestSpecification)
                .body(objectMapper.writeValueAsString(CreateMatchOddsRequest.builder()
                        .matchId(9999)
                        .odd(1.9)
                        .specifier(Specifier.HOME_WIN)
                        .build()))
                .when()
                .post("matchOdd/create")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(MATCH_DATA_NOT_FOUND));

        var homeWinResponse =
                given(requestSpecification)
                        .body(objectMapper.writeValueAsString(CreateMatchOddsRequest.builder()
                                .matchId(createdMatchId)
                                .odd(1.9)
                                .specifier(Specifier.HOME_WIN)
                                .build()))
                        .when()
                        .post("matchOdd/create")
                        .then()
                        .statusCode(201)
                        .extract();
        createdHomeWinOddId = homeWinResponse.jsonPath().get("id");

        var awayWinResponse =
                given(requestSpecification)
                        .body(objectMapper.writeValueAsString(CreateMatchOddsRequest.builder()
                                .matchId(createdMatchId)
                                .odd(3.0)
                                .specifier(Specifier.AWAY_WIN)
                                .build()))
                        .when()
                        .post("matchOdd/create")
                        .then()
                        .statusCode(201)
                        .extract();
        createdAwayWinOddId = awayWinResponse.jsonPath().get("id");

        given(requestSpecification)
                .body(objectMapper.writeValueAsString(CreateMatchOddsRequest.builder()
                        .matchId(createdMatchId)
                        .odd(1.9)
                        .specifier(Specifier.HOME_WIN)
                        .build()))
                .when()
                .post("matchOdd/create")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(ODD_ALREADY_PRESENT));
    }

    @Test
    @Order(4)
    public void getMatchOdds() {
        given(requestSpecification)
                .pathParam("matchId", createdMatchId)
                .when()
                .get("matchOdd/{matchId}")
                .then()
                .statusCode(200);

        given(requestSpecification)
                .pathParam("matchId", 9999)
                .when()
                .get("matchOdd/{matchId}")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(ODDS_NOT_FOUND));
    }

    @Test
    @Order(5)
    public void updateMatchData() throws JsonProcessingException {
        given(requestSpecification)
                .pathParam("matchId", createdMatchId)
                .body(objectMapper.writeValueAsString(UpdateMatchRequest.builder()
                        .matchDate("21/08/2023")
                        .matchTime("22:00")
                        .sport(Sport.FOOTBALL)
                        .teamA("TestTeamA")
                        .teamB("TestTeamB")
                        .build()))
                .when()
                .put("match/{matchId}")
                .then()
                .statusCode(200)
                .body("matchDate", Matchers.equalTo("21/08/2023"));
    }

    @Test
    @Order(6)
    public void updateMatchOdd() throws JsonProcessingException {
        given(requestSpecification)
                .pathParam("oddId", 8888)
                .body(objectMapper.writeValueAsString(UpdateMatchOddsRequest.builder()
                        .updatedOdd(2.1)
                        .build()))
                .when()
                .put("matchOdd/{oddId}")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(ODD_NOT_FOUND));

        given(requestSpecification)
                .pathParam("oddId", createdHomeWinOddId)
                .body(objectMapper.writeValueAsString(UpdateMatchOddsRequest.builder()
                        .updatedOdd(2.1)
                        .build()))
                .when()
                .put("matchOdd/{oddId}")
                .then()
                .statusCode(200)
                .body("odd", Matchers.equalTo(2.1F));
    }

    @Test
    @Order(7)
    public void deleteMatchOdd() {
        given(requestSpecification)
                .pathParam("oddId", createdHomeWinOddId)
                .when()
                .delete("matchOdd/{oddId}")
                .then()
                .statusCode(204);

        given(requestSpecification)
                .pathParam("oddId", 8888)
                .when()
                .delete("matchOdd/{oddId}")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(ODD_NOT_FOUND));
    }

    @Test
    @Order(8)
    public void deleteMatchData() {
        //successful deletion of match data
        given(requestSpecification)
                .pathParam("matchId", createdMatchId)
                .when()
                .delete("match/{matchId}")
                .then()
                .statusCode(204);

        //match data should not be present
        given(requestSpecification)
                .pathParam("matchId", createdMatchId)
                .when()
                .get("match/{matchId}")
                .then()
                .statusCode(404)
                .body(Matchers.equalTo(MATCH_DATA_NOT_FOUND));

        //match odds connected to deleted match data should be deleted
        given(requestSpecification)
                .pathParam("matchId", createdMatchId)
                .when()
                .get("matchOdd/{matchId}")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo(ODDS_NOT_FOUND));
    }

}
