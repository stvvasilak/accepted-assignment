package accepted.vasilakakis.domain;

import accepted.vasilakakis.config.LocalDateConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "match_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate matchDate;

    @Column(name = "match_time")
    private LocalTime matchTime;

    @Column(name = "team_a")
    private String teamA;

    @Column(name = "team_b")
    private String teamB;

    @Column(name = "sport")
    private int sport;
}
