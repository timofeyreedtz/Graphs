package com.company;

import java.util.ArrayList;

class GraphNode{
    private int from,to;
    private double weight,size;

    GraphNode(int from, int to, double weight, double size){
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.size = size;

    }
    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
public class Graph {
    private int num_of_tops, num_of_weights;
    private ArrayList<GraphNode> graph = new ArrayList<>();
    private final double[] size_array;
    private final double[] tops_array_from;
    Graph(int num_of_tops, int num_of_weights){
        this.num_of_tops = num_of_tops;
        this.num_of_weights = num_of_weights;
        size_array = new double[this.num_of_tops];
        tops_array_from = new double[this.num_of_tops];
        for(int i = 0;i < num_of_tops;i++){
            tops_array_from[i] = 0;
        }
    }
    private  void FillTopsArr() {
        for (int i = 0; i < num_of_weights; i++) {
                tops_array_from[graph.get(i).getFrom()-1]++;
        }
    }
    public void OptimizeSize(double V){
        size_array[0] = V;
        double size = 0;
        FillTopsArr();
        for (int i = 0;i < num_of_weights;i++){
            size  = size_array[graph.get(i).getFrom()-1]/tops_array_from[graph.get(i).getFrom()-1] ;
            if(size > graph.get(i).getWeight()) graph.get(i).setWeight(size);
            size_array[graph.get(i).getTo()-1] += size;
            graph.get(i).setSize(size);
        }
        graph.get(num_of_weights-1).setSize(size);
    }
    public void addGraphNode(int from, int to, double weight, double size){
        graph.add(new GraphNode(from,to,weight,size));
    }
    public ArrayList<GraphNode> getGraph() {
        return graph;
    }

    public void setGraph(ArrayList<GraphNode> graph) {
        this.graph = graph;
    }
    public void printGraph(){
        for (int i = 0; i<num_of_weights;i++){
            System.out.format("{ %d o---------o %d" +
                    "   пропускная способность канала: %.3f" +
                    "   прошедший объем данных через канал: %.3f}\n",graph.get(i).getFrom()
                    ,graph.get(i).getTo(),graph.get(i).getWeight(),graph.get(i).getSize());
        }
    }
    public void printArray(){
        for (int i = 0; i < num_of_tops;i++){
            System.out.format("Объем данных в %d вершине: %.3f\n" ,i+1, size_array[i]);
        }
    }
    public int getNum_of_tops() {
        return num_of_tops;
    }

    public void setNum_of_tops(int num_of_tops) {
        this.num_of_tops = num_of_tops;
    }

    public int getNum_of_weights() {
        return num_of_weights;
    }

    public void setNum_of_weights(int num_of_weights) {
        this.num_of_weights = num_of_weights;
    }
}
