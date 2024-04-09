package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WorkFlowServiceTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Configuration config;

    @InjectMocks
    private WorkFlowService workflowService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUpdateWorkflowStatus() {
        // Mocking the input request and response
        BirthRegistrationRequest mockRequest = new BirthRegistrationRequest();
        RequestInfo mockRequestInfo = new RequestInfo();
        mockRequest.setRequestInfo(mockRequestInfo);

        BirthRegistrationApplication application = new BirthRegistrationApplication();
        Workflow workflow = new Workflow();
        application.setWorkflow(workflow);
        mockRequest.setBirthRegistrationApplications(Collections.singletonList(application));

        ProcessInstance mockProcessInstance = new ProcessInstance();
        ProcessInstanceResponse mockResponse = new ProcessInstanceResponse();
        mockResponse.setProcessInstances(Collections.singletonList(mockProcessInstance));

        // Mocking dependencies
        when(repository.fetchResult(any(), any())).thenReturn(mockResponse);
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(mockResponse);
        when(config.getWfHost()).thenReturn("http://example.com/");
        when(config.getWfTransitionPath()).thenReturn("/transition");

        // Calling the method
        workflowService.updateWorkflowStatus(mockRequest);

        // Verifying the behavior
        verify(repository, times(1)).fetchResult(any(), any());
        verify(mapper, times(1)).convertValue(any(), eq(ProcessInstanceResponse.class));
        verify(config, times(1)).getWfHost();
        verify(config, times(1)).getWfTransitionPath();
    }


    @Test
    public void testCallWorkFlow() {
        // Setup
        ProcessInstanceRequest mockRequest = new ProcessInstanceRequest();
        ProcessInstanceResponse mockResponse = new ProcessInstanceResponse();
        mockResponse.setProcessInstances(Collections.singletonList(ProcessInstance.builder().state(new State()).build()));
        when(config.getWfHost()).thenReturn("http://workflow.com/");
        when(config.getWfTransitionPath()).thenReturn("http://example.com/");
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(mockResponse);

        when(repository.fetchResult(any(), any())).thenReturn(mockResponse);
        // Invoke
        State state = workflowService.callWorkFlow(mockRequest);
        // Verify
        assertNotNull(state);
    }

    @Test
    public void testGetCurrentWorkflow() {
        // Setup
        RequestInfo requestInfo = new RequestInfo();
        String tenantId = "tenantId";
        String businessId = "businessId";

        ProcessInstanceResponse mockResponse = new ProcessInstanceResponse();
        ProcessInstance mockInstance = new ProcessInstance();
        mockInstance.setBusinessId("businessId");
        mockResponse.setProcessInstances(Collections.singletonList(mockInstance));

        // Mock repository response
        when(config.getWfHost()).thenReturn("http://example.com/");
        when(config.getWfProcessInstanceSearchPath()).thenReturn("/processInstance/search");
        when(repository.fetchResult(any(), any())).thenReturn(new Object());
        when(mapper.convertValue(any(), eq(ProcessInstanceResponse.class))).thenReturn(mockResponse);

        // Invoke
        ProcessInstance processInstance = workflowService.getCurrentWorkflow(requestInfo, tenantId, businessId);

        verify(repository).fetchResult(any(), any());
        verify(mapper).convertValue(any(), eq(ProcessInstanceResponse.class));

        // Asserting the result
        assertNotNull(processInstance);
        assertEquals("businessId", processInstance.getBusinessId());    }

}
