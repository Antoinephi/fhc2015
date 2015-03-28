package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Input {
	
	static public ProblemData parse(String pathname) throws IOException {
		
		ProblemData data = new ProblemData();
		
		BufferedReader reader = new BufferedReader(new FileReader(pathname));
		
		/*
		 * INPUT HERE
		 */
		
		reader.close();
		
		return data;
	}

}
