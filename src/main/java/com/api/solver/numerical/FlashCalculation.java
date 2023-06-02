package com.api.solver.numerical;

import com.api.solver.propertyPackage.PropertyPackage;
import org.nfunk.jep.ParseException;

import java.util.ArrayList;
import java.util.List;

public class FlashCalculation {


    Solver solver = new Solver();
    PengRobinson PR = new PengRobinson();

    public FlashCalculation() throws ParseException {
    }

    public void setParams () throws ParseException {
        int N_c =3;
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


        PR.setParams(omega_i, T_cr, P_cr, xMol);

    }


    public String flashEquation (Double [] ki, Double [] xMol){
        int N_c = ki.length;
        List<String> params=generatePairs(ki,xMol);
        String eqn="";
        List<String>Nomij = new ArrayList<>();
        for (int i=0; i< N_c; i++){
            eqn=eqn+params.get(i)+"+";
        }
        for (int i=0; i< N_c; i++) {
            Nomij.add("(1+x*" + String.valueOf((ki[i] - 1)) + ")");
        }
        String nom=joinElements(Nomij);



        String equation = "("+eqn.substring(0,eqn.length()-1)+")/("+nom+")";
        System.out.println("("+eqn.substring(0,eqn.length()-1)+")");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------");
            System.out.println(equation);
        return equation;


    }

    public Double rachRice (Double [] Ki, Double [] xMol){

        int N_c = xMol.length;
        double value = 0.0;

        for (int i=0; i < N_c; i++){
        value = value + (xMol[i])*(Ki[i]-1);
        }

        return value;
    }

    public List<String> generatePairs(Double [] ki, Double [] xMol){

        List<String> pairs=new ArrayList<>();

        List<String> newPairs= new ArrayList<>();
        String nomVal = "";


        int N_c = ki.length;
System.out.println("----------++++++++++++++++++++++ K length"+N_c);
        List<String> ci=new ArrayList<>();

        Double [] jIndex = new Double[N_c-1];
        List<String>Bij = new ArrayList<>();
        List<String>Denij = new ArrayList<>();

        for (int i=0; i< N_c; i++){
            ci.add(String.valueOf(xMol[i]*(ki[i]-1)));


            System.out.println("----------+++++++ " + ci.get(i));
        }

        for (int i=0; i< N_c; i++){

            Bij.add("(1+x*"+String.valueOf(ki[i]-1)+")");

        }




        for (int i=0; i< N_c; i++){

            newPairs.add("("+ci.get(i)+")"+joinElements(removeAtIndex(Bij,i)));

            System.out.println("----------- " + newPairs.get(i));
        }

        return newPairs;

    }

    public Double solveVapFrac(Double [] ki, Double [] xMol,Double x0) throws ParseException {
        String eqn = flashEquation(ki,xMol);

        Double error=1e-4;
        int maxIter = 1000;
        System.out.print("Vap frac: ---------------------------------------------------- "+ solver.nRaphson(eqn,x0,error,maxIter));
        return solver.nRaphson(eqn,x0,error,maxIter);
    }


    public List<String> removeAtIndex(List<String> arr, int index){
        List<String> newArr = new ArrayList<>();
        for (int i=0; i< arr.size(); i++){
            if (i!=index){
                newArr.add(arr.get(i));
            }
        }

        return newArr;
    }


    public String joinElements (List<String> arr){
        String s="";

        for (int i=0; i< arr.size(); i++){
            s=s+arr.get(i);
        }
        return s;

    }


    public Double bubblePoint(Double T, Double [] xMol) throws ParseException {
        setParams();
         int MAX_ITER = 25;
         int N_c = xMol.length;

        Double[][] K = new Double[MAX_ITER][N_c];
        Double[][] x = new Double[MAX_ITER][N_c];
        Double[][] y = new Double[MAX_ITER][N_c];
        Double[][] FiL = new Double[MAX_ITER][N_c];
        Double[][] FiV = new Double[MAX_ITER][N_c];
        Double[][] FiLder1 = new Double[MAX_ITER][N_c];
        Double[][] FiVder1 = new Double[MAX_ITER][N_c];
        Double[][] FiLder2 = new Double[MAX_ITER][N_c];
        Double[][] FiVder2 = new Double[MAX_ITER][N_c];

        Double[] ZiLder1 = new Double[MAX_ITER];
        Double[] ZiVder1 = new Double[MAX_ITER];
        Double[] ZiLder2 = new Double[MAX_ITER];
        Double[] ZiVder2 = new Double[MAX_ITER];


        Double [] ZiL = new Double[MAX_ITER];
        Double [] ZiV = new Double[MAX_ITER];
        Double [] P = new Double [MAX_ITER];
        Double [] teta = new Double [MAX_ITER];
        Double [] [] PiSat =  new Double[MAX_ITER][N_c];


        Double [] A = new Double[]{15.726,15.7972,15.833};
        Double [] B = new Double[]{1872.46,3313.0,2477.07};
        Double [] C = new Double[]{-25.16,-67.71,-39.94};


        Double h=0.00001; //step for centered differenece approximation of derivative

      //  Double [] A = new Double[]{15.726,1872.46,-25.16};
      //  Double [] B = new Double[]{15.7972,3313.0,-67.71};
      //  Double [] C = new Double[]{15.833,2477.07,-39.94};

        Double Pinit = 0.0;
        Double [] yi = new Double[N_c];
        Double [] Pi0= PR.calcPi(T);
        Double[] sum = new Double[MAX_ITER];
        Double [] Pb = new Double[MAX_ITER];

        for (int i=0; i < N_c; i++){

            Pinit = Pinit +xMol[i]* Pi0[i];

        }

        System.out.println("===================== " + Pinit);
        Double[] ki0 = PR.calcKi(T, Pinit);
        Pb[0]=Pinit;

        for (int i=0; i < N_c; i++){

            y[0][i] = ki0[i] * xMol[i];
            K[0][i] = ki0[i];


        }

       for (int j = 0; j < MAX_ITER-1; j++) {

        //!compareFval(FiL[j],FiV[j],0.01)



                System.out.println("---" + Pinit);


            ZiL[j] = PR.calcZc(T, Pb[j], xMol).get(0);
           System.out.println("---------------------------------");
          solver.printArr(K[0]);

        y[j] = solver.prodArr(K[j], xMol);

                       ZiV[j] = PR.calcZc(T, Pb[j], y[j]).get(0);

                       ZiLder1[j] = PR.calcZc(T, Pb[j]+h, xMol).get(0);
                       ZiLder2[j] = PR.calcZc(T, Pb[j]-h, xMol).get(0);


                       if (PR.calcZc(T, Pb[j]+h, y[j]).size() > 1) {
                           ZiVder1[j] = PR.calcZc(T, Pb[j] + h, y[j]).get(1);

                       }

                       else{
                           ZiVder1[j] = PR.calcZc(T, Pb[j] + h, y[j]).get(0);
                       }
                       if (PR.calcZc(T, Pb[j]-h, y[j]).size() > 1) {
                           ZiVder2[j] = PR.calcZc(T, Pb[j] - h, y[j]).get(1);

                       }

                       else{
                           ZiVder2[j] = PR.calcZc(T, Pb[j] - h, y[j]).get(0);
                       }


                FiL[j] = PR.calcfi(T, Pb[j], xMol, ZiL[j]);
                FiV[j] = PR.calcfi(T, Pb[j], y[j], ZiV[j]);


               FiLder1[j] = PR.calcfi(T, Pb[j]+h, xMol,  ZiLder1[j]);
               FiVder1[j] = PR.calcfi(T, Pb[j]+h, y[j], ZiVder1[j]);

               FiLder2[j] = PR.calcfi(T, Pb[j]-h, xMol,  ZiLder2[j]);
               FiVder2[j] = PR.calcfi(T, Pb[j]-h, y[j], ZiVder2[j]);

                K[j+1] = solver.divArr(FiL[j], FiV[j]);
              Pb[j+1] = Pb[j] - (2*h*( 1-  vecSum(K[j],xMol) ))*Math.pow((vecSum(xMol,FiVder2[j])-vecSum(xMol,FiLder1[j])),-1);



           }


        System.out.println("Molar frac in vap. phase");
        solver.printMat(y);

        System.out.println("Fugacities in liq phase");
        solver.printMat(FiL);

        System.out.println("Fugacities in liq phase");
        solver.printMat(FiV);
        System.out.println("Zl in liq phase");
        solver.printArr(ZiL);
        System.out.println("ZV in vap phase");
        solver.printArr(ZiV);
        System.out.println("Pressure : ");
        solver.printArr(Pb);
        System.out.println("Estimated pressure******************************************************: " + Pinit);
        solver.printArr(teta);
        return P[0];

    }
   public Double checkSum (Double[] y){
        int len = y.length;
        Double sum = 0.0;
        for (int i=0; i < len; i++){
            sum = sum+y[i];
        }
        return sum;
   }

  public Double vecSum (Double[] arr1, Double []arr2){
      int len = arr1.length;
      Double sum = 0.0;
      for (int i=0; i < len; i++){
          sum = sum+arr1[i] * arr2[i];
      }
      return sum;
  }

   public boolean compareFval(Double[] Fl, Double[] Fv, Double err){
      int len = Fl.length;

       for (int i=0; i < len; i++){
         if (Math.abs(Fl[i] - Fv[i]) < err){
             return true;
         }


       }
       return false;
   }
    public Double [][] flashTP(Double T, Double press, Double [] xMol) throws ParseException {
        setParams();
        int MAX_ITER = 15;
        int N_c = xMol.length;

        Double[][] K = new Double[MAX_ITER][N_c];
        Double[][] x = new Double[MAX_ITER][N_c];
        Double[][] y = new Double[MAX_ITER][N_c];
        Double[][] FiL = new Double[MAX_ITER][N_c];
        Double[][] FiV = new Double[MAX_ITER][N_c];
        Double [] ZiL = new Double[MAX_ITER];
        Double [] ZiV = new Double[MAX_ITER];
        Double [] P = new Double [MAX_ITER];

        Double[] teta = new Double[MAX_ITER];


        Double[] ki0 = PR.calcKi(T, press);
        teta[0] = solveVapFrac(ki0, xMol,0.5);
        for (int i = 0; i < N_c; i++) {
            K[0][i]=ki0[i];

            x[0][i] = xMol[i]/(1.0-(1.0-ki0[i])*teta[0]);
            y[0][i] =x[0][i]* ki0[i];


            //  System.out.println(" Initial K values: =========== ");
            // solver.printArr(K[0]);


        }

        PR.calcZc(T,press,xMol);

        System.out.print("--------0000000000000000000000000000");
        solver.printArr(K[0]);



        for (int j = 0; j < MAX_ITER-1; j++) {
            solver.printArr(K[j]);
            teta[j] = solveVapFrac(K[j], xMol,0.0);

            System.out.println("===========================================(00001) k values: ");
            solver.printArr(K[0]);

            Double[] diff = solver.substract(K[j],1.0);
            Double [] product = solver.prodScal(diff,teta[j]);
            Double [] diff2 = solver.substract(product,1.0);

            x[j] = solver.divArr(xMol, diff2);//check this equation
            ZiL[j]=PR.calcZc(T,press,x[j]).get(0);
            solver.printArr(x[0]);

            PR.calcZc(T,press,x[j]);

            y[j] = solver.prodArr(x[j],K[j]);

            if ((PR.calcZc(T,press,y[j])).size() > 1) {
                ZiV[j] = PR.calcZc(T, press, y[j]).get(1);
            }

            else {
                ZiV[j] = PR.calcZc(T, press, y[j]).get(0);
            }
            PR.calcZc(T,press,y[j]);
            FiL[j] = PR.calcfi(T, press, x[j],ZiL[j]);

            solver.printArr(x[j]);

            solver.printArr(y[j]);

            FiV[j] = PR.calcfi(T, press, y[j],ZiV[j]);


            //  K[j+1] = solver.prodArr(solver.divArr(FiL[j], FiV[j]), K[j]);
            K[j+1]=solver.divArr(FiL[j],FiV[j]);

        }

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

        System.out.println("===================      "+ teta[0]);
        return y;
    }


}


