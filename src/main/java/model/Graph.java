package model;

import java.util.ArrayList;


public class Graph {
    private static int staticID = 0;
    private int id = ++staticID;
    private String graphName;
    private ArrayList<Node> nodes;

    public Graph() {
        this.nodes = new ArrayList<>();
    }

    public Graph(String graphName) {
        this.graphName = graphName;
        this.nodes = new ArrayList<>();
    }


    public ArrayList<Node> getNodes() {
        return this.nodes;
    }

    int getID() {
        return this.id;
    }

    private boolean isIdentity(int id) {
        for (Node node : nodes) {
            if (node.getNodeID() == id) {
                return false;
            }
        }
        return true;
    }


    public void addNode(Node node) {
        if (isIdentity(node.getNodeID())) {
            nodes.add(node);
        } else {
            System.out.println("Duplicate node");
        }

    }


    public Node searchNode(int nodeID) {
        for (Node node : nodes) {
            if (node.getNodeID() == nodeID) {
                return node;
            }
        }
        return null;
    }

    Node getOrtherNode(Edge edge, Node node) {
        if (edge.getNodeA().equals(node)) return edge.getNodeB();
        else return edge.getNodeA();
    }

}
