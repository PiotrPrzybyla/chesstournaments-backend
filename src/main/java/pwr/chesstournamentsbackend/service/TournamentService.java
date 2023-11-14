package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.chesstournamentsbackend.dto.CreateTournamentDTO;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.repository.OrganizerRepository;
import pwr.chesstournamentsbackend.repository.TournamentRepository;
import pwr.chesstournamentsbackend.repository.UserRepository;

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
    public List<Tournament> getAllTournaments(){
        LocalDateTime today = LocalDateTime.now();
        return tournamentRepository.findByDateAfter(today);
    }
    public List<Tournament> getAllTournaments(String uid) {

        LocalDateTime today = LocalDateTime.now();
        List<Tournament> allTournaments = tournamentRepository.findByDateAfter(today);
        Set<Tournament> userTournaments = userRepository.findByUid(uid)
                .map(User::getTournaments)
                .orElse(Collections.emptySet());
        Optional<Organizer> organizer = organizerRepository.findByUserUid(uid);
        if(organizer.isPresent()){
            List<Tournament> organizerTournaments = tournamentRepository.findByOrganizer_OrganizerId(organizer.get().getOrganizerId());
            return allTournaments.stream()
                    .filter(tournament -> !organizerTournaments.contains(tournament))
                    .toList().stream()
                    .filter(tournament -> !userTournaments.contains(tournament))
                    .collect(Collectors.toList());
        }
        return allTournaments.stream()
                .filter(tournament -> !userTournaments.contains(tournament))
                .collect(Collectors.toList());
    }
    public List<Tournament> getTournamentsByUser(String uid) {
        LocalDateTime today = LocalDateTime.now();
        List<Tournament> allTournaments = tournamentRepository.findByDateAfter(today);
        Set<Tournament> userTournaments = userRepository.findByUid(uid)
                .map(User::getTournaments)
                .orElse(Collections.emptySet());

        return allTournaments.stream()
                .filter(userTournaments::contains)
                .collect(Collectors.toList());
    }
    public List<Tournament> getActiveTournamentsByOrganizer(Integer organizerId){
        LocalDateTime today = LocalDateTime.now();
        List<Tournament> allTournaments = tournamentRepository.findByDateAfter(today);
        List<Tournament> organizerTournaments = tournamentRepository.findByOrganizer_OrganizerId(organizerId);

        return allTournaments.stream()
                .filter(organizerTournaments::contains)
                .collect(Collectors.toList());
    }
    public List<Tournament> getPastTournamentsByOrganizer(Integer organizerId){
        LocalDateTime today = LocalDateTime.now();
        List<Tournament> allTournaments = tournamentRepository.findByDateBefore(today);
        List<Tournament> organizerTournaments = tournamentRepository.findByOrganizer_OrganizerId(organizerId);

        return allTournaments.stream()
                .filter(organizerTournaments::contains)
                .collect(Collectors.toList());
    }
    public Optional<Tournament> findById(Integer id) {
        return tournamentRepository.findById(id);
    }
    public Tournament saveTournament(CreateTournamentDTO tournamentDTO, Integer organizerId) {
        Optional<Organizer> organizer = organizerRepository.findById(organizerId);
        Set<User> emptySet = new HashSet<>();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        LocalDateTime localDateTime = LocalDateTime.parse(tournamentDTO.getDate(), inputFormatter);
        Tournament tournament = new Tournament(tournamentDTO.getName(), tournamentDTO.getParticipantsAmount(), tournamentDTO.getAddress(), localDateTime, tournamentDTO.getDescription(), organizer.orElseThrow(), emptySet);

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
    public void joinTournament(Integer tournamentId, String uid) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id " + tournamentId));

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + uid));
        tournament.getUsers().add(user);

        tournamentRepository.save(tournament);
    }
    public boolean isUserRegisteredToTournament(String uid, Integer tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);

        if (tournament.isPresent()) {
            Set<User> usersRegistered = tournament.get().getUsers();
            return usersRegistered.stream().anyMatch(user -> user.getUid().equals(uid));
        }
        return false;
    }

    public List<User> getTournamentUsers(Integer tournamentId) {
        Set<User> users = userRepository.findByTournaments_TournamentId(tournamentId);
        return new ArrayList<>(users);
    }

    public Boolean isOrganizer(Integer organizerId, Integer tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        Optional<Organizer> organizer = organizerRepository.findById(organizerId);
        return tournament.orElseThrow().getOrganizer().equals(organizer.orElseThrow());
    }

    public void leaveTournament(Integer tournamentId, String uid) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id " + tournamentId));

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + uid));
        tournament.getUsers().remove(user);

        tournamentRepository.save(tournament);
    }
}
