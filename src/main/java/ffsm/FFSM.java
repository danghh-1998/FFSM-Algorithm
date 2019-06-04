package ffsm;

import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class FFSM {
    private static int count = 0;
    private static FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter(new File("src/main/resources/result/result.dat"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean equalsMatrix(ArrayList<ArrayList<String>> firstMatrix, ArrayList<ArrayList<String>> secondMatrix) {
        if (firstMatrix.size() != secondMatrix.size()) {
            return false;
        }
        for (int i = 0; i < firstMatrix.size(); i++) {
            for (int j = 0; j < secondMatrix.size(); j++) {
                if (!firstMatrix.get(i).get(j).equals(secondMatrix.get(i).get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static ArrayList<AdjMatrix> setUp(GraphDatabase database) {
        ArrayList<AdjMatrix> result = new ArrayList<>();
        for (Graph graph : database.getDatabase()) {
            for (Node node : graph.getNodes()) {
                ArrayList<ArrayList<String>> newMatrix = new ArrayList<>();
                newMatrix.add(new ArrayList<>());
                newMatrix.get(0).add(node.getLabel());
                Embedding newEmbedding = new Embedding(graph, node.getNodeID(), null);
                int position = -1;
                for (int i = 0; i < result.size(); i++) {
                    if (FFSM.equalsMatrix(newMatrix, result.get(i).getMatrix())) {
                        position = i;
                        break;
                    }
                }
                if (position == -1) {
                    ArrayList<Embedding> embeddings = new ArrayList<>();
                    embeddings.add(newEmbedding);
                    AdjMatrix newAdjMatrix = new AdjMatrix(newMatrix, embeddings);
                    result.add(newAdjMatrix);
                } else {
                    result.get(position).getEmbeddings().add(newEmbedding);
                }
            }
        }
        return result;
    }

    private static void writeFile(AdjMatrix adjMatrix) {
        try {
            for (int i = 0; i < adjMatrix.getMatrix().size(); i++) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j <= i; j++) {
                    stringBuilder.append(adjMatrix.getMatrix().get(i).get(j)).append(" ");
                }
                stringBuilder.append("\n");
                fileWriter.write(stringBuilder.toString());
            }
            fileWriter.write("Support: " + adjMatrix.getFrequency() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ffsmExplore(ArrayList<AdjMatrix> candidates, int minSup) {
        for (AdjMatrix candidate : candidates) {
            if (candidate.isCAM()) {
                if (candidate.getFrequency() >= minSup && candidate.getMatrix().size() > 1){
                    FFSM.writeFile(candidate);
                    count++;
                }
                ArrayList<AdjMatrix> newCandidate = new ArrayList<>();
                for (AdjMatrix otherCandidate : candidates) {
                    ArrayList<AdjMatrix> joinList = candidate.join(otherCandidate);
                    if (joinList != null) {
                        newCandidate.addAll(joinList);
                    }
                }
                ArrayList<AdjMatrix> extensionList = candidate.extension();
                if (extensionList != null) {
                    newCandidate.addAll(extensionList);
                }
                newCandidate.removeIf(adjMatrix -> (!adjMatrix.isSubCAM() || adjMatrix.getFrequency() < minSup));
                FFSM.ffsmExplore(newCandidate, minSup);
            }
        }
    }

    static void ffsmStartSearch(GraphDatabase database, int minSup) {
        ArrayList<AdjMatrix> candidates = FFSM.setUp(database);
        long start = System.currentTimeMillis();
        FFSM.ffsmExplore(candidates, minSup);
        long end = System.currentTimeMillis();
        System.out.println("Thoi gian thuc hien thuat toan: " + (end - start) + " ms");
        try {
            fileWriter.write("Co tong cong: " + count + " do thi thoa man");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
