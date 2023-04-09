package com.api.solver;

import com.api.solver.numerical.MathFunction;
import com.api.solver.numerical.Solver;
import com.api.solver.propertyPackage.PropertyPackage;
import org.nfunk.jep.ParseException;

public class PengRobinson {

    PropertyPackage props;

    Double volume;

    Double R = 8.31; //m3 * Pa / (mol * K)
    public void setParams (Double [] omega_i, Double [] T_cr,  Double [] P_cr, Double[] xMol) throws ParseException {
        props = new PropertyPackage(omega_i,T_cr,P_cr,xMol);

    }


    public Double calcZc (Double T, Double press,Double [] xMol) throws ParseException {
        props.setxMol(xMol);
        props.b_M();
        props.alfa_m(T);
        props.a_M(T);
        props.lambda_vec();
        props.attractParam(T,xMol);
        props.coVolParam(xMol);



        MathFunction func = new MathFunction();
        Solver solver = new Solver();

        double x0= 1.0;
        double error=0.0001;
        int maxIter = 10000;


        //props.calcKi(5.0,300.0);
        String eqn = props.PengRobinsonEq(press,T,xMol);
        Double sol = solver.newtonRaphson(eqn,x0,error,maxIter);


        return sol;
    }

    public Double [] calcKi (Double T, Double press) {

        return props.calcKi(T,press);
    }


    public Double calcMolVol (Double T, Double press,Double [] xMol) throws ParseException {

        props.setxMol(xMol);
        Double Z = calcZc(T,press,xMol);
        Double volume = (8.31*T*Z)/press;
        System.out.print("--------------Molar volume (cm3/mol):----------- " + volume);
        return volume;

    }


    public Double calcfi (Double T, Double press,Double [] xMol){
       // props.setxMol(xMol);


        return 0.0;
    }

}
