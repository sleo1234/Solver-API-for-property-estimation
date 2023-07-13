package com.api.solver.propertyPackage;

import com.api.solver.numerical.MathFunction;
import com.api.solver.numerical.Solver;
import org.nfunk.jep.ParseException;

import java.util.ArrayList;
import java.util.List;

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

        }
   return K_i;
    }


    public Double[] calcPi_sat (Double temp) {
        Double[] P_i = new Double[N_c];
        Double [] acc = omega_i;
        for (int i=0; i < N_c; i++) {

            P_i[i] = Math.exp(Math.log(P_cr[i])+(7.0/3.0)*Math.log(10.0)*(1+omega_i[i])*(1-T_cr[i]/temp));

        }
        return P_i;
    }

    public Double[] calcPi(Double[] A, Double [] B, Double [] C, Double T){
        int N_c = A.length;

        Double[] Pi = new Double[N_c];

        for (int i=0; i < N_c; i++) {
            Pi[i] = Math.exp(A[i]-B[i]/(T+C[i]))/7600.0;
        }
        return Pi;
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


    public Double [][] getAij (Double T, Double p){
        Double k = p/(T*T*R*R);
        Double [][] Aij = new Double[N_c][N_c];
        for (int i=0; i < N_c; i++) {
            for (int j=0; j < N_c; j++) {
                  Aij[i][j] = k *Math.sqrt(a_M(T)[i]*a_M(T)[j]);
            }
        }
       return Aij;
    }
    Solver solver = new Solver();
    public Double[] [] getAi (Double [][] mat, Double [] xMol){
        Double [] [] result = new Double[xMol.length][xMol.length];

        for (int j=0; j<xMol.length; j++) {
            result[j] = solver.prodArr(xMol, mat[j]);
        }

        return result;
    }

    public Double getSum (Double [][] mat, Double []xMol){
        Double sum = 0.0;

        for (int i=0; i < N_c; i++) {
            for (int j = 0; j < N_c; j++) {
             sum = sum + xMol[j]*mat[i][j];

            }
        }
        return sum;
    }

            public Double [] getVecSum(Double [] xMol, Double [][] mat){
           Double[] xSum = new Double[xMol.length];
           Double [][] newMat = new Double[xMol.length][xMol.length];
           Double sum = 0.0;
           for (int i=0; i< xMol.length; i++){
             newMat[i] = solver.prodArr(xMol,mat[i]);
           }
                for (int i=0; i< xMol.length; i++){
                    xSum[i] = sumMat(newMat[i]);
                }

           return xSum;
            }

    private Double sumMat(Double[] arr) {
        Double sum=0.0;

        for (int i=0; i < arr.length; i++){
        sum=sum+arr[i];
        }
        return sum;
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
System.out.println("======================================CoVolParam     "+coVolParam(x_mol));
          String exp = "x^3-" + coeff1 + "*x^2+" + coeff2 +"*x-"+coeff3;
          System.out.println("---- coeff1 " + coeff1 + " ---- coeff2 " + coeff2);
        System.out.println(exp);
       return exp;
    }



    public List<Double> analyticaLPengRobinsonEq (Double press, Double temp, Double [] x_mol)  {
        List<Double> sols = new ArrayList<>();
        

        Double A = (attractParam (temp,x_mol) * press) / (R * R * temp * temp);
        Double B = (coVolParam(x_mol) * press) /(R * temp);

        Double C2 = B-1.0;
        Double C1 = (A-3*B*B-2*B);
        Double C0 =(B*B*B+B*B-A*B);
        Double Q1 = C2*C1/6.0-C0/2.0-(C2*C2*C2)/27.0;
        Double P1 = C2*C2/9.0-C1/3.0;
        Double D = Q1*Q1-P1*P1*P1;
  System.out.println("----- "+D);
  Double teta = 0.0;

        if ( D >= 0.0){
            Double sign1 = (Q1+Math.sqrt(D))/(Math.abs(Q1+Math.sqrt(D)));
            Double sign2 = (Q1-Math.sqrt(D))/(Math.abs(Q1-Math.sqrt(D)));
            Double sol1 =sign1*Math.pow( Math.abs((Q1+Math.sqrt(D))),1.0/3.0)+sign2*Math.pow(Math.abs((Q1-Math.sqrt(D))),1.0/3.0)-C2/3.0;
            sols.add(sol1);
            System.out.println("----- "+ (Q1-Math.sqrt(D)));
        }

        else {


            Double t1 = (Q1*Q1)/(P1*P1*P1);
            Double t2 = (Math.sqrt(1-t1))/((Math.sqrt(t1)*(Q1/Math.abs(Q1))));
            if (Math.atan(t2) <0){
                teta = Math.atan(t2)+Math.PI;
            }
            else {
                teta = Math.atan(t2);
            }
            Double sol1 = 2*Math.sqrt(P1)*Math.cos(teta/3.0)-C2/3.0;
            Double sol2 = 2*Math.sqrt(P1)*Math.cos((teta+2*Math.PI)/3.0)-C2/3.0;
            Double sol3 = 2*Math.sqrt(P1)*Math.cos((teta+4*Math.PI)/3.0)-C2/3.0;
            sols.add(sol1);
            sols.add(sol2);
            sols.add(sol3);
        }
        return sols;
    }



    public String solveForP (Double temp, Double press,Double[] x_mol){


       String firstTerm = press + "-" + R * temp +"/(x-"+ coVolParam(x_mol) +")";
       //System.out.print("====="+firstTerm);
       String secondTerm = attractParam(temp, x_mol) +"/(x*(x+"+ coVolParam(x_mol) +")+"
               + coVolParam(x_mol) +"*(x-"+ coVolParam(x_mol) +"))";

        String eqn = firstTerm+"-"+secondTerm;
        System.out.print("====="+eqn);

        return eqn;
    }

    public String molVol (Double temp, Double press,Double[] x_mol){
         String b = String.valueOf((coVolParam(x_mol)));
         Double bb = (coVolParam(x_mol));
         String alfa = String.valueOf(attractParam (temp,x_mol));
        String firstTerm = R * temp +"/(x-"+b+")";
        String secondTerm = alfa+"/(x^2+2*x*"+ b + "-"+ bb * bb +")" ;
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
