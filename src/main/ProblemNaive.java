package main;

public class ProblemNaive extends Problem {
	
	public ProblemNaive(ProblemData data) {
		super(data);
	}

	public int columnDist(int x, int u) {
		return Math.min(Math.abs(x - u), Math.abs((x + this.data.getnX()) - u));
	}
	
	/**
	 * Return coverage score for the case (x, y)
	 */
	public int getScoreBalloon(int x, int y) {
		int score = 0;
		
		// If the balloon is not in the map
		if(y >= this.data.getnY() || y < 0)
			return -1;
		
		// (x - u)^2 + (columndist(y, v))^2 < V^2 => score + 1
		for(int i = x - this.data.getCoverageRadius(); i <= x + this.data.getCoverageRadius(); i++) {
			for(int j = y - (this.data.getCoverageRadius() - columnDist(x, i));
					j <= y + (this.data.getCoverageRadius() - columnDist(x, i)); j++) {
				if(this.data.isTarget(i,j))
					score++;
			}
		}
		return score;
	}
	
	public int nextTurnResult(Coord3 balloonCoord, int altitudeDeviation) {
		
		if(balloonCoord.z + altitudeDeviation < 0 || 
				balloonCoord.z + altitudeDeviation >= 8)
			return -1;
		
		Coord2 windVector = this.data.getWindVector(balloonCoord.x, balloonCoord.y, balloonCoord.z + altitudeDeviation);
		return getScoreBalloon(balloonCoord.x + windVector.x, 
				balloonCoord.y + windVector.y);
	}
	
	public void resolve() {
		for(int t = 0; t < this.data.getNbTurn(); t++) {
			System.out.println("Tour " + t);
			for(int b = 0; b < this.data.getNbBalloon(); b++) {
				if(t == b) {
					this.move[t][b] = 1;
					this.data.setBalloonsCoord(b, 
							this.data.newBalloonCoord(this.data.getBalloonsCoord(b), 1));
				} else if(t > b){
					int bestAltitude = 0;
					int bestAltitudeValue = 0;
					for(int i = -1; i <= 1; i++) {
						if(nextTurnResult(this.data.getBalloonsCoord(b), i) >= bestAltitudeValue) {
							bestAltitude = i;
							bestAltitudeValue = nextTurnResult(this.data.getBalloonsCoord(b), i);
						}
					}
					this.move[t][b] = bestAltitude;
					this.data.setBalloonsCoord(b, 
							this.data.newBalloonCoord(this.data.getBalloonsCoord(b), bestAltitude));
				}
			}
		}
	}

}
