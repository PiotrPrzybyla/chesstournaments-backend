package pwr.chesstournamentsbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pwr.chesstournamentsbackend.model.Group;
import pwr.chesstournamentsbackend.service.GroupService;

@RestController
@RequestMapping("/api/group")
public class GroupApiController {
    public final GroupService groupService;

    public GroupApiController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<Group> getUserById(@PathVariable Integer group_id) {
        return groupService.findById(group_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
