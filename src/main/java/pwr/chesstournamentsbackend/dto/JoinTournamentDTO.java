package pwr.chesstournamentsbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinTournamentDTO {
    @JsonProperty
    private Integer userId;
    @JsonProperty
    private Integer tournamentId;
}
