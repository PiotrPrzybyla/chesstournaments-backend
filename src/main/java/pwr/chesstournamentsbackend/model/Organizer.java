package pwr.chesstournamentsbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "organizer")
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer organizerId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters, setters...
}
