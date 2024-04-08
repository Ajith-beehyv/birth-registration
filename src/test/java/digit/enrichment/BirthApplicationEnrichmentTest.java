package digit.enrichment;

import digit.service.UserService;
import digit.util.IdgenUtil;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BirthApplicationEnrichmentTest {

    @InjectMocks
    private BirthApplicationEnrichment birthApplicationEnrichment;

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void enrichBirthApplication() {
        BirthRegistrationRequest birthRegistrationRequest = createBirthRegistrationRequest();
        when(idgenUtil.getIdList(any(), any(), any(), any(), anyInt()))
                .thenReturn(Collections.singletonList("BTR123456"));

        birthApplicationEnrichment.enrichBirthApplication(birthRegistrationRequest);

        List<BirthRegistrationApplication> applications = birthRegistrationRequest.getBirthRegistrationApplications();
        assertNotNull(applications);
        assertEquals(1, applications.size());
        for (BirthRegistrationApplication application : applications) {
            assertNotNull(application.getAuditDetails());
            assertNotNull(application.getId());
            assertNotNull(application.getApplicationNumber());
            assertNotNull(application.getAddress());
            assertNotNull(application.getFather());
            assertNotNull(application.getMother());
            assertNotNull(application.getAddress().getId());
            assertEquals(application.getAddress().getId().toString(), application.getAddress().getApplicantAddress().getAddressId());
        }
    }

    @Test
    void enrichBirthApplicationUponUpdate() {
        BirthRegistrationRequest birthRegistrationRequest = createBirthRegistrationRequest();
        BirthRegistrationApplication application = birthRegistrationRequest.getBirthRegistrationApplications().get(0);
        AuditDetails auditDetails = new AuditDetails("userId1", "userId2", 123456L, 123457L);
        application.setAuditDetails(auditDetails);

        birthApplicationEnrichment.enrichBirthApplicationUponUpdate(birthRegistrationRequest);

        assertEquals("userId1", application.getAuditDetails().getCreatedBy());
        assertEquals(123456L, application.getAuditDetails().getCreatedTime());
        assertEquals("userId3", application.getAuditDetails().getLastModifiedBy());
        assertNotEquals(123457L, application.getAuditDetails().getLastModifiedTime()); // Last modified time should be updated
    }

    private BirthRegistrationRequest createBirthRegistrationRequest() {
        RequestInfo requestInfo = new RequestInfo();
        org.egov.common.contract.request.User userInfo = new org.egov.common.contract.request.User();
        userInfo.setUuid("userId3");
        requestInfo.setUserInfo(userInfo);

        List<BirthRegistrationApplication> applications = new ArrayList<>();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setTenantId("tenantId");
        BirthApplicationAddress applicationAddress = BirthApplicationAddress.builder()
                .id(UUID.fromString("af1ef2ad-d092-49ad-851c-401d18a41784")).applicantAddress(new Address()).build();
        application.setAddress(applicationAddress);
        application.setFather(new User());
        application.setMother(new User());
        applications.add(application);
        return new BirthRegistrationRequest(requestInfo, applications);
    }

    @Test
    void testEnrichFatherUserOnSearch() {
        // Create a sample BirthRegistrationApplication
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setTenantId("tenant_id");
        User fatherUser = new User();
        fatherUser.setUuid("father_uuid");
        fatherUser.setTenantId("tenant_id");
        fatherUser.setName("Father Name");
        fatherUser.setUserName("father_user");
        fatherUser.setMobileNumber("1234567890");
        fatherUser.setEmailId("father@example.com");
        fatherUser.setType("type");
        fatherUser.setRoles(new ArrayList<>());

        // Mock the UserService to return a UserDetailResponse when searchUser is called
        UserDetailResponse userDetailResponse = new UserDetailResponse();
        List<User> userList = new ArrayList<>();
        userList.add(fatherUser);
        userDetailResponse.setUser(userList);
        when(userService.getStateLevelTenant(anyString())).thenReturn("state_tenant");
        when(userService.searchUser(anyString(), any())).thenReturn(userDetailResponse);

        // Call the method to be tested
        birthApplicationEnrichment.enrichFatherUserOnSearch(application);

        // Verify that the UserService's searchUser method was called with the correct arguments
        verify(userService, times(1)).searchUser(anyString(), any());

        // Assert that the application's father is correctly enriched
        assertEquals("father_uuid", application.getFather().getUuid());
        assertEquals("tenant_id", application.getFather().getTenantId());
        assertEquals("Father Name", application.getFather().getName());
        assertEquals("father_user", application.getFather().getUserName());
        assertEquals("1234567890", application.getFather().getMobileNumber());
        assertEquals("father@example.com", application.getFather().getEmailId());
        assertEquals(0, application.getFather().getRoles().size());
    }

    @Test
    void enrichMotherUserOnSearch() {
        // Create a sample BirthRegistrationApplication
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setTenantId("tenant_id");
        User motherUser = new User();
        motherUser.setUuid("mother_uuid");
        motherUser.setTenantId("tenant_id");
        motherUser.setName("Mother Name");
        motherUser.setUserName("mother_user");
        motherUser.setMobileNumber("1234567890");
        motherUser.setEmailId("mother@example.com");
        motherUser.setType("type");
        motherUser.setRoles(new ArrayList<>());

        // Mock the UserService to return a UserDetailResponse when searchUser is called
        UserDetailResponse userDetailResponse = new UserDetailResponse();
        List<User> userList = new ArrayList<>();
        userList.add(motherUser);
        userDetailResponse.setUser(userList);
        when(userService.getStateLevelTenant(anyString())).thenReturn("state_tenant");
        when(userService.searchUser(anyString(), any())).thenReturn(userDetailResponse);

        // Call the method to be tested
        birthApplicationEnrichment.enrichMotherUserOnSearch(application);

        // Verify that the UserService's searchUser method was called with the correct arguments
        verify(userService, times(1)).searchUser(anyString(), any());

        // Assert that the application's mother is correctly enriched
        assertEquals("mother_uuid", application.getMother().getUuid());
        assertEquals("tenant_id", application.getMother().getTenantId());
        assertEquals("Mother Name", application.getMother().getName());
        assertEquals("mother_user", application.getMother().getUserName());
        assertEquals("1234567890", application.getMother().getMobileNumber());
        assertEquals("mother@example.com", application.getMother().getEmailId());
        assertEquals(0, application.getMother().getRoles().size());
    }

}
