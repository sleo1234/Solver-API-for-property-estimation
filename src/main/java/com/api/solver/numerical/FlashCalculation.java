package com.api.solver.numerical;

import java.util.ArrayList;
import java.util.List;

public class FlashCalculation {




    public void setInitialValues () {

    }


    public String flashEquation (Double [] ki, Double [] xMol){
        int N_c = ki.length;
        List<String> params=generatePairs(ki,xMol);
        String eqn="";

        for (int i=0; i< N_c; i++){
            eqn=eqn+params.get(i)+"+";
        }

        System.out.println("Equation: "+eqn);

        return eqn;


    }
    public List<String> generatePairs(Double [] ki, Double [] xMol){

        List<String> pairs=new ArrayList<>();

        List<String> newPairs= new ArrayList<>();

        Double sum=0.0;
        int N_c = ki.length;
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
