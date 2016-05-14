
/*
 * Class for Directed Graph with Weights.
 * 
 * Author : Salih Tuc
 * Creation Date : 02.05.2016
 * 
 * (Updated) : 07.05.2016
 * 
 * 
 * */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DirectedGraph {
	HashMap<String, ArrayList<DirectedGraphsEdge>> fAdjacencyList = new HashMap<String, ArrayList<DirectedGraphsEdge>>();

	// Empty Constructor
	public DirectedGraph() {

	}

	// Adjacent edges of specific vertex
	public ArrayList<DirectedGraphsEdge> adjacentEdgesOf(String aVertex) {
		return fAdjacencyList.get(aVertex);
	}

	// All edges in the graph
	public ArrayList<DirectedGraphsEdge> allEdges() {
		ArrayList<DirectedGraphsEdge> zReturnList = new ArrayList<DirectedGraphsEdge>();

		for (String zFrom : fAdjacencyList.keySet()) {
			ArrayList<DirectedGraphsEdge> currentEdges = fAdjacencyList.get(zFrom);
			for (DirectedGraphsEdge e : currentEdges) {
				zReturnList.add(e);
			}
		}
		return zReturnList;
	}

	// All vertices in the graph.
	public Iterable<String> vertices() {
		HashSet<String> zEdgeSet = new HashSet<String>();
		for (DirectedGraphsEdge edge : allEdges()) {
			zEdgeSet.add(edge.getFrom());
			zEdgeSet.add(edge.getTo());
		}

		return zEdgeSet;
	}

	public int size() {
		return fAdjacencyList.size();
	}

	// Add new edge to the graph.
	public void addEdge(DirectedGraphsEdge aEdge) {
		// create empty connection set
		if (!fAdjacencyList.containsKey(aEdge.getFrom()))
			fAdjacencyList.put(aEdge.getFrom(), new ArrayList<DirectedGraphsEdge>());

		ArrayList<DirectedGraphsEdge> zCurrentEdges = fAdjacencyList.get(aEdge.getFrom());

		boolean edgeExists = false; // Using for edge is in graph or not.
		for (int i = 0; i < zCurrentEdges.size(); i++) {
			if (zCurrentEdges.get(i).getTo() == aEdge.getTo()) {
				zCurrentEdges.set(i, aEdge);
				edgeExists = true;
				break;
			}
		}

		if (!edgeExists)
			zCurrentEdges.add(aEdge);

		fAdjacencyList.put(aEdge.getFrom(), zCurrentEdges);
	}

}