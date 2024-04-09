package digit.service;

import digit.config.Configuration;
import digit.util.UserUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.user.UserSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class UserService {

    private UserUtil userUtil;

    private Configuration config;

    @Autowired
    public UserService(UserUtil userUtil, Configuration config) {
        this.userUtil = userUtil;
        this.config = config;
    }


    public void callUserService(BirthRegistrationRequest request) {
        request.getBirthRegistrationApplications().forEach(application -> {
            String tenantId = application.getTenantId();
            if (application.getFather() != null) {
                application.getFather().setUuid(upsertUser(application.getFather(), request.getRequestInfo()));
            }
            if (application.getFather() != null) {
                application.getMother().setUuid(upsertUser(application.getMother(), request.getRequestInfo()));
            }
        });
    }

    private String upsertUser(User user, RequestInfo requestInfo){

        User userServiceResponse = null;

        // Search using name, mobile number and user name
        UserDetailResponse userDetailResponse = searchUser(getStateLevelTenant(user.getTenantId()), user);
        if (userDetailResponse != null && !userDetailResponse.getUser().isEmpty()) {
            User userFromSearch = userDetailResponse.getUser().get(0);
            log.info(userFromSearch.toString());
            userServiceResponse = userDetailResponse.getUser().get(0);
        } else {
            userServiceResponse = createUser(requestInfo, user);
        }

        return userServiceResponse.getUuid();
    }

    public UserDetailResponse searchUser(String stateTenantId, User user) {

        UserSearchRequest userSearchRequest =new UserSearchRequest();
        userSearchRequest.setActive(true);
        userSearchRequest.setUserType("CITIZEN");
        userSearchRequest.setTenantId(stateTenantId);

        if(StringUtils.isEmpty(user.getName()) && StringUtils.isEmpty(user.getUserName())
                && StringUtils.isEmpty(user.getMobileNumber()) && StringUtils.isEmpty(user.getUuid())) {
            return null;
        }

        if(!StringUtils.isEmpty(user.getUuid())) {
            userSearchRequest.setUuid(Collections.singletonList(user.getUuid()));
        }
        if(!StringUtils.isEmpty(user.getName())) {
            userSearchRequest.setName(user.getName());
        }
        if(!StringUtils.isEmpty(user.getUserName())) {
            userSearchRequest.setUserName(user.getUserName());
        }
        if(!StringUtils.isEmpty(user.getMobileNumber())) {
            userSearchRequest.setUserName(user.getMobileNumber());
        }

        StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());
        return userUtil.userCall(userSearchRequest,uri);
    }

    private User createUser(RequestInfo requestInfo,User userInfo) {

        userUtil.addUserDefaultFields(userInfo.getMobileNumber(), userInfo.getTenantId(), userInfo);
        StringBuilder uri = new StringBuilder(config.getUserHost())
                .append(config.getUserContextPath())
                .append(config.getUserCreateEndpoint());

        CreateUserRequest user = new CreateUserRequest(requestInfo, userInfo);
        log.info(user.getUser().toString());
        UserDetailResponse userDetailResponse = userUtil.userCall(user, uri);

        return userDetailResponse.getUser().get(0);
    }

    public String getStateLevelTenant(String tenantId){
        return userUtil.getStateLevelTenant(tenantId);
    }
}