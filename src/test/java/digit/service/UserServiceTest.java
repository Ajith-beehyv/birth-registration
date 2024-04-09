package digit.service;

import digit.config.Configuration;
import digit.util.UserUtil;
import digit.web.models.BirthRegistrationApplication;
import digit.web.models.BirthRegistrationRequest;
import digit.web.models.User;
import digit.web.models.UserDetailResponse;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserUtil userUtil;

    @Mock
    private Configuration config;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void callUserService() {
        // Create a test BirthRegistrationRequest with applications containing father and mother
        BirthRegistrationRequest mockRequest = new BirthRegistrationRequest();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setFather(User.builder().tenantId("tenant").userName("father").build());
        application.setMother(User.builder().tenantId("tenant").userName("mother").build());
        mockRequest.setBirthRegistrationApplications(Collections.singletonList(application));

        RequestInfo mockRequestInfo = new RequestInfo();
        mockRequest.setRequestInfo(mockRequestInfo);

        when(userUtil.getStateLevelTenant(any())).thenReturn("state-tenant");
        when(config.getUserHost()).thenReturn("http://example.com/");
        when(config.getUserSearchEndpoint()).thenReturn("/searchUser");
        when(config.getUserCreateEndpoint()).thenReturn("/createUser");

        // Mock the upsertUser method to return a UUID
        UserDetailResponse mockResponse = new UserDetailResponse();
        User mockUser = new User();
        mockUser.setUuid("testUuid");
        mockResponse.setUser(Collections.singletonList(mockUser));
        when(userUtil.userCall(any(), any())).thenReturn(mockResponse);

        // Call the method under test
        userService.callUserService(mockRequest);

        // Verify that upsertUser was called twice with the correct parameters
        verify(userUtil, times(2)).userCall(any(), any());

        // Verify that the father and mother UUIDs were set
        assertEquals("testUuid", application.getFather().getUuid());
        assertEquals("testUuid", application.getMother().getUuid());
    }

    @Test
    void searchUser() {
        User user = new User();
        user.setName("Test User");
        user.setTenantId("testTenantId");

        UserDetailResponse mockResponse = new UserDetailResponse();
        User mockUser = new User();
        mockUser.setUuid("testUuid");
        mockResponse.setUser(Collections.singletonList(mockUser));

        // Mocking dependencies
        when(config.getUserHost()).thenReturn("http://example.com/");
        when(config.getUserSearchEndpoint()).thenReturn("/searchUser");
        when(userUtil.userCall(any(), any())).thenReturn(mockResponse);

        // Calling the method under test
        UserDetailResponse result = userService.searchUser("state-tenant", user);

        // Asserting the result
        assertEquals("testUuid", result.getUser().get(0).getUuid());
    }

    @Test
    void getStateLevelTenant() {
        when(userUtil.getStateLevelTenant(any())).thenReturn("state-tenant");
        assertEquals("state-tenant", userService.getStateLevelTenant(any()));
    }
}