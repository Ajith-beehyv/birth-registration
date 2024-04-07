package digit.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ServiceRequestRepositoryTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServiceRequestRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFetchResult_SuccessfulCall() {
        // Arrange
        StringBuilder uri = new StringBuilder("http://example.com/api");
        Object request = new Object();
        Map<String, Object> response = new HashMap<>();
        response.put("key", "value");

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(response);

        // Act
        Object actualResponse = repository.fetchResult(uri, request);

        // Assert
        assertEquals(response, actualResponse);
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    void testFetchResult_ClientErrorException() {
        // Arrange
        StringBuilder uri = new StringBuilder("http://example.com/api");
        Object request = new Object();

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenThrow(new HttpClientErrorException(null, "Client error message"));

        // Act & Assert
        assertThrows(HttpClientErrorException.class, () -> repository.fetchResult(uri, request));
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    void testFetchResult_Exception() {
        // Arrange
        StringBuilder uri = new StringBuilder("http://example.com/api");
        Object request = new Object();

        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenThrow(new RuntimeException("Some exception occurred"));

        // Act & Assert
        assertDoesNotThrow(() -> repository.fetchResult(uri, request));
        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
        verifyNoMoreInteractions(restTemplate);
    }
}
