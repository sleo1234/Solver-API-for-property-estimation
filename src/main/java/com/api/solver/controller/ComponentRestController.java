package com.api.solver.controller;


import com.api.componentdb.component.Component;
import com.api.componentdb.restcontroller.ValidateInput;
import com.api.solver.flashapi.FlashTPBody;
import com.api.solver.service.APIClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.nfunk.jep.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class ComponentRestController {

    @Autowired
    private APIClient apiClient;

    @PostMapping(value="save_component")

    public ResponseEntity<Component> addComponent (@RequestBody Component component)  throws ParseException, JsonProcessingException {
        return new ResponseEntity<>(apiClient.saveComponent(component), HttpStatus.CREATED);

    }
    @PostMapping("/validate_componentlist")

    public String checkList(@RequestBody ValidateInput input){

        String message="Stream OK.";

        HashMap<String, Double> userInput = input.getUserInput();
        Set<String> dbList = input.getDbList();
        ArrayList<String> invalidComponents = new ArrayList<>();
        Double checkSum=0.0;

        for (String key : userInput.keySet()){

            checkSum = checkSum+userInput.get(key);
            if (checkSum < 0.999 && checkSum >= 1.000001){
                message = "Stream not OK. Sum of mole fractions is not equal to 1.";
            }


            if (!dbList.contains(key)){
                invalidComponents.add(key);
            }
            if (invalidComponents.size()>0) {
                message = "Stream not OK. Following components are not present in the database: " + Arrays.toString(invalidComponents.toArray());
            }


        }

        return message;
    }


}
