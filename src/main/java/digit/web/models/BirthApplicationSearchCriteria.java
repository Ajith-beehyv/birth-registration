package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * BirthApplicationSearchCriteria
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-03-29T17:23:06.520606+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirthApplicationSearchCriteria {

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("ids")
    @Size(max = 50)
    private List<String> ids = null;

    @JsonProperty("applicationNumber")
    @Size(min = 2, max = 64)
    private String applicationNumber = null;


    public BirthApplicationSearchCriteria addIdsItem(String idsItem) {
        if (this.ids == null) {
            this.ids = new ArrayList<>();
        }
        this.ids.add(idsItem);
        return this;
    }

}
