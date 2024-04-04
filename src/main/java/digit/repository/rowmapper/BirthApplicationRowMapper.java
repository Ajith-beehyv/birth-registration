package digit.repository.rowmapper;

import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class BirthApplicationRowMapper implements ResultSetExtractor<List<BirthRegistrationApplication>>  {


    @Override
    public List<BirthRegistrationApplication> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<String,BirthRegistrationApplication> birthRegistrationApplicationMap = new LinkedHashMap<>();
        while (resultSet.next()){
            String uuid = resultSet.getString("registration_id");
            BirthRegistrationApplication birthRegistrationApplication = birthRegistrationApplicationMap.get(uuid);

            if(birthRegistrationApplication == null) {

                User father = User.builder().uuid(resultSet.getString("father_id")).build();
                User mother = User.builder().uuid(resultSet.getString("mother_id")).build();

                AuditDetails auditdetails = AuditDetails.builder()
                        .createdBy(resultSet.getString("registration_created_by"))
                        .createdTime(resultSet.getLong("registration_created_time"))
                        .lastModifiedBy(resultSet.getString("registration_last_modified_by"))
                        .lastModifiedTime(resultSet.getLong("registration_last_modified_time"))
                        .build();

                birthRegistrationApplication = BirthRegistrationApplication.builder()
                        .id(resultSet.getString("registration_id"))
                        .applicationNumber(resultSet.getString("registration_application_number"))
                        .tenantId(resultSet.getString("registration_tenant_id"))
                        .babyFirstName(resultSet.getString("baby_first_name"))
                        .babyLastName(resultSet.getString("baby_last_name"))
                        .father(father)
                        .mother(mother)
                        .doctorName(resultSet.getString("doctor_name"))
                        .hospitalName(resultSet.getString("hospital_name"))
                        .placeOfBirth(resultSet.getString("place_of_birth"))
                        .timeOfBirth(resultSet.getInt("time_of_birth"))
                        .auditDetails(auditdetails)
                        .build();
            }
            addChildrenToProperty(resultSet, birthRegistrationApplication);
            birthRegistrationApplicationMap.put(uuid, birthRegistrationApplication);
        }
        return new ArrayList<>(birthRegistrationApplicationMap.values());
    }

    private void addChildrenToProperty(ResultSet resultSet, BirthRegistrationApplication birthRegistrationApplication)
            throws SQLException {
        addAddressToApplication(resultSet, birthRegistrationApplication);
    }

    private void addAddressToApplication(ResultSet resultSet, BirthRegistrationApplication birthRegistrationApplication) throws SQLException {

        Address address = Address.builder()
                .latitude(resultSet.getDouble("address_latitude"))
                .longitude(resultSet.getDouble("address_longitude"))
                .addressId(resultSet.getString("address_id"))
                .addressNumber(resultSet.getString("address_number"))
                .addressLine1(resultSet.getString("address_line1"))
                .addressLine2(resultSet.getString("address_line2"))
                .city(resultSet.getString("address_city"))
                .pincode(resultSet.getString("address_pincode"))
                .detail(resultSet.getString("address_detail"))
                .build();

        BirthApplicationAddress birthApplicationAddress = BirthApplicationAddress.builder()
                .id(UUID.fromString(resultSet.getString("app_address_id")))
                .tenantId(resultSet.getString("app_address_tenant_id"))
                .applicationNumber(resultSet.getString("app_address_application_number"))
                .applicantAddress(address)
                .build();

        birthRegistrationApplication.setAddress(birthApplicationAddress);
    }

}
