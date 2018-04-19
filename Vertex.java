package graph;

public class Vertex implements Comparable<Vertex>{

	private String name;
	public boolean visited;
	public Integer dist;
	Vertex(String name)
	{
		this.name = name;
		visited = false;
	}
	
	
	public String getName() {return name;}
	
	public int compareTo(Vertex rhs) {
		return this.name.compareTo(rhs.name);
	}

	
	@Override
	public boolean equals(Object obj) {
		if(this.name.compareTo( ((Vertex)obj).name ) == 0){return true;}
		return false;
	}
	
	public String toString() 
	{
		return name + " ";
	}
	


}
