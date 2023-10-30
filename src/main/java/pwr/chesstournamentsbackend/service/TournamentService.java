package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.chesstournamentsbackend.dto.JoinTournamentDTO;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.repository.TournamentRepository;
import pwr.chesstournamentsbackend.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
    }

    public List<Tournament> getAllOrganizers() {
        return tournamentRepository.findAll();
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
    public void joinTournament(JoinTournamentDTO joinTournamentDTO) {
        Tournament tournament = tournamentRepository.findById(joinTournamentDTO.getTournamentId())
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id " + joinTournamentDTO.getTournamentId()));

        User user = userRepository.findById(joinTournamentDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + joinTournamentDTO.getUserId()));

        tournament.getUsers().add(user);

        tournamentRepository.save(tournament);
    }
    public boolean isUserRegisteredToTournament(Integer userId, Integer tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);

        if (tournament.isPresent()) {
            Set<User> usersRegistered = tournament.get().getUsers();
            return usersRegistered.stream().anyMatch(user -> user.getUserId().equals(userId));
        }
        return false;
    }
}
