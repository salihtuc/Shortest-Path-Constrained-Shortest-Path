
/*
 * Class for holding edges for DirectedGraph.java
 * 
 * Author : Salih Tuc
 * Creation Date : 02.05.2016
 * 
 * (Updated) : 08.05.2016
 * 
 * 
 * */

public class DirectedGraphsEdge {

	private String fFrom, fTo;
	private int fWeight;

	public DirectedGraphsEdge(String aFrom, String aTo, int aWeight) {
		this.fFrom = aFrom;
		this.fTo = aTo;
		this.fWeight = aWeight;
	}

	public String getFrom() {
		return fFrom;
	}

	public String getTo() {
		return fTo;
	}

	public int getWeight() {
		return fWeight;
	}

	@Override
	public String toString() {
		return "WeighedDigraphEdge [from=" + fFrom + ", to=" + fTo + "]";
	}

}