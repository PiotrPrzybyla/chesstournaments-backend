package pwr.chesstournamentsbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.chesstournamentsbackend.dto.CreateGroupDTO;
import pwr.chesstournamentsbackend.dto.ResponseMessage;
import pwr.chesstournamentsbackend.model.Category;
import pwr.chesstournamentsbackend.model.Group;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.service.GroupService;

import java.util.*;

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
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupDTO group) {
        Group newGroup = new Group();
        newGroup.setName(group.getName());
        newGroup.setDescription(group.getDescription());
        newGroup.setUsers(new HashSet<>());
        Group savedGroup = groupService.saveGroup(newGroup);
        return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
    }
    @DeleteMapping("/{group_id}")
    public ResponseEntity<ResponseMessage> deleteGroup(@PathVariable Integer group_id){
        groupService.deleteGroup(group_id);
        return new ResponseEntity<>(new ResponseMessage("Group Deleted"), HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{group_id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Integer group_id, @RequestBody Group group) {
        Group updatedGroup = groupService.updateGroup(group_id, group);
        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Group>> getAllGroups(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            List<Group> groups = groupService.getAllGroups(uid);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/suggestions")
    public ResponseEntity<List<Group>> getSuggestedGroups(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            List<Group> groups = groupService.getSuggestedGroups(uid);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Group>> getUserGroups(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            List<Group> groups = groupService.getUserGroups(uid);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/join/{group_id}")
    public ResponseEntity<ResponseMessage> registerUserToGroup(@PathVariable Integer group_id, HttpServletRequest request) {

        try {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("uid") != null){
                String uid = (String) session.getAttribute("uid");
                groupService.joinGroup(group_id, uid);
                return new ResponseEntity<>(new ResponseMessage("User joined successfully to the group"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("User not logged"), HttpStatus.UNAUTHORIZED);

        } catch(Exception e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/leave/{group_id}")
    public ResponseEntity<ResponseMessage> leaveUserFromGroup(@PathVariable Integer group_id, HttpServletRequest request) {

        try {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("uid") != null){
                String uid = (String) session.getAttribute("uid");
                groupService.leaveGroup(group_id, uid);
                return new ResponseEntity<>(new ResponseMessage("User left successfully the group"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("User not logged"), HttpStatus.UNAUTHORIZED);

        } catch(Exception e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/isMember/{group_id}")
    public ResponseEntity<Map<String, Boolean>> checkGroupRegistration(
            @PathVariable Integer group_id,HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){
            String uid = (String) session.getAttribute("uid");
            boolean isMember = groupService.isUserMemberOfAGroup(uid, group_id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("isMember", isMember);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("isMember", false);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

    }
}
