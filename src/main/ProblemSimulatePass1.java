package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProblemSimulatePass1 extends ProblemSimulate {

	public ProblemSimulatePass1(ProblemData data) {
		super(data);
	}

	public void resolvePass() {
		int totale = 0;
		for(int b=0; b<data.getNbBalloon(); b++) {
			computeCellScore();
			
			resetDynamicCopmpute();
			
			int score = computeBallonPath(new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, 0), 0);
			totale += score;
			System.out.println(b+" = "+score+" (totale = "+totale+")");
			
			List<Integer> path = new ArrayList<Integer>();
			backtracePath(new Coord3(data.getStartBalloon().x, data.getStartBalloon().y, 0), 0, path);
			
			System.out.println("length : "+path.size());
			
			for(int i=0; i<data.getNbTurn(); i++) {
				move[i][b] = i < path.size() ? path.get(i) : 0;
			}
			
			int checking = scoreChecking();
			
			if(checking != totale) {
				System.out.println("Checking failed ! "+totale+" / "+checking);
			}
		}
	}
	
	public int checkBalloonPath(Coord3 coordinates, int b, int t) {
		if(t == data.getNbTurn())
			return 0;
		
		
		
		
		
		int s = computeBallonPath(coordinates, t);
		
		if(move[t][b] == 0) {
			Coord3 c0 = destinationCell[coordinates.x][coordinates.y][coordinates.z];

			int s1 = currentScore[c0.x][c0.y][t]+checkBalloonPath(c0, b, t+1);
			
			if(s1 != s) {
				System.out.println("stable error : t="+t+" c=["+coordinates.x+";"+coordinates.y+";"+coordinates.z+"] ("+s1+" / "+s+")");
			}
			return s1;
		}
		else if(move[t][b] == -1) {
			Coord3 c1 = destinationCell[coordinates.x][coordinates.y][coordinates.z-1];
			
			int s2 =  currentScore[c1.x][c1.y][t]+checkBalloonPath(c1, b, t+1);
			if(s2 != s) {
				System.out.println("down error : t="+t+" c=["+c1.x+";"+c1.y+";"+c1.z+"] ("+s2+" / "+s+")");
			}
			return s2;
		}
		else {
			Coord3 c2 = destinationCell[coordinates.x][coordinates.y][coordinates.z+1];
			int s3 = currentScore[c2.x][c2.y][t]+checkBalloonPath(c2, b, t+1);
			if(s3 != s) {
				System.out.println("up error : : t="+t+" c=["+c2.x+";"+c2.y+";"+c2.z+"] ("+s3+" / "+s+")");
			}
			return s3;
		}
	}

}
