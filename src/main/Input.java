package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Input {
	
	static public ProblemData parse(String pathname) throws FileNotFoundException {
		
		ProblemData data = new ProblemData();
		
		BufferedReader reader = new BufferedReader(new FileReader(pathname));
		
		return data;
	}

}
