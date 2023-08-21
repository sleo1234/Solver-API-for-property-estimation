package com.api.solver.service;


import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



@FeignClient(url= "http://localhost:8181" ,value = "COMPONENTDB")
public interface APIClient {

    @GetMapping("/api/getProperties")
    ResponseEntity<Object> getPropertiesByName(@RequestParam("names") List<String> names);


    @GetMapping("/api/all_components")

    List<String> getAllComponents ();


}
