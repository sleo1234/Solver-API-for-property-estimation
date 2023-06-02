package com.api.solver.numerical;

import com.api.solver.numerical.MathFunction;
import com.api.solver.numerical.Solver;
import com.api.solver.propertyPackage.PropertyPackage;
import org.nfunk.jep.ParseException;

import java.util.Collections;
import java.util.List;

public class PengRobinson {

    PropertyPackage props =new PropertyPackage();
    Solver solver = new Solver();
    Double volume;

    List<Double> sols;
    Double Zl;
    Double Zv;

    Double [] fugL ;
    Double [] fugV ;

    public PengRobinson()throws ParseException {

    }

    Double R =8.3145; //m3 * Pa / (mol * K)
    public void setParams (Double [] omega_i, Double [] T_cr,  Double [] P_cr, Double[] xMol) throws ParseException {
        props = new PropertyPackage(omega_i,T_cr,P_cr,xMol);

    }


    public List< Double> calcZc (Double T, Double press,Double [] xMol) throws ParseException {
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
        double error=0.000001;
        int maxIter = 10000;


        //props.calcKi(5.0,300.0);
       //String eqn = props.PengRobinsonEq(press,T,xMol);
        //Double sol = solver.newtonRaphson(eqn,x0,error,maxIter);
         //sols = solver.findAllSol(eqn,0.0,2.0,0.4);
          sols=props.analyticaLPengRobinsonEq(press, T ,xMol);
          System.out.println("Solution --------------------------------- "+sols.get(0));
        if(sols.size() == 1){
            System.out.println("******************************************************** "+sols.get(0));
            System.out.println("********************************************************");
            System.out.println("********************************************************");
            System.out.println("*************** System is in one phase *****************");
            System.out.println("********************************************************");
            System.out.println("********************************************************");
        }


        else{
            System.out.println("********************************************************");
            System.out.println("********************************************************");
            System.out.println("*************** System is in 2  phases *");
            System.out.println("********************************************************");
            System.out.println("********************************************************"); //mininum solution is for liquid
           //maximum solution is for gases
        }
        Zl= Collections.min(sols);
        Zv= Collections.max(sols);
      System.out.println("[----------------------------------Zl] "+Zl);
      System.out.println("[----------------------------------Zv] "+Zv);
        return sols;
    }

    public Double [] calcKi (Double T, Double press) {

        return props.calcKi(T,press);
    }


    public Double [] calcPi (Double T) {

        return props.calcPi_sat(T);
    }

    public Double [] Antoine(Double [] A, Double [] B, Double [] C,Double T){
        return props.calcPi(A,B,C,T);
    }






    public Double[] calcfi (Double T, Double press,Double [] xMol,Double Zalfa) throws ParseException {
        props.setxMol(xMol);


        Double Vm = ((Zalfa*R*T)/press);// cm3/mol
        System.out.println("----------------Molar volume: " + Vm);
        System.out.println("----------------Compress. factor : " + Zalfa);
        double bm=props.coVolParam(xMol);
        double aa = props.attractParam(T,xMol);
        Double [] bi =  props.b_M();
        Double rad2=Math.sqrt(2);

        Double sum =props.Am(T,xMol);
        Double sum2 = props.AmRad(T,xMol);

        Double [] ai = props.a_M(T);
        Double [] aii = props.alfa_m(T);
        Double [] fi = new Double[xMol.length];
        Double [] ffi = new Double[xMol.length];
        Double [] fug = new Double[xMol.length];
        Double [] fugPure = new Double[xMol.length];
        Double [] alfai = props.a_M(T);
        Double [] exponent =new Double[xMol.length];
        Double Bm = bm*press/(R*T);
        Double Am = aa*press/(R*R*T*T);
        Double [] fi0 = new Double[xMol.length];
        Double [] fPure = new Double[xMol.length];
        Double [] Bi = new Double[xMol.length];

        solver.printMat(props.getAij(T,press));
        Double [] vecSum =props.getVecSum(xMol,props.getAij(T,press));
        System.out.println("00000000000000---------------------------- VEC SUM   ");
        solver.printArr(vecSum);

        Double aSum = props.getSum(props.getAij(T,press),xMol);
        for (int i=0; i <xMol.length; i++ ){
            Bi[i] = press*bi[i]/(R*T);

            ffi[i] =(Bi[i]/Bm)*(Zalfa-1.0)-Math.log(Zalfa-Bm)-
                    (Am/(2*rad2*Bm))*( (2.0/Am)*vecSum[i] - Bi[i]/Bm)*Math.log((Zalfa+(1+rad2)*Bm)/(Zalfa+(1-rad2)*Bm));

            fug[i] = Math.exp(ffi[i]);

            System.out.println("Fugacity coeff in phase alfa: " +fug[i]);
            System.out.println("Bi-----------------------------: " +Bi[i]);
            System.out.println("Bm-----------------------------: " +Bm);
            System.out.println("Am-----------------------------: " +Math.log((Zalfa+2.414*Bm)*Math.pow((Zalfa-0.414*Bm),-1.0)));
        }

        return fug;

    }


    public Double getZl() {
        return Zl;
    }

    public Double getZv() {
        return Zv;
    }

    public Double[] getFugL() {
        return fugL;
    }

    public Double[] getFugV() {
        return fugV;
    }
}
