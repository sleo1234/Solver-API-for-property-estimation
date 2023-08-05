package com.api.solver.controller;


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

import java.util.List;

@RestController
@RequestMapping("/api")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class FlashTPRestController {


    @Autowired
    private APIClient apiClient;


    @PostMapping("/flash_tp")
    public ResponseEntity<FlashTPResponse> flashTP (@RequestBody FlashTPBody flashTPBody) throws ParseException {
      int listLength = flashTPBody.getXMol().size();
          String [] compNames = new String[listLength];

        // call api from comp db
        FlashCalculation flash = new FlashCalculation();

        for (int i=0 ; i < flashTPBody.getXMol().size(); i++){
            compNames[i] = flashTPBody.getNames().get(i);
        }


        List<String> names = flashTPBody.getNames();
        Object componentDbServiceResponse = apiClient.getPropertiesByName(compNames);
        System.out.println(componentDbServiceResponse.toString());
           //flash.setParams();

            Double [] x_init= new Double[flashTPBody.getXMol().size()];

            for (int i=0 ; i < listLength; i++){
                x_init[i] = flashTPBody.getXMol().get(i);
            }

        flash.flashTP(flashTPBody.getT(), flashTPBody.getP(), x_init);




            Double [] x = flash.getVapComp();
            Double [] y = flash.getLiqComp();
          Double vapFrac = flash.getVapFrac();
        FlashTPResponse response  = new FlashTPResponse(x,y,vapFrac);

         return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/testFeign")
    public ResponseEntity<Object> testFeignClient(@RequestParam("names") String[] names){

        ResponseEntity<Object> componentDbServiceResponse = apiClient.getPropertiesByName(names);

        return new ResponseEntity<>(componentDbServiceResponse,HttpStatus.OK);
    }


}
