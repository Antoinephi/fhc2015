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
	
	private Coord2 startBalloon;
	
	public ProblemData() {
		targetsCase = new ArrayList<Coord2>();
	}

	public void setAreaDimension(int nX, int nY, int nZ) {
		this.nX = nX;
		this.nY = nY;
		this.nZ = nZ;
		
		windsVectors = new Coord2[nX][nY][nZ];
	}
	
	public Coord2 getWindVector(int x, int y, int z) {
		return this.windsVectors[x][y][z];
	}

	public void setNbTarget(int nbTarget) {
		this.nbTarget = nbTarget;
	}

	public void setCoverageRaius(int coverageRadius) {
		this.coverageRadius = coverageRadius;
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

}
