package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public abstract class Problem {
	
	protected ProblemData data;
	
	public Problem(ProblemData data) {
		this.data = data;
	}
	
	public abstract void resolve();
	
	public void output(String pathname) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new File(pathname));
		
		/*
		 * OUTPUT HERE
		 */
		for (int t = 0 ; t < this.getNbTurn() ; t++) {
			for (int i = 0 ; i  < this.getNbBalloon() ; i++) {
				writer.print(this.data.getMove(t, i)+" ");
			}
			writer.print("\n");
		}
		
		writer.close();
		
	}

}
