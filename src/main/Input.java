package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Input {
	
	static public ProblemData parse(String pathname) throws IOException {
		
		ProblemData data = new ProblemData();
		
		BufferedReader reader = new BufferedReader(new FileReader(pathname));
		
		/*
		 * INPUT HERE
		 */
		
		String currentLine = reader.readLine();
		
		Scanner sc = new Scanner(currentLine);
		
		int nX = sc.nextInt();
		int nY = sc.nextInt();
		int nZ = sc.nextInt();
		
		data.setAreaDimension(nX, nY, nZ);
		
		currentLine = reader.readLine();
		
		sc = new Scanner(currentLine);
		
		int nbTarget = sc.nextInt();
		int coverageRadius = sc.nextInt();
		int nbBalloon = sc.nextInt();
		int nbTurn = sc.nextInt();
		
		data.setNbTarget(nbTarget);
		data.setCoverageRadius(coverageRadius);
		data.setNbBalloon(nbBalloon);
		data.setNbTurn(nbTurn);
		
		sc.close();
		
		currentLine = reader.readLine();
		sc = new Scanner(currentLine);
		int startBallonY = sc.nextInt();
		int startBallonX = sc.nextInt();
		
		data.setStartBalloon(new Coord2(startBallonX, startBallonY));
		
		sc.close();
		
		for(int i=0; i<nbTarget; i++) {
			currentLine = reader.readLine();
			sc = new Scanner(currentLine);
			
			int y = sc.nextInt();
			int x = sc.nextInt();
			
			data.addTargetCase(new Coord2(x, y));
			
			sc.close();
		}
		
		for(int i=0; i<nZ; i++) {
			
			for(int j=0; j<nX; j++) {
				currentLine = reader.readLine();
				sc = new Scanner(currentLine);

				for(int k=0; k<nY; k++) {
					int y = sc.nextInt();
					int x = sc.nextInt();
					
					data.addWindVector(j, k, i, new Coord2(x, y));
				}
				
				sc.close();
			}
		}
		
		System.out.println("INPUT");
		
		reader.close();
		
		return data;
	}

}
