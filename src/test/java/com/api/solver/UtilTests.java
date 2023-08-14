package com.api.solver;

import com.api.solver.util.ApiUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UtilTests {


    @Test
    public void testToArrayConversion(){

        ApiUtil apiUtil = new ApiUtil();

        List<Double> myList = new ArrayList<>();

        myList.add(1.0);
        myList.add(2.0);
     Double[] result =   apiUtil.toArray(myList);

     for (int i=0; i< result.length; i++) {
         System.out.println(result[i]);

     }

    }
}
