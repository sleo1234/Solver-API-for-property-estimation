package com.api.solver.controller;


import com.api.solver.flashapi.ComponentResponseBody;
import com.api.solver.flashapi.FlashTPBody;
import com.api.solver.flashapi.FlashTPResponse;
import com.api.solver.numerical.FlashCalculation;
import com.api.solver.service.APIClient;

import com.api.solver.util.ApiUtil;
import org.nfunk.jep.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api")
@ImportAutoConfiguration({FeignAutoConfiguration.class})

public class FlashTPRestController {


    @Autowired
    private APIClient apiClient;



    ApiUtil apiUtil = new ApiUtil();


    @PostMapping("/flash_tp")
    public ResponseEntity<FlashTPResponse> flashTP (@RequestBody FlashTPBody flashTPBody) throws ParseException, JsonProcessingException {


        int len = flashTPBody.getXmol().size();

        Double[] Tc = new Double[len];
        Double[] Pc = new Double[len];
        Double[] acc = new Double[len];
        Double [] x_init= new Double[len];

        List<String> names = flashTPBody.getNames();

        ResponseEntity<Object> componentDbServiceResponse = apiClient.getPropertiesByName(names);
        Object responseDb = componentDbServiceResponse.getBody();


        System.out.println("-----------------------------------");
        System.out.println(responseDb);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       String jsonReponse = mapper.writeValueAsString(componentDbServiceResponse.getBody());
        ComponentResponseBody responseApiClient = mapper.readValue(jsonReponse,
                ComponentResponseBody.class);

        FlashCalculation flash = new FlashCalculation();


        List<Double> Pcr = responseApiClient.getPc();
        List<Double> Tcr = responseApiClient.getTc();
        List<Double> omega = responseApiClient.getOmega();



             x_init = apiUtil.toArray(flashTPBody.getXmol());
             Tc = apiUtil.toArray(Tcr);
             Pc = apiUtil.toArray(Pcr);
             acc = apiUtil.toArray(omega);

        flash.setParams(acc,Tc,Pc,x_init);
        flash.flashTP(flashTPBody.getT(), flashTPBody.getP(), x_init,responseApiClient);





            Double [] x = flash.getVapComp();
            Double [] y = flash.getLiqComp();
            Double vapFrac = flash.getVapFrac();
        FlashTPResponse response  = new FlashTPResponse(x,y,vapFrac);

         return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @GetMapping("/all_components")
    public List<String> testFeignClient(){


      List<String> names = apiClient.getAllComponents();


      return names;


    }



    @GetMapping("/test")
    public String check(@RequestParam("params") List<String> params){


        return params.get(0)+params.get(1);
    }



}
