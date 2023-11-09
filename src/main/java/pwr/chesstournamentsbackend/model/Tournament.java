package pwr.chesstournamentsbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tournament")
public class Tournament {
    public Tournament(String name, Integer participantsAmount, String address, LocalDateTime date, String description, Organizer organizer, Set<User> users) {
        this.name = name;
        this.participantsAmount = participantsAmount;
        this.address = address;
        this.date = date;
        this.description = description;
        this.organizer = organizer;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tournamentId;
    private String name;
    private Integer participantsAmount;
    private String address;
    private LocalDateTime date;
    private String description;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @ManyToMany
    @JoinTable(
            name = "tournament_user",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private Set<User> users = new HashSet<>();

}
