package com.api.solver;

import com.api.solver.flashapi.ComponentResponseBody;
import com.api.solver.flashapi.FlashTPBody;
import com.api.solver.numerical.FlashCalculation;
import com.api.solver.numerical.PengRobinson;
import com.api.solver.numerical.Solver;
import com.api.solver.propertyPackage.PropertyPackage;
import org.junit.jupiter.api.Test;
import org.nfunk.jep.ParseException;
import org.yaml.snakeyaml.nodes.SequenceNode;

import java.util.*;

public class SolverTests {

    @Test
    public void testIsothermalFlash() throws ParseException {

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

        xMol[0] = 0.5; //
        xMol[1] = 0.25;  //
        xMol[2] = 0.25; //

        Double T = 400.0;
        Double press = 3.0;
        int MAX_ITER = 5;

        PengRobinson PR = new PengRobinson();
        PR.setParams(omega_i, T_cr, P_cr, xMol);

        String exp = "";


        Double[][] K = new Double[MAX_ITER][N_c];
        Double[][] x = new Double[MAX_ITER][N_c];
        Double[][] y = new Double[MAX_ITER][N_c];
        Double[][] FiL = new Double[MAX_ITER][N_c];
        Double[][] FiV = new Double[MAX_ITER][N_c];
        Double[] ZiL = new Double[MAX_ITER];
        Double[] ZiV = new Double[MAX_ITER];
        FlashCalculation flash = new FlashCalculation();

        Double[] teta = new Double[MAX_ITER];


        Double[] ki0 = PR.calcKi(T, press);
        teta[0] = flash.solveVapFrac(ki0, xMol, 0.5);
        for (int i = 0; i < N_c; i++) {
            K[0][i] = ki0[i];

            x[0][i] = xMol[i] / (1.0 - (1.0 - ki0[i]) * teta[0]);
            y[0][i] = x[0][i] * ki0[i];


            //  System.out.println(" Initial K values: =========== ");
            // solver.printArr(K[0]);


        }

        PR.calcZc(T, press, xMol);

        System.out.print("--------0000000000000000000000000000");
        solver.printArr(K[0]);


        for (int j = 0; j < MAX_ITER - 1; j++) {
            solver.printArr(K[j]);
            teta[j] = flash.solveVapFrac(K[j], xMol, 0.5);

            System.out.println("===========================================(00001) k values: ");
            solver.printArr(K[0]);

            Double[] diff = solver.substract(K[j], 1.0);
            Double[] product = solver.prodScal(diff, teta[j]);
            Double[] diff2 = solver.substract(product, 1.0);

            x[j] = solver.divArr(xMol, diff2);//check this equation
            ZiL[j] = PR.calcZc(T, press, x[j]).get(0);
            solver.printArr(x[0]);

            PR.calcZc(T, press, x[j]);

            //ZiV[j] = PR.calcZc(T,press,y[j]);
            y[j] = solver.prodArr(x[j], K[j]);
            ZiV[j] = PR.calcZc(T, press, y[j]).get(0);
            PR.calcZc(T, press, y[j]);
            FiL[j] = PR.calcfi(T, press, x[j], ZiL[j]);
            System.out.println("===========================================X values: ");
            solver.printArr(x[j]);
            System.out.println("===========================================Y values: ");
            solver.printArr(y[j]);

            FiV[j] = PR.calcfi(T, press, y[j], ZiV[j]);

            System.out.println("===========================================K values: ");
            System.out.println("===========================================Zl values: ");
            solver.printArr(ZiL);
            System.out.println("===========================================Zv values: ");
            solver.printArr(ZiV);
            //  K[j+1] = solver.prodArr(solver.divArr(FiL[j], FiV[j]), K[j]);
            K[j + 1] = solver.divArr(FiL[j], FiV[j]);
            solver.printArr(K[j]);
        }


        System.out.println("===================      " + teta[0]);


        System.out.println("************************* X");

        solver.printMat(x);
        System.out.println("************************* Molar frac matrix liquid phase");
        solver.printMat(y);
        System.out.println("************************* Molar frac matrix vapour phase");
        solver.printArr(teta);


        System.out.println("************************* Fugacities matrix liquid phase");
        solver.printMat(FiL);

        System.out.println("************************* Fugacities matrix vapour phase");
        solver.printMat(FiV);
        System.out.println("************************* END");

        System.out.println("************************* K matrix ");
        solver.printMat(K);
        System.out.println("************************* END");


    }


    @Test
    public void testBubblePointFlash() throws ParseException {
        FlashCalculation flash = new FlashCalculation();
        Double[] xMol = new Double[3];

        xMol[0] = 0.5; //
        xMol[1] = 0.25;  //
        xMol[2] = 0.25; //
        Double T = 400.0;
     flash.bubblePoint2(T, xMol);
     //flash.bubblePoint(T, xMol);
        flash.bubblePointBisect(T,xMol);
    }


    @Test
    public void testDewPointFlash() throws ParseException {
        FlashCalculation flash = new FlashCalculation();
        Double[] xMol = new Double[3];

        xMol[0] = 0.2; //
        xMol[1] = 0.3;  //
        xMol[2] = 0.5; //
        Double T = 400.0;
        flash.dewPoint(T, xMol);
    }



    @Test
    public void testFlashTP() throws ParseException {
        FlashCalculation flash = new FlashCalculation();
        Double[] xMol = new Double[3];
        int N_c = xMol.length;

        Double T = 420.0;
        Double press = 3.0;



        //props.setParams();
        Double[] omega_i = new Double[N_c]; //0.153, 0.199,0.255
        Double[] T_cr = new Double[N_c]; //K 369.8,425.2,469.7

        Double[] P_cr = new Double[N_c]; //



        omega_i[0] = 0.153;
        omega_i[1] = 0.199;
        omega_i[2] = 0.251;

        T_cr[0] = 369.8;
        T_cr[1] = 425.2;
        T_cr[2] = 469.7;

        P_cr[0] = 4.25; //MPa
        P_cr[1] = 3.8;
        P_cr[2] = 3.37;

        xMol[0] = 0.5; //
        xMol[1] = 0.25;  //
        xMol[2] = 0.25; //
        flash.setParams(omega_i,T_cr,P_cr,xMol);

        ComponentResponseBody body = new ComponentResponseBody();

        body.setTc(Arrays.asList(T_cr));
        body.setPc(Arrays.asList(P_cr));
        body.setOmega(Arrays.asList(omega_i));

        //flash.flashTXVapFrac(T, press, xMol,body);

        flash.flahTXNRaphson(T,0.5,xMol,body);

    }





    @Test
    public void testRachfordRice() throws ParseException {
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

        xMol[0] = 0.5; //
        xMol[1] = 0.25;  //
        xMol[2] = 0.25; //

        Double T = 400.0;
        Double press = 3.0;
        int MAX_ITER = 5;

        PengRobinson PR = new PengRobinson();
        PR.setParams(omega_i, T_cr, P_cr, xMol);


        List<Double> sols = new ArrayList<>();
        Double[] yi = new Double[N_c];

        Double error = 1e-4;
        int max = 10000;

        // PR.calcfi(T,press,xMol);
        //ESTIMATE PRESSURE
        Double[] Pi0 = PR.calcPi(T);
        Double Pinit = 0.0;
        for (int i = 0; i < N_c; i++) {

            Pinit = Pinit + xMol[i] * Pi0[i];
        }
        Double[] ki0 = PR.calcKi(T, Pinit);
        for (int i = 0; i < N_c; i++) {

            yi[i] =  ki0[i] * xMol[i];
        }
        System.out.println("Estimated pressure: " + Pinit);
        solver.printArr(yi);

        Double Zl = PR.calcZc(T, press, xMol).get(0);
        Double[] FiL = PR.calcfi(T, Pinit, xMol, Zl);
        Double Zv = PR.calcZc(T, press, xMol).get(0);
        Double[] FiV = PR.calcfi(T, Pinit, yi, Zv);

        System.out.println("*Z***: " + Zl + " ******Zv " + Zv);
        solver.printArr(FiL);
        solver.printArr(FiV);
        Double[] newKi = solver.prodArr(ki0, solver.divArr(FiL, FiV));
        System.out.println("New Ki------------");
        solver.printArr(newKi);
        Double[] newYi = solver.prodArr(newKi, xMol);
        System.out.println("New Yi------------");
        solver.printArr(newYi);
    }


    @Test
    public void testArrayOperations() throws ParseException {

        Solver solver = new Solver();

        Double[] arr1 = new Double[]{1.0, 2.0, 3.0};
        Double[] arr2 = new Double[]{1.2, 2.1, 3.2};
        Double[][] mat1 = new Double[][]{{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}};


        solver.printArr(solver.divArr(mat1[0], arr1));
        solver.printMat(solver.multMat(mat1, 2.0));
        Double[] result = solver.substract(arr1,arr2);

        solver.printArr(result);
    }


    @Test

    public void testNormalization () throws ParseException {

        Double [] vec = new Double [] {4.0,3.0,1.0};
        FlashCalculation flash = new FlashCalculation();

        Solver solver = new Solver();

        solver.printArr(flash.normalize(vec));
    }

    @Test
    public void testFlashEquation() throws ParseException {
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

        xMol[0] = 0.5; //
        xMol[1] = 0.25;  //
        xMol[2] = 0.25; //

        Double T = 500.0;
        Double press = 3.0;
        int MAX_ITER = 5;

        PengRobinson PR = new PengRobinson();
        PR.setParams(omega_i, T_cr, P_cr, xMol);
       FlashCalculation flash = new FlashCalculation();
       Double [] ki = PR.calcKi(T,press);
       solver.printArr(ki);
       flash.flashEquation(ki,xMol);

    }

}
