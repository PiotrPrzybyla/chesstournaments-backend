package pwr.chesstournamentsbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.dto.CreateTournamentDTO;
import pwr.chesstournamentsbackend.dto.ResponseMessage;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.service.OrganizerService;
import pwr.chesstournamentsbackend.service.TournamentService;

import java.util.*;

@RestController
@RequestMapping("/api/tournament")
@CrossOrigin(origins = "http://localhost:3000")

public class TournamentApiController {
    public final TournamentService tournamentService;
    public final OrganizerService organizerService;

    public TournamentApiController(TournamentService tournamentService, OrganizerService organizerService) {
        this.tournamentService = tournamentService;
        this.organizerService = organizerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tournament>> getAllTournaments(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            List<Tournament> tournaments = tournamentService.getAllTournaments(uid);
            return new ResponseEntity<>(tournaments, HttpStatus.OK);
        }
            List<Tournament> tournaments = tournamentService.getAllTournaments();
            return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Tournament>> getTournamentsByUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            List<Tournament> userTournaments = tournamentService.getTournamentsByUser(uid);
            if(userTournaments.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(userTournaments, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/isMember/{tournamentId}")
    public ResponseEntity<Map<String, Boolean>> checkRegistration(
            @PathVariable Integer tournamentId,HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            boolean isMember = tournamentService.isUserRegisteredToTournament(uid, tournamentId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("isMember", isMember);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("isMember", false);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

    }
    @PostMapping("/join/{tournament_id}")
    public ResponseEntity<ResponseMessage> registerUserToTournament(@PathVariable Integer tournament_id, HttpServletRequest request) {

        try {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("uid") != null){
                String uid = (String) session.getAttribute("uid");
                tournamentService.joinTournament(tournament_id, uid);
                return new ResponseEntity<>(new ResponseMessage("User joined successfully to the tournament"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("User not logged"), HttpStatus.UNAUTHORIZED);

        } catch(Exception e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{tournament_id}")
    public ResponseEntity<Tournament> getUserById(@PathVariable Integer tournament_id) {
        return tournamentService.findById(tournament_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Tournament> createTournament(@RequestBody CreateTournamentDTO tournament, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            Optional<Organizer> organizer = organizerService.findByUserUid(uid);
            if(organizer.isEmpty()) {
                return new ResponseEntity<>(new Tournament(), HttpStatus.FORBIDDEN);
            }
            Tournament savedTournament = tournamentService.saveTournament(tournament, organizer.get().getOrganizerId());
            return new ResponseEntity<>(savedTournament, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new Tournament(), HttpStatus.UNAUTHORIZED);

    }

    @DeleteMapping("/{tournament_id}")
    public ResponseEntity<ResponseMessage> deleteTournament(@PathVariable Integer tournament_id) {
        tournamentService.deleteTournament(tournament_id);
        return new ResponseEntity<>( new ResponseMessage("Tournament Deleted"), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{tournament_id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable Integer tournament_id, @RequestBody Tournament tournament) {
        Tournament updatedTournament = tournamentService.updateTournament(tournament_id, tournament);
        return new ResponseEntity<>(updatedTournament, HttpStatus.OK);
    }



    @GetMapping("/organizer/past")
    public ResponseEntity<List<Tournament>> getPastTournamentsByOrganizer(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            Optional<Organizer> organizer = organizerService.findByUserUid(uid);
            if(organizer.isEmpty()){
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.FORBIDDEN);
            }
            List<Tournament> organizerTournaments = tournamentService.getPastTournamentsByOrganizer(organizer.get().getOrganizerId());
            if(organizerTournaments.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(organizerTournaments, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/organizer/active")
    public ResponseEntity<List<Tournament>> getActiveTournamentsByOrganizer(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            Optional<Organizer> organizer = organizerService.findByUserUid(uid);
            if(organizer.isEmpty()){
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.FORBIDDEN);
            }
            List<Tournament> organizerTournaments = tournamentService.getActiveTournamentsByOrganizer(organizer.get().getOrganizerId());
            if(organizerTournaments.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
            return new ResponseEntity<>(organizerTournaments, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/isOrganizer/{tournament_id}")
    public ResponseEntity<Map<String, Boolean>> getIsOrganizer(@PathVariable Integer tournament_id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            Optional<Organizer> organizer = organizerService.findByUserUid(uid);
            if(organizer.isEmpty()){
                Map<String, Boolean> response = new HashMap<>();
                response.put("isOrganizer", false);
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
            Boolean isOrganizer = tournamentService.isOrganizer(organizer.get().getOrganizerId(),tournament_id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("isOrganizer", isOrganizer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("isOrganizer", false);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);


    }
    @GetMapping("/users/{tournamentId}")
    public ResponseEntity<List<User>> getTournamentUsers(@PathVariable Integer tournamentId) {
        List<User> tournamentUsers = tournamentService.getTournamentUsers(tournamentId);
        if(tournamentUsers.isEmpty()) {
            return new ResponseEntity<>( new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(tournamentUsers, HttpStatus.OK);
    }

}
