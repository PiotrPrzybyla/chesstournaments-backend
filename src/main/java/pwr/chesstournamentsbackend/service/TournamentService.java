package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.chesstournamentsbackend.dto.CreateTournamentDTO;
import pwr.chesstournamentsbackend.dto.JoinTournamentDTO;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.repository.OrganizerRepository;
import pwr.chesstournamentsbackend.repository.TournamentRepository;
import pwr.chesstournamentsbackend.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final OrganizerRepository organizerRepository;
    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, UserRepository userRepository, OrganizerRepository organizerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.organizerRepository = organizerRepository;
    }

    public List<Tournament> getAllTournaments(Integer userId) {
        LocalDateTime today = LocalDateTime.now();
        List<Tournament> allTournaments = tournamentRepository.findByDateAfter(today); // Fetch only future tournaments
        Set<Tournament> userTournaments = userRepository.findById(userId)
                .map(User::getTournaments)
                .orElse(Collections.emptySet());

        return allTournaments.stream()
                .filter(tournament -> !userTournaments.contains(tournament))
                .collect(Collectors.toList());
    }
    public List<Tournament> getTournamentsByUser(Integer userId) {
        return tournamentRepository.findByUsers_userId(userId);
    }
    public Optional<Tournament> findById(Integer id) {
        return tournamentRepository.findById(id);
    }
    public Tournament saveTournament(CreateTournamentDTO tournamentDTO) {
        Organizer organizer = organizerRepository.findById(tournamentDTO.getOrganizerId()).orElseThrow(() -> new EntityNotFoundException("Organizer not found with id " + tournamentDTO.getOrganizerId()));
        Set<User> emptySet = new HashSet<>();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        LocalDateTime localDateTime = LocalDateTime.parse(tournamentDTO.getDate(), inputFormatter);
        Tournament tournament = new Tournament(tournamentDTO.getName(), tournamentDTO.getParticipantsAmount(), tournamentDTO.getAddress(), localDateTime, tournamentDTO.getDescription(), organizer, emptySet);

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
