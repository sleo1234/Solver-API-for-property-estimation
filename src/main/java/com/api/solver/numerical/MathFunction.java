package com.api.solver.numerical;

import org.lsmp.djep.djep.DJep;
import org.lsmp.djep.djep.DiffRulesI;
import org.lsmp.djep.djep.diffRules.MacroDiffRules;
import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

public class MathFunction {

    public MathFunction () throws ParseException {

    }
    JEP jep = new JEP();
    DJep dJep = new DJep();


    public String derivativeOf (String function) throws ParseException {
        dJep.addStandardFunctions();
        dJep.addStandardConstants();
        dJep.addComplex();
        dJep.setAllowUndeclared(true);
        dJep.setAllowAssignment(true);
        dJep.addStandardDiffRules();
        dJep.setImplicitMul(true);


        Node node = dJep.parse(function);
        Node dNode = dJep.differentiate(node,"x");
        Node simb = dJep.simplify(dNode);
       // dJep.println(simb);

        String derFunc = dJep.toString(dNode);
        //System.out.println("Derivative of " +function + "is: " + derFunc);
        //System.out.println(dJep.evaluate(dNode));
                return derFunc;
    }
    public Node derFunc(String function) throws ParseException {
        dJep.addStandardFunctions();
        //dJep.addStandardConstants();
        //dJep.addComplex();
        dJep.setAllowUndeclared(true);
        dJep.setAllowAssignment(true);
        dJep.addStandardDiffRules();

        MacroDiffRules rule = new MacroDiffRules(dJep,"ln","1/x");

        dJep.addDiffRule(rule);
        dJep.setImplicitMul(true);
        Node node = dJep.parse(function);
        Node dNode = dJep.differentiate(node,"x");
        Node simb = dJep.simplify(dNode);
        dJep.println(simb);

        String derFunc = dJep.toString(dNode);
       // System.out.println("Derivative of " +function + "is: " + derFunc);
       // System.out.println(dJep.evaluate(dNode));
        return dNode;
    }


    public double evalFun(String expression,double val) throws ParseException {
        jep.addStandardFunctions();
       // jep.addStandardConstants();

        //jep.addComplex();
        jep.setAllowUndeclared(true);
        jep.setAllowAssignment(true);

        jep.setImplicitMul(true);
        jep.addVariable("x",val);

        Node node = jep.parse(expression);
        double value=(double) jep.evaluate(node);

        return value;
    }
    public double evalDer(String expression,double val) throws ParseException {
        dJep.addVariable("x",val);

        Node dNode = derFunc(expression);
        return (double) dJep.evaluate(dNode);
    }

}
