package com.api.solver.numerical;

import com.api.solver.numerical.MathFunction;
import com.api.solver.numerical.Solver;
import com.api.solver.propertyPackage.PropertyPackage;
import org.nfunk.jep.ParseException;

import java.util.List;

public class PengRobinson {

    PropertyPackage props =new PropertyPackage();

    Double volume;

    List<Double> sols;
    Double Zl;
    Double Zv;

    public PengRobinson()throws ParseException {

    }

    Double R =8.31; //m3 * Pa / (mol * K)
    public void setParams (Double [] omega_i, Double [] T_cr,  Double [] P_cr, Double[] xMol) throws ParseException {
        props = new PropertyPackage(omega_i,T_cr,P_cr,xMol);

    }


    public Double calcZc (Double T, Double press,Double [] xMol) throws ParseException {
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
        String eqn = props.PengRobinsonEq(press,T,xMol);
        Double sol = solver.newtonRaphson(eqn,x0,error,maxIter);
         sols = solver.findAllSol(eqn,-1.0,5.0,0.1);

        if(sols.size() == 1){
            System.out.println("********************************************************");
            System.out.println("********************************************************");
            System.out.println("*************** System is in one phase *****************");
            System.out.println("********************************************************");
            System.out.println("********************************************************");
        }

        else{
            System.out.println("********************************************************");
            System.out.println("********************************************************");
            System.out.println("*************** System is in" + sols.size()+  " phases *");
            System.out.println("********************************************************");
            System.out.println("********************************************************"); //mininum solution is for liquid
           //maximum solution is for gases
        }


        return sol;
    }

    public Double [] calcKi (Double T, Double press) {

        return props.calcKi(T,press);
    }


    public Double calcMolVol (Double T, Double press,Double [] xMol) throws ParseException {

        props.setxMol(xMol);
        Double Z = calcZc(T,press,xMol);
        Double volume = (8.31*T*Z)/press;
        System.out.print("--------------Molar volume (cm3/mol):----------- " + volume);
        return volume;

    }


    public Double[] calcfi (Double T, Double press,Double [] xMol) throws ParseException {
       props.setxMol(xMol);
       Double Zalfa = calcZc (T, press, xMol);

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

        for (int i=0; i <xMol.length; i++ ){
            ffi[i] =(bi[i]/bm)*(Zalfa-1)-Math.log(Zalfa-Bm)-(Am/(2*Math.sqrt(2))*Bm)*( (2.0/aa)*sum - bi[i]/bm)*Math.log((Zalfa+2.414*Bm)/(Zalfa-0.414*Bm));
           System.out.println("Fugacity coeff in phase alfa: " +Math.exp(ffi[i]));
           fug[i] = Math.exp(ffi[i]);
       }

//       for (int i=0; i <xMol.length; i++ ){
//
//           fug[i] = (bi[i]/bm)*(Zalfa-1)-Math.log(Zalfa-Bm)-(Am/(2.0*rad2*Bm))*((2.0/aa)*sum-bi[i]/bm)*Math.log((Zalfa+2.414*Bm)/(Zalfa-0.414*Bm));
//           System.out.println("fugacity in phase alfa-----: " +Math.exp(fug[i]));
//    }
////
//    for (int i=0; i <xMol.length; i++ ){
//         exponent[i] = (Math.log(Vm/(Vm-bm)) + bi[i]/(Vm-bm) + (2*sum/(R*T*bm))*( Math.log(Vm/(Vm+bm)))+ (bi[i]*aa/(R*T*bm*bm))*(Math.log((Vm+bm)/Vm)-bm/(Vm+bm))-Math.log(Zalfa/xMol[i]));
//
//        fi[i] = Math.exp(exponent[i]);
//      //   System.out.println("Exponent (2): " + exponent[i]);
//          System.out.println("fugacity in phase alfa(2): " + fi[i]);
//     }
//
//        for (int i=0; i <xMol.length; i++ ){
//           exponent[i] = Zalfa-1+Math.log(22400.0/(Vm-bm))+
//                    ((bi[i]*aa -2*Math.sqrt(aa)*Math.sqrt(aii[i])*bm)/(2*rad2*R*T*bm*bm))*Math.log((Vm+bm+rad2*bm)/(Vm+bm-rad2*bm))+
//           (bi[i]-bm)/(Vm-bm)+ ((bi[i]-bm)*aa*Vm)/(R*T*(2*bm*bm-Math.pow(Vm+bm,2)));
//
//
//            fug[i] = Math.exp(exponent[i]);
//
//
//            System.out.println("fugacity in phase alfa(3): " + fug[i]);
//        }

        return fug;
    }

}
