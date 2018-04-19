package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeSet;

public class Graph {

	private ArrayList<Vertex> vertices;
	private Integer[][] edges;
	private int clock;
	private HashMap<Vertex,Integer> pre;
	private HashMap<Vertex,Integer> post;
	
	public static final int DIJKSTRA = 2;
	public static final int BELLMANFORD = 1;

	Graph(ArrayList<Vertex> userGraph)
	{
		clock = 0;
		
		vertices = userGraph;
		
		for(Vertex v : vertices) 
		{
			v.visited = false;
		}
		
		edges = new Integer[userGraph.size()][userGraph.size()];
	}
	
	
	
	/*
	 * Graph management
	 */
	public ArrayList<Vertex> getVertices()
	{
		return vertices;
	}
	
	public Vertex getVertex(String name) 
	{
		for(Vertex v : vertices) 
		{
			if (v.getName().equals(name)) {return v;}
		}
		return null;
	}
	
	public void setDirectedEdge(Vertex start, Vertex end, int weight) 
	{
		//Get the vertices index or catch if could not get index
		try {
			int vI = vertices.indexOf(start);
			int uI = vertices.indexOf(end);
			edges[vI][uI] = weight;
		} 
		catch (Exception e) {
			return;
		}
		

	}
	
	public void setUndirectedEdge(Vertex v, Vertex u, int weight) 
	{
		setDirectedEdge(v, u, weight);
		setDirectedEdge(u, v, weight);
	}

	
	
	public HashMap<Vertex, Integer> getEdges(Vertex v)
	{
		try {
			HashMap<Vertex, Integer> edgesOf = new HashMap<Vertex, Integer>();
			int i = vertices.indexOf(v);
			for(int j = 0; j < vertices.size(); j++) 
			{
				if(edges[i][j] != null) { edgesOf.put(vertices.get(j), edges[i][j]); } //if there's a set weight for the edge i->j, then send j to edgesOf}
			}
			
			
			return edgesOf;
		} 
		catch (Exception e) 
		{
			return null;
		}
	}
	
	public ArrayList<Vertex> getBackEdges(Vertex v)
	{
		try {
			ArrayList<Vertex> edgesOf = new ArrayList<Vertex>();
			int i = vertices.indexOf(v);
			for(int j = 0; j < vertices.size(); j++) 
			{
				if(edges[j][i] != null) { edgesOf.add(vertices.get(j)); }//if there's a set weight for the edge i->j, then send j to edgesOf}
			}
			
			
			return edgesOf;
		} 
		catch (Exception e) 
		{
			return null;
		}
	}
	
	
	
	
	
	
	/*
	 * Graph Algorithms
	 */
	
	public Stack<Vertex> getShortestPath(Vertex s, Vertex end, int algorithm)
	{
		Stack<Vertex> path = new Stack<Vertex>();
		path.push(end);
		
		//Get the shortest path spanning tree for S
		Graph G;
		if(algorithm == 1) {G = this.getBellManFordGraph(s);}
		else if (algorithm == 2) {G = this.getDijkstraGraph(s);}
		else {G = this.getBellManFordGraph(s);}
		
		//Climb up spanning tree from end by getting back edges
		ArrayList<Vertex> parents = G.getBackEdges(end);
		if (parents.isEmpty()) {return path;}
		
		do {
			path.push(parents.get(0));
			parents = G.getBackEdges(parents.get(0));
		} while (!(parents.isEmpty()));
		
		
		return path;
		
	}
	
	public Graph getBellManFordGraph(Vertex s)
	{
		Graph G = new Graph(vertices);
		HashMap<Vertex, Integer> E;
		HashMap<Vertex, Vertex> BFEdges = new HashMap<Vertex, Vertex>();
		boolean sInG = false;
		
		//Set the vertex s as distance 0, the rest as null
		for(Vertex x : vertices) 
		{
			if (x.equals(s)) {x.dist = 0; sInG = true;}
			else x.dist = null;
		}
		if(!sInG) return null;
		
		//Repeat V-1 times
		for(int i = 1; i < vertices.size(); i++) 
		{
			//For each vertex, get their edges and update
			for(Vertex u : vertices) 
			{
				//Go through each edge
				E = getEdges(u);
				for(Vertex v : E.keySet()) 
				{
					if(update(u, v, E.get(v))) 
					{
						BFEdges.put(v, u);
					}
				}
			}
		}
		
		//After V-1 times, should have BFEdges of the edges we want to set as our BF graph
		//B is the end nodes, A is the previous
		for(Vertex b : BFEdges.keySet()) 
		{
			Vertex a = BFEdges.get(b);
			G.setDirectedEdge(a, b, a.dist-b.dist);
		}
		
		
		return G;
	}
	
	private boolean update(Vertex u, Vertex v, int w) 
	{
		if(u.dist == null) {return false;}
		else if(v.dist == null || v.dist > (u.dist + w)) {v.dist = u.dist + w; return true;}
		return false;
	}
	
	public Graph getDijkstraGraph(Vertex s) 
	{
		Graph G = new Graph(vertices);
		HashMap<Vertex, Integer> E;
		HashMap<Vertex, Vertex> DJSEdges = new HashMap<Vertex, Vertex>();
		boolean sInG = false;
		
		//Set the vertex s as distance 0, the rest as null
		for(Vertex x : vertices) 
		{
			if (x.equals(s)) {x.dist = 0; sInG = true;}
			else x.dist = null;
		}
		if(!sInG) return null;
		
		
		//Will hold sorted data where vertices are compared by their distance
		//Will never imply that two vertices with same distance are "equal"
		TreeSet<Vertex> heap = new TreeSet<Vertex>(new Comparator<Vertex>() {
			public int compare(Vertex o1, Vertex o2) 
			{
				if (o1.getName().compareTo(o2.getName()) == 0) {return 0;}
				else if(o1.dist == null) {return 1;}
				else if(o2.dist == null) {return -1;}
				else if(o1.dist > o2.dist) {return 1;}
				else return -1;
			}
			
		});
		
		heap.addAll(vertices); //Adds all vertices to heap, sorted by dist and where 'null' = largest int
		
		Vertex u;
		while((u = heap.pollFirst()) != null) 
		{
			E = this.getEdges(u);
			
			for(Vertex v : E.keySet()) 
			{
				//Checks to see if dist needs to be updated
				if(update(u, v, E.get(v))) 
				{
					//if dist was updated, reorder heap
					heap.remove(v);
					heap.add(v);
					DJSEdges.put(v, u);
				}
			}
		}
		
		for(Vertex b : DJSEdges.keySet()) 
		{
			Vertex a = DJSEdges.get(b);
			G.setDirectedEdge(a, b, a.dist-b.dist);
		}
		
		
		return G;
		
		
		
	}
	
	public ArrayList<Vertex> topoLogicalOrder()
	{
		this.dfs();
		
		return topoMergeSort(vertices);
	}
	private ArrayList<Vertex> topoMergeSort(ArrayList<Vertex> vertexArray) 
	{
		if(vertexArray.size() <= 1) {return vertexArray;}
		
		ArrayList<Vertex> left = new ArrayList<Vertex>(vertexArray.subList(0, (vertexArray.size()/2)));
		ArrayList<Vertex> right = new ArrayList<Vertex>(vertexArray.subList((vertexArray.size()/2),vertexArray.size()));
		
		left = topoMergeSort(left);
		right = topoMergeSort(right);
		
		return(merge(left, right));
	}
	
	private ArrayList<Vertex> merge(ArrayList<Vertex> left, ArrayList<Vertex> right) 
	{
		ArrayList<Vertex> toReturn = new ArrayList<Vertex>();
		
		int i = 0;
		int j = 0;
		
		//Merge in descending order (greatest gets put inside)
		while(i < left.size() && j < right.size()) 
		{
			//Case - left index is greater
			int postL = post.get(left.get(i));
			int postR = post.get(right.get(j));
			if(postL > postR) {toReturn.add(left.get(i)); i++;}
			//Case - left is equal or less than right
			else {toReturn.add(right.get(j)); j++;}
		}
		while(i < left.size()) {toReturn.add(left.get(i)); i++;}
		while(j < right.size()) {toReturn.add(right.get(j)); j++;}
		
		return toReturn;
	}

	public void dfs() 
	{
		clock = 0; 
		pre = new HashMap<Vertex, Integer>();
		post = new HashMap<Vertex, Integer>();
		
		for(Vertex v : vertices) 
		{
			v.visited = false;
		}
		
		for(Vertex v : vertices) 
		{
			if(!v.visited) {explore(v);}
		}
	}
	
	public void bfs(Vertex s) 
	{
		HashMap<Vertex, Integer> dist = new HashMap<Vertex, Integer>();
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		for(Vertex v : vertices) 
		{
			dist.put(v, null);
		}
		
		//Sets distance to itself as 0
		dist.put(s, 0);
		
		queue.add(s);
		
		//While the queue isn't empty
		while(!queue.isEmpty()) 
		{
			Vertex u = queue.removeFirst(); //Eject from queue
			for (Vertex e : getEdges(u).keySet())  //For each edge in the queue
			{
				if(dist.get(e) == null) //if the edge's distance has not been set
				{
					queue.add(e); //add it to queue
					dist.put(e, dist.get(u) + 1); //set distance to a level below U
				}
			}
		}
		
	}
	
	private void explore(Vertex v) 
	{
		v.visited = true;
		pre.put(v, ++clock);
		for(Vertex u : getEdges(v).keySet()) 
		{
			if(!u.visited) {explore(u);}
		}
		post.put(v, ++clock);
	}
	
	
	public Graph reverseGraph() 
	{
		
		Graph R = new Graph(vertices);
		
		//For each of the vertices
		for(Vertex v : vertices) 
		{
			//For each of v's edges
			for(Vertex e : this.getEdges(v).keySet()) 
			{
				//Add an edge to reversed of e -> v
				R.setDirectedEdge(e, v, 1);
			}
		}
		return R;
	}
	
	
	
	public void printEdges() 
	{
		for(int i = 0; i < edges.length; i++) 
		{
			String line = "[";
			for(int j = 0; j < edges[i].length - 1; j++) 
			{
				line += edges[i][j] + ", ";
			}
			line += edges[i][edges[i].length - 1] + "]";
			System.out.println(line);
		}
	}
	
	public String toString() 
	{
		//Go through each vertices and show its edges
		String toReturn = "";
		
		
		for(Vertex v : vertices) 
		{
			HashMap<Vertex, Integer> E = getEdges(v);
			
			if(!E.isEmpty())
			{
				toReturn = toReturn + v + "(";
				for(Vertex edge : E.keySet()) 
				{
					toReturn = toReturn + edge.getName() + ", ";
				}
				toReturn = toReturn.trim().substring(0, toReturn.length() - 2);
				toReturn = toReturn + ")\n";
			}
			else {toReturn = toReturn + v + "[NULL]\n";}
		}
		return toReturn;
	}
	
	
	
}
