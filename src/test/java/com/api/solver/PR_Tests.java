package com.api.solver;

import com.api.solver.numerical.MathFunction;
import com.api.solver.numerical.Solver;
import com.api.solver.propertyPackage.PropertyPackage;
import org.junit.jupiter.api.Test;
import org.nfunk.jep.ParseException;

public class PR_Tests {


    PropertyPackage props = new PropertyPackage();

    @Test
    public void testRCalc() throws ParseException {

        props.setParams();


        props.b_M();
        props.alfa_m();
        props.a_M();
        props.lambda_vec();
        props.attractParam();
        props.coVolParam();

        Double press = 10.0;
        Double temp = 400.0;

        MathFunction func = new MathFunction();
        Solver solver = new Solver();

        double x0=100;
        double error=0.001;
        int maxIter = 10000;

       String eqn = props.PengRobinsonEq(press, temp);
       solver.newtonRaphson(eqn,x0,error,maxIter);

    }
}
