package pwr.chesstournamentsbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Setter
@Getter
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;


    @Column(name = "category_name")
    private String categoryName;


    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private Set<User> users = new HashSet<>();

}