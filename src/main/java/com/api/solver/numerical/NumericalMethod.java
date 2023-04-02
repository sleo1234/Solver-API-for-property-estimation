package com.api.solver.numerical;

public class NumericalMethod {
    private double initialSol;
    private int maxIter;


    private int iter;

    public int getIter() {
        return iter;
    }

    public void setIter(int iter) {
        this.iter = iter;
    }

    private double err;

    private String methodType;




    private String variable;

    private String equation;
    public NumericalMethod(){

    }

    public NumericalMethod(double initialSol, int maxIter, double err, String methodType, String variable, String equation) {
        this.initialSol = initialSol;
        this.maxIter = maxIter;
        this.err = err;
        this.methodType = methodType;
        this.variable = variable;
        this.equation = equation;
    }

    public double getInitialSol() {
        return initialSol;
    }

    public void setInitialSol(double initialSol) {
        this.initialSol = initialSol;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(int maxIter) {
        this.maxIter = maxIter;
    }

    public double getErr() {
        return err;
    }

    public void setErr(double err) {
        this.err = err;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }





    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    private boolean isConverged(){


        return iter > maxIter ? false : true;
    }
}
