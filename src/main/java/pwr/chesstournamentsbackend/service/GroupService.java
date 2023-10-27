package pwr.chesstournamentsbackend.service;

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
}
