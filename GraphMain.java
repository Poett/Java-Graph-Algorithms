package graph;

import java.util.Stack;

public class GraphMain {

	public static void main(String[] args) {

		Loader l = new Loader("./graph/graph.txt");
		String a = "Math-Comp Science";
		String b = "Oak Tree Apt";
		
		Graph G = l.getGraph();
		
		
		System.out.println(G);
		System.out.println(G.getBellManFordGraph(G.getVertex(a)));
		System.out.println(G.getDijkstraGraph(G.getVertex(a)));
		
		
		Stack<Vertex> BFpath = G.getShortestPath(G.getVertex(a), G.getVertex(b), Graph.BELLMANFORD);
		Stack<Vertex> DJSpath = G.getShortestPath(G.getVertex(a), G.getVertex(b), Graph.DIJKSTRA);
		
		System.out.println("Bellman-Ford Path: ");
		while(!(BFpath.isEmpty())) 
		{
			System.out.println(BFpath.pop());
		}
		
		System.out.println();
		
		System.out.println("Dijkstra Path: ");
		while(!(DJSpath.isEmpty())) 
		{
			System.out.println(DJSpath.pop());
		}
//		

	}

}
