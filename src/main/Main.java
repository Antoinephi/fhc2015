package main;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		ProblemData data = Input.parse("data/in");
		
		Problem problem = new ProblemNaive2(data);
		
		problem.resolve();
		
		System.out.println("phase 2");
		
		Problem problem2 = new ProblemNaive2(data);
		
		problem2.output("data/out");
	}

}
