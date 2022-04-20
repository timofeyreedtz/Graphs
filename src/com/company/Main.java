package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Graph g = new Graph(5,9);
        g.addGraphNode(1,2,3);
        g.addGraphNode(1,3,3);
        g.addGraphNode(2,1,1);
        g.addGraphNode(2,4,3);
        g.addGraphNode(3,1,1);
        g.addGraphNode(3,5,3);
        g.addGraphNode(4,1,1);
        g.addGraphNode(4,2,0.2);
        g.addGraphNode(4,5,5);

        Scanner s = new Scanner(System.in);
        System.out.println("Введите входной объем данных: ");
        double V = s.nextDouble();
        System.out.println("Входной объем данных: " + V);
        g.OptimizeSize(V);
        g.printArray();
    }
}
