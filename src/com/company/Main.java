package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	Graph g = new Graph(9,17);
    g.addGraphNode(1,2,5,0);
    g.addGraphNode(1,4,5,0);
    g.addGraphNode(1,3,5,0);
    g.addGraphNode(2,4,5,0);
    g.addGraphNode(2,7,5,0);
    g.addGraphNode(3,4,5,0);
    g.addGraphNode(3,5,5,0);
    g.addGraphNode(3,6,5,0);
    g.addGraphNode(4,6,5,0);
    g.addGraphNode(4,7,5,0);
    g.addGraphNode(5,6,5,0);
    g.addGraphNode(5,8,5,0);
    g.addGraphNode(6,7,3,0);
    g.addGraphNode(6,8,3,0);
    g.addGraphNode(6,9,4,0);
    g.addGraphNode(7,9,3,0);
    g.addGraphNode(8,9,3,0);
    Scanner s = new Scanner(System.in);
    System.out.println("Введите входной объем данных: ");
    double V = s.nextDouble();
    System.out.println("Входной объем данных: " + V);
    g.OptimizeSize(V);
    g.printGraph();
    g.printArray();
    }
}
