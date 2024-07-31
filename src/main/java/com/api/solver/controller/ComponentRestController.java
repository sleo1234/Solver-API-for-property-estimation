package com.api.solver.controller;


import com.api.componentdb.component.Component;
import com.api.solver.flashapi.FlashTPBody;
import com.api.solver.service.APIClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.nfunk.jep.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComponentRestController {

    @Autowired
    private APIClient apiClient;

    @PostMapping(value="api/save_component")

    public ResponseEntity<Component> addComponent (@RequestBody Component component)  throws ParseException, JsonProcessingException {
        return new ResponseEntity<>(apiClient.saveComponent(component), HttpStatus.CREATED);

    }


}
