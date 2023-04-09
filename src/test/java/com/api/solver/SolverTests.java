package com.api.solver;

import com.api.solver.numerical.Solver;
import org.junit.jupiter.api.Test;
import org.nfunk.jep.ParseException;

public class SolverTests {

    @Test
    public void testSolver() throws ParseException {

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

        P_cr[0] = 4.25; //MPa
        P_cr[1] = 3.8;
        P_cr[2] = 3.37;

        xMol[0] = 0.0;
        xMol[1] =  0.5;
        xMol[2]  = 0.5;


        PengRobinson PR = new PengRobinson();
       PR.setParams(omega_i,T_cr,P_cr,xMol);
       PR.calcZc(298.0,0.1,xMol);
       PR.calcMolVol(298.0,0.1,xMol);


    }
}
