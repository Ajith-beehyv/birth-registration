package digit.web.controllers;


import digit.service.BirthRegistrationService;
import digit.util.ResponseInfoFactory;
import digit.web.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
* API tests for BirthApiController
*/

@SpringBootTest
@AutoConfigureMockMvc
public class BirthApiControllerTest {


    @Mock
    private BirthRegistrationService birthRegistrationService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private BirthApiController birthApiController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBirthRegistrationV1CreatePost() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        // Set up your request object here

        List<BirthRegistrationApplication> applications = new ArrayList<>();
        // Set up expected response data

        when(birthRegistrationService.registerBtRequest(any())).thenReturn(applications);

        ResponseEntity<BirthRegistrationResponse> responseEntity = birthApiController.birthRegistrationV1CreatePost(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testBirthRegistrationV1SearchPost() {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        BirthApplicationSearchCriteria criteria = new BirthApplicationSearchCriteria();
        // Set up your request objects here

        List<BirthRegistrationApplication> applications = new ArrayList<>();
        // Set up expected response data

        when(birthRegistrationService.searchBtApplications(any(), any())).thenReturn(applications);

        ResponseEntity<BirthRegistrationResponse> responseEntity = birthApiController.birthRegistrationV1SearchPost(requestInfoWrapper, criteria);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testBirthRegistrationV1UpdatePost() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        // Set up your request object here

        List<BirthRegistrationApplication> applications = new ArrayList<>();
        // Set up expected response data

        when(birthRegistrationService.updateBtApplication(any())).thenReturn(applications);

        ResponseEntity<BirthRegistrationResponse> responseEntity = birthApiController.birthRegistrationV1UpdatePost(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
