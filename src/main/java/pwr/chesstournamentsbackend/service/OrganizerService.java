package pwr.chesstournamentsbackend.service;

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
}
