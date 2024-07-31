package com.api.solver.service;


import com.api.componentdb.component.Component;
import com.api.componentdb.entity.ComponentResponseBody;
import com.api.solver.flashapi.FlashTPResponse;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



@FeignClient(url= "http://localhost:8181" ,value = "COMPONENTDB")
public interface APIClient {

    @GetMapping("/api/getProperties")
    ResponseEntity<ComponentResponseBody> getPropertiesByName(@RequestParam("names") List<String> names);


    @GetMapping("/api/all_components")

    List<String> getAllComponents ();

    @PostMapping("/api/add_component")
    Component saveComponent(@RequestBody Component component);



}
