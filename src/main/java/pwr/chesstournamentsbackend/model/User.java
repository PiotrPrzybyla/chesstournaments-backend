package pwr.chesstournamentsbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String login;
    private String name;
    private String surname;
    private Integer age;
    private String uid;
    private String email;
    @ManyToOne
    @JoinColumn(name="category_id")
    @JsonBackReference
    private Category category;
    private String description;
    @ManyToMany(mappedBy = "users")
    @JsonBackReference
    private Set<Tournament> tournaments = new HashSet<>();
    @ManyToMany(mappedBy = "users")
    @JsonBackReference
    private Set<Group> groups = new HashSet<>();
}