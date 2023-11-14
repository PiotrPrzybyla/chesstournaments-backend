package pwr.chesstournamentsbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateGroupDTO {

    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
}
