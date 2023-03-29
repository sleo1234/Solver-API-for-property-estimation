package com.api.solver.numerical;

import org.nfunk.jep.ParseException;

public class Solver {

    public Solver() {

    }

    MathFunction fun = new MathFunction();

    public double newtonRaphson(String eqn, double x0, double error, int maxIter) throws ParseException {
        int iter = 0; //number of iterations
        double sol = 0.0; // the returned solution

        double h = fun.evalFun(eqn, x0) / fun.evalDer(eqn, x0);
        while (Math.abs(h) > error && iter < maxIter) {


            h = fun.evalFun(eqn, x0) / fun.evalDer(eqn, x0);
            x0 = x0 - h;
            iter++;
            System.out.println("Solution: " + sol + " after " + iter + " iterations");
            sol = x0;
        }

        System.out.println("Solution: " + sol + " after " + iter + " iterations");


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
