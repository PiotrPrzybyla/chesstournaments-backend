package pwr.chesstournamentsbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.service.OrganizerService;

@RestController
@RequestMapping("/api/organizer")
public class OrganizerApiController {
    public final OrganizerService organizerService;

    public OrganizerApiController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @GetMapping("/{organizer_id}")
    public ResponseEntity<Organizer> getUserById(@PathVariable Integer organizer_id) {
        return organizerService.findById(organizer_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
