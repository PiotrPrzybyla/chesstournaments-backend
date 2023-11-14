package pwr.chesstournamentsbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateUserDTO {
    @JsonProperty
    private String name;
    @JsonProperty
    private String surname;
    @JsonProperty
    private String username;
}
