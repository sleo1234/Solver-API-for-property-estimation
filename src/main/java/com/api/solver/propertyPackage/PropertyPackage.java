package com.api.solver.propertyPackage;

import com.api.solver.numerical.MathFunction;
import org.nfunk.jep.ParseException;

public class PropertyPackage {


    // C3H8, n-C4H10, n-C5H12 mixture
    //

    int N_c ; // no. of components

    MathFunction fun = new MathFunction();



    Double [] omega_i = new Double[N_c]; //0.153, 0.199,0.255
    Double [] T_cr = new Double[N_c]; //K 369.8,425.2,469.7

    Double[] P_cr = new Double[N_c]; //

    Double[] x_m = new Double[N_c];

    Double R = 8.31; // J/ mol*K

    Double[] molVol_i = new Double[N_c];




    Double [] xMol = new Double[N_c];

    public PropertyPackage() throws ParseException {

    }



    public Double[] calcKi (Double temp, Double press) {
        Double[] K_i = new Double[N_c];
        Double [] acc = omega_i;
        for (int i=0; i < N_c; i++) {

            K_i[i] = (P_cr[i]/press)*Math.exp(5.37*(1+acc[i])*(1- T_cr[i]/temp));
        System.out.println("K " + i+" "+K_i[i]);
        }
   return K_i;
    }

    public PropertyPackage(Double[] omega_i, Double[] t_cr, Double[] p_cr,Double[] xMol) throws ParseException {
        this.omega_i = omega_i;
        T_cr = t_cr;
        P_cr = p_cr;
        N_c = xMol.length;
        this.xMol = xMol;
    }
//P_cr = {42.5,38.0,33.7};


    public Double[] a_M (Double T){

        Double [] a = new Double[N_c];
        Double [] alfa = alfa_m (T);
        for (int i=0; i < N_c; i++) {
            a[i] = 0.45724 * alfa[i] * R * R * (T_cr[i] * T_cr[i]) / P_cr[i];

        }

        return a;

    }

    public Double Am (Double T, Double [] xMol){


        Double[] am = a_M(T);
        Double sum =0.0;


        for (int i=0; i < N_c; i++) {
           for (int j=0; j < N_c; j++) {
               sum = sum +xMol[j]* Math.sqrt(am[i] *am[j]);
           }
        }

        return sum;

    }


    public Double alfam (Double T, Double [] xMol){



        Double [] alfa = alfa_m (T);
        Double sum=0.0;


        for (int i=0; i < N_c; i++) {
            for (int j=0; j < N_c; j++) {
                if (xMol[i] == 0.0) {
                    alfa[i]=0.0;
                    alfa[j]=0.0;
                    sum = sum + xMol[j] * alfa[i] * alfa[j];
                }
            }
        }

        return sum;

    }


    public Double AmRad (Double T, Double [] xMol){


        Double[] am = a_M(T);
        Double sum =0.0;


        for (int i=0; i < N_c; i++) {

                sum = sum +xMol[i]* (am[i]);

        }

        return sum;

    }


    public Double[] b_M (){

        Double [] b = new Double[N_c];

        for (int i=0; i < N_c; i++) {
            b[i] = 0.077796 * R * (T_cr[i] ) / P_cr[i];
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

    public Double [] alfa_m (Double T) {
       Double [] alfa = new Double[N_c];
       Double [] lambda = lambda_vec();


        for (int i=0; i < N_c; i++) {



         alfa[i] = Math.pow(1+ lambda[i] * (1 - Math.sqrt(T/T_cr[i])),2);
         System.out.println(alfa[i]);
        }
        return alfa;

    }


     public Double attractParam (Double T, Double [] x_mol)  {
        Double A =0.0;
         Double[] am = a_M(T);
         Double sum = 0.0;

         for (int i=0; i < N_c; i++) {
             for (int j=0; j < N_c; j++) {
                 sum = sum +x_mol[i] * x_mol[j] * Math.sqrt( am[i] * am[j]);
             }
         }
         A = sum;
         return sum;
     }

     public Double coVolParam ( Double[] x_mol) {
        Double B = 0.0;

        Double[] bm = b_M();

         Double sum = 0.0;
         for (int i=0; i < N_c; i++) {
             sum = sum + bm[i] * x_mol[i];
         }
     B = sum;

         return B;
     }

    public String PengRobinsonEq (Double press, Double temp, Double [] x_mol)  {

        Double A = (attractParam (temp,x_mol) * press) / (R * R * temp * temp);
        Double B = (coVolParam(x_mol) * press) /(R * temp);

        double coeff1 = 1.0-B;
        double coeff2 = (A-2*B-3*B*B);
        double coeff3 =(A*B-B*B-B*B*B);

          String exp = "x^3-" + String.valueOf(coeff1)+ "*x^2+" + String.valueOf(coeff2)+"*x-"+coeff3;
          System.out.println("---- coeff1 " + coeff1 + " ---- coeff2 " + coeff2);
        System.out.println(exp);
       return exp;
    }



    public String solveForP (Double temp, Double press,Double[] x_mol){


       String firstTerm = String.valueOf(press)+ "-" + String.valueOf(R*temp)+"/(x-"+String.valueOf(coVolParam(x_mol))+")";
       //System.out.print("====="+firstTerm);
       String secondTerm = String.valueOf(attractParam (temp,x_mol))+"/(x*(x+"+String.valueOf(coVolParam(x_mol))+")+"
               +String.valueOf(coVolParam(x_mol))+"*(x-"+String.valueOf(coVolParam(x_mol))+"))";

        String eqn = firstTerm+"-"+secondTerm;
        System.out.print("====="+eqn);

        return eqn;
    }

    public String molVol (Double temp, Double press,Double[] x_mol){
         String b = String.valueOf((coVolParam(x_mol)));
         Double bb = (coVolParam(x_mol));
         String alfa = String.valueOf(attractParam (temp,x_mol));
        String firstTerm = String.valueOf(R*temp)+"/(x-"+b+")";
        String secondTerm = alfa+"/(x^2+2*x*"+ b + "-"+String.valueOf(bb*bb)+")" ;
        String eqn = firstTerm+"+"+secondTerm;
        System.out.print("====="+eqn);

        return eqn;
    }

    public int getN_c() {
        return N_c;
    }

    public Double[] getOmega_i() {
        return omega_i;
    }

    public Double[] getT_cr() {
        return T_cr;
    }

    public Double[] getP_cr() {
        return P_cr;
    }

    public Double[] getxMol() {
        return xMol;
    }

    public void setOmega_i(Double[] omega_i) {
        this.omega_i = omega_i;
    }

    public void setT_cr(Double[] t_cr) {
        T_cr = t_cr;
    }

    public void setP_cr(Double[] p_cr) {
        P_cr = p_cr;
    }

    public void setX_m(Double[] x_m) {
        this.x_m = x_m;
    }

    public void setxMol(Double[] xMol) {
        this.xMol = xMol;
    }
}
