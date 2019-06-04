package model;

import java.util.ArrayList;

public class GraphDatabase {
    private ArrayList<Graph> database;

    public GraphDatabase() {
        database = new ArrayList<>();
    }

    public ArrayList<Graph> getDatabase() {
        return database;
    }

}
