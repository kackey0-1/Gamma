package it.chalmers.gamma.factories;

import it.chalmers.gamma.domain.GroupType;
import it.chalmers.gamma.domain.dto.group.FKITSuperGroupDTO;
import it.chalmers.gamma.service.FKITSuperGroupService;
import it.chalmers.gamma.utils.GenerationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockSuperGroupFactory {

    @Autowired
    private FKITSuperGroupService superGroupService;

    public FKITSuperGroupDTO generateSuperGroup(String groupName) {
        return this.generateSuperGroup(groupName, GroupType.COMMITTEE);
    }

    public FKITSuperGroupDTO generateSuperGroup(String groupName, GroupType groupType) {
        return new FKITSuperGroupDTO(
                groupName,
                groupName,
                groupType,
                GenerationUtils.generateRandomString()
        );
    }

    public FKITSuperGroupDTO saveSuperGroup(FKITSuperGroupDTO superGroup) {
        return this.superGroupService.createSuperGroup(superGroup);
    }
}
