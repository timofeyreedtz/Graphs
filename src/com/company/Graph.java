package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

class GraphNode{
    private int from,to;
    private double weight,size,delay ;

    GraphNode(int from, int to, double weight,double size,double delay){
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.size = size;
        this.delay = delay;
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
    public double getDelay() {
        return delay;
    }
    public void setDelay(double delay) {
        this.delay = delay;
    }
}
public class Graph {
    private int num_of_tops, num_of_weights;
    private final List<List<Integer>> adj;
    private ArrayList<GraphNode> graph = new ArrayList<>();
    private final ArrayList<Thread> thread = new ArrayList<>();
    private final double[] size_array;
    private final boolean[] isWorking;
    private final double[][] matrix;
    private final int[] tops_array_from;
    Graph(int num_of_tops, int num_of_weights){
        this.num_of_tops = num_of_tops;
        adj = new ArrayList<>(num_of_tops);
        this.num_of_weights = num_of_weights;
        size_array = new double[this.num_of_tops];
        isWorking = new boolean[this.num_of_weights];
        matrix = new double[this.num_of_tops][this.num_of_tops];
        tops_array_from = new int[this.num_of_tops];
        for(int i = 0;i<num_of_weights;i++){
            isWorking[i] = true;
        }
        for(int i = 0;i < num_of_tops;i++){
            adj.add(new LinkedList<>());
            tops_array_from[i] = 0;
            for (int j = 0;j < num_of_tops;j++){
                matrix[i][j] = 0;
            }
        }
    }
    private boolean isCyclicUtil(int i, boolean[] visited,
                                 boolean[] recStack)
    {

        // Mark the current node as visited and
        // part of recursion stack
        /*if (recStack[i] )
            return true;

        if (visited[i])
            return false;

        visited[i] = true;*/

        recStack[i] = true;
        List<Integer> children = adj.get(i);

        for (Integer c: children)
            if (recStack[c]) {
                return true;
            }
        else if(!visited[c] && isCyclicUtil(c,visited,recStack)){
            return true;
            }
        recStack[i] = false;
        visited[i] = true;
        return false;
    }
    private boolean isNeedToBeBlocked(int i,int from){
        if( adj.get(graph.get(i).getFrom()-1).size() == 0){
            isWorking[i] = false;
            for (int k = 0; k < num_of_weights; k++) {
                if (graph.get(k).getTo() == graph.get(i).getFrom()) {
                    isWorking[k] = false;
                    isNeedToBeBlocked(k,graph.get(i).getFrom());
                }
            }
        }
        else if(adj.get(graph.get(i).getFrom()-1).size() == 1 && from != graph.get(i).getFrom()){
            if(!isWorking[i]){
                for (int k = 0; k < num_of_weights; k++){
                    if (graph.get(k).getTo() == graph.get(i).getFrom()) {
                        isWorking[k] = false;
                        isNeedToBeBlocked(k,graph.get(i).getFrom());
                    }
                }
            }
        }
        return false;
    }
    private void isCyclic() {
        fillAdj();
        // Mark all the vertices as not visited and
        // not part of recursion stack
        boolean[] visited = new boolean[num_of_tops];
        boolean[] recStack = new boolean[num_of_tops];
        // Call the recursive helper function to
        // detect cycle in different DFS trees
        for (int i = 0; i < num_of_weights; i++) {
            if (graph.get(i).getFrom() > graph.get(i).getTo()) {
                if (isCyclicUtil(graph.get(i).getFrom() - 1, visited, recStack)) {
                    isWorking[i] = false;
                    Integer in = graph.get(i).getTo()-1;
                    adj.get(graph.get(i).getFrom()-1).remove(in);
                    isNeedToBeBlocked(i,(graph.get(i).getFrom()));

                }
            }
        }
    }
    private void fillAdj(){
        for (int j = 0;j < num_of_weights; j++){
            adj.get(graph.get(j).getFrom()-1).add(graph.get(j).getTo()-1);
        }
    }
    private void maxFlow(){
        System.out.format("Максимальный поток из начальной в конечную вершины = %.1f\n"
                ,MaxFlow_Ford_Fulkerson.findMaxFlow(matrix,0,num_of_tops-1,num_of_tops));
    }
    private  void FillTopsArr(int [] arr) {
        for (int i = 0; i < num_of_weights; i++) {
            if( !isWorking[i]){
                i++;
            }
            if(i == num_of_weights){
                break;
            }
            arr[graph.get(i).getFrom()-1]++;
        }
    }
    public void OptimizeSize(double V){
        isCyclic();
        size_array[0] = V;
        FillTopsArr(tops_array_from);
        Optimize();
        maxFlow();
    }
    private int calcTops(int i){
        int k = 0;
        for (int j = 0;j < num_of_weights; j++){
            if (graph.get(i).getFrom() == graph.get(j).getFrom()){
                k++;
            }
        }
        return k;
    }
  /*  private void Run()  {
        int k = 0;
        for(int i = 0;i < num_of_weights;i++){
            if (isWorking[i]){
                int finalI = i;
                thread.add(new Thread(() -> Optimize(finalI)));
            }
        }
        for(int i = 0;i < num_of_tops;i++){
            int to = tops_array_from[i];
            if(i == 0){
                k = 0;
            }
            else k+=calcTops(k);
            for(int j = k; j < k + to; j++){
                thread.get(j).start();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/

    private  void Optimize() {
        for(int i = 0;i < num_of_weights;i++){
            if(isWorking[i]){
                double size;
                size  = size_array[graph.get(i).getFrom()-1]/calcTops(i) ;
                graph.get(i).setSize(size);
                matrix[graph.get(i).getFrom()-1][graph.get(i).getTo()-1] = size;
                graph.get(i).setDelay(Math.exp(size));
                size  = checkIfOverload(size,i,size_array); // обработка в случае нехватки пропускной способности канала
                System.out.println("from " + graph.get(i).getFrom()  + " to " + graph.get(i).getTo()  + ": "+ size);
                size_array[graph.get(i).getTo()-1] += size;
                if(i == num_of_weights-1){
                    size_array[graph.get(i).getFrom()-1] = 0;
                }
                else if (graph.get(i).getFrom() != graph.get(i+1).getFrom() ){
                    size_array[graph.get(i).getFrom()-1] = 0;
                }
            }
        }

    }
    private double checkIfOverload(double size, int i,double [] arr) {
        while (size > graph.get(i).getWeight()){
            size -= graph.get(i).getWeight();
            System.out.println("from " + graph.get(i).getFrom()  + " to " + graph.get(i).getTo()  + ": "+ graph.get(i).getWeight());
            arr[graph.get(i).getTo()-1] += graph.get(i).getWeight();

        }
        return size;
    }
    public void addGraphNode(int from, int to, double weight,double size,double delay){
        graph.add(new GraphNode(from,to,weight,size,delay));
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
                            "   пропускная способность канала: %.3f" +
                            "   время обработки: %.3f" +
                            "   прошедший объем: %.3f\n"
                            ,graph.get(i).getFrom()
                    ,graph.get(i).getTo(),graph.get(i).getWeight(),graph.get(i).getDelay(),graph.get(i).getSize());
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
