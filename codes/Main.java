/*
 * 2015-2016 Spring 
 * BBM204 - Software Lab. II - Assignment 5
 * Finding Shortest Path & Constrained Shortest Path
 * 
 * Author : Salih Tuc
 * Creation Date : 02.05.2016
 * 
 * (Updated) : 13.05.2016
 * 
 * */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {

	/* Data structures which using for Constrained Shortest Path */
	// Holds mustpass nodes
	public static ArrayList<String> fMustPassList = new ArrayList<String>();

	// Holds the path between two vertices.
	static HashMap<Pair<String, String>, ArrayList<String>> fPathsForPairs = new HashMap<Pair<String, String>, ArrayList<String>>();

	// Holds weights for vertices.
	static HashMap<String, Integer> fVertexWeights = new HashMap<String, Integer>();
	/**/

	/* Other Variables */
	static int fLineCounter = 0; // Holds number of lines in file. Using for
									// eliminate first line.
	static String fStartNode = ""; // Holds start node for shortest paths.
	static String fEndNode = ""; // Holds final node for shortest paths.

	static PrintWriter fWriter = null;

	public static void main(String args[]) {
		DirectedGraph zGraph = new DirectedGraph();

		try {
			fWriter = new PrintWriter(new File(args[1]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fWriter.println("b21228778\n");
		operate(args[0], zGraph, fWriter);

		fWriter.close();

	}

	// Read input file, manage operations for application.
	public static void operate(String aPath, DirectedGraph aGraph, PrintWriter aWriter) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(aPath)))) {
			for (String zLine; (zLine = br.readLine()) != null;) {
				zLine = zLine.trim();

				if (fLineCounter == 0) {
					String zStartEnd[] = zLine.split(",");
					fStartNode = zStartEnd[0].split(":")[1];
					fEndNode = zStartEnd[1].split(":")[1];
					fLineCounter++;
				} else {

					// System.out.println(zLine);

					String zTokens[] = zLine.split(" ");

					if (zLine.endsWith("mustpass")) {
						fMustPassList.add(zTokens[0].substring(0, zTokens[0].length() - 1));
					}
					String zFrom = zTokens[0].substring(0, zTokens[0].length() - 1);
					addToGraph(zFrom, zTokens[1].split(","), aGraph);
				}

			}

			/* First Part // Regular Shortest Path */
			Dijkstra zDijkstraObject = new Dijkstra(aGraph);
			ArrayList<String> zPrintList = zDijkstraObject.shortestPath(fStartNode, fEndNode);

			aWriter.print("Shortest Path:\tDistance=" + zDijkstraObject.fTotalWeight + ".000000\tPath=(");
			printListNormally(zPrintList, aWriter);
			System.out.println(" ------------> " + zDijkstraObject.fTotalWeight);
			aWriter.print(")\n\n");

			/* Second Part // Constrained Shortest Path */
			DirectedGraph zSubGraph = createMustpassGraph(fStartNode, fEndNode, fMustPassList, zDijkstraObject);

			LinkedList<String> zVisited = new LinkedList<String>();
			zVisited.add(fStartNode);
			depthFirst(zSubGraph, zVisited, fEndNode, aWriter);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Add edges to the graph. It called for every line in input file.
	public static void addToGraph(String aFrom, String[] aEdgeLine, DirectedGraph aGraph) {
		for (String zEdge : aEdgeLine) {
			String zAttributes[] = zEdge.split("\\(");

			String zTo = zAttributes[0];
			int zWeight = Integer.parseInt(zAttributes[1].substring(0, zAttributes[1].length() - 1));

			DirectedGraphsEdge zWeighedEdge = new DirectedGraphsEdge(aFrom, zTo, zWeight);
			aGraph.addEdge(zWeighedEdge);

		}
	}

	// Create subgraph which include start node, mustpass nodes and end node
	public static DirectedGraph createMustpassGraph(String aStartNode, String aEndNode, ArrayList<String> aMustPassList,
			Dijkstra aFind) {

		DirectedGraph zSubGraph = new DirectedGraph();

		for (String zVertex : aMustPassList) {
			Pair<String, String> zPair = new Pair<String, String>(aStartNode, zVertex);
			ArrayList<String> zPath = aFind.shortestPath(aStartNode, zVertex);
			fPathsForPairs.put(zPair, zPath);
			DirectedGraphsEdge zEdge = new DirectedGraphsEdge(aStartNode, zVertex, aFind.fTotalWeight);
			zSubGraph.addEdge(zEdge);

			Pair<String, String> zPair2 = new Pair<String, String>(zVertex, aEndNode);
			ArrayList<String> zPath2 = aFind.shortestPath(zVertex, aEndNode);
			fPathsForPairs.put(zPair2, zPath2);
			DirectedGraphsEdge zEdge2 = new DirectedGraphsEdge(zVertex, aEndNode, aFind.fTotalWeight);
			zSubGraph.addEdge(zEdge2);
		}

		int i = 0;
		while (i + 1 < aMustPassList.size()) {
			Pair<String, String> zPair = new Pair<String, String>(aMustPassList.get(i), aMustPassList.get(i + 1));
			ArrayList<String> zPath = aFind.shortestPath(aMustPassList.get(i), aMustPassList.get(i + 1));
			fPathsForPairs.put(zPair, zPath);
			DirectedGraphsEdge zEdge = new DirectedGraphsEdge(aMustPassList.get(i), aMustPassList.get(i + 1),
					aFind.fTotalWeight);
			zSubGraph.addEdge(zEdge);
			i++;
		}

		return zSubGraph;
	}

	// Depth first search for finding all paths. It used for finding the path
	// with all elements.
	public static void depthFirst(DirectedGraph aGraph, LinkedList<String> aVisited, String aEndNode,
			PrintWriter aWriter) {
		ArrayList<DirectedGraphsEdge> zEdges = aGraph.adjacentEdgesOf(aVisited.getLast());
		LinkedList<String> zNodes = new LinkedList<String>();

		for (DirectedGraphsEdge edge : zEdges) {
			zNodes.add(edge.getTo());
			fVertexWeights.put(edge.getTo(), edge.getWeight());
		}

		// examine adjacent nodes
		for (String zNode : zNodes) {
			if (aVisited.contains(zNode)) {
				continue;
			}
			if (zNode.equals(aEndNode)) {
				aVisited.add(zNode);

				printOrganizedPath(aVisited, aWriter);

				aVisited.removeLast();
				break;
			}
		}
		for (String zNode : zNodes) {
			if (aVisited.contains(zNode) || zNode.equals(aEndNode)) {
				continue;
			}
			aVisited.addLast(zNode);

			depthFirst(aGraph, aVisited, aEndNode, aWriter);
			aVisited.removeLast();
		}

	}

	// Prints Constrained Shortest Path
	private static void printOrganizedPath(LinkedList<String> aVisited, PrintWriter aWriter) {

		if (aVisited.containsAll(fMustPassList)) {
			int i = 0;
			aWriter.print("Constrained Shortest Path:\tDistance=" + returnWeight() + ".000000\tPath=(");
			while (i + 1 < aVisited.size()) {

				if (i + 1 == aVisited.size() - 1) {
					printListNormally(
							fPathsForPairs.get(new Pair<String, String>(aVisited.get(i), aVisited.get(i + 1))),
							aWriter);
				} else {
					printListWithoutLastElement(
							fPathsForPairs.get(new Pair<String, String>(aVisited.get(i), aVisited.get(i + 1))),
							aWriter);
				}

				i++;
			}
			aWriter.print(")\n");

		}

	}

	// Prints given list without last element. (And also add mustpass for
	// specific nodes)
	private static void printListWithoutLastElement(ArrayList<String> aList, PrintWriter aWriter) {
		aList.remove(aList.size() - 1);

		for (String s : aList) {
			System.out.print(s);
			aWriter.print(s);
			if (fMustPassList.contains(s)) {
				System.out.print("(mustpass)");
				aWriter.print("(mustpass)");
			}
			System.out.print(" ");
			aWriter.print(" ");
		}
	}

	// Prints given list normally. Do not add whitespace after last element.
	private static void printListNormally(ArrayList<String> aList, PrintWriter aWriter) {
		for (int i = 0; i < aList.size(); i++) {
			System.out.print(aList.get(i));
			aWriter.print(aList.get(i));
			if (i + 1 != aList.size()) {
				System.out.print(" ");
				aWriter.print(" ");
			}
		}
	}

	// Return constrained shortest path's weight.
	private static int returnWeight() {
		Collection<Integer> values = fVertexWeights.values();

		int zSum = 0;

		for (int zValue : values) {
			zSum += zValue;
		}

		return zSum;
	}
}
