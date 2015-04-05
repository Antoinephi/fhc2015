package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProblemSimulatePass3 extends ProblemSimulate {
	private String previousOut;

	public ProblemSimulatePass3(ProblemData data, String previousOut) {
		super(data);
		this.previousOut = previousOut;
	}

	public void resolvePass() {
		loadOutput(previousOut);
		
		
		
		int bestScore = scoreChecking();
		System.out.println("actually : "+bestScore);
		while(true) {
			
			for(int currentBalloon=0; currentBalloon<data.getNbBalloon(); currentBalloon++) {
	
				System.out.println("Launch simulation for balloon "+(currentBalloon+1)+"/"+data.getNbBalloon());
	
				resetWithoutBalloon(currentBalloon);
				
				computeCellScore();
				resetDynamicCopmpute();
				
				
				int score = computeBallonPath(new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, 0), 0);

				System.out.println("find score : "+score);
	
				List<Integer> path = new ArrayList<Integer>();
				backtracePath(new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, 0), 0, path);
				System.out.println("path length : "+path.size());
	
				for(int i=0; i<data.getNbTurn(); i++) {
					this.move[i][currentBalloon] = i < path.size() ? path.get(i) : 0;
				}
				
				int scores = scoreChecking();
				System.out.println("Total : "+scores);
				
				if(scores > bestScore) {
					System.out.println("saved!");
					try {
						output("data/outPass2");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					bestScore = scores;
				}
			}
		}
		
	}
	
	public int compute2BallonPath(Coord3 coordinates1, Coord3 coordinates2, int turn) {
		
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
		int scoreS = 0;
		Coord3 coordS = null;
		coordS = coordinates.z == 0 ? coordinates : destinationCell[coordinates.x][coordinates.y][coordinates.z];
		scoreS = computeBallonPath(coordS, turn+1)+(coordS != null && coordinates.z > 0 ? currentScore[coordS.x][coordS.y][turn] : 0);
		
		//change = -1
		
		int scoreD = 0;
		Coord3 coordD = null;
		if(coordinates.z > 1) {
			coordD = destinationCell[coordinates.x][coordinates.y][coordinates.z-1];
			scoreD = computeBallonPath(coordD, turn+1)+(coordD != null ? currentScore[coordD.x][coordD.y][turn] : 0);
		}
		
		// change +1
		int scoreU = 0;
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

	private void resetWithoutBalloon(int currentBalloon) {
		resetCover();
		
		Coord3[] coordinates = new Coord3[data.getNbBalloon()];
		for(int b=0; b<data.getNbBalloon(); b++) {
			coordinates[b] = new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, 0);
		}
		
		
		for(int t=0; t<data.getNbTurn(); t++) {
			
			
			for(int b=0; b<data.getNbBalloon(); b++) {
				
				if(b == currentBalloon)
					continue;
				
				if(coordinates[b] == null)
					continue;

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
								coverredMap[cX][cY][t] = true;
							}
						}
					}

				}
				
			}
			

		}
		
	}

}
