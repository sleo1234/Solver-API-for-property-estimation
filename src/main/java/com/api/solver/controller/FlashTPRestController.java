package com.api.solver.controller;


import com.api.solver.flashapi.ComponentResponseBody;
import com.api.solver.flashapi.FlashTPBody;
import com.api.solver.flashapi.FlashTPResponse;
import com.api.solver.numerical.FlashCalculation;
import com.api.solver.service.APIClient;

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


    @PostMapping("/flash_tp")
    public ResponseEntity<FlashTPResponse> flashTP (@RequestBody FlashTPBody flashTPBody) throws ParseException, JsonProcessingException {


        int len = flashTPBody.getXmol().size();

        Double[] Tc = new Double[len];
        Double[] Pc = new Double[len];
        Double[] acc = new Double[len];

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

        System.out.println("-----------------------------------");
        System.out.println(responseApiClient.toString());
        // call api from comp db
        FlashCalculation flash = new FlashCalculation();


        List<Double> Pcr = responseApiClient.getPc();
        List<Double> Tcr = responseApiClient.getTc();
        List<Double> omega = responseApiClient.getOmega();

           //flash.setParams(responseApiClient.getPc(),responseApiClient.getTc(),responseApiClient.getOmega());

            Double [] x_init= new Double[len];

             for (int i=0; i < len ; i++){
                 x_init[i] = flashTPBody.getXmol().get(i);
                 Tc[i] = Tcr.get(i);
                 Pc[i] = Pcr.get(i);
                 acc[i] = omega.get(i);
             }
        flash.setParams(acc,Tc,Pc,x_init);
        flash.flashTP(flashTPBody.getT(), flashTPBody.getP(), x_init,responseApiClient);





            Double [] x = flash.getVapComp();
            Double [] y = flash.getLiqComp();
          Double vapFrac = flash.getVapFrac();
        FlashTPResponse response  = new FlashTPResponse(x,y,vapFrac);

         return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @GetMapping("/testFeign")
    public ResponseEntity<Object> testFeignClient(@RequestParam("names") List<String> names){




        ResponseEntity<Object> componentDbServiceResponse = apiClient.getPropertiesByName(names);

        return new ResponseEntity<>(componentDbServiceResponse,HttpStatus.OK);
    }



    @GetMapping("/test")
    public String check(@RequestParam("params") List<String> params){


        return params.get(0)+params.get(1);
    }



}
