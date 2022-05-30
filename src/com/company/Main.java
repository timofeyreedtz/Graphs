package com.company;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(5,8);
        g.addGraphNode(1,2,20);
        g.addGraphNode(1,3,20);
        g.addGraphNode(2,4,20);
        g.addGraphNode(2,5,40);
        g.addGraphNode(3,2,10);
        g.addGraphNode(3,4,10);
        g.addGraphNode(3,5,20);
        g.addGraphNode(4,5,30);
        Scanner s = new Scanner(System.in);
        System.out.println("Введите входной объем данных: ");
        double V = s.nextDouble();
        System.out.println("Входной объем данных: " + V);
        long start = System.nanoTime();
        g.OptimizeSize(V);
        long finish = System.nanoTime();
        long elapsed = finish - start;
     //   System.out.format("Прошло времени, нс: %.5f\n" ,(double)elapsed/ 1_000_000_000);
        g.printGraph();
    }
}
