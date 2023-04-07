package com.api.solver;

import com.api.solver.numerical.MathFunction;
import com.api.solver.numerical.Solver;
import com.api.solver.propertyPackage.PropertyPackage;
import org.junit.jupiter.api.Test;
import org.nfunk.jep.ParseException;

public class PR_Tests {


    PropertyPackage props = new PropertyPackage();

    public PR_Tests() throws ParseException {
    }

    @Test
    public void testRCalc() throws ParseException {
     int N_c = 3;

        //props.setParams();
        Double [] omega_i = new Double[N_c]; //0.153, 0.199,0.255
        Double [] T_cr = new Double[N_c]; //K 369.8,425.2,469.7

        Double[] P_cr = new Double[N_c]; //


        Double [] xMol = new Double[N_c];


        omega_i[0] = 0.153;
        omega_i[1] = 0.199;
        omega_i[2] = 0.255;

        T_cr[0] = 369.8;
        T_cr[1] = 425.2;
        T_cr[2] = 469.7;

        P_cr[0] = 4.25; //Pa
        P_cr[1] = 3.8;
        P_cr[2] = 3.37;

        xMol[0] = 0.5;
        xMol[1] =  0.5;
        xMol[2]  = 0.0;

        PropertyPackage props = new PropertyPackage(omega_i, T_cr,  P_cr,xMol);
        Double T = 400.0;

        props.b_M();
        props.alfa_m(T);
        props.a_M(T);
        props.lambda_vec();
        props.attractParam(T,xMol);
        props.coVolParam(xMol);

        Double press =1.0; //MPa
        Double temp =600.0;

        MathFunction func = new MathFunction();
        Solver solver = new Solver();

        double x0= 90;
        double error=0.001;
        int maxIter = 10000;


        props.calcKi(5.0,300.0);
      String eqn = props.PengRobinsonEq(press,temp,xMol);
       //solver.newtonRaphson(eqn,x0,error,maxIter);

       solver.newtonRaphson(eqn,x0,error,maxIter);



    }
}
