package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

/**
 * This object holds list of documents attached during the transaciton for a property
 */
@Schema(description = "This object holds list of documents attached during the transaciton for a property")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-03-29T17:23:06.520606+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document {

    @JsonProperty("id")
    @Size(max = 64)
    private String id = null;

    @JsonProperty("documentType")
    private String documentType = null;

    @JsonProperty("fileStore")
    private String fileStore = null;

    @JsonProperty("documentUid")
    @Size(max = 64)
    private String documentUid = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;


}
