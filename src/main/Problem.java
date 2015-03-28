package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public abstract class Problem {
	
	protected ProblemData data;
	
	/* solution build by problem */
	protected int [][] move;
	
	public Problem(ProblemData data) {
		this.data = data;
		this.move = new int [this.data.getNbTurn()][this.data.getNbBalloon()];
	}
	
	public abstract void resolve();
	
	public void output(String pathname) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new File(pathname));
		
		/*
		 * OUTPUT HERE
		 */
		for (int t = 0 ; t < this.data.getNbTurn() ; t++) {
			for (int i = 0 ; i  < this.data.getNbBalloon() ; i++) {
				writer.print(this.move[t][i]+" ");
			}
			writer.print("\n");
		}
		
		writer.close();
		
	}

}
