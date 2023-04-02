package com.api.solver.propertyPackage;

import com.api.solver.numerical.MathFunction;

public class PropertyPackage {


    // C3H8, n-C4H10, n-C5H12 mixture
    //

    int N_c = 3; // no. of components

    MathFunction fun = new MathFunction();



    Double [] omega_i = new Double[N_c]; //0.153, 0.199,0.255
    Double [] T_cr = new Double[N_c]; //K 369.8,425.2,469.7

    Double[] P_cr = new Double[N_c]; //

    Double[] x_m = new Double[N_c];

    Double R = 8.31; // J/ mol*K

    Double T = 373.0; // K;

    Double [] xMol = new Double[N_c];

    //P_cr = {42.5,38.0,33.7};

    public void setParams () {


        omega_i[0] = 0.153;
        omega_i[1] = 0.199;
        omega_i[2] = 0.255;

        T_cr[0] = 369.8;
        T_cr[1] = 425.2;
        T_cr[2] = 469.7;

        P_cr[0] = 42.5;
        P_cr[1] = 38.0;
        P_cr[2] = 33.7;

        xMol[0] = 0.1;
        xMol[1] = 0.3;
        xMol[2]  = 0.6;


    }


    public Double[] a_M (){

        Double [] a = new Double[N_c];
        Double [] alfa = alfa_m ();
        for (int i=0; i < N_c; i++) {
            a[i] = 0.45724 * alfa[i] * R * R * (T_cr[i] * T_cr[i]) / P_cr[i];

        }

        return a;

    }




    public Double[] b_M (){

        Double [] b = new Double[N_c];

        for (int i=0; i < N_c; i++) {
            b[i] = 0.07796 * R * (T_cr[i] ) / P_cr[i];
            System.out.println(b[i]);
        }



        return b;

    }




    public Double [][] binaryCoeffsMat(){
        Double[][] k_mn = new Double[N_c][N_c];


        return k_mn;

    }


    public Double[] lambda_vec (){
        Double[] lamb_vec = new Double[N_c];

        Double [] acc = omega_i;

        for (int i=0; i < N_c; i++) {
            if (acc[i] < 0.5215){

              lamb_vec[i] = 0.37464 + 1.5423 * acc[i] - 0.26992 * acc[i] * acc[i];
            }

            else {
                lamb_vec[i] = 0.3796 + 1.485 * acc[i] - 0.1644 * acc[i] * acc[i] + 0.01666 * acc[i] * acc[i] * acc[i];
            }

        }
        return lamb_vec;
    }

    public Double [] alfa_m () {
       Double [] alfa = new Double[N_c];
       Double [] lambda = lambda_vec();

        for (int i=0; i < N_c; i++) {


         alfa[i] = Math.pow((1 + lambda[i] * (1 - Math.sqrt(T / T_cr[i]))),2);
         System.out.println(alfa[i]);
        }
        return alfa;

    }


     public Double attractParam ()  {
        Double A =0.0;
         Double[] am = a_M();
         Double sum = 0.0;

         for (int i=0; i < N_c; i++) {
             for (int j=0; j < N_c; j++) {
                 sum = sum + xMol[i] * xMol[j] * Math.sqrt( am[i] * am[j]);
             }
         }
         A = sum;
         return sum;
     }

     public Double coVolParam ( ) {
        Double B = 0.0;

        Double[] bm = b_M();

         Double sum = 0.0;
         for (int i=0; i < N_c; i++) {
             sum = sum + bm[i] * xMol[i];
         }
     B = sum;

         return B;
     }

    public String PengRobinsonEq (Double press, Double temp)  {

        Double A = (attractParam () * press) / (R * R * temp * temp);
        Double B = (coVolParam() * press) /(R * T);

          String exp = "x^3-" + String.valueOf(1.0-B)+ "*x^2+" + String.valueOf(A-2*B-3*B*B - (A * B - B * B - B * B * B));
        System.out.println(exp);
       return exp;
    }

}
