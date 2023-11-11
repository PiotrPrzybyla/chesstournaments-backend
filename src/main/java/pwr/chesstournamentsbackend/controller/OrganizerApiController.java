package pwr.chesstournamentsbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.dto.ResponseMessage;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.service.OrganizerService;

import java.util.ArrayList;

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
    @GetMapping("/user")
    public ResponseEntity<Organizer> getOrganizerByUserUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            return organizerService.findByUserUid(uid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return new ResponseEntity<>(new Organizer(), HttpStatus.UNAUTHORIZED);

    }
    @PostMapping("user")
    public ResponseEntity<Organizer> createOrganizer(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            Organizer savedOrganizer = organizerService.saveOrganizer(uid);
            return new ResponseEntity<>(savedOrganizer, HttpStatus.CREATED);

        }
        return new ResponseEntity<>(new Organizer(), HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{organizer_id}")
    public ResponseEntity<ResponseMessage> deleteOrganizer(@PathVariable Integer organizer_id) {
        organizerService.deleteOrganizer(organizer_id);
        return new ResponseEntity<>(new ResponseMessage("Organizer Deleted"), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{organizer_id}")
    public ResponseEntity<Organizer> updateOrganizer(@PathVariable Integer organizer_id, @RequestBody Organizer organizer) {
        Organizer updatedOrganizer = organizerService.updateOrganizer(organizer_id, organizer);
        return new ResponseEntity<>(updatedOrganizer, HttpStatus.OK);
    }

}
