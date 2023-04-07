package com.api.solver.numerical;

import org.nfunk.jep.ParseException;

import java.util.ArrayList;
import java.util.Vector;

public class Solver {

    public Solver() throws ParseException {

    }

    MathFunction fun = new MathFunction();

    ArrayList<Double> vec ;
    public double newtonRaphson(String eqn, Double x0, Double error, int maxIter) throws ParseException {
        int iter = 0; //number of iterations

        Double sol = 0.0; // the returned solution
        int flag=0;

        Double [] vec = new Double[maxIter];
        vec[0] = x0;

       // double h = fun.evalFun(eqn, x0) / fun.evalDer(eqn, x0);


        for (int i=1; i < maxIter; i++) {


            vec[i] = vec[i-1]-fun.evalFun(eqn,vec[i-1]) / (fun.evalDer(eqn,vec[i-1]));

            if (Math.abs(vec[i] - vec[i-1]) <= error ) {


                sol=vec[i];
                 flag = 1;
                System.out.println("Solution: " + sol + " after " + i + " iterations");

            }


            if (flag>0) break;


        }
        flag = -1;
        if (flag>0) {
            System.out.println("Solution not found.");
        }
        return sol;
    }

    public double bisect(String eqn, double a, double b, double error, int maxIter) throws ParseException {

        double sol = 0;
        int flag=0;
        int iter = 0;
        if (fun.evalFun(eqn, a) * fun.evalFun(eqn, b) > 0) {
            System.out.println("Choose a and b such f(a) * f(b) is negative");
          flag=-1;
        }

        while (iter < maxIter) {
            if (flag < 0){
                System.out.println("Solution was not found");
                break;
            }
           sol = (a + b) / 2;
            if (fun.evalFun(eqn, sol) == 0 || (b - a) / 2 < error) {

                return sol;
            }
            iter++;
            if (fun.evalFun(eqn, sol) * fun.evalFun(eqn, a) > 0) {
             a=sol;
             System.out.println("Solution: " + sol + " after " + iter + " iterations");
            }
            else{
                b=sol;
            }

        }

       return sol;
    }

}
