package pwr.chesstournamentsbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.service.OrganizerService;

@RestController
@RequestMapping("/api/organizer")
@CrossOrigin(origins = "http://localhost:3000")
public class OrganizerApiController {
    public final OrganizerService organizerService;

    public OrganizerApiController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @GetMapping("/{organizer_id}")
    public ResponseEntity<Organizer> getOrganizerById(@PathVariable Integer organizer_id) {
        return organizerService.findById(organizer_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Organizer> getOrganizerByUserUserId(@PathVariable Integer user_id) {
        return organizerService.findByUserUserId(user_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Organizer> createOrganizer(@RequestBody Organizer organizer) {
        Organizer savedOrganizer = organizerService.saveOrganizer(organizer);
        return new ResponseEntity<>(savedOrganizer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{organizer_id}")
    public ResponseEntity<Void> deleteOrganizer(@PathVariable Integer organizer_id) {
        organizerService.deleteOrganizer(organizer_id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{organizer_id}")
    public ResponseEntity<Organizer> updateOrganizer(@PathVariable Integer organizer_id, @RequestBody Organizer organizer) {
        Organizer updatedOrganizer = organizerService.updateOrganizer(organizer_id, organizer);
        return new ResponseEntity<>(updatedOrganizer, HttpStatus.OK);
    }

}
