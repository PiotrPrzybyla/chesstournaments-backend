package pwr.chesstournamentsbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.dto.JoinTournamentDTO;
import pwr.chesstournamentsbackend.dto.ResponseMessage;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.service.TournamentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tournament")
@CrossOrigin(origins = "http://localhost:3000")

public class TournamentApiController {
    public final TournamentService tournamentService;

    public TournamentApiController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllOrganizers();
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }
    @GetMapping("/{tournament_id}")
    public ResponseEntity<Tournament> getUserById(@PathVariable Integer tournament_id) {
        return tournamentService.findById(tournament_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
        Tournament savedTournament = tournamentService.saveTournament(tournament);
        return new ResponseEntity<>(savedTournament, HttpStatus.CREATED);
    }

    @DeleteMapping("/{tournament_id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer tournament_id) {
        tournamentService.deleteTournament(tournament_id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{tournament_id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable Integer tournament_id, @RequestBody Tournament tournament) {
        Tournament updatedTournament = tournamentService.updateTournament(tournament_id, tournament);
        return new ResponseEntity<>(updatedTournament, HttpStatus.OK);
    }
    @PostMapping("/join")
    public ResponseEntity<ResponseMessage> registerUserToTournament(@RequestBody JoinTournamentDTO joinTournamentDTO) {

        try {
            tournamentService.joinTournament(joinTournamentDTO);
            return new ResponseEntity<>(new ResponseMessage("User joined successfully to the tournament"), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/isMember/{tournamentId}/{userId}")
    public ResponseEntity<Map<String, Boolean>> checkRegistration(
            @PathVariable Integer tournamentId,
            @PathVariable Integer userId) {

        boolean isMember = tournamentService.isUserRegisteredToTournament(userId, tournamentId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isMember", isMember);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
