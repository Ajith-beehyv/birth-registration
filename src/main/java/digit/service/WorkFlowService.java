package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class WorkFlowService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ServiceRequestRepository repository;

    @Autowired
    private Configuration config;

    public void updateWorkflowStatus(BirthRegistrationRequest birthRegistrationRequest) {
        birthRegistrationRequest.getBirthRegistrationApplications().forEach(application -> {
            ProcessInstance processInstance = getProcessInstanceForBTR(application, birthRegistrationRequest.getRequestInfo());
            ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest(birthRegistrationRequest.getRequestInfo(), Collections.singletonList(processInstance));
            callWorkFlow(workflowRequest);
        });
    }

    public State callWorkFlow(ProcessInstanceRequest workflowReq) {

        ProcessInstanceResponse response = null;
        StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));
        Object optional = repository.fetchResult(url, workflowReq);
        response = mapper.convertValue(optional, ProcessInstanceResponse.class);
        return response.getProcessInstances().get(0).getState();
    }

    private ProcessInstance getProcessInstanceForBTR(BirthRegistrationApplication application, RequestInfo requestInfo) {
        Workflow workflow = application.getWorkflow();

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setBusinessId(application.getApplicationNumber());
        processInstance.setAction(workflow.getAction());
        processInstance.setModuleName("birth-services");
        processInstance.setTenantId(application.getTenantId());
        processInstance.setBusinessService("BTR");
        processInstance.setDocuments(workflow.getDocuments());
        processInstance.setComment(workflow.getComment());

        if(!CollectionUtils.isEmpty(workflow.getAssignees())){
            List<User> users = new ArrayList<>();

            workflow.getAssignees().forEach(uuid -> {
                User user = new User();
                user.setUuid(uuid);
                users.add(user);
            });

            processInstance.setAssignes(users);
        }

        return processInstance;

    }

    public ProcessInstance getCurrentWorkflow(RequestInfo requestInfo, String tenantId, String businessId) {

        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();

        StringBuilder url = getSearchURLForProcessInstanceWithParams(tenantId, businessId);

        Object res = repository.fetchResult(url, requestInfoWrapper);
        ProcessInstanceResponse response = null;

        try{
            response = mapper.convertValue(res, ProcessInstanceResponse.class);
        }
        catch (Exception e){
            throw new CustomException("PARSING_ERROR","Failed to parse workflow search response");
        }

        if(response!=null && !CollectionUtils.isEmpty(response.getProcessInstances()) && response.getProcessInstances().get(0)!=null)
            return response.getProcessInstances().get(0);

        return null;
    }

    private StringBuilder getSearchURLForProcessInstanceWithParams(String tenantId, String businessService) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfProcessInstanceSearchPath());
        url.append("?tenantId=").append(tenantId);
        url.append("&businessIds=").append(businessService);
        return url;
    }
}
