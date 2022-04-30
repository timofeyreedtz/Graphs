package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Graph g = new Graph(7,12);
        g.addGraphNode(1,2,4,0,0);
        g.addGraphNode(1,3,4,0,0);
        g.addGraphNode(1,4,4,0,0);
        g.addGraphNode(2,3,4,0,0);
        g.addGraphNode(2,6,4,0,0);
        g.addGraphNode(3,4,4,0,0);
        g.addGraphNode(3,5,4,0,0);
        g.addGraphNode(3,7,1,0,0);
        g.addGraphNode(4,5,4,0,0);
        g.addGraphNode(5,1,4,0,0);
        g.addGraphNode(5,7,4,0,0);
        g.addGraphNode(6,7,4,0,0);
        Scanner s = new Scanner(System.in);
        System.out.println("Введите входной объем данных: ");
        double V = s.nextDouble();
        System.out.println("Входной объем данных: " + V);
        long start = System.nanoTime();
        g.OptimizeSize(V);
        long finish = System.nanoTime();
        long elapsed = finish - start;
        System.out.format("Прошло времени, нс: %.5f\n" ,(double)elapsed/ 1_000_000_000);
        g.printGraph();
    }
}
