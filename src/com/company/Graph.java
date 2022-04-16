package com.company;

import java.util.ArrayList;
import java.util.Optional;

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
    private final int[] count;
    private final boolean[] isChanged;
    private final double[] size_array_for_cycle_points;
    private final double[] tops_array_from;
    Graph(int num_of_tops, int num_of_weights){
        this.num_of_tops = num_of_tops;
        this.num_of_weights = num_of_weights;
        size_array = new double[this.num_of_tops];
        count = new int[this.num_of_tops];
        size_array_for_cycle_points = new double[this.num_of_tops];
        tops_array_from = new double[this.num_of_tops];
        isChanged = new boolean[this.num_of_tops];
        for(int i = 0;i < num_of_tops;i++){
            tops_array_from[i] = 0;
            isChanged[i] = false;
            count[i] = 0;
        }
    }
    private  void FillTopsArr(double [] arr) {
        for (int i = 0; i < num_of_weights; i++) {
            arr[graph.get(i).getFrom()-1]++;
        }
    }
    public void OptimizeSize(double V){
        size_array[0] = V;
        FillTopsArr(tops_array_from);
        Optimize();
    }

    private void Optimize() {
        double size = 0;
        int from = 0;
        for (int i = 0 ; i < num_of_weights; i++){
            if ((graph.get(i).getFrom() > graph.get(i).getTo())){ // проверка на цикл
                size  = size_array[graph.get(i).getFrom()-1]/tops_array_from[graph.get(i).getFrom()-1] ; // находим объем в вершине
                tops_array_from[graph.get(i).getFrom()-1]--; //  уменьшаем количество исходящих путей в вершине на 1
                if(size > graph.get(i).getWeight()) graph.get(i).setWeight(size);
                size_array_for_cycle_points[graph.get(i).getTo()-1] = size; // отправляем полученный объем в вершину, в которой образуется новый цикл

                size_array[graph.get(i).getFrom()-1] -= size; // уменьшаем объем в текущей вершине на величину отправленного объёма
                for(int k = 0;k < num_of_weights;k++){
                    if(graph.get(i).getTo() == graph.get(k).getFrom()) // ищем узел, с которого начнем следующий цикл
                        from = k;
                        break;
                }
                for(int k = from;k < num_of_weights;k++){
                    while(graph.get(k).getFrom() > graph.get(k).getTo()){ /*
                    проверка на цикл: если нашли необработанный цикл, игнорируем его, но уменьшаем кол-во исходящих путей в нем на 1*/
                        if(k > i){
                            tops_array_from[graph.get(k).getFrom()-1]--;
                            isChanged[graph.get(k).getFrom()-1] = true;
                            count[graph.get(k).getFrom()-1]++;
                        }
                        k++;

                    }/*
                    распределяем объем в нужные вершины
                    */
                    size  = size_array_for_cycle_points[graph.get(k).getFrom()-1]/tops_array_from[graph.get(k).getFrom()-1] ;
                    if(size > graph.get(k).getWeight()) graph.get(k).setWeight(size);
                    size_array_for_cycle_points[graph.get(k).getTo()-1] += size;
                }
                /*
                суммируем объем, прошедший через цикл с объемом в конечной вершине графа
                 */
                size_array[num_of_tops-1] += size_array_for_cycle_points[num_of_tops-1];
                for (int k = 0;k < num_of_tops;k++){
                    size_array_for_cycle_points[k] = 0;
                    /*
                    возвращаем исходное количество вершин в необработанных циклах
                    */
                    if(isChanged[k]){
                        tops_array_from[k] += count[k];
                        isChanged[k] = false;
                        count[k] = 0;
                    }
                }
            }
            else{ // распределение объема
                size  = size_array[graph.get(i).getFrom()-1]/tops_array_from[graph.get(i).getFrom()-1] ;
                if(size > graph.get(i).getWeight()) graph.get(i).setWeight(size);
                size_array[graph.get(i).getTo()-1] += size;
                graph.get(i).setSize(size);
            }
        }
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
