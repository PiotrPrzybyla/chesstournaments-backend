package pwr.chesstournamentsbackend.service;

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

}
