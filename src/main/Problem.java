package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public abstract class Problem {
	
	private ProblemData data;
	
	public Problem(ProblemData data) {
		this.data = data;
	}
	
	public abstract void resolve();
	
	public void output(String pathname) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new File(pathname));
		
		/*
		 * OUTPUT HERE
		 */
		
		writer.close();
		
	}

}
