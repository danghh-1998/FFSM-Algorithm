package model;


public class Edge {
    private Node nodeA;
    private Node nodeB;
    private String label;

    Node getNodeA() {
        return nodeA;
    }

    Node getNodeB() {
        return nodeB;
    }


    String getLabel() {
        return label;
    }


    public Edge(Node nodeA, Node nodeB, String label) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.label = label;
    }
}
