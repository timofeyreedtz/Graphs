package com.company;

import java.util.ArrayList;
import java.util.Optional;

class GraphNode{
    private int from,to;
    private double weight;

    GraphNode(int from, int to, double weight){
        this.from = from;
        this.to = to;
        this.weight = weight;

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
                while (size > graph.get(i).getWeight()){ // обработки циклов в случае если недостачи пропускной способности канала
                    size -= graph.get(i).getWeight();
                    System.out.println("from " + graph.get(i).getFrom()  + " to " + graph.get(i).getTo()  + ": "+ graph.get(i).getWeight());
                    size_array_for_cycle_points[graph.get(i).getTo()-1] = graph.get(i).getWeight();;
                    System.out.println("Loop start!");
                    loopProcessing(graph.get(i).getWeight(),i);
                    size_array[graph.get(i).getFrom()-1] -= graph.get(i).getWeight();
                    System.out.println("Loop end!");
                }
                System.out.println("from " + graph.get(i).getFrom() + " to " + graph.get(i).getTo() + ": "+ size);
                size_array_for_cycle_points[graph.get(i).getTo()-1] = size; // отправляем полученный объем в вершину, в которой образуется новый цикл
                size_array[graph.get(i).getFrom()-1] -= size; // уменьшаем объем в текущей вершине на величину отправленного объёма
                System.out.println("Loop start!");
                loopProcessing(size,i);
                System.out.println("Loop end!");
            }
            else{ // распределение объема

                size  = size_array[graph.get(i).getFrom()-1]/tops_array_from[graph.get(i).getFrom()-1] ;
                checkIfOverload(size,i,size_array); // обработка в случае нехватки пропускной способности канала
                System.out.println("from " + graph.get(i).getFrom()  + " to " + graph.get(i).getTo()  + ": "+ size);
                size_array[graph.get(i).getTo()-1] += size;
                if(i == num_of_weights-1){
                    size_array[graph.get(i).getFrom()-1] = 0;
                }
                else if (graph.get(i).getFrom() != graph.get(i+1).getFrom()){
                    size_array[graph.get(i).getFrom()-1] = 0;
                }
            }
        }
    }

    private void loopProcessing(double size, int i) {
        int from = 0;
        for(int k = 0;k < num_of_weights;k++){
            if(graph.get(i).getTo() == graph.get(k).getFrom()) // ищем узел, с которого начнем следующий цикл
                from = k;
                break;
        }
        for(int k = from; k < num_of_weights; k++){
            while(graph.get(k).getFrom()-1 > graph.get(k).getTo()-1){ /*
            проверка на цикл: если нашли необработанный цикл, игнорируем его, но уменьшаем кол-во исходящих путей в нем на 1*/
                if(k > i){
                    tops_array_from[graph.get(k).getFrom()-1]--;
                    isChanged[graph.get(k).getFrom()-1] = true;
                    count[graph.get(k).getFrom()-1]++;
                }
                k++;
                if(k == num_of_weights){
                    break;              // если дошли до последнего узла - выходим
                }

            }/*
            распределяем объем в нужные вершины
            */
            if(k == num_of_weights){  // если дошли до последнего узла - выходим
                break;
            }
            checkIfOverload(size, i,size_array_for_cycle_points);
            size = size_array_for_cycle_points[graph.get(k).getFrom()-1]/tops_array_from[graph.get(k).getFrom()-1] ;
            System.out.println("from " + graph.get(k).getFrom() + " to " + graph.get(k).getTo() + ": "+ size);
            size_array_for_cycle_points[graph.get(k).getTo()-1] += size;
        }
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
        if(i == num_of_weights-1){
            size_array_for_cycle_points[graph.get(i).getFrom()-1] = 0;
        }
        else if (graph.get(i).getFrom() != graph.get(i+1).getFrom()){
            size_array_for_cycle_points[graph.get(i).getFrom()-1] = 0;
        }
    }

    private void checkIfOverload(double size, int i,double [] arr) {
        while (size > graph.get(i).getWeight()){
            size -= graph.get(i).getWeight();
            System.out.println("from " + graph.get(i).getFrom()  + " to " + graph.get(i).getTo()  + ": "+ graph.get(i).getWeight());
            arr[graph.get(i).getTo()-1] += graph.get(i).getWeight();
        }
    }

    public void addGraphNode(int from, int to, double weight){
        graph.add(new GraphNode(from,to,weight));
    }
    public void deleteGraphNode(int i){
        graph.remove(i);
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
                            "   пропускная способность канала: %.3f\n"
                            ,graph.get(i).getFrom()
                    ,graph.get(i).getTo(),graph.get(i).getWeight());
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
