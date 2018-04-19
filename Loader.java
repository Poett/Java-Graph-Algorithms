package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Loader {

	private String fileName;

	Loader(String fileName)
	{
		this.fileName = fileName;
	}

	public Graph getGraph() 
	{
		Scanner scanner;
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();



		try {
			scanner = new Scanner(new File(fileName));

			//First loop to create each course vertex
			while(scanner.hasNext()) 
			{
				try {

					//Skip comments
					String line;
					do {
						line = scanner.nextLine();
					} while (line.startsWith("#") && scanner.hasNext());

					String[] tokens = line.split(":", 2);
					vertices.add(new Vertex(tokens[0]));
					

				}catch (Exception e) {
				}
			}

			Graph G = new Graph(vertices);


			scanner = new Scanner(new File(fileName));


			//Second loop to set prereqs as directed edges
			while(scanner.hasNext()) 
			{
				try {
					//Skip comments
					String line;
					do {
						line = scanner.nextLine();
					} while (line.startsWith("#") && scanner.hasNext());
					
					//Go through each vertex and add its directed edges
					
					String[] tokens = line.split(":", 2);
					//Edges = array of strings separated by ;'s with the [ ] trimmed off
					String[] edges = tokens[1].trim().substring(1, tokens[1].length()-2).split(";");
					Vertex start = new Vertex(tokens[0]);
					Vertex end;
					
					//Edges array contains arrays of " Name , weight "
					for(String s : edges) 
					{
						tokens = s.split(",");
						Integer weight = Integer.parseInt(tokens[1].trim());
						end = new Vertex(tokens[0].trim());
						
						G.setDirectedEdge(start, end, weight);
						
					}
					
					
					
					
					
					
				}catch (Exception e) {
					// TODO: handle exception
				}
			}


			scanner.close();
			return G;
		}
		catch (FileNotFoundException e1) {

			return null;
		}
	}
}
