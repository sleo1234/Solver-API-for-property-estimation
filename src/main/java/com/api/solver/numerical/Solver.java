package com.api.solver.numerical;

import org.nfunk.jep.ParseException;

import java.util.*;

public class Solver {

    public Solver() throws ParseException {

    }

    MathFunction fun = new MathFunction();


    public Double [] divArr(Double [] arr1, Double [] arr2){
        Double [] newArr= new Double[arr1.length];

        for (int i=0; i < arr1.length; i++){
            newArr[i] = arr1[i]/arr2[i];
        }

        return newArr;
    }

    public Double [] divScal(Double k, Double [] arr){
        Double [] newArr= new Double[arr.length];

        for (int i=0; i < arr.length; i++){
            newArr[i] = k/arr[i];
        }

        return newArr;
    }
        public Double [] substract(Double [] arr, Double k){

                for (int i=0; i < arr.length; i++){
                    arr[i] =k-arr[i];
                }
                return arr;
        }

        public Double [] prodArr(Double [] arr1, Double [] arr2){

            for (int i=0; i < arr1.length; i++){
                arr1[i] = arr1[i]*arr2[i];
            }
        return arr1;
        }
    public Double [] prodScal(Double [] arr, Double k){

        for (int i=0; i < arr.length; i++){
            arr[i] = arr[i]*k;
        }
        return arr;
    }
    public void printMat(Double [][] mat){

        for (int i = 0; i < mat.length; i++) {

            // Loop through all elements of current row
            for (int j = 0; j < mat[i].length; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }

}
public void printArr(Double [] arr){
    for (int i=0; i < arr.length; i++){
        System.out.println(arr[i]);
    }
}

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
    public double nRaphson(String eqn, Double x0, Double error, int maxIter) throws ParseException {
        int iter = 0; //number of iterations
        Double h=0.00001;
        Double sol = 0.0; // the returned solution
        int flag=0;

        Double [] vec = new Double[maxIter];
        vec[0] = x0;

        // double h = fun.evalFun(eqn, x0) / fun.evalDer(eqn, x0);


        for (int i=1; i < maxIter; i++) {


            vec[i] = vec[i-1]-fun.evalFun(eqn,vec[i-1]) / (((fun.evalFun(eqn,vec[i-1]+h)-(fun.evalFun(eqn,vec[i-1]-h))))*Math.pow(2*h,-1));

            if (Math.abs(vec[i] - vec[i-1]) <= error ) {


                sol=vec[i];
                flag = 1;
              //  System.out.println("Solution: " + sol + " after " + i + " iterations");

            }


            if (flag>0) break;


        }
        flag = -1;
        if (flag>0) {
            System.out.println("Solution not found.");
        }
        return sol;
    }

    public List<Double> findAllSol(String eqn, Double a, Double b, Double step ) throws ParseException {
        Double error =1e-8;
        int maxIter = 10000;
        List<Double> sols = new ArrayList<>();
        Set<Double> uniqueSols = new HashSet<>();
        for (Double k=a; k<b; k=k+step){
            Double k0=nRaphson(eqn, k,  error,  maxIter);
            sols.add(nRaphson(eqn, k,  error,  maxIter));
        }


        List<Double> truncSols = sols.stream().map(c -> trunc(c,7)).toList();
        for (int i=0; i < sols.size(); i++){
            uniqueSols.add(truncSols.get(i));
            //  System.out.println("-------------   "+truncSols.get(i));
        }
        //System.out.println("-------------   "+uniqueSols);
        List<Double> result =new ArrayList<>(uniqueSols);
        return result;
    }

    public Double trunc(Double a, int noOfDecimals){
        a = a* Math.pow(10, noOfDecimals);
        a = Math.floor(a);
        a = a / Math.pow(10, noOfDecimals);
        return a;
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
