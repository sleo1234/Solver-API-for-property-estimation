package com.api.solver;

import com.api.solver.numerical.FlashCalculation;
import com.api.solver.numerical.PengRobinson;
import com.api.solver.numerical.Solver;
import com.api.solver.propertyPackage.PropertyPackage;
import org.junit.jupiter.api.Test;
import org.nfunk.jep.ParseException;
import org.yaml.snakeyaml.nodes.SequenceNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        xMol[0] = 0.7; //
        xMol[1] = 0.2;  //
        xMol[2] = 0.1; //

        Double T = 400.0;
        Double press = 0.2;
        int MAX_ITER=5;

        PengRobinson PR = new PengRobinson();
        PR.setParams(omega_i, T_cr, P_cr, xMol);
        // PR.calcZc(298.0,0.1,xMol);
        // PR.calcMolVol(298.0,0.1,xMol);
       // Double[] newFi = PR.calcfi(T, press, xMol);
        String exp = "";


        Double[][] K = new Double[MAX_ITER][N_c];
        Double[][] x = new Double[MAX_ITER][N_c];
        Double[][] y = new Double[MAX_ITER][N_c];
        Double[][] FiL = new Double[MAX_ITER][N_c];
        Double[][] FiV = new Double[MAX_ITER][N_c];
        FlashCalculation flash = new FlashCalculation();

        Double[] teta = new Double[MAX_ITER];


        Double[] ki0 = PR.calcKi(T, press);
        teta[0] = flash.solveVapFrac(ki0, xMol);
        for (int i = 0; i < N_c; i++) {
            K[0][i]=ki0[i];

            x[0][i] = xMol[i]/(1.0-(1.0-ki0[i])*teta[0]);
            y[0][i] = ki0[i]*x[0][i];

          //  System.out.println(" Initial K values: =========== ");
           // solver.printArr(K[0]);
        System.out.println(" Y values(---------): =========== "+y[i][0]);

       }


        FiL[0] = PR.calcfi(T, press, x[0]);
        FiV[0] = PR.calcfi(T, press, y[0]);


        for (int j = 0; j < MAX_ITER-1; j++) {
            solver.printArr(K[j]);
            teta[j] = flash.solveVapFrac(K[j], xMol);

            System.out.println("===========================================(00001) k values: ");
            solver.printArr(K[0]);

            Double[] diff = solver.substract(K[j],1.0);
            Double [] product = solver.prodScal(diff,teta[j]);
            Double [] diff2 = solver.substract(product,1.0);

            x[j] = solver.divArr(xMol, diff2);//check this equation

            solver.printArr(x[0]);
            y[j] = solver.prodArr(K[j], x[j]);
            FiL[j] = PR.calcfi(T, press, x[j]);
            System.out.println("===========================================X values: ");
            solver.printArr(x[j]);
            System.out.println("===========================================Y values: ");
            solver.printArr(y[j]);

            FiV[j] = PR.calcfi(T, press, y[j]);

            System.out.println("===========================================K values: ");

            K[j+1] = solver.prodArr(solver.divArr(FiL[j], FiV[j]), K[j]);
            solver.printArr(K[j]);
        }



        System.out.println("===================      "+ teta[0]);




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




    }


    @Test
    public void testNumericalMethod() throws ParseException {
        String eqn = "x^3-1.1774058941448093*x^2+2.1085211039159772*x+0.3537581591303739";

        Solver solver = new Solver();

        int maxIter = 10000;
        Double x0=1.0;
        Double error = 0.0001;

      //  Double sol = solver.nRaphson(eqn,x0,error,maxIter);
       List<Double> sols = solver.findAllSol(eqn,0.0,10.0,0.1);
        sols.forEach(System.out :: println);
    }




    @Test
    public void testRachfordRice () throws ParseException {
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

        Double T = 298.0;
        Double press = 6.0;
        int MAX_ITER = 5;

        PengRobinson PR = new PengRobinson();
        PR.setParams(omega_i, T_cr, P_cr, xMol);
        Double[] ki0 = PR.calcKi(T, press);

        List<Double> sols = new ArrayList<>();

        Double error = 1e-4;
        int max = 10000;
        String eqn = "x^3+0.98*x^2+0.3*x+0.003";

    }



            @Test
    public void testArrayOperations () throws ParseException {

        Solver solver = new Solver();

        Double [] arr1 =new Double[] {1.0,2.0,3.0};
        Double [] arr2 =new Double[] {1.2,2.1,3.2};
        Double [][] mat1= new Double[][] {  {1.0,2.0,3.0},
                                           {4.0,5.0,6.0},
                                            {7.0,8.0,9.0}};


              solver.printArr(solver.divArr(mat1[0],arr1));
        Double [] result = solver.prodArr(arr1,arr2);
        solver.printArr(result);
            }

}
