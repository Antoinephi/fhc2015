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
		data.setCoverageRaius(coverageRadius);
		data.setNbBalloon(nbBalloon);
		data.setNbTurn(nbTurn);
		
		
		reader.close();
		
		return data;
	}

}
