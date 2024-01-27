package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pwr.chesstournamentsbackend.model.Group;
import pwr.chesstournamentsbackend.model.Organizer;
import pwr.chesstournamentsbackend.model.Tournament;
import pwr.chesstournamentsbackend.model.User;
import pwr.chesstournamentsbackend.repository.GroupRepository;
import pwr.chesstournamentsbackend.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }
    public Optional<Group> findById(Integer id) {
        return groupRepository.findById(id);
    }
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }
    public void deleteGroup(Integer id){
        groupRepository.deleteById(id);
    }
    public Group updateGroup(Integer id, Group group) {
        if (groupRepository.existsById(id)) {
            group.setGroupId(id);
            return groupRepository.save(group);
        } else {
            throw new EntityNotFoundException("Category not found with id " + id);
        }
    }


    public void joinGroup(Integer groupId, String uid) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with id " + groupId));

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + uid));
        group.getUsers().add(user);

        groupRepository.save(group);
    }

    public List<Group> getAllGroups(String uid) {
        return groupRepository.findAll();
    }

    public boolean isUserMemberOfAGroup(String uid, Integer groupId) {
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isPresent()) {
            Set<User> usersRegistered = group.get().getUsers();
            return usersRegistered.stream().anyMatch(user -> user.getUid().equals(uid));
        }
        return false;
    }

    public void leaveGroup(Integer groupId, String uid) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id " + groupId));

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + uid));
        group.getUsers().remove(user);

        groupRepository.save(group);
    }

    public List<Group> getOtherGroups(String uid) {

        List<Group> suggestedGroups = groupRepository.findAll();
        Set<Group> userGroups = userRepository.findByUid(uid)
                .map(User::getGroups)
                .orElse(Collections.emptySet());

        return suggestedGroups.stream()
                .filter(group -> !userGroups.contains(group))
                .collect(Collectors.toList());


    }

    public List<Group> getUserGroups(String uid) {
        List<Group> suggestedGroups = groupRepository.findAll();
        Set<Group> userGroups = userRepository.findByUid(uid)
                .map(User::getGroups)
                .orElse(Collections.emptySet());

        return suggestedGroups.stream()
                .filter(userGroups::contains)
                .collect(Collectors.toList());
    }
}
