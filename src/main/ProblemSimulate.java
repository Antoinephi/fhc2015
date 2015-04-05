package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public abstract class ProblemSimulate extends Problem {
	
	protected boolean[][][] coverredMap;
	private int[][][][] dynamicScore;
	private int[][][][] dynamicPath;
	protected int[][][] currentScore;
	protected boolean[][] targetCell;
	protected Coord3[][][] destinationCell;
	private Random tieBreak;

	public ProblemSimulate(ProblemData data) {
		super(data);
	}
	
	abstract public void resolvePass();

	public void resolve() {
		
		tieBreak = new Random(0xB00B5);
		
		System.out.println("pre-compute...");
		preComputeAtStart();

		System.out.println("call pass...");
		resolvePass();
		
	}

	private void preComputeAtStart() {
		coverredMap = new boolean[data.getnX()][data.getnY()][data.getNbTurn()+1];
		
		 resetCover();
		
		dynamicScore = new int[data.getnX()][data.getnY()][data.getnZ()+1][data.getNbTurn()+1];
		dynamicPath = new int[data.getnX()][data.getnY()][data.getnZ()+1][data.getNbTurn()+1];
		
		currentScore = new int[data.getnX()][data.getnY()][data.getNbTurn()+1];
		
		targetCell = new boolean[data.getnX()][data.getnY()];
		
		for(int x=0; x<data.getnX(); x++) {
			Arrays.fill(targetCell[x], false);
		}
		
		for(int i=0; i<data.getTargetsCase().size(); i++)
			targetCell[data.getTargetsCase().get(i).x][data.getTargetsCase().get(i).y] = true;
		
		destinationCell = new Coord3[data.getnX()][data.getnY()][data.getnZ()+1];
		for(int x=0; x<data.getnX(); x++) {
			for(int y=0; y<data.getnY(); y++) {
				for(int z=0; z<data.getnZ()+1; z++) {
					if(z == 0)
						destinationCell[x][y][z] = null;
					else {
						Coord3 destination = null;
						
						Coord2 vector = data.getWindVector(x, y, z-1);
						
						int destX = x+vector.x;
						
						if(destX >= data.getnX()) {
							destX = destX  - data.getnX();
						}
						else if(destX < 0) {
							destX = data.getnX() + destX;
						}

						int destY = y+vector.y;
						
						if(destY >= 0 && destY < data.getnY())
							destination = new Coord3(destX, destY, z);
						
						destinationCell[x][y][z] = destination;
					}
				}
			}
		}
	}
	
	public void computeCellScore() {
		int radiusSquare = data.getCoverageRadius()*data.getCoverageRadius();
		for(int x=0; x<data.getnX(); x++) {
			for(int y=0; y<data.getnY(); y++) {
				for(int t=0; t<data.getNbTurn()+1; t++) {
					int score = 0;
					for(int diffX = -data.getCoverageRadius()-1; diffX < data.getCoverageRadius()+1; diffX++) {
						int destX = x+diffX;
						
						if(destX >= data.getnX()) {
							destX = destX  - data.getnX();
						}
						else if(destX < 0) {
							destX = data.getnX() + destX;
						}
						for(int diffY = -data.getCoverageRadius()-1; diffY < data.getCoverageRadius()+1; diffY++) {
							int destY = y+diffY;
							
							if(destY >= 0 && destY < data.getnY()) {
								if(diffX*diffX+diffY*diffY <= radiusSquare) {
									if(!coverredMap[destX][destY][t] && targetCell[destX][destY])
										score++;
								}
							}
						}
					}
					
					currentScore[x][y][t] = score;
					
				}
			}
		}
	}
	
	public void resetCover() {
		for(int x=0; x<data.getnX(); x++) {
			for(int y=0; y<data.getnY(); y++) {
				Arrays.fill(coverredMap[x][y], false);
			}
		}
	}
	
	public void resetDynamicCopmpute() {
		for(int x=0; x<data.getnX(); x++) {
			for(int y=0; y<data.getnY(); y++) {
				for(int z=0; z<data.getnZ()+1; z++) {
					Arrays.fill(dynamicScore[x][y][z], -1);
				}
			}
		}
	}
	
	public int computeBallonPath(Coord3 coordinates, int turn) {
		
		if(turn == data.getNbTurn())
			return 0;
		/*else if(turn == data.getNbTurn()) {
			if(coordinates != null) {
				dynamicPath[coordinates.x][coordinates.y][coordinates.z][turn] = 0;
				dynamicScore[coordinates.x][coordinates.y][coordinates.z][turn] = currentScore[coordinates.x][coordinates.y][turn];
			}
			return 0;
			
		}*/
		
		if(coordinates == null)
			return 0;
		
		if(dynamicScore[coordinates.x][coordinates.y][coordinates.z][turn] != -1) {
			return dynamicScore[coordinates.x][coordinates.y][coordinates.z][turn];
		}
		
		//change = 0
		int scoreS = -1;
		Coord3 coordS = null;
		coordS = coordinates.z == 0 ? coordinates : destinationCell[coordinates.x][coordinates.y][coordinates.z];
		scoreS = computeBallonPath(coordS, turn+1)+(coordS != null && coordinates.z > 0 ? currentScore[coordS.x][coordS.y][turn] : 0);
		
		//change = -1
		
		int scoreD = -1;
		Coord3 coordD = null;
		if(coordinates.z > 1) {
			coordD = destinationCell[coordinates.x][coordinates.y][coordinates.z-1];
			scoreD = computeBallonPath(coordD, turn+1)+(coordD != null ? currentScore[coordD.x][coordD.y][turn] : 0);
		}
		
		// change +1
		int scoreU = -1;
		Coord3 coordU = null;
		if(coordinates.z < data.getnZ()) {
			coordU = destinationCell[coordinates.x][coordinates.y][coordinates.z+1];
			scoreU = computeBallonPath(coordU, turn+1)+(coordU != null ? currentScore[coordU.x][coordU.y][turn] : 0);
		}
		
		Coord3 coordWin = null;
		int scoreWin = 0;
		if(scoreS > scoreD && scoreS > scoreU || scoreD > scoreU && scoreS == scoreD && tieBreak.nextBoolean() || scoreD < scoreU && scoreS == scoreU && tieBreak.nextBoolean()) {
			scoreWin = scoreS;
			coordWin = coordS;
			dynamicPath[coordinates.x][coordinates.y][coordinates.z][turn] = 0;
			
		}
		else if(scoreD > scoreU || scoreD == scoreU && tieBreak.nextBoolean()) {
			scoreWin = scoreD;
			coordWin = coordD;		
			dynamicPath[coordinates.x][coordinates.y][coordinates.z][turn] = -1;
		}
		else {
			scoreWin = scoreU;
			coordWin = coordU;
			dynamicPath[coordinates.x][coordinates.y][coordinates.z][turn] = 1;
		}
		
		dynamicScore[coordinates.x][coordinates.y][coordinates.z][turn] = scoreWin;
		
		return scoreWin;
	}
	
	public void backtracePath(Coord3 coordinates, int turn, List<Integer> path) {
		
	
		if(turn >= data.getNbTurn())
			return;
		
		if(coordinates == null)
			return;
		
		//System.out.println(coordinates.x+" "+coordinates.y+" "+coordinates.z+" => "+dynamicScore[coordinates.x][coordinates.y][coordinates.z][turn]+" "+dynamicPath[coordinates.x][coordinates.y][coordinates.z][turn]);

		if(dynamicPath[coordinates.x][coordinates.y][coordinates.z][turn] == 0) {
			path.add(new Integer(0));
			Coord3 coord = coordinates.z == 0 ? coordinates : destinationCell[coordinates.x][coordinates.y][coordinates.z];
			if(coord != null) {
				if(coordinates.z !=  0)
					cover(coord, turn);
				backtracePath(coord, turn+1, path);
			}
		}
		else if(dynamicPath[coordinates.x][coordinates.y][coordinates.z][turn] == -1) {
			path.add(new Integer(-1));
			if(coordinates.z > 1) {
				Coord3 coord = destinationCell[coordinates.x][coordinates.y][coordinates.z-1];
				if(coord != null) {
					cover(coord, turn);
					backtracePath(coord, turn+1, path);
				}
			}
		}
		else {
			path.add(new Integer(1));
			if(coordinates.z < data.getnZ()) {
				Coord3 coord = destinationCell[coordinates.x][coordinates.y][coordinates.z+1];
				if(coord != null) {
					cover(coord, turn);
					backtracePath(coord, turn+1, path);
				}
			}
		}
	}

	public void cover(Coord3 coord, int turn) {
		int radiusSquare = data.getCoverageRadius()*data.getCoverageRadius();
		for(int diffX = -data.getCoverageRadius()-1; diffX < data.getCoverageRadius()+1; diffX++) {
			int destX = coord.x+diffX;
			
			if(destX >= data.getnX()) {
				destX = destX  - data.getnX();
			}
			else if(destX < 0) {
				destX = data.getnX() + destX;
			}
			for(int diffY = -data.getCoverageRadius()-1; diffY < data.getCoverageRadius()+1; diffY++) {
				int destY = coord.y+diffY;
				
				if(destY >= 0 && destY < data.getnY()) {
					if(diffX*diffX+diffY*diffY <= radiusSquare) {
						coverredMap[destX][destY][turn] = true;
					}
				}
			}
		}
	}
	
	public int scoreChecking() {
		int score = 0;
		int scoreBalloon[] = new int[data.getNbBalloon()];
		
		Coord3[] coordinates = new Coord3[data.getNbBalloon()];
		for(int b=0; b<data.getNbBalloon(); b++) {
			coordinates[b] = new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, 0);
		}
		
		
		for(int t=0; t<data.getNbTurn(); t++) {
			
			boolean coverredMap[][] = new boolean[data.getnX()][data.getnY()];
			
			for(int b=0; b<data.getNbBalloon(); b++) {
				
				if(coordinates[b] == null)
					continue;
				
				if(coordinates[b].z == 1 && move[t][b] == -1) {
					System.out.println("Error go too low.");
					System.exit(0);
				}
				else if(coordinates[b].z == 8 && move[t][b] == 1) {
					System.out.println("Error go too high.");
					System.exit(0);
				}

				int newZ = coordinates[b].z+move[t][b];
				
				if(newZ == 0)
					continue;
				
				Coord3 newCoord = null;
				if(newZ > 0 && newZ <= data.getnZ())
					newCoord = destinationCell[coordinates[b].x][coordinates[b].y][newZ];
				
				if(newCoord == null) {
					continue;
				}
				
				coordinates[b] = newCoord;
				
				

				int currentScore = 0;
				for(int x = -data.getCoverageRadius()-1; x < data.getCoverageRadius()+1; x++) {
					int cX = coordinates[b].x+x;
					if(cX >= data.getnX()) {
						cX = cX  - data.getnX();
					}
					else if(cX < 0) {
						cX = data.getnX() + cX;
					}
					for(int y = -data.getCoverageRadius()-1; y < data.getCoverageRadius()+1; y++) {
						int cY = coordinates[b].y+y;
						if(cY >= 0 && cY < data.getnY()) {
							if(x*x+y*y <= data.getCoverageRadius()*data.getCoverageRadius()) {
								if(!coverredMap[cX][cY] && targetCell[cX][cY]) {
									currentScore++;
									scoreBalloon[b]++;
								}
								coverredMap[cX][cY] = true;
							}
						}
					}

				}
				score += currentScore;
				
			}
			

		}
		
		return score;
	}
	
	public void loadOutput(String pathname) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pathname));

			String line;

			int currentTurn = 0;
			while((line = reader.readLine()) != null) {
				Scanner sc = new Scanner(line);

				for(int i=0; i<data.getNbBalloon(); i++) {
					this.move[currentTurn][i] = sc.nextInt();
				}
				
				sc.close();

				currentTurn++;
			}

			reader.close();

			if(currentTurn != data.getNbTurn())
				throw new RuntimeException("Incorrecte number of turn : "+currentTurn);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 