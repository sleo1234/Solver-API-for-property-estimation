package com.api.solver;

import com.api.solver.controller.FlashTPRestController;
import com.api.solver.service.APIClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.support.RestGatewaySupport;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlashTPRestController.class)
public class APICLientTests {

    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private APIClient apiClient;

    private MockRestServiceServer mockServer;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer((RestGatewaySupport) webApplicationContext.getAutowireCapableBeanFactory().getBean(APIClient.class));
    }

    @Test
    public void testGetPropertiesByName() {
        List<String> names = Arrays.asList("propane", "n-butane", "n-pentane");

        mockServer.expect(requestTo("http://localhost:8181/api/getProperties?names=propane,n-butane,n-pentane"))
                .andRespond(withSuccess("{\"response\":\"mock response\"}", MediaType.APPLICATION_JSON));

        apiClient.getPropertiesByName(names);

        mockServer.verify();
    }

    
}