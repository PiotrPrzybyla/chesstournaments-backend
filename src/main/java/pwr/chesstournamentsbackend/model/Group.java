package pwr.chesstournamentsbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Setter
@Getter

@Entity
@Table(name = "`group`")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupId;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;

    @ManyToMany
    @JoinTable(
            name = "`group_user`",
            joinColumns = @JoinColumn(name = "`group_id`"),
            inverseJoinColumns = @JoinColumn(name = "`user_id`")
    )
    @JsonManagedReference
    private Set<User> users = new HashSet<>();
}
