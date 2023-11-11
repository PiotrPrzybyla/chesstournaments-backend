package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.repository.OrganizerRepository;
import pwr.chesstournamentsbackend.repository.UserRepository;

import java.util.Optional;
@Service
public class OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrganizerService(OrganizerRepository organizerRepository, UserRepository userRepository) {
        this.organizerRepository = organizerRepository;
        this.userRepository = userRepository;
    }
    public Optional<Organizer> findById(Integer id) {
        return organizerRepository.findById(id);
    }
    public Optional<Organizer> findByUserUserId(Integer id) {return organizerRepository.findByUserUserId(id);}
    public Optional<Organizer> findByUserUid(String uid) {return organizerRepository.findByUserUid(uid);}
    public Organizer saveOrganizer(Organizer organizer) {

        return organizerRepository.save(organizer);
    }
    public Organizer saveOrganizer(String uid){
        Optional<User> user = userRepository.findByUid(uid);
        Organizer organizer = new Organizer();
        if(user.isPresent()){
            organizer.setUser(user.get());
            return organizerRepository.save(organizer);
        } else return new Organizer();


    }

    public void deleteOrganizer(Integer id) {
        organizerRepository.deleteById(id);
    }

    public Organizer updateOrganizer(Integer id, Organizer organizer) {
        if (organizerRepository.existsById(id)) {
            organizer.setOrganizerId(id);
            return organizerRepository.save(organizer);
        } else {
            throw new EntityNotFoundException("Organizer not found with id " + id);
        }
    }
}
