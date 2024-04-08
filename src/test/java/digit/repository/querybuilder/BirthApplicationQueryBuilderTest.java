package digit.repository.querybuilder;

import digit.web.models.BirthApplicationSearchCriteria;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BirthApplicationQueryBuilderTest {

    @Test
    void testGetBirthApplicationSearchQuery() {
        // Arrange
        BirthApplicationSearchCriteria criteria = new BirthApplicationSearchCriteria();
        criteria.setTenantId("tenant123");
        criteria.setIds(Arrays.asList("id1", "id2"));
        criteria.setApplicationNumber("APP123");

        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        BirthApplicationQueryBuilder queryBuilder = new BirthApplicationQueryBuilder();
        String query = queryBuilder.getBirthApplicationSearchQuery(criteria, preparedStmtList);

        // Assert
        String expectedQuery = "SELECT reg.id AS registration_id, reg.tenantid AS registration_tenant_id, reg.applicationnumber AS registration_application_number, reg.babyfirstname AS baby_first_name, reg.babylastname AS baby_last_name, reg.fatherid AS father_id, reg.motherid AS mother_id, reg.doctorname AS doctor_name, reg.hospitalname AS hospital_name, reg.placeofbirth AS place_of_birth, reg.timeofbirth AS time_of_birth, reg.createdby AS registration_created_by, reg.lastmodifiedby AS registration_last_modified_by, reg.createdtime AS registration_created_time, reg.lastmodifiedtime AS registration_last_modified_time, appAddr.id AS app_address_id, appAddr.tenantId AS app_address_tenant_id, appAddr.applicationnumber AS app_address_application_number, addr.latitude AS address_latitude, addr.longitude AS address_longitude, addr.buildingname AS address_building_name, addr.addressid AS address_id, addr.addressnumber AS address_number, addr.addressline1 AS address_line1, addr.addressline2 AS address_line2, addr.city AS address_city, addr.pincode AS address_pincode, addr.detail AS address_detail FROM eg_bt_registration reg LEFT JOIN eg_btr_app_address appAddr ON reg.applicationNumber = appAddr.applicationNumber LEFT JOIN eg_bt_address addr ON appAddr.id = addr.addressId WHERE  reg.tenantid = ?  AND  reg.id IN (  ?, ? )  AND  reg.applicationnumber = ?  ORDER BY reg.createdtime DESC ";
        assertEquals(expectedQuery, query);
    }
}