package it.chalmers.gamma.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.chalmers.gamma.GammaApplication;
import it.chalmers.gamma.db.entity.Post;
import it.chalmers.gamma.db.entity.Text;
import it.chalmers.gamma.domain.GroupType;
import it.chalmers.gamma.domain.dto.group.FKITGroupDTO;
import it.chalmers.gamma.domain.dto.group.FKITSuperGroupDTO;
import it.chalmers.gamma.domain.dto.membership.MembershipDTO;
import it.chalmers.gamma.domain.dto.post.PostDTO;
import it.chalmers.gamma.domain.dto.user.GoldappsITUserDTO;
import it.chalmers.gamma.domain.dto.user.ITUserDTO;
import it.chalmers.gamma.factories.MockFKITGroupFactory;
import it.chalmers.gamma.factories.MockITUserFactory;
import it.chalmers.gamma.factories.MockMembershipFactory;
import it.chalmers.gamma.factories.MockPostFactory;
import it.chalmers.gamma.factories.MockSuperGroupFactory;
import it.chalmers.gamma.utils.CharacterTypes;
import it.chalmers.gamma.utils.GenerationUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = GammaApplication.class)
@ActiveProfiles("test")
public class GoldappsTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoldappsITUserDTO.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockITUserFactory mockITUserFactory;

    @Autowired
    private MockFKITGroupFactory mockFKITGroupFactory;

    @Autowired
    private MockMembershipFactory mockMembershipFactory;

    @Autowired
    private MockSuperGroupFactory mockSuperGroupFactory;

    @Autowired
    private MockPostFactory mockPostFactory;

    private MockMvc mockMvc;

    @Before
    public void setupTests() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

    }

    private List<FKITGroupDTO> generateGroups(int nGroups, GroupType groupType) {
        List<FKITGroupDTO> groups = new ArrayList<>();
        for (int i = 0; i < nGroups; i++) {
            FKITSuperGroupDTO superGroup = this.mockSuperGroupFactory.saveSuperGroup(this.mockSuperGroupFactory
                    .generateSuperGroup(GenerationUtils.generateRandomString(), groupType));
            FKITGroupDTO group = this.mockFKITGroupFactory.saveGroup(this.mockFKITGroupFactory
                    .generateActiveFKITGroup(GenerationUtils
                            .generateRandomString(20, CharacterTypes.LOWERCASE), superGroup));
            groups.add(group);
        }
        return groups;
    }

    private List<MembershipDTO> generateUsersInGroups(int nUsers, List<FKITGroupDTO> groups) {
        Random rand = new Random();
        List<MembershipDTO> users = new ArrayList<>();
        List<PostDTO> posts = List.of(
                this.mockPostFactory.savePost(this.mockPostFactory.generatePost(
                        new Text("Ordförande", "Chairman"), "ordforanden@chalmers.it")),
                this.mockPostFactory.savePost(this.mockPostFactory.generatePost(
                        new Text("Kassör", "Treasurer"), "kassorer@chalmers.it")),
                this.mockPostFactory.savePost(this.mockPostFactory.generatePost(
                        new Text("Medlem", "Member"), "")));
        for (int i = 0; i < nUsers; i++) {
            ITUserDTO user = this.mockITUserFactory.saveUser(this.mockITUserFactory
                    .generateITUser(GenerationUtils.generateRandomString(8, CharacterTypes.LOWERCASE), true));
            users.add(this.mockMembershipFactory.saveMembership(
                    this.mockMembershipFactory.generateMembership(posts.get(i % posts.size()),
                            groups.get(rand.nextInt(groups.size())), user)));
        }
        return users;
    }

    private String getGoldappsResponseAsString(String mapperValue, ObjectMapper mapper) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/goldapps"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        Map<String, Object> map = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        return mapper.writeValueAsString(map.get(mapperValue));
    }

    @WithUserDetails("admin")
    @Test
    public void activeUsersInCommitteeReturnCorrectly() throws Exception {
        List<FKITGroupDTO> groups = generateGroups(10, GroupType.COMMITTEE);
        List<MembershipDTO> memberships = generateUsersInGroups(100, groups);
        ObjectMapper mapper = new ObjectMapper();
        String goldappsString = this.getGoldappsResponseAsString("users", mapper);
        List<GoldappsITUserDTO> goldappsUsers = mapper.readValue(goldappsString,
                new TypeReference<>() {
                });
        for (MembershipDTO membership : memberships) {
            boolean foundMatch = false;
            ITUserDTO user = membership.getUser();
            for (GoldappsITUserDTO goldappsUser : goldappsUsers) {      // Inefficient, but should be fine
                if (user.getCid().equals(goldappsUser.getCid())) {
                    Assert.assertEquals(goldappsUser.getFirstName(), user.getFirstName());
                    Assert.assertEquals(goldappsUser.getSecondName(), user.getLastName());
                    Assert.assertEquals(goldappsUser.getNick(), user.getNick());
                    Assert.assertEquals(goldappsUser.getMail(), user.getEmail());
                    foundMatch = true;
                }
            }
            if (!foundMatch) {
                LOGGER.error(String.format("failed as expected user %s was not found in goldapps response",
                        user.getCid()));
                Assert.fail();
            }
        }
    }

    @WithUserDetails("admin")
    @Test
    public void nonActiveUsersDontReturn() throws Exception {
        List<ITUserDTO> nonActiveUsers = new ArrayList<>();
        // Goldapps endpoint crashes if no Ordf or Kass exists
        List<FKITGroupDTO> groups = generateGroups(1, GroupType.COMMITTEE);
        List<MembershipDTO> memberships = generateUsersInGroups(10, groups);

        for (int i = 0; i < 10; i++) { // Generate 10 users not in any group
            ITUserDTO user = this.mockITUserFactory.saveUser(this.mockITUserFactory.generateITUser(
                    GenerationUtils.generateRandomString(8, CharacterTypes.LOWERCASE), true));
            nonActiveUsers.add(user);
        }
        this.testUsersAreNotReturned(nonActiveUsers);
    }

    private void testUsersAreNotReturned(List<ITUserDTO> notReturnedUsers) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String goldappsString = this.getGoldappsResponseAsString("users", mapper);
        List<GoldappsITUserDTO> goldappsUsers = mapper.readValue(goldappsString,
                new TypeReference<>() {
                });
        for (ITUserDTO notReturnedUser : notReturnedUsers) {
            for (GoldappsITUserDTO goldappsUser : goldappsUsers) {
                Assert.assertNotEquals(notReturnedUser.getCid(), goldappsUser.getCid());
            }
        }
    }

    @WithUserDetails("admin")
    @Test
    public void societyMembersDontReturn() throws Exception{
        // Goldapps endpoint crashes if no Ordf or Kass exists
        //List<FKITGroupDTO> committees = this.generateGroups(1, GroupType.COMMITTEE);
        //List<MembershipDTO> committeeMemberships = this.generateUsersInGroups(10, committees);
        List<FKITGroupDTO> societies = this.generateGroups(10, GroupType.SOCIETY);
        List<MembershipDTO> societyMemberships = this.generateUsersInGroups(100, societies);
        this.testUsersAreNotReturned(societyMemberships.stream()
                .map(MembershipDTO::getUser).collect(Collectors.toList()));
    }

    @WithUserDetails("admin")
    @Test
    public void groupsReturnCorrectly() throws Exception {
        List<FKITGroupDTO> groups = generateGroups(10, GroupType.COMMITTEE);
        List<MembershipDTO> memberships = generateUsersInGroups(100, groups);
        ObjectMapper mapper = new ObjectMapper();
        String goldappsString = this.getGoldappsResponseAsString("groups", mapper);
        //List<>
    }
}
