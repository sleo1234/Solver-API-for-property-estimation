package com.api.solver;

import com.api.solver.numerical.MathFunction;
import com.api.solver.numerical.Solver;
import org.nfunk.jep.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients

public class Application {

	public static void main(String[] args) throws ParseException {
		SpringApplication.run(Application.class, args);

		MathFunction func = new MathFunction();
		Solver solver = new Solver();
		String eqn = "x^2+x^3+1";
        double x0=100;
		double error=0.001;
		int maxIter = 10000;
        //solver.newtonRaphson(eqn,x0,error,maxIter);
		//solver.bisect(eqn,-100,100,error,maxIter);
		//func.derivativeOf("cos(e^x)");
		//func.evalDer("cos(e^x)",0);
		//func.evalFun("cos(x)",2);

	}



}
