package pwr.chesstournamentsbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "organizer")
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer organizerId;

    @OneToOne
    @JoinColumn(name = "user_user_id")
    private User user;
}
