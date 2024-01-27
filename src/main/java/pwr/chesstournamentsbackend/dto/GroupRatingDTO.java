package pwr.chesstournamentsbackend.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import pwr.chesstournamentsbackend.model.Group;
import pwr.chesstournamentsbackend.model.User;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GroupRatingDTO extends Group {

    @JsonProperty
    private Integer rating;
    @JsonProperty
    private Integer groupId;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private Set<User> users;
    public GroupRatingDTO(Group group, Integer rating){
        this.rating = rating;
        groupId = group.getGroupId();
        name = group.getName();
        description = group.getDescription();
        users = group.getUsers();
    }
}
