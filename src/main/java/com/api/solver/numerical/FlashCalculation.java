package com.api.solver.numerical;

import com.api.solver.flashapi.ComponentResponseBody;
import com.api.solver.flashapi.FlashTPBody;
import com.api.solver.propertyPackage.PropertyPackage;
import com.api.solver.util.ApiUtil;
import org.nfunk.jep.ParseException;

import java.util.ArrayList;
import java.util.List;

public class FlashCalculation {


    Solver solver = new Solver();

    ApiUtil apiUtil = new ApiUtil();
    PengRobinson PR = new PengRobinson();
    Double Pcm=0.0;
    Double Tcm=0.0;

    Double [] vapComp;
    Double [] liqComp;

    Double vapFrac;

    public FlashCalculation() throws ParseException {
    }

    public void setParams (Double [] omega, Double [] Tc, Double [] Pc, Double [] xMol) throws ParseException {
        int N_c =Pc.length;

        PropertyPackage props = new PropertyPackage();
        Solver solver = new Solver();





        PR.setParams(omega, Tc, Pc, xMol);

        for (int i=0; i < N_c; i++){
            Pcm = Pcm+xMol[i] * Pc[i];
            Tcm = Tcm+xMol[i] * Tc[i];
        }

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
            Nomij.add("(1+x*" + (ki[i] - 1) + ")");
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

            Bij.add("(1+x*"+ (ki[i] - 1) +")");

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

         int MAX_ITER = 25;
         int N_c = xMol.length;

         Double [] M = new Double []{44.0,58.0,72.0};
         Double Mmed =0.0;
         Double bubblePress=0.0;
         Double[] vapFrac = new Double[N_c];

        Double[][] K = new Double[MAX_ITER][N_c];
        Double[][] x = new Double[MAX_ITER][N_c];
        Double[][] y = new Double[MAX_ITER][N_c];
        Double[][] FiL = new Double[MAX_ITER][N_c];
        Double[][] FiV = new Double[MAX_ITER][N_c];
        Double[][] FiLder1 = new Double[MAX_ITER][N_c];
        Double[][] FiVder1 = new Double[MAX_ITER][N_c];
        Double[][] FiLder2 = new Double[MAX_ITER][N_c];
        Double[][] FiVder2 = new Double[MAX_ITER][N_c];
        Double [][] Fder = new Double[MAX_ITER][N_c];
        Double [][] F = new Double[MAX_ITER][N_c];
        Double[] ZiLder1 = new Double[MAX_ITER];
        Double[] ZiVder1 = new Double[MAX_ITER];
        Double[] ZiLder2 = new Double[MAX_ITER];
        Double[] ZiVder2 = new Double[MAX_ITER];
        Double[] VmV = new Double[MAX_ITER];
        Double[] VmL = new Double[MAX_ITER];


        Double [] ZiL = new Double[MAX_ITER];
        Double [] ZiV = new Double[MAX_ITER];
        Double [] P = new Double [MAX_ITER];
        Double [] teta = new Double [MAX_ITER];
        Double [] [] PiSat =  new Double[MAX_ITER][N_c];


        Double [] A = new Double[]{15.726,15.7972,15.833};
        Double [] B = new Double[]{1872.46,3313.0,2477.07};
        Double [] C = new Double[]{-25.16,-67.71,-39.94};


        Double h=1.0E-12; //step for centered differenece approximation of derivative

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
           Mmed = Mmed + xMol[i]*M[i];

        }



        Pb[0]=1.036*Pcm*(T/Tcm);
        Double[] ki0 = PR.calcKi(T, Pb[0]);
//1.036*Pcm*(T/Tcm);
        System.out.println("===================== " + Pb[0]);
        for (int i=0; i < N_c; i++){

            y[0][i] =xMol[i]*ki0[i];
            K[0][i] = ki0[i];


        }

       for (int j = 0; j < MAX_ITER-1; j++) {

        //!compareFval(FiL[j],FiV[j],0.01)



                System.out.println("---" + Pinit);


            ZiL[j] = PR.calcZc(T, Pb[j], xMol).get(0);
           System.out.println("------------------------------00000---");




                   y[j+1] = (solver.prodArr(K[j], xMol));
                  if (PR.calcZc(T, Pb[j], y[j]).size() >1) {
                      ZiV[j] = PR.calcZc(T, Pb[j], y[j]).get(1);
                  }



                  else{
                      ZiV[j] = PR.calcZc(T, Pb[j], y[j]).get(0);
                  }
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


                     VmV[j] = 8.314*T*ZiV[j]/Pb[j];
                     VmL[j] = 8.314*T*ZiL[j]/Pb[j];
                 System.out.print("-----------------------------Liquid density (g/cm3): " + Mmed/VmL[j]);
                FiL[j] = PR.calcfi(T, Pb[j], xMol, ZiL[j]);
                FiV[j] = PR.calcfi(T, Pb[j], y[j], ZiV[j]);


               FiLder1[j] = PR.calcfi(T, Pb[j]+h, xMol,  ZiLder1[j]);
               FiVder1[j] = PR.calcfi(T, Pb[j]+h, y[j], ZiVder1[j]);

               FiLder2[j] = PR.calcfi(T, Pb[j]-h, xMol,  ZiLder2[j]);
               FiVder2[j] = PR.calcfi(T, Pb[j]-h, y[j], ZiVder2[j]);







          Fder[j] = solver.prodArr(xMol,solver.prodScal(solver.substract(solver.divArr(FiLder1[j],FiVder1[j]),
                  solver.divArr(FiLder2[j],FiVder2[j])),1.0/(2.0*h)));//Fder

       F[j] = solver.add(solver.prodArr(solver.divArr(FiL[j],FiV[j]),xMol),-1.0);//F

        //sumOfVec()


             Pb[j+1] = Pb[j] -sumOfVec(F[j])/sumOfVec(Fder[j]);



              K[j+1] = solver.divArr(FiL[j], FiV[j]);




           System.out.println("_----- error: " + (Pb[j+1] - Pb[j]));
           if (Math.abs(Pb[j+1] - Pb[j]) < 1e-8 && sumOfVec(y[j+1]) >0.998) {
            bubblePress=Pb[j+1];
            vapFrac=y[j+1];
               break;
           }

           }

        System.out.println("Molar frac in vap. phase");
        solver.printMat(y);

        System.out.println("Fugacities in liq. phase");
        solver.printMat(FiL);

        System.out.println("Fugacities in vap. phase");
        solver.printMat(FiV);
        System.out.println("Fder");
        solver.printMat(Fder);
        System.out.println("F_");
        solver.printMat(F);
        System.out.println("FiLder1");
        solver.printMat(FiLder1);
        System.out.println("FiLder2");
        solver.printMat(FiLder2);
        System.out.println("FiVder1");
        solver.printMat(FiVder1);
        System.out.println("FiVder2");
        solver.printMat(FiVder2);
        System.out.println("Zl in liq phase");
        solver.printArr(ZiL);
        System.out.println("ZV in vap phase");
        solver.printArr(ZiV);
        System.out.println("ZiVder1");
        solver.printArr(ZiVder1);
        System.out.println("ZiVder2");
        solver.printArr(ZiVder2);
        System.out.println("ZiLder1");
        solver.printArr(ZiLder1);
        System.out.println("ZiJder2");
        solver.printArr(ZiLder2);
        System.out.println("K   ");
        solver.printMat(K);
        System.out.println("Pressure : ");
        solver.printArr(Pb);


        System.out.println("Bubble point pressure : " + bubblePress+" MPa with vap frac: ");
        solver.printArr(vapFrac);


        return bubblePress;

    }





    public Double bubblePoint2(Double T, Double [] xMol) throws ParseException {

        int MAX_ITER = 25;
        int N_c = xMol.length;

        Double [] M = new Double []{44.0,58.0,72.0};
        Double Mmed =0.0;
        Double bubblePress=0.0;
        Double[] vapFrac = new Double[N_c];

        Double[][] K = new Double[MAX_ITER][N_c];
        Double[][] x = new Double[MAX_ITER][N_c];
        Double[][] y = new Double[MAX_ITER][N_c];
        Double[][] FiL = new Double[MAX_ITER][N_c];
        Double[][] FiV = new Double[MAX_ITER][N_c];
        Double[][] FiLder1 = new Double[MAX_ITER][N_c];
        Double[][] FiVder1 = new Double[MAX_ITER][N_c];
        Double[][] FiLder2 = new Double[MAX_ITER][N_c];
        Double[][] FiVder2 = new Double[MAX_ITER][N_c];
        Double[][] FiLder = new Double[MAX_ITER][N_c];
        Double[][] FiVder= new Double[MAX_ITER][N_c];
        Double[] ZiLder1 = new Double[MAX_ITER];
        Double[] ZiVder1 = new Double[MAX_ITER];
        Double[] ZiLder2 = new Double[MAX_ITER];
        Double[] ZiVder2 = new Double[MAX_ITER];
        Double[] VmV = new Double[MAX_ITER];
        Double[] VmL = new Double[MAX_ITER];

        Double [][] Fder = new Double[MAX_ITER][N_c];
        Double [][] F = new Double[MAX_ITER][N_c];

        Double [] ZiL = new Double[MAX_ITER];
        Double [] ZiV = new Double[MAX_ITER];
        Double [] P = new Double [MAX_ITER];
        Double [] teta = new Double [MAX_ITER];
        Double [] [] PiSat =  new Double[MAX_ITER][N_c];


        Double [] A = new Double[]{15.726,15.7972,15.833};
        Double [] B = new Double[]{1872.46,3313.0,2477.07};
        Double [] C = new Double[]{-25.16,-67.71,-39.94};


        Double h=1.0E-8; //step for centered differenece approximation of derivative

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
            Mmed = Mmed + xMol[i]*M[i];

        }



        Pb[0]=1.036*Pcm*(T/Tcm);
        Double[] ki0 = PR.calcKi(T, Pb[0]);
//1.036*Pcm*(T/Tcm);
        System.out.println("===================== " + Pb[0]);
        for (int i=0; i < N_c; i++){

            y[0][i] =xMol[i]*ki0[i];
            K[0][i] = ki0[i];


        }
         ZiL[0] =PR.calcZc(T,Pb[0],xMol).get(0);
        if (PR.calcZc(T,Pb[0],y[0]).size() > 1) {
            ZiV[0] = PR.calcZc(T, Pb[0], y[0]).get(1);
        }

        else{
            ZiV[0] = PR.calcZc(T, Pb[0], y[0]).get(0);
        }

        FiL[0]=PR.calcfi(T,Pb[0],xMol,ZiL[0]);
        FiV[0]=PR.calcfi(T,Pb[0],y[0],ZiV[0]);

        for (int j = 0; j < MAX_ITER-1; j++) {

            //!compareFval(FiL[j],FiV[j],0.01)



            System.out.println("---" + Pinit);

            y[j+1] = (solver.prodArr(K[j], xMol));
            ZiL[j] = PR.calcZc(T, Pb[j], xMol).get(0);
            System.out.println("------------------------------00000---");





            if (PR.calcZc(T, Pb[j], y[j]).size() >1) {
                ZiV[j] = PR.calcZc(T, Pb[j], y[j]).get(1);
            }



            else{
                ZiV[j] = PR.calcZc(T, Pb[j], y[j]).get(0);
            }
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


            VmV[j] = 8.314*T*ZiV[j]/Pb[j];
            VmL[j] = 8.314*T*ZiL[j]/Pb[j];
            System.out.print("-----------------------------Liquid density (g/cm3): " + Mmed/VmL[j]);
            FiL[j] = PR.calcfi(T, Pb[j], xMol, ZiL[j]);
            FiV[j] = PR.calcfi(T, Pb[j], y[j], ZiV[j]);


            FiLder1[j] = PR.calcfi(T, Pb[j]+h, xMol,  ZiLder1[j]);
            FiVder1[j] = PR.calcfi(T, Pb[j]+h, y[j], ZiVder1[j]);

            FiLder2[j] = PR.calcfi(T, Pb[j]-h, xMol,  ZiLder2[j]);
            FiVder2[j] = PR.calcfi(T, Pb[j]-h, y[j], ZiVder2[j]);


           FiLder[j] =solver.substract(FiLder1[j],FiLder2[j]);
           FiVder[j] = solver.substract(FiVder1[j],FiVder2[j]);



           Fder[j] =solver.divArr(solver.prodArr(xMol,
                           solver.substract(solver.prodScal(solver.prodArr(FiLder[j],FiV[j]),1.0/(2.0*h)),solver.prodScal(solver.prodArr(FiVder[j],FiL[j]), 1.0/(2.0*h)))),
                   solver.prodArr(FiV[j],FiV[j]));

          F[j] = solver.prodArr(xMol,
                   K[j]);


           // Double [][] funDer = calcFder( FiL,  FiLder, FiV, FiVder, xMol);

            System.out.print("================= FunDer: ");
           // solver.printMat(funDer);

            //Double [][] fun = calcF(K,xMol);
            System.out.print("================= Fun: ");
           // solver.printMat(fun);

           Pb[j+1] = Pb[j] - (sumOfVec(F[j])-1.0)/sumOfVec(Fder[j]);

            K[j+1] = solver.divArr(FiL[j], FiV[j]);




            System.out.println("_----- error: " + (Pb[j+1] - Pb[j]));
            if (Math.abs(Pb[j+1] - Pb[j]) < 1e-8 && sumOfVec(y[j+1]) >0.998) {
                bubblePress=Pb[j+1];
                vapFrac=y[j+1];
                break;
            }

        }

        System.out.println("Molar frac in vap. phase");
        solver.printMat(y);

        System.out.println("Fugacities in liq. phase");
        solver.printMat(FiL);

        System.out.println("Fugacities in vap. phase");
        solver.printMat(FiV);
        System.out.println("FiVder1");
        solver.printMat(FiVder1);
        System.out.println("F---------");
        solver.printMat(F);
        System.out.println("Fder---------");
        solver.printMat(Fder);
        System.out.println("FiLder1");
        solver.printMat(FiLder1);
        System.out.println("FiLder2");
        solver.printMat(FiLder2);
        System.out.println("Zl in liq phase");
        solver.printArr(ZiL);
        System.out.println("ZV in vap phase");
        solver.printArr(ZiV);
        System.out.println("ZiVder1");
        solver.printArr(ZiVder1);
        System.out.println("ZiVder2");
        solver.printArr(ZiVder2);
        System.out.println("ZiLder1");
        solver.printArr(ZiLder1);
      //  System.out.println("F ----------");
      //  solver.printArr(F);
        System.out.println("K   ");
        solver.printMat(K);
        System.out.println("Pressure : ");
        solver.printArr(Pb);


        System.out.println("Bubble point pressure : " + bubblePress+" MPa with vap frac: ");
        solver.printArr(vapFrac);


        return bubblePress;

    }



    public static Double [][] calcFder (Double [][] FiL, Double [][] FiLDer, Double [][] FiV, Double [][] FiVDer, Double [] xMol){
        int N_c = xMol.length;
        int MAX_ITER = 25;
        Double [][] xMat=new Double[MAX_ITER][N_c];

        for (int j=0; j < MAX_ITER-1; j++){

            xMat[j]=xMol;

        }

        Double [][] mat = new Double [MAX_ITER][N_c];
        Double h = 1e-4;
        //for (int j=0; j < MAX_ITER; j++){
        // for (int i=0; i < N_c; i++){

        for (int i=0; i < MAX_ITER-1; i++){

            for (int j=0; j < N_c; j++){

                mat[i][j] = xMat[i][j]*(FiLDer[i][j]*FiV[i][j]/(2*h)-FiVDer[i][j]*FiL[i][j]/(2*h))/(FiV[i][j]*FiV[i][j]);

            }

        }

        return mat;
    }



    public static Double [][] calcF(Double[][] Ki, Double [] xMol){

        int N_c = xMol.length;
        int MAX_ITER = 25;
        Double [][] xMat=new Double[MAX_ITER][N_c];

        for (int j=0; j < MAX_ITER-1; j++){

            xMat[j]=xMol;

        }

        Double [] F = new Double [MAX_ITER];
        Double [][] mat = new Double [MAX_ITER][N_c];
        Double h = 1e-4;

        for (int i=0; i < MAX_ITER-1; i++){

            for (int j=0; j < N_c; j++){

                mat[i][j] = xMat[i][j]*Ki[i][j]-1.0;
            }

        }

        return mat;
    }

    public Double bubblePointBisect(Double T, Double [] xMol) throws ParseException {


        int MAX_ITER = 25;
        int N_c = xMol.length;

         Double sol =0.0;

      Double Pinit=1.036*Pcm*(T/Tcm);
        Double bubbleP=0.0;

        Double[][] K = new Double[MAX_ITER][N_c];
        Double[][] Ka = new Double[MAX_ITER][N_c];
        Double[][] Kb = new Double[MAX_ITER][N_c];
        Double[][] Kab = new Double[MAX_ITER][N_c];
        Double[][] ya = new Double[MAX_ITER][N_c];
        Double[][] yb = new Double[MAX_ITER][N_c];
        Double[][] yab = new Double[MAX_ITER][N_c];
        Double[][] FiL = new Double[MAX_ITER][N_c];
        Double[][] FiV = new Double[MAX_ITER][N_c];
        Double[][] FiLa = new Double[MAX_ITER][N_c];
        Double[][] FiLb = new Double[MAX_ITER][N_c];
        Double[][] FiVb = new Double[MAX_ITER][N_c];
        Double[][] FiVa = new Double[MAX_ITER][N_c];
        Double [] ZiL = new Double[MAX_ITER];
        Double [] ZiV = new Double[MAX_ITER];
        Double [] ZiLa = new Double[MAX_ITER];
        Double [] ZiLb = new Double[MAX_ITER];
        Double [] ZiVa = new Double[MAX_ITER];
        Double [] ZiVb = new Double[MAX_ITER];

        Double [] yi = new Double[N_c];
        Double [] Pi0= PR.calcPi(T);
        Double[] sum = new Double[MAX_ITER];
        Double [] Pb = new Double[MAX_ITER];



        Double a =Pinit-2.0;
        Double b=Pinit+1.5;
        sol =(a+b)/2;

        Double[] ki0a = PR.calcKi(T, a);
        Double[] ki0b = PR.calcKi(T, b);
        Double[] ki0ab = PR.calcKi(T, sol);
        //1.036*Pcm*(T/Tcm);
        System.out.println("===================== " + Pinit);
        for (int i=0; i < N_c; i++){

            ya[0][i] =xMol[i]*ki0a[i];
            yb[0][i] =xMol[i]*ki0b[i];
            yab[0][i] =xMol[i]*ki0ab[i];
            Ka[0][i] = ki0a[i];
            Kb[0][i] = ki0b[i];
            Kab[0][i] = ki0ab[i];


        }


              yab[0]=normalize(solver.prodArr(xMol,ki0ab));

        for (int j = 0; j < MAX_ITER-1; j++) {


            ya[j+1] =normalize (solver.prodArr(Ka[j], xMol));
            yb[j+1] = normalize(solver.prodArr(Kb[j], xMol));
            yab[j+1] = normalize(solver.prodArr(Kab[j], xMol));

        ZiLa[j] = PR.calcZc(T, a, xMol).get(0);
        ZiLb[j] = PR.calcZc(T, b, xMol).get(0);
        ZiL[j] = PR.calcZc(T, sol, xMol).get(0);



            if ( PR.calcZc(T, sol, yab[j]).size() > 1){

                ZiV[j] = PR.calcZc(T, sol, yab[j]).get(1);
            } else {
                ZiV[j] = PR.calcZc(T, sol, yab[j]).get(0);
            }
           if (PR.calcZc(T, a, ya[j]).size() > 1) {
               ZiVa[j] = PR.calcZc(T, a, ya[j]).get(1);

           }

           else{
               ZiVa[j] = PR.calcZc(T, a, ya[j]).get(0);
           }


            if (PR.calcZc(T, b, yb[j]).size() > 1) {
                ZiVb[j] = PR.calcZc(T, b, yb[j]).get(1);

            }

            else{
                ZiVb[j] = PR.calcZc(T, b, yb[j]).get(0);
            }

            FiLa[j] = PR.calcfi(T, a, xMol, ZiLa[j]);
            FiLb[j] = PR.calcfi(T, b, xMol, ZiLb[j]);

            FiVa[j] = PR.calcfi(T, a, ya[j], ZiVa[j]);
            FiVb[j] = PR.calcfi(T, b, yb[j], ZiVb[j]);

            Ka[j+1] = solver.divArr(FiLa[j], FiVa[j]);
            Kb[j+1] = solver.divArr(FiLb[j], FiVb[j]);




            ZiL[j]=PR.calcZc(T,sol,xMol).get(0);

            if ( PR.calcZc(T,sol,yab[j]).size()>1){
                ZiV[j]=PR.calcZc(T,sol,yab[j]).get(1);
            }

            else{
                ZiV[j]=PR.calcZc(T,sol,yab[j]).get(0);
            }


            FiL[j] = PR.calcfi(T,sol,xMol,ZiL[j]);
            FiV[j] = PR.calcfi(T,sol,yab[j],ZiV[j]);
            Kab[j+1] = solver.divArr(FiL[j], FiV[j]);


                if ((Math.abs(vecSum(solver.divArr(FiL[j], FiV[j]), xMol)-1.0)  <1e-4 && (b-a)/2 < 1e-4)  ) {
                    bubbleP = sol;
                    Pb[j]=sol;
                    System.out.println("***************************************************val+ "+ (vecSum(solver.divArr(FiL[j], FiV[j]), xMol)-1.0));
                    break;
                }
                if (((vecSum(solver.divArr(FiL[j], FiV[j]), xMol))-1.0) * (vecSum(solver.divArr(FiLa[j], FiVa[j]), xMol)-1.0) > 0 ) {
                    a = sol;
                    Pb[j]=sol;
                    System.out.println("***************************************************a "+ a);
                } else {
                    b = sol;
                    Pb[j]=sol;

                    System.out.println("=b " + b);
                }

           sol=(a+b)/2;

        }

        System.out.println("Molar frac in vap. phase");
        solver.printMat(yab);

          System.out.println("****************** "+bubbleP);
        System.out.println("Pb");
        solver.printArr(Pb);
        return sol;
    }
     public Double bubblePointMVNR(Double T, Double [] xMol) throws ParseException{


         int MAX_ITER = 25;
         int N_c = xMol.length;

         Double [] M = new Double []{44.0,58.0,72.0};

         Double bubbleP=0.0;

         Double[][] K = new Double[MAX_ITER][N_c];

         Double[][] y = new Double[MAX_ITER][N_c];
         Double[][] FiL = new Double[MAX_ITER][N_c];
         Double[][] FiV = new Double[MAX_ITER][N_c];
         Double [] ZiL = new Double[MAX_ITER];
         Double [] ZiV = new Double[MAX_ITER];

         Double Pinit = 4.0;
         Double [] yi = new Double[N_c];
         Double [] Pi0= PR.calcPi(T);
         Double[] sum = new Double[MAX_ITER];
         Double [] Pb = new Double[MAX_ITER];


         Pb[0] = Pinit;

             FiV[0][0]=1.0;
             FiV[0][1]=1.0;
             FiV[0][2]=1.0;

         for (int j = 0; j < MAX_ITER-1; j++) {

             //!compareFval(FiL[j],FiV[j],0.01)


             System.out.println("---" + Pinit);


             ZiL[j] = PR.calcZc(T, Pb[j], xMol).get(0);
             FiL[j] = PR.calcfi(T, Pb[j], xMol, ZiL[j]);
             y[0]=solver.prodArr(solver.divArr(FiL[0],FiV[0]),xMol);


             if (PR.calcZc(T, Pb[j], y[j]).size() >1) {
                 ZiV[j] = PR.calcZc(T, Pb[j], y[j]).get(1);
             }



             else{
                 ZiV[j] = PR.calcZc(T, Pb[j], y[j]).get(0);
             }

             FiV[j] = PR.calcfi(T, Pb[j], y[j], ZiV[j]);

             y[j+1]=normalize(solver.prodArr(solver.divArr(FiL[j],FiV[j]),xMol));
             if (sumOfVec(y[j]) < 0.99){
                 Pb[j+1]=Pb[j]+0.5;
             }
             else{
                 Pb[j+1]=Pb[j]-0.1;
             }

         }


         System.out.println("Molar frac in vap. phase");
         solver.printMat(y);
         System.out.println("Pressure : ");
         solver.printArr(Pb);

             return bubbleP;
     }
   public Double dewPoint(Double T, Double [] xMol) throws ParseException {

        int MAX_ITER = 25;
        int N_c = xMol.length;

        Double [] M = new Double []{44.0,58.0,72.0};
        Double Mmed =0.0;
        Double dewPress=0.0;
        Double[] vapFrac = new Double[N_c];

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
        Double[] VmV = new Double[MAX_ITER];
        Double[] VmL = new Double[MAX_ITER];


        Double [] ZiL = new Double[MAX_ITER];
        Double [] ZiV = new Double[MAX_ITER];
        Double [] P = new Double [MAX_ITER];
        Double [] teta = new Double [MAX_ITER];
        Double [] [] PiSat =  new Double[MAX_ITER][N_c];


        Double [] A = new Double[]{15.726,15.7972,15.833};
        Double [] B = new Double[]{1872.46,3313.0,2477.07};
        Double [] C = new Double[]{-25.16,-67.71,-39.94};


        Double h=1E-6; //step for centered differenece approximation of derivative

        //  Double [] A = new Double[]{15.726,1872.46,-25.16};
        //  Double [] B = new Double[]{15.7972,3313.0,-67.71};
        //  Double [] C = new Double[]{15.833,2477.07,-39.94};

        Double Pinit = 0.0;
        Double [] xi = new Double[N_c];
        Double [] Pi0= PR.calcPi(T);
        Double[] sum = new Double[MAX_ITER];
        Double [] Pd = new Double[MAX_ITER];



        for (int i=0; i < N_c; i++){

            Pinit = Pinit +xMol[i]* Pi0[i];
            Mmed = Mmed + xMol[i]*M[i];

        }



        Pd[0]=3.0;
       Double[] ki0 = PR.calcKi(T, Pd[0]);

        //System.out.println("===================== " + Pb[0]);
        for (int i=0; i < N_c; i++){

            x[0][i] = xMol[i]/ki0[i];
            K[0][i] = ki0[i];


        }

        for (int j = 0; j < MAX_ITER-1; j++) {

            //!compareFval(FiL[j],FiV[j],0.01)



            System.out.println("---" + Pinit);


            ZiL[j] = PR.calcZc(T, Pd[j], x[j]).get(0);
            System.out.println("------------------------------00000---");




            x[j+1] = solver.divArr(xMol,K[j]);
            if (PR.calcZc(T, Pd[j], xMol).size() >1) {
                ZiV[j] = PR.calcZc(T, Pd[j], xMol).get(1);
            }



            else{
                ZiV[j] = PR.calcZc(T, Pd[j], xMol).get(0);
            }
            ZiLder1[j] = PR.calcZc(T, Pd[j]+h, x[j]).get(0);
            ZiLder2[j] = PR.calcZc(T, Pd[j]-h, x[j]).get(0);


            if (PR.calcZc(T, Pd[j]+h, xMol).size() > 1) {
                ZiVder1[j] = PR.calcZc(T, Pd[j] + h, xMol).get(1);

            }

            else{
                ZiVder1[j] = PR.calcZc(T, Pd[j] + h, xMol).get(0);
            }
            if (PR.calcZc(T, Pd[j]-h, xMol).size() > 1) {
                ZiVder2[j] = PR.calcZc(T, Pd[j] - h, xMol).get(1);

            }

            else{
                ZiVder2[j] = PR.calcZc(T, Pd[j] - h, xMol).get(0);
            }


          //  VmV[j] = 8.314*T*ZiV[j]/Pb[j];
           // VmL[j] = 8.314*T*ZiL[j]/Pb[j];
          //  System.out.print("-----------------------------Liquid density (g/cm3): " + Mmed/VmL[j]);
            FiL[j] = PR.calcfi(T, Pd[j], x[j], ZiL[j]);
            FiV[j] = PR.calcfi(T, Pd[j], xMol, ZiV[j]);


            FiLder1[j] = PR.calcfi(T, Pd[j]+h, x[j],  ZiLder1[j]);
            FiVder1[j] = PR.calcfi(T, Pd[j]+h, xMol, ZiVder1[j]);

            FiLder2[j] = PR.calcfi(T, Pd[j]-h, x[j],  ZiLder2[j]);
            FiVder2[j] = PR.calcfi(T, Pd[j]-h, xMol, ZiVder2[j]);



            Double  Fder = (vecSum(xMol,solver.divArr(FiVder1[j],FiLder1[j]))-vecSum(xMol,solver.divArr(FiVder2[j],FiLder2[j])))/(2.0*h);

            Pd[j+1] = Pd[j] - (vecSumDiv(xMol,solver.divArr(FiL[j], FiV[j]))-1.0)*Math.pow(Fder,-1.0);




            K[j+1] = solver.divArr(FiL[j], FiV[j]);
            System.out.println("_----- error: " + (Pd[j+1] - Pd[j]));
            if (Math.abs(Pd[j+1] - Pd[j]) < 1e-8 && sumOfVec(x[j+1]) >0.998) {
                dewPress=Pd[j+1];
                vapFrac=x[j+1];
                break;
            }

        }

        System.out.println("Molar frac in Liq. phase");
        solver.printMat(x);

        System.out.println("Fugacities in liq. phase");
        solver.printMat(FiL);

        System.out.println("Fugacities in vap. phase");
        solver.printMat(FiV);
        System.out.println("FiVder1");
        solver.printMat(FiVder1);
        System.out.println("FiVder2");
        solver.printMat(FiVder2);
        System.out.println("FiLder1");
        solver.printMat(FiLder1);
        System.out.println("FiLder2");
        solver.printMat(FiLder2);
        System.out.println("Zl in liq phase");
        solver.printArr(ZiL);
        System.out.println("ZV in vap phase");
        solver.printArr(ZiV);
        System.out.println("ZiVder1");
        solver.printArr(ZiVder1);
        System.out.println("ZiVder2");
        solver.printArr(ZiVder2);
        System.out.println("ZiLder1");
        solver.printArr(ZiLder1);
        System.out.println("ZiJder2");
        solver.printArr(ZiLder2);
        System.out.println("K   ");
        solver.printMat(K);
        System.out.println("Pressure : ");
        solver.printArr(Pd);


        System.out.println("Bubble point pressure : " + dewPress+" MPa with vap frac: ");
        solver.printArr(vapFrac);


        return dewPress;

    }

  public Double vecSum (Double[] arr1, Double []arr2){
      int len = arr1.length;
      Double sum = 0.0;
      for (int i=0; i < len; i++){
          sum = sum+arr1[i] * arr2[i];
      }
      return sum;
  }

    public Double vecSumDiv (Double[] arr1, Double []arr2){
        int len = arr1.length;
        Double sum = 0.0;
        for (int i=0; i < len; i++){
            sum = sum+arr1[i] / arr2[i];
        }
        return sum;
    }

    public Double sumOfVec (Double[] arr){
        int len = arr.length;
        Double sum = 0.0;
        for (int i=0; i < len; i++){
            sum = sum+arr[i];
        }
        return sum;
    }


    public Double[] normalize(Double[] vector){
      int len = vector.length;
      Double [] normvec=new Double[len];
      Double sum = 0.0;

      for (int i=0; i< len; i++){
          sum = sum+vector[i];

      }

        for (int i=0; i< len; i++){
          normvec[i] = vector[i]*(1/sum);

        }


        return normvec;
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
    public Double [][] flashTP(Double T, Double press, Double[] xMol, ComponentResponseBody body) throws ParseException {


        Double[] acc = apiUtil.toArray(body.getOmega());
        Double[] Pc = apiUtil.toArray(body.getPc());
        Double[] Tc = apiUtil.toArray(body.getTc());
        setParams(acc,Tc,Pc,xMol);



        System.out.println("----------------------line1171 "+(body.getOmega().get(0)));
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
        System.out.println("************************* Molar frac matrix liquid phase");
        solver.printMat(x);
        System.out.println("************************* Molar frac matrix vapour phase");
        solver.printMat(y);

        solver.printArr(teta);


        System.out.println("************************* Fugacities matrix liquid phase");
        solver.printMat(FiL);

        System.out.println("************************* Fugacities matrix vapour phase");
        solver.printMat(FiV);
        System.out.println("************************* END");

        System.out.println("************************* K matrix ");
        solver.printMat(K);
        System.out.println("************************* END");


        vapComp=y[MAX_ITER-2];
        liqComp=x[MAX_ITER-2];
        vapFrac=teta[0];
        System.out.println("===================   line 1272   ");
        solver.printArr(vapComp);
        return y;
    }


    public Double[] getVapComp(){

        return vapComp;
    }

    public Double[] getLiqComp(){

        return liqComp;
    }

    public Double getVapFrac(){

        return vapFrac;
    }

}


