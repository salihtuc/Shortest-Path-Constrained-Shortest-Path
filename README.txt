Hacettepe University, Computer Engineering Department
2015-2016 Spring Semestre
BBM 204 - Software Lab II - Assignment 5

Author : Salih Tuc
School Number : 21228778

This project is using for calculating shortest path and constrained shortest path between two nodes.

Project includes 5 java class:

---
Main.java -> Takes two main arguments; an input file and output file. Which are explained above.
		  -> It's the manager class for project. 
		  		-> It reads from input file and writes to output file. 
		  		-> Calculates regular shortest path using Dijkstra.java.
		  		-> Calculates constrained shortest path using specific functions inside it. (DFS-based)
---
Dijkstra.java -> Find regular shortest path between two nodes. User should create an object for this class and then should call shortestPath() method.
---
DirectedGraph.java -> Directed Graph with Weights. User should create object for this class. Functions' explanations are in the class file.
---
DirectedGraphsEdge.java -> Edge for the graph. User should create an object with source/destination edges and weight.
---
Pair.java -> Holds two object together, regardless classes' types.
---

** Files **

---
input file -> Includes start node, end node and edges with weights between nodes.

Sample input file:

S:1,D:2
1. 1(6),2(28),3(10)
2. 1(20),2(1) mustpass
3. 2(9)

--
output file -> Includes shortest paths and their weights together.
--

***NOTE: Some outputs may shown in console; but the output file is the base and real and true output for this assignment.
