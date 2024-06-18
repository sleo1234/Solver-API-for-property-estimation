package com.api.solver.numerical;

import com.api.solver.flashapi.ComponentResponseBody;
import com.api.solver.util.ApiUtil;
import lombok.Getter;
import lombok.Setter;
import org.nfunk.jep.ParseException;

import java.util.List;

@Getter
@Setter
public class FlashUtil {


    Solver solver = new Solver();

    ApiUtil apiUtil = new ApiUtil();
    PengRobinson PR = new PengRobinson();

    Double[] acc ;
    Double[] Pc ;
    Double[] Tc ;
    Double[] xMol;
    FlashCalculation flashCalculation = new FlashCalculation();

    int iter=0;
    int MAX_ITER;
    int N_c = xMol.length;

    Double[][] K = new Double[MAX_ITER][N_c];
    Double[][] x = new Double[MAX_ITER][N_c];
    Double[][] y = new Double[MAX_ITER][N_c];
    Double[][] FiL = new Double[MAX_ITER][N_c];
    Double[][] FiV = new Double[MAX_ITER][N_c];
    Double [] ZiL = new Double[MAX_ITER];
    Double [] ZiV = new Double[MAX_ITER];
    Double [] P = new Double [MAX_ITER];
    Double[] s1,s2=new Double[N_c];
    Double[] teta = new Double[MAX_ITER];


    public FlashUtil() throws ParseException {
    }

    public FlashUtil(Double[] acc, Double[] pc, Double[] tc, Double[] xMol) throws ParseException {
        this.acc = acc;
        this.Pc = pc;
        this.Tc = tc;
        this.xMol = xMol;
    }

    public void procedure(Double T, Double press, Double[] xMol) throws ParseException {
        flashCalculation.setParams(acc, Pc, Tc, xMol);
        Double[] ki0 = PR.calcKi(T, press);
        Double val = flashCalculation.evalRachfordRice(ki0,xMol,0.0);
        System.out.println("-------line 1199 " + val);
        teta[0] = flashCalculation.solveVapFrac(ki0, xMol,0.5);
        for (int i = 0; i < N_c; i++) {
            K[0][i]=ki0[i];

            x[0][i] = xMol[i]/(1.0-(1.0-ki0[i])*teta[0]);
            y[0][i] =x[0][i]* ki0[i];


            //  System.out.println(" Initial K values: =========== ");
            // solver.printArr(K[0]);


        }

        PR.calcZc(T,press,xMol);

        System.out.print("--------0000000000000000000000000000");
        // solver.printArr(K[0]);

        List<Double> compressibility = PR.calcZc(T, press, xMol);
        int sols = compressibility.size();

        if (sols > 1){
            //  System.out.println("===========================================(Line 1221 " + compressibility.get(0));
            // System.out.println("===========================================(Line 1222 " + compressibility.get(1));
        }

        else{
            // System.out.println("===========================================(Line 1226 " + compressibility.get(0));
        }


        for (int j = 0; j < MAX_ITER-1; j++) {
            //solver.printArr(K[j]);
            teta[j] = flashCalculation.solveVapFrac(K[j], xMol, 0.5);

            System.out.println("===========================================(00001) k values: ");
            // solver.printArr(K[0]);


            Double[] diff = solver.substract(K[j],1.0);
            Double [] product = solver.prodScal(diff,teta[j]);
            Double [] diff2 = solver.substract(product,1.0);

            x[j] = solver.divArr(xMol, diff2);//check this equation
            ZiL[j]=PR.calcZc(T,press,x[j]).get(0);
            // solver.printArr(x[0]);

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

            // solver.printArr(x[j]);

            // solver.printArr(y[j]);

            FiV[j] = PR.calcfi(T, press, y[j],ZiV[j]);


            //  K[j+1] = solver.prodArr(solver.divArr(FiL[j], FiV[j]), K[j]);
            K[j+1]=solver.divArr(FiL[j],FiV[j]);
            iter++;

            s1=y[j];
            s2=y[j+1];
            Double error=flashCalculation.calcError(y[j],K[j+1]);
            //if (Math.abs(sumOfVec(s1)-sumOfVec(s2))<0.001) {
            //  System.out.println("Flash stopped after "+iter+" iterations");
            //break;
            //}

            System.out.println("error is: "+error);
            if (Math.abs(error) < 0.0001){
                System.out.println("Flash stopped after "+iter+" iterations");
                break;
            }
        }
    }



}
