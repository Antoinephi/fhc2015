package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ProblemNaive2 extends Problem {

	public ProblemNaive2(ProblemData data) {
		super(data);
	}

	public void resolve() {
		System.out.println("Launch problem naive 2");
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File("data/scoreList"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		for(int k=0; k<data.getnZ(); k++) {
		
			int n = 0;
			
			for(int i=0; i<data.getnX(); i++) {
				for(int j=0; j<data.getnY(); j++) {
					List<Integer> path;
					if((path = findPathTo(i, j, k)) != null) {
						int score = computePathScore(i, j, k, data.getNbTurn()-path.size());
						int score2 = getScorePath(path);
						System.out.println(score2+" + "+score);
						writer.println(i+" "+j+" "+k+" "+score+score2);
						n++;
					}
				}
			}
			
			System.out.println(k+" => "+n);
		}
		
		writer.close();
	}

	
	public int getScorePath(List<Integer> paths) {
		int score = 0, currentZ = -1;
		Coord2 nextCoord = new Coord2(this.data.getStartBalloon().x,this.data.getStartBalloon().y);
		for (Integer move : paths) {
			currentZ += move.intValue();
			nextCoord = computeCoord(nextCoord.x, nextCoord.y, currentZ);
			if (nextCoord != null)
				score += getScoreBalloon(nextCoord.x, nextCoord.y);
		}
		return score;
	}
	
	
/*
	public boolean hasCycle(int x, int y, int z) {
		int currentX = x, currentY = y, currentZ = z;
		
		int nTurn = 0;
		
		int score = 0;
		
		while(true) {
			
			if(currentY < 0 || currentY >= data.getnY()) {
				return false;
			}
			
			//System.out.println(currentX+" "+currentY+" "+currentZ);
			
			currentX += data.getWindVector(currentX, currentY, currentZ).x;
			
			if(currentX < 0) {
				currentX = data.getnX()+currentX;
			}
			
			else if(currentX >= data.getnX()) {
				currentX = currentX-data.getnX();
			}
			
			//System.out.println(currentX+" "+currentY+" "+currentZ);
			
			currentY += data.getWindVector(currentX, currentY, currentZ).y;
			
			score += this.data.getScoreBalloon(currentX,currentY)!=-1?this.data.getScoreBalloon(currentX,currentY):0;
					 
			if(currentX == x && currentY == y && currentZ == z) {
				if (score > 0)
					System.out.println(score+" in "+nTurn+ " turns");
				return true;
			}
			
			nTurn++;
			
			if(nTurn >= this.data.getNbTurn()) {
				if (score > 0)
					System.out.println(score+" in "+nTurn+ " turns");
				return true;
			}
		}
	}
	*/
	

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
		for(int i = (x - this.data.getCoverageRadius()) % this.data.getnX(); 
				i <= (x + this.data.getCoverageRadius()) % this.data.getnX(); i++) {
			for(int j = y - (this.data.getCoverageRadius() - columnDist(x, i));
					j <= y + (this.data.getCoverageRadius() - columnDist(x, i)); j++) {
				if(this.data.isTarget(i,j) && this.data.isCovered(this.data.getTargetIndex(i, j)) == 0)
					score++;
			}
		}
		return score;
	}
	
	
	public int computePathScore(int begX, int begY, int begZ, int maxTurn) {
		int currentX = begX, currentY = begY, currentZ = begZ;
		
		int nTurn = 0;
		int score = 0;
		
		while(true) {
			
			Coord2 c = computeCoord(currentX, currentY, currentZ);
			
			if(c == null)
				return score;
			else {
				currentX = c.x;
				currentY = c.y;
			}
			
			int i = getScoreBalloon(currentX, currentY);
			
			if(i == -1) {
				System.err.println("WTF");
				System.exit(0);
			}
			
			score += i;
			
			nTurn++;
			
			if(nTurn >= maxTurn)
				return score;
		}
	}

}
