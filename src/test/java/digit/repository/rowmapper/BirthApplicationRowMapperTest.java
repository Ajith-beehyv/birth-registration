package digit.repository.rowmapper;

import digit.web.models.Address;
import digit.web.models.BirthRegistrationApplication;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BirthApplicationRowMapperTest {

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private BirthApplicationRowMapper rowMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testExtractData_WithResultSetContainingData_ShouldReturnList() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("registration_application_number")).thenReturn("123");
        when(resultSet.getString("registration_tenant_id")).thenReturn("tenant123");
        when(resultSet.getString("registration_id")).thenReturn("1");
        when(resultSet.getString("baby_first_name")).thenReturn("John");
        when(resultSet.getString("baby_last_name")).thenReturn("Doe");
        when(resultSet.getString("father_id")).thenReturn("father123");
        when(resultSet.getString("mother_id")).thenReturn("mother123");
        when(resultSet.getString("doctor_name")).thenReturn("Dr. Smith");
        when(resultSet.getString("hospital_name")).thenReturn("Hospital A");
        when(resultSet.getString("place_of_birth")).thenReturn("City A");
        when(resultSet.getInt("time_of_birth")).thenReturn(8);
        when(resultSet.getString("registration_created_by")).thenReturn("user123");
        when(resultSet.getLong("registration_created_time")).thenReturn(123456L);
        when(resultSet.getString("registration_last_modified_by")).thenReturn("user456");
        when(resultSet.getLong("registration_last_modified_time")).thenReturn(123457L);
        when(resultSet.getString("registration_application_number")).thenReturn("234");
        when(resultSet.getString("app_address_application_number")).thenReturn("234");
        when(resultSet.getString("app_address_id")).thenReturn("7b357fb9-90fc-4766-894e-f8e1d837a015");
        when(resultSet.getString("app_address_tenant_id")).thenReturn("tenant123");
        when(resultSet.getString("app_address_application_number")).thenReturn("123");
        when(resultSet.getDouble("address_latitude")).thenReturn(1.0);
        when(resultSet.getDouble("address_longitude")).thenReturn(2.0);
        when(resultSet.getString("address_id")).thenReturn("7b357fb9-90fc-4766-894e-f8e1d837a015");
        when(resultSet.getString("address_number")).thenReturn("456");
        when(resultSet.getString("address_line1")).thenReturn("Address Line 1");
        when(resultSet.getString("address_line2")).thenReturn("Address Line 2");
        when(resultSet.getString("address_city")).thenReturn("City A");
        when(resultSet.getString("address_pincode")).thenReturn("12345");
        when(resultSet.getString("address_detail")).thenReturn("Detail A");

        // Act
        List<BirthRegistrationApplication> result = rowMapper.extractData(resultSet);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        BirthRegistrationApplication application = result.get(0);
        assertNotNull(application);
        assertEquals("234", application.getApplicationNumber());
        assertEquals("tenant123", application.getTenantId());
        assertEquals("1", application.getId());
        assertEquals("John", application.getBabyFirstName());
        assertNotNull(application.getMother());
        assertEquals("Hospital A", application.getHospitalName());
        assertEquals(8, application.getTimeOfBirth());
        assertNotNull(application.getAuditDetails());
        assertEquals("user123", application.getAuditDetails().getCreatedBy());
        assertEquals(123457L, application.getAuditDetails().getLastModifiedTime());
        assertNotNull(application.getAddress());
        assertEquals("123", application.getAddress().getApplicationNumber());
        assertEquals("tenant123", application.getAddress().getTenantId());
        assertNotNull(application.getAddress().getApplicantAddress());
        Address address = application.getAddress().getApplicantAddress();
        assertEquals("7b357fb9-90fc-4766-894e-f8e1d837a015", address.getAddressId());
    }
}