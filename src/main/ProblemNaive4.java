package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ProblemNaive4 extends Problem {

	public ProblemNaive4(ProblemData data) {
		super(data);
	}

	public void resolve() {
		//load file
		BufferedReader reader = null;;
		BufferedReader reader2 = null;;
		final int scoreList[][][] = new int[this.data.getnX()][this.data.getnY()][this.data.getnZ()];
		final int scoreListRare[][][] = new int[this.data.getnX()][this.data.getnY()][this.data.getnZ()];
		try {
			reader = new BufferedReader(new FileReader("data/scoreList"));
			reader2 = new BufferedReader(new FileReader("data/scoreRareList"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line;
		try {
			while((line = reader.readLine()) != null) {
				Scanner sc = new Scanner(line);
				
				int x = sc.nextInt();
				int y = sc.nextInt();
				int z = sc.nextInt();
				int score = sc.nextInt();
				
				scoreList[x][y][z] = score;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while((line = reader2.readLine()) != null) {
				Scanner sc = new Scanner(line);
				
				int x = sc.nextInt();
				int y = sc.nextInt();
				int z = sc.nextInt();
				int score = sc.nextInt();
				
				scoreListRare[x][y][z] = score;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		List<Coord3> sortedIndex = new ArrayList<Coord3>();
		List<Coord3> sortedRareIndex = new ArrayList<Coord3>();
		
		Collections.sort(sortedIndex, new Comparator<Coord3>() {
			public int compare(Coord3 index1, Coord3 index2) {
				return scoreList[index2.x][index2.y][index2.z]-scoreList[index1.x][index1.y][index1.z];
			}
		});
		
		Collections.sort(sortedRareIndex, new Comparator<Coord3>() {
			public int compare(Coord3 index1, Coord3 index2) {
				return scoreListRare[index2.x][index2.y][index2.z]-scoreListRare[index1.x][index1.y][index1.z];
			}
		});
		
		List<List<Integer>> listPath = new ArrayList<List<Integer>>();
		
		for(int i=0; i<data.getNbBalloon(); i += 2) {
			Coord3 c = sortedIndex.get(i/2);
			Coord3 c2 = sortedRareIndex.get(i/2);
			listPath.add(findPathTo(c.x, c.y, c.z));
			listPath.add(findPathTo(c2.x,c2.y, c2.z));
			System.out.println(c.x+" "+c.y+" "+c.z);
		}
		
		int tempo = 1;
		
		for(int t=0; t<data.getNbTurn(); t++) {
			for(int i=0; i<data.getNbBalloon(); i++) {
				if(t >= listPath.get(i).size() || t-tempo*i < 0)
					this.move[t][i] = 0;
				else
					this.move[t][i] = listPath.get(i).get(t-tempo*i);
				
			}
		}
		
	}
	
	

}
