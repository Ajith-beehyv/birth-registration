package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateUserRequest {

    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("user")
    private User user;

}
