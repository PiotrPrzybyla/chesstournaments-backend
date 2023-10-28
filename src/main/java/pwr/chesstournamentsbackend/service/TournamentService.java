package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.repository.TournamentRepository;

import java.util.Optional;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }
    public Optional<Tournament> findById(Integer id) {
        return tournamentRepository.findById(id);
    }
    public Tournament saveTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    public void deleteTournament(Integer id) {
        tournamentRepository.deleteById(id);
    }

    public Tournament updateTournament(Integer id, Tournament tournament) {
        if (tournamentRepository.existsById(id)) {
            tournament.setTournamentId(id);
            return tournamentRepository.save(tournament);
        } else {
            throw new EntityNotFoundException("Tournament not found with id " + id);
        }
    }

}
