package com.api.solver;

import com.api.componentdb.entity.ComponentResponseBody;
import com.api.solver.controller.FlashTPRestController;
import com.api.solver.flashapi.FlashTPBody;
import com.api.solver.service.APIClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlashTPRestController.class)
public class FlashTPRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean
    private APIClient apiClient;

    @Test
    public void testFlashTP() throws Exception {
        FlashTPBody requestBody = new FlashTPBody();
        // set properties of requestBody
        requestBody.setNames(Arrays.asList("propane", "n-butane", "n-pentane"));

          requestBody.setXmol(Arrays.asList(0.5, 0.25, 0.25));
        requestBody.setP(3.0);
        requestBody.setT(400.0);

       System.out.println(requestBody.getNames());
         List<Double> Tc = Arrays.asList(369.83, 425.2, 469.7);
         List<Double> Pc = (Arrays.asList(4.246, 3.8, 3.374));
         List<Double> omega = Arrays.asList(0.152, 0.193, 0.251);
        ComponentResponseBody mockResponse = new ComponentResponseBody(Tc, Pc, omega);

       when(apiClient.getPropertiesByName(any())).thenReturn(ResponseEntity.ok(mockResponse));

       String url = "/api/flash_tp";
        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk()).andReturn();
           System.out.println(result.getResponse().getContentAsString());

    }
}