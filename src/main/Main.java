package main;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		ProblemData data = Input.parse("data/in");
		
		Problem problem = new ProblemSimulatePass2(data, "data/pass1");
		
		problem.resolve();
		
		problem.output("data/pass2");
		/*
		Problem problemOpti = new ProblemSimulateOpti(data, "data/out");
		
		problemOpti.resolve();
		*/
	}

}
