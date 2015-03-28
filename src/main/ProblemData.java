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
	
	public Coord2 getStartBalloon() {
		return this.startBalloon;
	}

}
