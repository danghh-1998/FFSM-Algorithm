package model;


import java.util.ArrayList;


public class Embedding {
    private Graph graph;
    private int lastNode;
    private Embedding parent;


    public Embedding(Graph graph, int lastNode, Embedding parent) {
        this.graph = graph;
        this.lastNode = lastNode;
        this.parent = parent;
    }

    Graph getGraph() {
        return graph;
    }

    int getLastNode() {
        return lastNode;
    }

    Embedding getParent() {
        return parent;
    }

    ArrayList<Integer> getNodeList() {

        Embedding emb = new Embedding(this.getGraph(), this.getLastNode(), this.getParent());
        ArrayList<Integer> nodeList = new ArrayList<>();
        while (emb != null) {
            nodeList.add(emb.getLastNode());
            emb = emb.getParent();
        }

        return nodeList;
    }
}
