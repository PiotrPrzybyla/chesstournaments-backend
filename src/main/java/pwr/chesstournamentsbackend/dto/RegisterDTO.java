package pwr.chesstournamentsbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pwr.chesstournamentsbackend.model.Category;

@Getter
@Setter
public class RegisterDTO {
    @JsonProperty
    private String idToken;
    @JsonProperty
    private String login;
    @JsonProperty
    private String name;
    @JsonProperty
    private String surname;
    @JsonProperty
    private Integer age;
    @JsonProperty
    private Integer categoryId;
}
