package digit.enrichment;

import digit.service.UserService;
import digit.util.IdgenUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class BirthApplicationEnrichment {

    private IdgenUtil idgenUtil;

    private UserService userService;

    @Autowired
    public BirthApplicationEnrichment(IdgenUtil idgenUtil, UserService userService) {
        this.idgenUtil = idgenUtil;
        this.userService = userService;
    }

    public void enrichBirthApplication(BirthRegistrationRequest birthRegistrationRequest) {
        List<String> birthRegistrationIdList = idgenUtil.getIdList(birthRegistrationRequest.getRequestInfo(), birthRegistrationRequest.getBirthRegistrationApplications().get(0).getTenantId(), "btr.registrationid", "", birthRegistrationRequest.getBirthRegistrationApplications().size());
        int index = 0;
        for(BirthRegistrationApplication application : birthRegistrationRequest.getBirthRegistrationApplications()){
            // Enrich audit details
            AuditDetails auditDetails = AuditDetails.builder().createdBy(birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            application.setAuditDetails(auditDetails);

            // Enrich UUID
            application.setId(UUID.randomUUID().toString());

            //Enrich application number from Idgen
            application.setApplicationNumber(birthRegistrationIdList.get(index++));

            application.getAddress().setApplicationNumber(application.getApplicationNumber());
            application.getAddress().setId(UUID.randomUUID());
            application.getAddress().getApplicantAddress().setAddressId(application.getAddress().getId().toString());
        }
    }

    public void enrichBirthApplicationUponUpdate(BirthRegistrationRequest birthRegistrationRequest) {
        long currentTimeMillis = System.currentTimeMillis();
        String lastModifiedBy = birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid();

        for (BirthRegistrationApplication application : birthRegistrationRequest.getBirthRegistrationApplications()) {
            application.getAuditDetails().setLastModifiedTime(currentTimeMillis);
            application.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        }
    }

    public void enrichFatherUserOnSearch(BirthRegistrationApplication application) {
        String stateTenant = userService.getStateLevelTenant(application.getTenantId());
        UserDetailResponse fatherUserResponse = userService.searchUser(stateTenant, application.getFather());
        User fatherUser = fatherUserResponse.getUser().get(0);
        log.info(fatherUser.toString());
        User father = User.builder()
                .uuid(fatherUser.getUuid())
                .tenantId(fatherUser.getTenantId())
                .name(fatherUser.getName())
                .userName(fatherUser.getUserName())
                .mobileNumber(fatherUser.getMobileNumber())
                .emailId(fatherUser.getEmailId())
                .type(fatherUser.getType())
                .roles(fatherUser.getRoles()).build();
        application.setFather(father);
    }

    public void enrichMotherUserOnSearch(BirthRegistrationApplication application) {
        String stateTenant = userService.getStateLevelTenant(application.getTenantId());
        UserDetailResponse motherUserResponse = userService.searchUser(stateTenant, application.getMother());
        User motherUser = motherUserResponse.getUser().get(0);
        log.info(motherUser.toString());
        User mother = User.builder()
                .uuid(motherUser.getUuid())
                .tenantId(motherUser.getTenantId())
                .name(motherUser.getName())
                .userName(motherUser.getUserName())
                .mobileNumber(motherUser.getMobileNumber())
                .emailId(motherUser.getEmailId())
                .type(motherUser.getType())
                .roles(motherUser.getRoles()).build();
        application.setMother(mother);
    }

}
