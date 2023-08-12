package accepted.vasilakakis.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MatchOdds")
public class MatchOdd {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match matchId;

    @Column(name = "specifier")
    private String specifier;

    @Column(name = "odd")
    private double odd;
}
