/**
 * Project 5 - Dijkstra's Algorithm
 * The following program constructs a graph from a data file 
 * and returns the shortest path from one airport to another.
 * @author Anthony Jaramillo
 * Finilized on 4/29/2022
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Dijkstra {
	// Static graph so dijkstra algorithm has access without passing as an argument
	public static ArrayList<Vertex> graph = new ArrayList<Vertex>();
	
	public static void main(String[] args) {
		File file = new File("airports.txt");
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Parse data to graph
		String[] verticeList;
		while(input.hasNext()) {
			verticeList = input.nextLine().split(" ");
			Vertex vertex = new Vertex();
			vertex.name = verticeList[0];
			vertex.weight = 0;
			vertex.known = false;
			graph.add(vertex);
			
			// Add edges to adj list
			for(int i = 2; i < verticeList.length; i += 3) {
				Vertex adjVertex = new Vertex();
				adjVertex.name = verticeList[i];
				adjVertex.weight = Integer.parseInt(verticeList[i+1]);
				adjVertex.known = false;
				vertex.adj.add(adjVertex);
			}
		}
		
		input.close();
		input = new Scanner(System.in);
		String s = "";
		
		do {
			// Prompt user
			System.out.print("Enter departure airport:  ");
			String departureAirport = input.nextLine();
			System.out.print("Enter arrival airport:    ");
			String arrivalAirport = input.nextLine();
			System.out.println();
			
			Vertex sourceVertex = null;
			Vertex destinationVertex = null;
			
			// Traverse graph and find airports
			for(Vertex v : graph) {
				if(v.name.equalsIgnoreCase(departureAirport)) {
					sourceVertex = v;
				}
				else if(v.name.equalsIgnoreCase(arrivalAirport)) {
					destinationVertex = v;
				}
			}
			
			dijkstra(sourceVertex);
			
			// Display data
			System.out.println("Price:       " + destinationVertex.weight);
			System.out.println("Connections: " + getConnections(destinationVertex));
			System.out.print("Route:       ");
			printPath(destinationVertex);
			
			System.out.print("\n\nCheck another route (Y/N)? ");
			s = input.nextLine();
		} while(s.equalsIgnoreCase("Y"));
		input.close();
	}
	 
	/*
	 * Updates graph to find the shortest distance from source vertex S 
	 * to every other vertex reachable from S.
	 */
	public static void dijkstra(Vertex s) {
		// Initialize graph vertices
		HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
		int i = 0;
		for(Vertex v : graph) {
			v.weight = 0;
			v.known = false;
			v.path = null;
			hashmap.put(v.name, i);	// preserving index
			i++;
		}
		
		// Start Dijkstra at vertex S
		s.known = true;
		BinaryHeap<Vertex> heap = new BinaryHeap<Vertex>(); // Helps find lowest weighted vertex from source vertex
		
		// Initialize adj list of vertex S
		for(Vertex v : s.adj) {
			heap.insert(v);		// First time seeing adj vertices so add to heap
			int cvw = v.weight;
			int index = hashmap.get(v.name);
			v = graph.get(index);	// To edit indentical vertex in graph
			v.weight = cvw;		// Preserving weight
			v.path = s;			// Preserving path
		}
		
		while(heap.isEmpty() == false) {
			// Visit lowest weighted vertex from source vertex found thus far
			Vertex v = heap.deleteMin();	
			v = graph.get(hashmap.get(v.name));
			v.known = true;
			
			// Traverse through vertex's adjacency list
			for(Vertex w : v.adj) {
				int cvw = w.weight;
				w = graph.get(hashmap.get(w.name));
				
				if(w.known == false) {	// Vertex has not been visited yet
					if(v.weight + cvw < w.weight) {	// If traveling from source vertex to this vertex is bigger than what was previously saved
						// Then update weight and path from the current vertex
						w.weight = v.weight + cvw;
						w.path = v;
					}
					else if(w.weight == 0) {	// If this is the first encounter with the vertex
						// Then update weight and path from the current vertex
						w.weight = v.weight + cvw;
						heap.insert(w);			// First time seeing vertex so add to heap
						w.path = v;
					}
				}
			}
		}
	}
	
	/*
	 * Calculate number of stops from departure aiport to arrival airport
	 */
	public static int getConnections(Vertex v) {
		if(v.path != null && v.path.path != null) {
			return 1 + getConnections(v.path);
		}
		return 0;
	}
	
	/*
	 * Print shortest path to v after dijkstra has run.
	 * Assume that the path exists.
	 */
	public static void printPath(Vertex v) {
		if(v.path != null) {
			printPath(v.path);
			System.out.print(" -> ");
		}
		System.out.print(v);
	}
}
