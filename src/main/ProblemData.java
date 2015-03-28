package main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProblemData {
	
	private int nX;
	private int nY;
	private int nZ;
	
	private int nbTarget;
	private int coverageRadius;
	private int nbBalloon;
	private int nbTurn;
	
	private List<Coord2> targetsCase;
	
	private Coord2[][][] windsVectors;
	
	private Coord3[] balloonsCoord;
	// [] = tour, [][] = balloon
	private int[][] altitudeChanges;
	
	private Coord2 startBalloon;
	
	private Map<Integer, Integer> targetCovered;
	
	public ProblemData() {
		targetsCase = new ArrayList<Coord2>();
	}

	public void setAreaDimension(int nX, int nY, int nZ) {
		this.setnX(nX);
		this.setnY(nY);
		this.setnZ(nZ);
		
		windsVectors = new Coord2[nX][nY][nZ];
	}
	
	public Coord2 getWindVector(int x, int y, int z) {
		/*System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);*/
		return this.windsVectors[x][y][z];
	}

	public void setNbTarget(int nbTarget) {
		this.nbTarget = nbTarget;
	}
	
	public void initTarget() {
		targetCovered = new LinkedHashMap<Integer, Integer>();
		for(int i = 0; i < nbTarget; i++) {
			targetCovered.put(i, 0);
		}
	}

	public void setNbBalloon(int nbBalloon) {
		this.nbBalloon = nbBalloon;
	}
	
	public void setNbTurn(int nbTurn) {
		this.nbTurn = nbTurn;
	}
	
	public void setStartBalloon(Coord2 coord) {
		this.startBalloon = coord;
	}
	
	public void initBalloonsCoord() {
		this.balloonsCoord = new Coord3[nbBalloon];
		this.altitudeChanges = new int[nbTurn][nbBalloon];
		for(int i = 0; i < nbBalloon; i++){
			this.balloonsCoord[i] = new Coord3(this.startBalloon.x, 
					this.startBalloon.y, -1);
		}
	}
	
	public void addTargetCase(Coord2 coord) {
		targetsCase.add(coord);
	}
	
	public void addWindVector(int x, int y, int z, Coord2 vector) {
		windsVectors[x][y][z] = vector;
	}

	
	public int getNbBalloon() {
		return this.nbBalloon;
	}
	
	public boolean isTarget(int u, int v){
		return this.targetsCase.contains(new Coord2(u, v));
	}
	
	public int getNbTurn() {
		return this.nbTurn;
	}


	public int getCoverageRadius() {
		return coverageRadius;
	}

	public void setCoverageRadius(int coverageRadius) {
		this.coverageRadius = coverageRadius;
	}

	public int getnX() {
		return nX;
	}

	public void setnX(int nX) {
		this.nX = nX;
	}

	public int getnY() {
		return nY;
	}

	public void setnY(int nY) {
		this.nY = nY;
	}

	public int getnZ() {
		return nZ;
	}

	public void setnZ(int nZ) {
		this.nZ = nZ;
	}

	public int getAltitudeChanges(int i, int j) {
		return altitudeChanges[i][j];
	}

	public void setAltitudeChanges(int i, int j, int direction) {
		this.altitudeChanges[i][j] = direction;
	}

	public Coord3 getBalloonsCoord(int i) {
		return balloonsCoord[i];
	}

	public void setBalloonsCoord(int i , Coord3 coo) {
		this.balloonsCoord[i] = coo;
	}	

	public Coord2 getStartBalloon() { 
		return this.startBalloon; 
	}
	
	public Coord3 newBalloonCoord(Coord3 balloonCoord, int altitude) {
		Coord2 windVector = this.getWindVector(balloonCoord.x, balloonCoord.y, balloonCoord.z + altitude);
		return new Coord3((balloonCoord.x + windVector.x) % this.nX, balloonCoord.y + windVector.y, balloonCoord.z + altitude);
	}
	
	public void setTargetCovered(int i, int j) {
		this.targetCovered.put(i, j);
	}
	
	public int getTargetIndex(int i, int j) {
		for(int k = 0; k < this.nbTarget; k++) {
			if(this.targetsCase.get(k).equals(new Coord2(i, j))) {
				return k;
			}
		}
		return -1;
	}
	
	public int isCovered(int i) {
		return this.targetCovered.get(i);
	}
		
	public int columnDist(int x, int u) {
		return Math.min(Math.abs(x - u), Math.abs((x + getnX()) - u));
	}

	public int getScoreBalloon(int x, int y) {
		int score = 0;
		
		// If the balloon is not in the map
		if(x >= getnY() || y < 0)
			return -1;
		
		// (x - u)^2 + (columndist(y, v))^2 < V^2 => score + 1
		for(int i = x - this.coverageRadius ; i <= x + this.coverageRadius ; i++) {
			for(int j = y - (getCoverageRadius() - columnDist(x, i));
					j <= y + (getCoverageRadius() - columnDist(x, i)); j++) {
				if(isTarget(i,j))
					score++;
			}
		}
		return score;
	}
	
	public List<Coord2> getTargetsCase() {
		return targetsCase;
	}
	
	public boolean isInCoverageRadius(Coord2 target, Coord2 balloon) {
		for(int i = balloon.x - this.coverageRadius ; i <= balloon.x + this.coverageRadius ; i++) {
			for(int j = balloon.y - (getCoverageRadius() - columnDist(balloon.x, i));
					j <= balloon.y + (getCoverageRadius() - columnDist(balloon.x, i)); j++) {
				if (target.x == i && target.y == j) {
					return true;
				}
				
			}
		}
		return false;
	}
	

}
