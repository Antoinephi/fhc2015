package main;

import java.util.ArrayList;
import java.util.List;
public class ProblemSimulate extends Problem {
	
	private boolean[][][] coverredMap;
	private int[][][][] dynamicScore;
	private int[][][] currentScore;
	private int[][] targetMap;
	private int[][][] destination;
	
	private int progress;
	private int dynProgress;
	private int dynUses;
	public ProblemSimulate(ProblemData data) {
		super(data);
	}

	public void resolve() {
		
		int totalScore = 0;
		//initialize covered map
		coverredMap = new boolean[data.getnX()][data.getnY()][data.getNbTurn()];
		for(int i=0; i<data.getnX(); i++) {
			for(int j=0; j<data.getnY(); j++) {
				for(int k=0; k<data.getNbTurn(); k++) {
					coverredMap[i][j][k] = false;
				}
			}
		}
		
		targetMap = new int[data.getnX()][data.getnY()];
		for(int i=0; i<data.getnX(); i++) {
			for(int j=0; j<data.getnY(); j++) {
				targetMap[i][j] = data.getTargetIndex(i, j);
			}
		}
		
		System.out.println(data.getnX()*data.getnY()*data.getnZ()*data.getNbTurn());
		dynamicScore = new int[data.getnX()][data.getnY()][data.getnZ()][data.getNbTurn()+1];
		
		currentScore = new int[data.getnX()][data.getnY()][data.getNbTurn()+1];
		
		for(int currentBalloon=0; currentBalloon<data.getNbBalloon(); currentBalloon++) {
			//reset dynamic arrays
			for(int i=0; i<data.getnX(); i++) {
				for(int j=0; j<data.getnY(); j++) {
					for(int k=0; k<data.getnZ(); k++) {
						for(int l=0; l<data.getNbTurn(); l++) {
							dynamicScore[i][j][k][l] = -1;
						}
					}
				}
			}
			
			System.out.println("compute score ...");
			
			computeScore();
			
			System.out.println("compute path ...");
			
			//buildBestPath(0);
			
			System.out.println("Launch simulatin for balloon "+(currentBalloon+1)+"/"+data.getNbBalloon());
			
			progress = 0;
			int score =findBestPath(new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, -1), 0);
			totalScore += score;
			System.out.println("find score : "+score+" / "+totalScore);
			
			List<Integer> path = new ArrayList<Integer>();
			retrieveBestPath(new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, -1), 0, path);
			
			System.out.println("path length : "+path.size());
			
			for(int i=0; i<data.getNbTurn(); i++) {
				this.move[i][currentBalloon] = i < path.size() ? path.get(i) : 0;
			}
			

		}
	}
	
	public void buildBestPath(int turn) {
		if(turn == data.getNbTurn()-1) {
			for(int i=0; i<data.getnX(); i++) {
				for(int j=0; j<data.getnY(); j++) {
					for(int k=0; k<data.getnZ(); k++) {
						dynamicScore[i][j][k][turn] = currentScore[i][j][turn];
					}
				}
			}	
		}
		else {
			buildBestPath(turn+1);
			for(int i=0; i<data.getnX(); i++) {
				for(int j=0; j<data.getnY(); j++) {
					for(int k=0; k<data.getnZ(); k++) {
						Coord3 coordinate = new Coord3(i, j, k);
						Coord3 c1 = move(coordinate, 0);
						int score1 = 0;
						
						if(c1 != null) {
							score1 = dynamicScore[c1.x][c1.y][c1.z][turn+1];
						}
							
						Coord3 c2 = move(coordinate, -1);
						int score2 = 0;
						if(c2 != null) {
							score2 = dynamicScore[c2.x][c2.y][c2.z][turn+1];
						}
						
						Coord3 c3 = move(coordinate, 1);
						int score3 = 0;
						if(c3 != null) {
							score3 = dynamicScore[c3.x][c3.y][c3.z][turn+1];
						}
						
						int score = 0;
						if(score1 > score2 && score1 > score3) {
							score = score1;
						}
						else if(score2 > score3) {
							score = score2;
						}
						else {
							score = score3;			
						}
						dynamicScore[i][j][k][turn] = currentScore[i][j][turn]+score;
					}
				}
			}
		}
		

		//System.out.println("[time="+((float)(System.nanoTime()-beg)/1000000.0f)+"ms]best path build for turn "+turn);

			
	}
	
	public int findBestPath(Coord3 coordinate, int turn) {
		
		if(turn >= data.getNbTurn()+1)
			return 0;
		
		if(coordinate.z != -1 && dynamicScore[coordinate.x][coordinate.y][coordinate.z][turn] != -1) {
			return dynamicScore[coordinate.x][coordinate.y][coordinate.z][turn];
		}

		Coord3 c = null; 

		Coord3 c1 = move(coordinate, 0);
		int score1 = c1 != null ? findBestPath(c1,  turn+1) : 0;
		
		Coord3 c2 = move(coordinate, -1);
		int score2 = c2 != null ? findBestPath(c2,  turn+1) : 0;
		
		Coord3 c3 = move(coordinate, 1);
		int score3 = c3 != null ? findBestPath(c3,  turn+1) : 0;

		
		int score = 0;
		if(score1 > score2 && score1 > score3) {
			score = score1;
			c = c1;
		}
		else if(score2 > score3) {
			score = score2;
			c = c2;
		}
		else {
			score = score3;		
			c = c3;
		}
		
		int nScore = score+(c != null && c.z != -1 ? currentScore[coordinate.x][coordinate.y][turn] : 0);
		
		if(coordinate.z != -1) {
			dynamicScore[coordinate.x][coordinate.y][coordinate.z][turn] = nScore;
		}
		return nScore;
	}
	
	public Coord3 move(Coord3 coordinate, int altitudeChange) {
		
		if(coordinate.z == -1 && altitudeChange == 0)
			return new Coord3(coordinate.x, coordinate.y, coordinate.z);
		
		int z = coordinate.z+altitudeChange;
		
		if(z < 0 || z >= data.getnZ())
			return null;
		
		Coord2 vector = data.getWindVector(coordinate.x, coordinate.y, z);
		
		int x = oRingCompute(coordinate.x+vector.x);
		
		int y = coordinate.y+vector.y;
		
		if(y < 0 || y >= data.getnY())
			return null;

		return new Coord3(x, y, z);
	}
	
	public void computeScore() {
		
	
		for(int k=0; k<data.getNbTurn(); k++) {
			for(int i=0; i<data.getnX(); i++) {
				for(int j=0; j<data.getnY(); j++) {
			
					int score = 0;
					for(int x = -data.getCoverageRadius(); x < data.getCoverageRadius(); x++) {
						int cX = oRingCompute(i+x);
						for(int y = -data.getCoverageRadius(); y < data.getCoverageRadius(); y++) {
							int cY = j+y;
							if(cY >= 0 && cY < data.getnY()) {
								if(x*x+y*y <= data.getCoverageRadius()*data.getCoverageRadius()) {
									if(targetMap[cX][cY] != -1 && !coverredMap[cX][cY][k])
										score++;
								}
							}
						}
					}
					
					currentScore[i][j][k] = score;
					
				}
			}
		}
	}
	
	public void setCovered(Coord3 coordinate, int turn) {

		for(int x = -data.getCoverageRadius(); x < data.getCoverageRadius(); x++) {
			int cX = oRingCompute(coordinate.x+x);
			for(int y = -data.getCoverageRadius(); y < data.getCoverageRadius(); y++) {
				int cY = coordinate.y+y;
				if(cY >= 0 && cY < data.getnY()) {
					if(x*x+y*y <= data.getCoverageRadius()*data.getCoverageRadius()) {
						coverredMap[cX][cY][turn] = true;
					}
				}
			}
		}
	}
	
	public int oRingCompute(int x) {
		if(x < 0) {
			return data.getnX()+x;
		}
		else if(x >= data.getnX()) {
			return x-data.getnX();
		}
		else
			return x;
	}
	
	public void retrieveBestPath(Coord3 coordinate, int turn, List<Integer> list) {
		if(turn >= data.getNbTurn())
			return;
		
		
		Coord3 c1 = move(coordinate, 0);
		int score1 = c1 != null && c1.z != -1 ? dynamicScore[c1.x][c1.y][c1.z][turn+1] : -1;
		
		Coord3 c2 = move(coordinate, -1);
		int score2 = c2 != null && c2.z != -1 ? dynamicScore[c2.x][c2.y][c2.z][turn+1] : -1;
		
		Coord3 c3 = move(coordinate, 1);
		int score3 = c3 != null && c3.z != -1 ? dynamicScore[c3.x][c3.y][c3.z][turn+1] : -1;
		
		if(score1 == -1 &&  score2 == -1 && score3 == -1) {
			list.add(new Integer(0));
			retrieveBestPath(coordinate, turn+1, list);
		}
		else if(score1 > score2 && score1 > score3) {
			list.add(new Integer(0));
			setCovered(c1, turn);
			retrieveBestPath(c1, turn+1, list);
		}
		else if(score2 > score3) {
			list.add(new Integer(-1));
			setCovered(c2, turn);
			retrieveBestPath(c2, turn+1, list);
		}
		else if(c3 != null) {
			list.add(new Integer(1));
			setCovered(c3, turn);
			retrieveBestPath(c3, turn+1, list);		
		}
		else
			return;

	}
	
	public int[] getBalloonScore() {
		int[] allScore = new int[data.getNbBalloon()];
		
		coverredMap = new boolean[data.getnX()][data.getnY()][data.getNbTurn()];
		for(int i=0; i<data.getnX(); i++) {
			for(int j=0; j<data.getnY(); j++) {
				for(int k=0; k<data.getNbTurn(); k++) {
					coverredMap[i][j][k] = false;
				}
			}
		}
		
		for(int b=0; b<data.getNbBalloon(); b++) {
			
			computeScore();
			
			Coord3 currentCoord = new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, -1);
			
			for(int t=0; t<data.getNbTurn(); t++) {
				currentCoord = move(currentCoord, move[t][b]);
				if(currentCoord.z != -1)
					setCovered(currentCoord, t);
				
				
			}

		}
		return 0;
	}

}



