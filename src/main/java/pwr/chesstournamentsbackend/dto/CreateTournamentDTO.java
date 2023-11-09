package pwr.chesstournamentsbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateTournamentDTO {
    @JsonProperty
    private Integer tournamentId;
    @JsonProperty
    private String name;
    @JsonProperty
    private Integer participantsAmount;
    @JsonProperty
    private String address;
    @JsonProperty
    private String date;
    @JsonProperty
    private String description;
    @JsonProperty
    private Integer organizerId;

}
