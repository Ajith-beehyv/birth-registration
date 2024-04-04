package digit.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.service.BirthRegistrationService;
import digit.util.ResponseInfoFactory;
import digit.web.models.BirthApplicationSearchCriteria;
import digit.web.models.BirthRegistrationApplication;
import digit.web.models.BirthRegistrationRequest;
import digit.web.models.BirthRegistrationResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-03-29T17:23:06.520606+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class BirthApiController {

    private final ObjectMapper objectMapper;

    private BirthRegistrationService birthRegistrationService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public BirthApiController(ObjectMapper objectMapper, ResponseInfoFactory responseInfoFactory, BirthRegistrationService birthRegistrationService) {
        this.objectMapper = objectMapper;
        this.responseInfoFactory = responseInfoFactory;
        this.birthRegistrationService = birthRegistrationService;
    }

    @RequestMapping(value = "/birth/registration/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<BirthRegistrationResponse> birthRegistrationV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new Birth Registration Application(s) + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody BirthRegistrationRequest body) {
        List<BirthRegistrationApplication> applications = birthRegistrationService.registerBtRequest(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        BirthRegistrationResponse response = BirthRegistrationResponse.builder().birthRegistrationApplications(applications).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/birth/registration/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<BirthRegistrationRequest> birthRegistrationV1SearchPost(@Valid RequestInfo requestInfo, @Parameter(in = ParameterIn.DEFAULT, description = "Parameter to carry Request metadata in the request body", schema = @Schema()) @Valid @RequestBody BirthApplicationSearchCriteria body) {
        List<BirthRegistrationApplication> applications = birthRegistrationService.searchBtApplications(requestInfo, body);
        BirthRegistrationRequest response = BirthRegistrationRequest.builder().birthRegistrationApplications(applications).requestInfo(requestInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/birth/registration/v1/_update", method = RequestMethod.POST)
    public ResponseEntity<BirthRegistrationResponse> birthRegistrationV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new (s) + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody BirthRegistrationRequest body) {
        List<BirthRegistrationApplication> applications = birthRegistrationService.updateBtApplication(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        BirthRegistrationResponse response = BirthRegistrationResponse.builder().birthRegistrationApplications(applications).responseInfo(responseInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
