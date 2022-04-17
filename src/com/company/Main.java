package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Graph g = new Graph(6,21);
        g.addGraphNode(1,2,5,0);
        g.addGraphNode(1,4,5,0);
        g.addGraphNode(1,3,5,0);
        g.addGraphNode(2,1,5,0);
        g.addGraphNode(2,4,5,0);
        g.addGraphNode(3,1,5,0);
        g.addGraphNode(3,2,5,0);
        g.addGraphNode(3,4,5,0);
        g.addGraphNode(3,5,5,0);
        g.addGraphNode(4,1,5,0);
        g.addGraphNode(4,2,5,0);
        g.addGraphNode(4,3,5,0);
        g.addGraphNode(4,5,5,0);
        g.addGraphNode(4,6,5,0);
        g.addGraphNode(5,1,5,0);
        g.addGraphNode(5,2,5,0);
        g.addGraphNode(5,3,5,0);
        g.addGraphNode(5,4,5,0);
        g.addGraphNode(5,6,5,0);
        g.addGraphNode(6,1,5,0);
        g.addGraphNode(6,2,5,0);
        Scanner s = new Scanner(System.in);
        System.out.println("Введите входной объем данных: ");
        double V = s.nextDouble();
        System.out.println("Входной объем данных: " + V);
        g.OptimizeSize(V);
        g.printArray();
    }
}
