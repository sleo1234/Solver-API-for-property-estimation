package com.api.solver;

import com.api.componentdb.restcontroller.ValidateInput;
import com.api.solver.controller.ComponentRestController;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PasteListMethodTests {
    ComponentRestController componentRestController = new ComponentRestController();
    ValidateInput input = new ValidateInput();

    @Test
    void testPasteListMethod() {
        // Paste the code snippet here
        Set<String> names = new HashSet<>();
        names.add("methane");
        names.add("ethane");
        names.add("propane");
        names.add("n-butane");

        HashMap<String,Double> list = new HashMap<>();
        list.put("methane",0.5);
        list.put("cis-han",0.3);
        input.setDbList(names);
        input.setUserInput(list);

       String response = componentRestController.checkList(input);
       System.out.println(response);
    }

}
