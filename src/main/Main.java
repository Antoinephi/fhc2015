package main;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		ProblemData data = Input.parse("data/in");
		
		Problem problem = new ProblemSimulate(data);
		
		problem.resolve();
		
		problem.output("data/out");
	}

}
