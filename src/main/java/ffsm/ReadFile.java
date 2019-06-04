package ffsm;

import model.Edge;
import model.Graph;
import model.GraphDatabase;
import model.Node;

import java.io.*;

class ReadFile {

    static GraphDatabase readFile(String fileNumber) {
        GraphDatabase database = new GraphDatabase();
        Graph graph = new Graph();
        Node node;
        Edge edge;
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/resources/testFile/test" + fileNumber + ".dat"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == '#') {
                    if (graph.getNodes().size() != 0) {
                        database.getDatabase().add(graph);
                    }
                    graph = new Graph(line.substring(1));
                    continue;
                }
                String[] splits = line.split(" ");
                if (splits.length == 2) {
                    node = new Node(Integer.parseInt(splits[0]), splits[1]);
                    graph.addNode(node);
                    continue;
                }
                Node nodeA = graph.searchNode(Integer.parseInt(splits[0]));
                Node nodeB = graph.searchNode(Integer.parseInt(splits[1]));
                edge = new Edge(nodeA, nodeB, splits[2]);
                nodeA.getNeighbors().add(edge);
                edge = new Edge(nodeB, nodeA, splits[2]);
                nodeB.getNeighbors().add(edge);
            }
            if (graph.getNodes().size() != 0) {
                database.getDatabase().add(graph);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return database;
    }


}
