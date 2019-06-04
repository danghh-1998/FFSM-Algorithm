package model;

import java.util.ArrayList;


public class Node {
    private int nodeID;
    private String label;
    private ArrayList<Edge> neighbors;

    public String getLabel() {
        return label;
    }

    public ArrayList<Edge> getNeighbors() {
        return this.neighbors;
    }


    public Node(int nodeID, String label) {
        this.label = label;
        this.nodeID = nodeID;
        this.neighbors = new ArrayList<>();
    }

    public int getNodeID() {
        return nodeID;
    }
}
