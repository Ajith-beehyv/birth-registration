package digit.validator;

import digit.repository.BirthRegistrationRepository;
import digit.web.models.BirthApplicationSearchCriteria;
import digit.web.models.BirthRegistrationApplication;
import digit.web.models.BirthRegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
@Slf4j
public class BirthApplicationValidator {

    private BirthRegistrationRepository repository;

    @Autowired
    public BirthApplicationValidator(BirthRegistrationRepository repository) {
        this.repository = repository;
    }

    public void validateBirthApplication(BirthRegistrationRequest birthRegistrationRequest) {
        if (birthRegistrationRequest == null || birthRegistrationRequest.getBirthRegistrationApplications() == null) {
            throw new IllegalArgumentException("Birth registration request or its applications cannot be null");
        }

        birthRegistrationRequest.getBirthRegistrationApplications().forEach(application -> {
            if(ObjectUtils.isEmpty(application.getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating birth registration applications");
        });
    }

    public BirthRegistrationApplication validateApplicationExistence(BirthRegistrationApplication birthRegistrationApplication) {

        if (birthRegistrationApplication == null || birthRegistrationApplication.getApplicationNumber() == null) {
            throw new IllegalArgumentException("Birth registration application or its number cannot be null");
        }
        List<BirthRegistrationApplication> applications = repository
                .getApplications(BirthApplicationSearchCriteria.builder().applicationNumber(birthRegistrationApplication.getApplicationNumber()).build());

        if (applications.isEmpty()) {
            throw new CustomException("EG_BT_APP_NOT_FOUND", "Birth registration application not found");
        }

        return applications.get(0);
    }
}
