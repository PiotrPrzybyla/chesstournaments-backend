package pwr.chesstournamentsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pwr.chesstournamentsbackend.model.Group;
import pwr.chesstournamentsbackend.repository.GroupRepository;

import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
    public Optional<Group> findById(Integer id) {
        return groupRepository.findById(id);
    }
    public Group saveCategory(Group group) {
        return groupRepository.save(group);
    }
    public void deleteCategory(Integer id){
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
}
