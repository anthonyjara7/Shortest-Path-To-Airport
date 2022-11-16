import java.util.ArrayList;

public class Vertex implements Comparable<Vertex>{
	public ArrayList<Vertex> adj;	// Adjacency list
	public boolean known;
	public int weight;
	public Vertex path;
	public String name;
	public int index;
	
	/*
	 * Initialize vertex
	 */
	public Vertex() {
		adj = new ArrayList<Vertex>();
	}

	/*
	 * Compares vertices by weight
	 * @return int dictating if >, <, or =
	 */
	@Override
	public int compareTo(Vertex v) {
		if(weight > v.weight) {
			return 1;
		}
		else if(weight < v.weight) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
	public String toString() {
		return name;
	}
	
	/*
	 * Compares equality of two vertices by name
	 * @return true if same name, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		Vertex v = (Vertex) o;
		if(name.equals(v.name)) {
			return true;
		}
		else {
			return false;
		}
	}
}
