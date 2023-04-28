package com.api.solver.numerical;

import org.nfunk.jep.ParseException;

import java.util.ArrayList;
import java.util.List;

public class FlashCalculation {


  Solver solver = new Solver();

    public FlashCalculation() throws ParseException {
    }

    public void setInitialValues () {

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
            Nomij.add("(x*" + String.valueOf((ki[i] - 1)) + ")");
        }
        String nom=joinElements(Nomij);



        String equation = "("+eqn.substring(0,eqn.length()-1)+")/(1+"+nom+")";
        System.out.println("("+eqn.substring(0,eqn.length()-1)+")");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------" +
                "--------------------------------------------------------------");
        System.out.println("(1+"+nom+")");
        return equation;


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

    public Double solveVapFrac(Double [] ki, Double [] xMol) throws ParseException {
        String eqn = flashEquation(ki,xMol);
        Double x0=1.0;
        Double error=1e-4;
        int maxIter = 1000;
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

}
