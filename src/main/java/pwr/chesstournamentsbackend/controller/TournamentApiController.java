package pwr.chesstournamentsbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.service.TournamentService;

@RestController
@RequestMapping("/api/tournament")
public class TournamentApiController {
    public final TournamentService tournamentService;

    public TournamentApiController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
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

}
