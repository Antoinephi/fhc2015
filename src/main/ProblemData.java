package main;

import java.util.ArrayList;
import java.util.List;

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
	
	public ProblemData() {
		targetsCase = new ArrayList<Coord2>();
		this.balloonsCoord = new Coord3[nbBalloon];
		this.altitudeChanges = new int[nbTurn][nbBalloon];
		for(int i = 0; i < balloonsCoord.length; i++){
			this.balloonsCoord[i].x = this.startBalloon.x;
			this.balloonsCoord[i].y = this.startBalloon.y;
			this.balloonsCoord[i].z = 0;
		}
	}

	public void setAreaDimension(int nX, int nY, int nZ) {
		this.setnX(nX);
		this.setnY(nY);
		this.setnZ(nZ);
		
		windsVectors = new Coord2[nX][nY][nZ];
	}
	
	public Coord2 getWindVector(int x, int y, int z) {
		return this.windsVectors[x][y][z];
	}

	public void setNbTarget(int nbTarget) {
		this.nbTarget = nbTarget;
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
	
	public Coord2 newBallonCoord(Coord2 balloonCoord, int altitude) {
		Coord2 windVector = this.getWindVector(balloonCoord.x, balloonCoord.y, altitude);
		return new Coord2(balloonCoord.x + windVector.x, balloonCoord.y + windVector.y);
	}

}
