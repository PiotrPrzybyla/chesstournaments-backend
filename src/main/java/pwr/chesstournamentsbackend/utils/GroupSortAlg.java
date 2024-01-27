package pwr.chesstournamentsbackend.utils;


import pwr.chesstournamentsbackend.dto.GroupRatingDTO;
import pwr.chesstournamentsbackend.model.Group;
import pwr.chesstournamentsbackend.model.User;

import java.util.*;

public class GroupSortAlg {
    public static List<GroupRatingDTO> sortGroups(List<Group> groups, User user){
        List<GroupRatingDTO> suggestedGroups = new ArrayList<>();
        for (Group group: groups) {
            int intRating = getAgeRating(user, group);
            suggestedGroups.add(new GroupRatingDTO(group, intRating));
        }

        suggestedGroups.sort((o1, o2) -> o2.getRating().compareTo(o1.getRating()));

        return suggestedGroups.size() > 3
                ? suggestedGroups.subList(0, 3)
                : suggestedGroups;
    }

    private static int getAgeRating(User user, Group group) {
        double counter = 0;
        for (User groupUser: group.getUsers()) {
            int ageDifference = Math.abs(user.getAge() - groupUser.getAge());
            if(ageDifference <20){
                counter+= 20-ageDifference;
            }
        }
        double rating;
        if(group.getUsers().isEmpty()){
            rating = 0;
        } else {
            rating = counter/(20* group.getUsers().size());
        }
        return (int) (rating*100);
    }
}
