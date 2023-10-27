package pwr.chesstournamentsbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String login;
    private String name;
    private String surname;
    private String age;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
    private String description;

    // Getters, setters...
}