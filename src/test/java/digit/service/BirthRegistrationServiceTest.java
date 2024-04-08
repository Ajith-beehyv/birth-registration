package digit.service;

import digit.enrichment.BirthApplicationEnrichment;
import digit.kafka.Producer;
import digit.repository.BirthRegistrationRepository;
import digit.validator.BirthApplicationValidator;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BirthRegistrationServiceTest {

    @Mock
    private BirthApplicationValidator validator;

    @Mock
    private BirthApplicationEnrichment enrichmentUtil;

    @Mock
    private UserService userService;

    @Mock
    private BirthRegistrationRepository birthRegistrationRepository;

    @Mock
    private Producer producer;

    @Mock
    private WorkFlowService workFlowService;

    @InjectMocks
    private BirthRegistrationService birthRegistrationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterBtRequest() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setId("123");
        List<BirthRegistrationApplication> applications = new ArrayList<>(Collections.singletonList(application));
        request.setBirthRegistrationApplications(applications);

        doNothing().when(validator).validateBirthApplication(any());
        doNothing().when(userService).callUserService(any());
        doNothing().when(workFlowService).updateWorkflowStatus(any());
        doNothing().when(producer).push(anyString(), any());

        List<BirthRegistrationApplication> result = birthRegistrationService.registerBtRequest(request);

        verify(enrichmentUtil).enrichBirthApplication(request);
        verify(validator).validateBirthApplication(request);
        verify(userService).callUserService(request);
        verify(workFlowService).updateWorkflowStatus(request);
        verify(producer).push("save-bt-application", request);

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getId());
    }

    @Test
    public void testSearchBtApplications() {
        RequestInfo requestInfo = new RequestInfo();
        BirthApplicationSearchCriteria searchCriteria = new BirthApplicationSearchCriteria();
        when(birthRegistrationRepository.getApplications(any())).thenReturn(Collections.emptyList());

        // Invoke
        List<BirthRegistrationApplication> result = birthRegistrationService.searchBtApplications(requestInfo, searchCriteria);

        // Verify
        assertEquals(0, result.size());
        verify(birthRegistrationRepository, times(1)).getApplications(searchCriteria);
    }

    @Test
    public void testUpdateBtApplication() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setId("123");
        List<BirthRegistrationApplication> applications = Collections.singletonList(application);
        request.setBirthRegistrationApplications(applications);

        when(validator.validateApplicationExistence(application)).thenReturn(application);
        //hen(producer.push(anyString(), any())).thenReturn(true);

        List<BirthRegistrationApplication> result = birthRegistrationService.updateBtApplication(request);

        verify(validator).validateApplicationExistence(application);
        verify(enrichmentUtil).enrichBirthApplicationUponUpdate(request);
        verify(workFlowService).updateWorkflowStatus(request);
        verify(producer).push("update-bt-application", request);

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getId());
    }
}
