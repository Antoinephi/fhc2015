package main;

import java.util.ArrayList;
import java.util.List;

public class ProblemNaive2 extends Problem {

	public ProblemNaive2(ProblemData data) {
		super(data);
	}

	public void resolve() {
		System.out.println("Launch problem naive 2");
		
		System.out.println(">"+hasCycle(0, 0, 0));
		
		for(int k=0; k<data.getnZ(); k++) {
		
			int n = 0;
			
			for(int i=0; i<data.getnX(); i++) {
				for(int j=0; j<data.getnY(); j++) {
					List<Integer> path;
					if((path = findPathTo(i, j, k)) != null) {
						System.out.println(getScorePath(path) + " : "+ computePathScore(i, j, k, data.getNbTurn()-path.size()));
						n++;
					}
				}
			}
			
			System.out.println(k+" => "+n);
		}
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
	
	
	public List<Integer> findPathTo(int x, int y, int z) {
		
		List<Integer> path = new ArrayList<Integer>();
		
		path.add(new Integer(1));

		Coord2 beg = this.computeCoord(this.data.getStartBalloon().x, this.data.getStartBalloon().y, 0);
		
		int currentX =  beg.x, currentY =  beg.y, currentZ = 0;
		
		int nTurn = 0;
		
		Coord2 target = new Coord2(x, y);
		
		while(true) {
			
			float Zratio = 0.75f;
			
			//Compute with current 
			int dCurr = 0;
			Coord2 cZcurr = computeCoord(currentX, currentY, currentZ);
			if(cZcurr != null) {
				dCurr = getDistance(cZcurr, target);
				if(z == currentZ)
					dCurr *= Zratio;
			}
			else
				dCurr = Integer.MAX_VALUE;
			
			int dUp = 0;
			Coord2 cZup = null;
			if(currentZ+1 >= data.getnZ())
				dUp = Integer.MAX_VALUE;
			else {
				cZup = computeCoord(currentX, currentY, currentZ+1);
				if(cZup != null) {
					dUp = getDistance(cZup, target);
					if(z > currentZ)
						dUp *= Zratio;	
				}
				else
					dUp = Integer.MAX_VALUE;
			}
			
			int dDown = 0;
			Coord2 cZdown = null;
			
			if(currentZ-1 < 0)
				dDown = Integer.MAX_VALUE;
			else {
				cZdown = computeCoord(currentX, currentY, currentZ-1);
				if(cZdown != null) {
					dDown = getDistance(cZdown, target);
					if(z < currentZ)
						dDown *= Zratio;		
				}
				else
					dDown = Integer.MAX_VALUE;
			}
			
			//best z
			if(cZcurr != null && dCurr <= dUp && dCurr <= dDown) {
				currentX = 	cZcurr.x;
				currentY = cZcurr.y;
				path.add(new Integer(0));
			}
			else if(cZup != null && dUp < dDown) {
				currentX = 	cZup.x;
				currentY = cZup.y;
				currentZ += 1;
				path.add(new Integer(1));
			}
			else if(cZdown != null) {
				currentX = 	cZdown.x;
				currentY = cZdown.y;
				currentZ -= 1;
				path.add(new Integer(-1));
			}
			else {
				return null;
			}
			
			nTurn++;
			
			if(nTurn >= this.data.getNbTurn())
				return null;
			
			if(currentX == x && currentY == y && currentZ == z) {
				return path;
			}
			
			
		}
	}

	public Coord2 computeCoord(int currentX, int currentY, int currentZ) {
		
		currentX += data.getWindVector(currentX, currentY, currentZ).x;
		
		if(currentX < 0) {
			currentX = data.getnX()+currentX;
		}
		else if(currentX >= data.getnX()) {
			currentX = currentX-data.getnX();
		}
		
		currentY += data.getWindVector(currentX, currentY, currentZ).y;
		
		if(currentY < 0 || currentY >= data.getnY()) {
			return null;
		}
		else {
			return new Coord2(currentX, currentY);
		}
	}
	
	public int getDistance(Coord2 c1, Coord2 c2) {
		return (c1.x-c2.x)*(c1.x-c2.x)+(c1.y-c2.y)*(c1.y-c2.y);
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
		for(int i = (x - this.data.getCoverageRadius()) % this.data.getnX(); 
				i <= (x + this.data.getCoverageRadius()) % this.data.getnX(); i++) {
			for(int j = y - (this.data.getCoverageRadius() - columnDist(x, i));
					j <= y + (this.data.getCoverageRadius() - columnDist(x, i)); j++) {
				if(this.data.isTarget(i,j))
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
