package pwr.chesstournamentsbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
