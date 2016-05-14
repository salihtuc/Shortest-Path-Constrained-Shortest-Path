
/*
 * Class for calculating shortest path between two nodes using Dijkstra's algorithm.
 * 
 * Author : Salih Tuc
 * Creation Date : 18.04.2014
 * 
 * (Updated): 09.05.2016
 * 
 * 
 * */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class Dijkstra {
	private int fGraphSize;
	private HashMap<String, Integer> fWeightForVertex; // store weights for each vertex
	private HashMap<String, String> fPreviousNode; // store previous vertex
	private PriorityQueue<String> fPriorityQueue; // store vertices that need to be visited
	private DirectedGraph fGraph; // graph object
	int fTotalWeight = 0;

	public Dijkstra(DirectedGraph aGraph) {
		this.fGraph = aGraph;
		fGraphSize = aGraph.size();
	}

	// Find the shortest path between source and destination
	public ArrayList<String> shortestPath(String aSourceVertex, String aDestinationVertex) {
		fPreviousNode = new HashMap<String, String>();
		fWeightForVertex = new HashMap<String, Integer>();
		fPriorityQueue = new PriorityQueue<String>(fGraphSize, queueComparator);

		// First of all, all vertices have highest weight
		for (String vertex : fGraph.vertices())
			fWeightForVertex.put(vertex, Integer.MAX_VALUE);

		fPreviousNode.put(aSourceVertex, "-"); // - means no previous vertex
		fWeightForVertex.put(aSourceVertex, 0); // weight to has to be 0
		fPriorityQueue.add(aSourceVertex); // enqueue first vertex

		while (fPriorityQueue.size() > 0) {
			String zCurrentNode = fPriorityQueue.poll();
			ArrayList<DirectedGraphsEdge> zNeighbors = fGraph.adjacentEdgesOf(zCurrentNode);

			if (zNeighbors == null)
				continue;

			for (DirectedGraphsEdge neighbor : zNeighbors) {
				String zNextVertex = neighbor.getTo();

				int newDistance = fWeightForVertex.get(zCurrentNode) + neighbor.getWeight();
				if (fWeightForVertex.get(zNextVertex) == Integer.MAX_VALUE) {
					fPreviousNode.put(zNextVertex, zCurrentNode);
					fWeightForVertex.put(zNextVertex, newDistance);
					fPriorityQueue.add(zNextVertex);
				} else {
					if (fWeightForVertex.get(zNextVertex) > newDistance) {
						fPreviousNode.put(zNextVertex, zCurrentNode);
						fWeightForVertex.put(zNextVertex, newDistance);
					}
				}
			}
		}

		// Path from A to B will be stored here
		ArrayList<String> zFinalPath = new ArrayList<String>();

		Stack<String> zTempPath = new Stack<String>();
		Stack<Integer> zWeights = new Stack<Integer>();

		zTempPath.push(aDestinationVertex);

		String zTempVertex = aDestinationVertex;
		while (fPreviousNode.containsKey(zTempVertex) && !fPreviousNode.get(zTempVertex).equals("-")) {
			zWeights.push(fWeightForVertex.get(zTempVertex));
			zTempVertex = fPreviousNode.get(zTempVertex);
			zTempPath.push(zTempVertex);
		}

		// Put node in ArrayList in reversed order
		while (zTempPath.size() > 0) {
			zFinalPath.add(zTempPath.pop());
		}

		fTotalWeight = 0;
		for (Integer a : zWeights) {
			if (fTotalWeight == 0)
				fTotalWeight += a;
		}

		return zFinalPath;
	}

	// Comparator for priority queue
	public Comparator<String> queueComparator = new Comparator<String>() {
		public int compare(String a, String b) {
			if (fWeightForVertex.get(a) > fWeightForVertex.get(b)) {
				return 1;
			} else if (fWeightForVertex.get(a) < fWeightForVertex.get(b)) {
				return -1;
			}
			return 0;
		}
	};
}