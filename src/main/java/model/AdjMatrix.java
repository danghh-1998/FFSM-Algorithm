package model;

import java.util.ArrayList;
import java.util.HashMap;

public class AdjMatrix {
    private ArrayList<ArrayList<String>> matrix;
    private ArrayList<Embedding> embeddings;

    public AdjMatrix() {

    }

    public AdjMatrix(ArrayList<ArrayList<String>> matrix, ArrayList<Embedding> embeddings) {
        this.matrix = matrix;
        this.embeddings = embeddings;
    }

    public ArrayList<ArrayList<String>> getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<String>> matrix) {
        this.matrix = matrix;
    }

    public ArrayList<Embedding> getEmbeddings() {
        return embeddings;
    }

    private void setEmbeddings(ArrayList<Embedding> embeddings) {
        this.embeddings = embeddings;
    }

    private static String generateCode(ArrayList<ArrayList<String>> matrix) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j <= i; j++) {
                stringBuilder.append(matrix.get(i).get(j));
            }
        }
        return stringBuilder.toString();
    }


    private ArrayList<ArrayList<String>> copyMatrix() {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            result.add(new ArrayList<>());
            for (int j = 0; j < matrix.size(); j++) {
                result.get(i).add(matrix.get(i).get(j));
            }
        }
        return result;
    }

    private static int compare(String firstString, String secondString) {
        HashMap<Character, Integer> characterWeight = new HashMap<>();
        int weight = 0;
        for (int i = 48; i <= 57; i++) {
            characterWeight.put((char) i, weight++);
        }
        for (int i = 122; i >= 97; i--) {
            characterWeight.put((char) i, weight++);
        }
        for (int i = 90; i >= 65; i--) {
            characterWeight.put((char) i, weight++);
        }
        int length = Math.min(firstString.length(), secondString.length());
        for (int i = 0; i < length; i++) {
            if (firstString.charAt(i) != secondString.charAt(i)) {
                return characterWeight.get(firstString.charAt(i)) - characterWeight.get(secondString.charAt(i));
            }
        }
        if (firstString.length() == secondString.length()) {
            return 0;
        }
        if (length == secondString.length()) {
            return 1;
        } else {
            return -1;
        }
    }

    private static void swapRow(ArrayList<ArrayList<String>> matrix, int firstRow, int secondRow) {
        ArrayList<String> row = matrix.get(firstRow);
        matrix.set(firstRow, matrix.get(secondRow));
        matrix.set(secondRow, row);
    }

    private static void swapCol(ArrayList<ArrayList<String>> matrix, int firstCol, int secondCol) {
        for (ArrayList<String> row : matrix) {
            String item = row.get(firstCol);
            row.set(firstCol, row.get(secondCol));
            row.set(secondCol, item);
        }
    }
    private static boolean checkNodeList(String string){
        HashMap<Character, Integer> characterWeight = new HashMap<>();
        int weight = 0;
        for (int i = 48; i <= 57; i++) {
            characterWeight.put((char) i, weight++);
        }
        for (int i = 122; i >= 97; i--) {
            characterWeight.put((char) i, weight++);
        }
        for (int i = 90; i >= 65; i--) {
            characterWeight.put((char) i, weight++);
        }
        for (int i = 0; i < string.length() - 1; i++){
            if (characterWeight.get(string.charAt(i)) - characterWeight.get(string.charAt(i + 1)) < 0){
                return false;
            }
        }
        return true;
    }
    public boolean isCAM() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.matrix.size(); i++){
            stringBuilder.append(this.matrix.get(i).get(i));
        }
        if (!AdjMatrix.checkNodeList(stringBuilder.toString())) {
            return false;
        }
        ArrayList<ArrayList<String>> test = this.copyMatrix();
        String code = AdjMatrix.generateCode(test);
        for (int i = 0; i < test.size() - 1; i++) {
            for (int j = i + 1; j < test.size(); j++) {
                AdjMatrix.swapRow(test, i, j);
                AdjMatrix.swapCol(test, i, j);
                if (AdjMatrix.compare(AdjMatrix.generateCode(test), code) > 0) {
                    return false;
                }
                test = this.copyMatrix();
            }
        }
        return true;
    }

    private boolean isInner() {
        int count = 0;
        for (int i = 0; i < matrix.size() - 1; i++) {
            if (!matrix.get(matrix.size() - 1).get(i).equals("0")) {
                count++;
            }
        }
        return count > 1;
    }


    private int getLastEdge() {
        for (int i = this.matrix.size() - 2; i >= 0; i--) {
            if (!matrix.get(matrix.size() - 1).get(i).equals("0")) {
                return i;
            }
        }
        return 0;
    }

    private AdjMatrix joinCase1(AdjMatrix otherMatrix) {
        int lastEdgeThisMatrix = this.getLastEdge();
        int lastEdgeOtherMatrix = otherMatrix.getLastEdge();
        if (lastEdgeOtherMatrix == lastEdgeThisMatrix) {
            return null;
        }
        AdjMatrix adjMatrix = new AdjMatrix();
        ArrayList<ArrayList<String>> newMatrix = this.copyMatrix();
        int size = this.matrix.size();
        newMatrix.get(matrix.size() - 1).set(lastEdgeOtherMatrix, otherMatrix.getMatrix().get(size - 1).get(lastEdgeOtherMatrix));
        newMatrix.get(lastEdgeOtherMatrix).set(size - 1, otherMatrix.getMatrix().get(size - 1).get(lastEdgeOtherMatrix));
        ArrayList<Embedding> newEmbeddings = new ArrayList<>();
        for (Embedding emb : this.embeddings) {
            for (Embedding otherEmb : otherMatrix.getEmbeddings()) {
                if (emb.getParent().equals(otherEmb.getParent())) {
                    if (emb.getLastNode() == otherEmb.getLastNode()) {
                        Embedding embedding = new Embedding(emb.getGraph(), emb.getLastNode(), emb);
                        newEmbeddings.add(embedding);
                    }
                }
            }
        }
        adjMatrix.setMatrix(newMatrix);
        adjMatrix.setEmbeddings(newEmbeddings);
        return adjMatrix;
    }

    private AdjMatrix joinCase2(AdjMatrix otherMatrix) {
        AdjMatrix adjMatrix = new AdjMatrix();
        ArrayList<ArrayList<String>> newMatrix = otherMatrix.copyMatrix();
        for (int i = 0; i < this.matrix.size(); i++) {
            for (int j = 0; j < this.matrix.size(); j++) {
                newMatrix.get(i).set(j, this.matrix.get(i).get(j));
            }
        }
        ArrayList<Embedding> newEmbeddings = new ArrayList<>();
        for (Embedding emb : this.embeddings) {
            for (Embedding otherEmb : otherMatrix.getEmbeddings()) {
                if (emb.getParent().equals(otherEmb.getParent())) {
                    Embedding embedding = new Embedding(otherEmb.getGraph(), otherEmb.getLastNode(), emb);
                    newEmbeddings.add(embedding);
                }
            }
        }
        adjMatrix.setMatrix(newMatrix);
        adjMatrix.setEmbeddings(newEmbeddings);
        return adjMatrix;
    }

    private AdjMatrix joinCase3a(AdjMatrix otherMatrix) {
        ArrayList<ArrayList<String>> newMatrix = this.copyMatrix();
        int lastEdgeThisMatrix = this.getLastEdge();
        int lastEdgeOtherMatrix = otherMatrix.getLastEdge();
        if (lastEdgeOtherMatrix == lastEdgeThisMatrix) {
            return null;
        }
        AdjMatrix adjMatrix = new AdjMatrix();
        newMatrix.get(matrix.size() - 1).set(lastEdgeOtherMatrix,
                otherMatrix.getMatrix().get(matrix.size() - 1).get(lastEdgeOtherMatrix));
        newMatrix.get(lastEdgeOtherMatrix).set(matrix.size() - 1,
                otherMatrix.getMatrix().get(matrix.size() - 1).get(lastEdgeOtherMatrix));
        ArrayList<Embedding> newEmbeddings = new ArrayList<>();
        for (Embedding emb : this.embeddings) {
            for (Embedding otherEmb : otherMatrix.getEmbeddings()) {
                if (emb.getParent().equals(otherEmb.getParent())) {
                    if (emb.getLastNode() == otherEmb.getLastNode()) {
                        Embedding embedding = new Embedding(emb.getGraph(), emb.getLastNode(), emb);
                        newEmbeddings.add(embedding);
                    }
                }
            }
        }
        adjMatrix.setMatrix(newMatrix);
        adjMatrix.setEmbeddings(newEmbeddings);
        return adjMatrix;
    }

    private AdjMatrix joinCase3b(AdjMatrix otherMatrix) {
        AdjMatrix adjMatrix = new AdjMatrix();
        int size = this.matrix.size();
        ArrayList<ArrayList<String>> newMatrix = new ArrayList<>();
        for (int i = 0; i < size + 1; i++) {
            newMatrix.add(new ArrayList<>());
            for (int j = 0; j < size + 1; j++) {
                newMatrix.get(i).add("0");
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newMatrix.get(i).set(j, this.matrix.get(i).get(j));
            }
        }
        for (int i = 0; i < size - 1; i++) {
            newMatrix.get(size).set(i, otherMatrix.getMatrix().get(size - 1).get(i));
            newMatrix.get(i).set(size, otherMatrix.getMatrix().get(size - 1).get(i));
        }
        newMatrix.get(size).set(size, otherMatrix.getMatrix().get(size - 1).get(size - 1));
        ArrayList<Embedding> newEmbeddings = new ArrayList<>();
        for (Embedding emb : this.embeddings) {
            for (Embedding otherEmb : otherMatrix.getEmbeddings()) {
                if (emb.getParent() != null && otherEmb.getParent() != null) {
                    if (emb.getParent().equals(otherEmb.getParent())) {
                        if (emb.getLastNode() != otherEmb.getLastNode()) {
                            Embedding embedding = new Embedding(emb.getGraph(), otherEmb.getLastNode(), emb);
                            newEmbeddings.add(embedding);
                        }
                    }
                }
            }
        }
        adjMatrix.setMatrix(newMatrix);
        adjMatrix.setEmbeddings(newEmbeddings);
        return adjMatrix;
    }

    private ArrayList<ArrayList<String>> genMaxSubMatrix() {
        if (this.isInner()) {
            ArrayList<ArrayList<String>> subMatrix = this.copyMatrix();
            int lastEdge = this.getLastEdge();
            subMatrix.get(subMatrix.size() - 1).set(lastEdge, "0");
            subMatrix.get(lastEdge).set(subMatrix.size() - 1, "0");
            return subMatrix;
        } else {
            ArrayList<ArrayList<String>> subMatrix = new ArrayList<>();
            for (int i = 0; i < this.matrix.size() - 1; i++) {
                subMatrix.add(new ArrayList<>());
                for (int j = 0; j < this.matrix.size() - 1; j++) {
                    subMatrix.get(i).add(this.matrix.get(i).get(j));
                }
            }
            return subMatrix;
        }
    }

    public ArrayList<AdjMatrix> join(AdjMatrix otherMatrix) {
        if (this.matrix.size() == 1 && otherMatrix.getMatrix().size() == 1) {
            return null;
        }
        if (!AdjMatrix.generateCode(this.genMaxSubMatrix()).equals(AdjMatrix.generateCode(otherMatrix.genMaxSubMatrix()))) {
            return null;
        }
        ArrayList<AdjMatrix> adjMatrices = new ArrayList<>();
        boolean isInnerThisMatrix = this.isInner();
        boolean isInnerOtherMatrix = otherMatrix.isInner();
        String firstCode = AdjMatrix.generateCode(this.getMatrix());
        String secondCode = AdjMatrix.generateCode(otherMatrix.getMatrix());
        boolean canJoin = AdjMatrix.compare(firstCode, secondCode) >= 0;
        if (isInnerThisMatrix && isInnerOtherMatrix) {
            if (canJoin) {
                AdjMatrix newAdjMatrix = this.joinCase1(otherMatrix);
                if (newAdjMatrix != null) {
                    adjMatrices.add(newAdjMatrix);
                    return adjMatrices;
                } else {
                    return null;
                }
            }
        }
        if (isInnerThisMatrix && !isInnerOtherMatrix) {
            if (canJoin) {
                AdjMatrix newAdjMatrix = this.joinCase2(otherMatrix);
                if (newAdjMatrix != null) {
                    adjMatrices.add(newAdjMatrix);
                    return adjMatrices;
                } else {
                    return null;
                }
            }
        }
        if (!isInnerOtherMatrix && !isInnerThisMatrix) {
            int thisMatrixSize = this.getMatrix().size();
            int otherMatrixSize = otherMatrix.getMatrix().size();
            if (thisMatrixSize != otherMatrixSize) {
                return null;
            }
            if (this.getMatrix().get(thisMatrixSize - 1).get(thisMatrixSize - 1).equals(otherMatrix.getMatrix().get(otherMatrixSize - 1).get(otherMatrixSize - 1)) &&
                    !firstCode.equals(secondCode)) {
                if (canJoin) {
                    AdjMatrix newAdjMatrix = this.joinCase3a(otherMatrix);
                    if (newAdjMatrix != null) {
                        adjMatrices.add(newAdjMatrix);
                    }
                }
                AdjMatrix newAdjMatrix = this.joinCase3b(otherMatrix);
                if (newAdjMatrix != null) {
                    adjMatrices.add(newAdjMatrix);
                }
                if (adjMatrices.size() != 0) {
                    return adjMatrices;
                } else {
                    return null;
                }
            } else {
                AdjMatrix newAdjMatrix = this.joinCase3b(otherMatrix);
                if (newAdjMatrix != null) {
                    adjMatrices.add(newAdjMatrix);
                    return adjMatrices;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private static boolean equalsMatrix(ArrayList<ArrayList<String>> firstMatrix, ArrayList<ArrayList<String>> secondMatrix) {
        if (firstMatrix.size() != secondMatrix.size()) {
            return false;
        }
        for (int i = 0; i < firstMatrix.size(); i++) {
            for (int j = 0; j < firstMatrix.size(); j++) {
                if (!firstMatrix.get(i).get(j).equals(secondMatrix.get(i).get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isSubCAM(){
        AdjMatrix test = new AdjMatrix(this.genMaxSubMatrix(), null);
        return test.isCAM();
    }
    public ArrayList<AdjMatrix> extension() {
        ArrayList<AdjMatrix> extensionList = new ArrayList<>();
        for (Embedding embedding : embeddings) {
            int lastNodeID = embedding.getLastNode();
            ArrayList<Integer> nodeList = embedding.getNodeList();
            Node lastNode = embedding.getGraph().searchNode(lastNodeID);
            ArrayList<Edge> edges = lastNode.getNeighbors();
            for (Edge edge : edges) {
                Node otherNode = embedding.getGraph().getOrtherNode(edge, lastNode);
                boolean isExist = false;
                for (Integer integer : nodeList) {
                    if (integer.equals(otherNode.getNodeID())) {
                        isExist = true;
                        break;
                    }
                }
                if (isExist) {
                    continue;
                }
                ArrayList<ArrayList<String>> newMatrix = new ArrayList<>();
                for (int i = 0; i < this.matrix.size(); i++) {
                    newMatrix.add(new ArrayList<>());
                    for (int j = 0; j < this.matrix.size(); j++) {
                        newMatrix.get(i).add(this.matrix.get(i).get(j));
                    }
                }
                newMatrix.add(new ArrayList<>());
                for (int i = 0; i < this.matrix.size() - 1; i++) {
                    newMatrix.get(i).add("0");
                    newMatrix.get(this.matrix.size()).add("0");
                }
                newMatrix.get(this.matrix.size() - 1).add(edge.getLabel());
                newMatrix.get(this.matrix.size()).add(edge.getLabel());
                newMatrix.get(this.matrix.size()).add(otherNode.getLabel());
                Embedding newEmbedding = new Embedding(embedding.getGraph(), otherNode.getNodeID(), embedding);
                int position = -1;
                for (int i = 0; i < extensionList.size(); i++) {
                    if (AdjMatrix.equalsMatrix(newMatrix, extensionList.get(i).getMatrix())) {
                        position = i;
                        break;
                    }
                }
                if (position == -1) {
                    ArrayList<Embedding> embeddingList = new ArrayList<>();
                    embeddingList.add(newEmbedding);
                    AdjMatrix newAdjMatrix = new AdjMatrix(newMatrix, embeddingList);
                    extensionList.add(newAdjMatrix);
                } else {
                    extensionList.get(position).getEmbeddings().add(newEmbedding);
                }
            }
        }
        return extensionList;
    }

    public int getFrequency() {
        ArrayList<Integer> graphIDList = new ArrayList<>();
        for (Embedding embedding : embeddings) {
            boolean isExist = false;
            for (Integer graphID : graphIDList) {
                if (graphID.equals(embedding.getGraph().getID())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                graphIDList.add(embedding.getGraph().getID());
            }
        }
        return graphIDList.size();
    }
}
