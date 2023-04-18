package com.api.solver;

import com.api.solver.numerical.FlashCalculation;
import com.api.solver.numerical.PengRobinson;
import com.api.solver.numerical.Solver;
import com.api.solver.propertyPackage.PropertyPackage;
import org.junit.jupiter.api.Test;
import org.nfunk.jep.ParseException;

public class SolverTests {

    @Test
    public void testSolver() throws ParseException {

        int N_c = 3;
        PropertyPackage props = new PropertyPackage();
        Solver solver = new Solver();
        //props.setParams();
        Double[] omega_i = new Double[N_c]; //0.153, 0.199,0.255
        Double[] T_cr = new Double[N_c]; //K 369.8,425.2,469.7

        Double[] P_cr = new Double[N_c]; //


        Double[] xMol = new Double[N_c];


        omega_i[0] = 0.153;
        omega_i[1] = 0.199;
        omega_i[2] = 0.251;

        T_cr[0] = 369.8;
        T_cr[1] = 425.2;
        T_cr[2] = 469.7;

        P_cr[0] = 4.25; //MPa
        P_cr[1] = 3.8;
        P_cr[2] = 3.37;

        xMol[0] = 0.9; //
        xMol[1] = 0.05;  //N2
        xMol[2] = 0.05; //CH4

        Double T = 400.0;
        Double press = 2.9;

        PengRobinson PR = new PengRobinson();
        PR.setParams(omega_i, T_cr, P_cr, xMol);
        // PR.calcZc(298.0,0.1,xMol);
        // PR.calcMolVol(298.0,0.1,xMol);
        Double[] newFi = PR.calcfi(T, press, xMol);
        String exp = "";
        Double[] ki = PR.calcKi(T, press);
        Double denVal = 0.0;
     /*   String nomVal = "";
       for (int i=0; i < N_c; i++) {


           denVal = denVal +(1-ki[i])*xMol[i];

           nomVal = nomVal+"(1-x)*"+String.valueOf(ki[i])+"+";
           String den = String.valueOf(denVal);

          exp = den+"/"+nomVal;

           System.out.println("========================="+ den);
       }

       String equation = denVal+"/("+nomVal+"x)";
        System.out.println("========================="+ equation);
              solver.bisect(equation,0.0,100.0,0.0003,1000);
    }*/
        FlashCalculation flash = new FlashCalculation();
        flash.flashEquation(ki,xMol);

    }
}
