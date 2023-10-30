package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.repository.OrganizerRepository;

import java.util.Optional;
@Service
public class OrganizerService {
    private final OrganizerRepository organizerRepository;

    @Autowired
    public OrganizerService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }
    public Optional<Organizer> findById(Integer id) {
        return organizerRepository.findById(id);
    }
    public Optional<Organizer> findByUserUserId(Integer id) {return organizerRepository.findByUserUserId(id);}
    public Organizer saveOrganizer(Organizer organizer) {
        return organizerRepository.save(organizer);
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
