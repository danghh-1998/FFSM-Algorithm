package ffsm;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhap thu tu bo test: ");
        String fileNumber = scanner.nextLine();
        System.out.print("Nhap minsup: ");
        GraphDatabase database = ReadFile.readFile(fileNumber);
        int minsup = Integer.parseInt(scanner.nextLine());
        FFSM.ffsmStartSearch(database, minsup);
//        AdjMatrix adjMatrix = new AdjMatrix();
//        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
//        matrix.add(new ArrayList<>(Arrays.asList("2", "5", "5", "0", "0")));
//        matrix.add(new ArrayList<>(Arrays.asList("5", "2", "0", "5", "0")));
//        matrix.add(new ArrayList<>(Arrays.asList("5", "0", "2", "0", "5")));
//        matrix.add(new ArrayList<>(Arrays.asList("0", "5", "0", "2", "0")));
//        matrix.add(new ArrayList<>(Arrays.asList("0", "0", "5", "0", "2")));
//        adjMatrix.setMatrix(matrix);
//        System.out.println(adjMatrix.isCAM());
    }
}
