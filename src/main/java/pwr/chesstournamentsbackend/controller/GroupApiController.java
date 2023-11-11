package pwr.chesstournamentsbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.dto.ResponseMessage;
import pwr.chesstournamentsbackend.model.Category;
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
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group savedGroup = groupService.saveCategory(group);
        return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
    }
    @DeleteMapping("/{group_id}")
    public ResponseEntity<ResponseMessage> deleteGroup(@PathVariable Integer group_id){
        groupService.deleteCategory(group_id);
        return new ResponseEntity<>(new ResponseMessage("Group Deleted"), HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{group_id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Integer group_id, @RequestBody Group group) {
        Group updatedGroup = groupService.updateGroup(group_id, group);
        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }
}
