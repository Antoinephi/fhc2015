package main;

public class ProblemNaive extends Problem {
	
	public ProblemNaive(ProblemData data) {
		super(data);
	}

	public int columnDist(int y, int v) {
		return Math.abs(y - v);
	}
	
	public int getScoreBalloon(int x, int y) {
		int score = 0;
		/*// (x - u)^2 + (columndist(y, v))^2 < V^2 => score + 1
		for(int j = y - this.data.getCoverageRadius(); j <= y + V; j++) {
			for(int i = x - (this.data.getCoverageRadius() - columnDist(y, j);
					i <= x + (this.data.getCoverageRadius() - columnDist(y, j)); i++) {
				if(data.isTarget(i,j))
					score++;
			}
		}*/
		return score;
	}
	
	public int nextTurnResult(int x, int y, int z, int altitudeDeviation) {
		
		return 0;
	}

	public void resolve() {

	}

}
