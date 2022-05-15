package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
class ThreadStack{
    private Thread thread;
    private int from;
    ThreadStack(int from, Thread thread){
        this.from = from;
        this.thread = thread;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
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
    private   List<List<Integer>> adj;
    private ArrayList<GraphNode> graph = new ArrayList<>();
    private final ArrayList<ThreadStack> thread = new ArrayList<>();
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
    private void isNeedToBeBlocked(int i,int from){

        if( adj.get(graph.get(i).getFrom()-1).size() == 0 && from != num_of_tops) {
            //isWorking[i] = false;
            for (int k = 0; k < num_of_weights; k++) {
                if (graph.get(k).getTo() == graph.get(i).getFrom()) {
                    isWorking[k] = false;
                    isNeedToBeBlocked(k,graph.get(i).getFrom());
                }
            }
        }
        else if(adj.get(graph.get(i).getFrom()-1).size() == 1 && from != graph.get(i).getFrom() /*&& from != num_of_tops*/ ){
            if(!isWorking[i]){
                for (int k = 0; k < num_of_weights; k++){
                    if (graph.get(k).getTo() == graph.get(i).getFrom() && isWorking[k]) {
                        isWorking[k] = false;
                        isNeedToBeBlocked(k,graph.get(i).getFrom());
                    }
                }
            }
        }
    }
    private void isCyclic() {
        boolean[] visited = new boolean[num_of_tops];
        boolean[] recStack = new boolean[num_of_tops];
        for (int i = 0; i < num_of_weights; i++) {
            if (graph.get(i).getFrom() == num_of_tops){
                isWorking[i] = false;
            }
        }
        fillAdj();
        for (int i = 0; i < num_of_weights; i++){
            if (graph.get(i).getFrom() > graph.get(i).getTo() && calcTops(i) != 1) {
                for (Integer c:adj.get(graph.get(i).getTo() - 1))
                    if(graph.get(i).getFrom()-1 == c){
                        isWorking[i] = false;
                        adj.get(graph.get(i).getFrom() - 1).remove((Integer) (graph.get(i).getTo()-1));
                        break;
                    }
            }
            else if(calcTops(i) == 1){
                if(isCyclicUtil(graph.get(i).getTo() - 1,visited,recStack)){
                    isWorking[i] = false;
                    Integer in = graph.get(i).getTo()-1;
                    adj.get(graph.get(i).getFrom()-1).remove(in);
                    isNeedToBeBlocked(i,(graph.get(i).getFrom()));
                }
            }
        }

        for (int i = 0; i < num_of_weights; i++) {
            if (graph.get(i).getFrom() > graph.get(i).getTo() ) {
                if (isCyclicUtil(graph.get(i).getTo() - 1, visited, recStack)
                       && isWorking[i] ){
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
            if( isWorking[i]) {
                arr[graph.get(i).getFrom()-1]++;
            }
        }
    }
    public void OptimizeSize(double V){
        isCyclic();
        FillTopsArr(tops_array_from);
        size_array[0] = V;
        /*for (int i = 0; i < num_of_weights; i++){
            Optimize(i);
        }*/
        Run();
        maxFlow();
    }

    private int findTop(int from){
        for(int i = 0;i < num_of_weights;i++){
            if(graph.get(i).getFrom() == from){
                return i;
            }
        }
        return 0;
    }
    private int calcTops(int i){
        int k = 0;
        for (int j = 0;j < num_of_weights; j++){
            if (graph.get(i).getFrom() == graph.get(j).getFrom() && isWorking[j]){
                k++;
            }
        }
        return k;
    }
    private  void Optimize(int i) {
        if(isWorking[i]){
            if(graph.get(i).getFrom() > graph.get(i).getTo()){
                optimizeHelp(i);
                int from = findTop(graph.get(i).getTo());
                for(int k = from; k < i;k++){
                    Optimize(k);
                }
            }
            else{
                optimizeHelp(i);
            }
        }
    }

    private void optimizeHelp(int i) {
        double size;
        size  = size_array[graph.get(i).getFrom()-1]/tops_array_from[graph.get(i).getFrom()-1] ;
        graph.get(i).setSize(size);
        matrix[graph.get(i).getFrom()-1][graph.get(i).getTo()-1] += size;
        graph.get(i).setDelay(Math.exp(size));
        size  = checkIfOverload(size, i,size_array);
        System.out.println("from " + graph.get(i).getFrom()  + " to " + graph.get(i).getTo()  + ": "+ size);
        size_array[graph.get(i).getTo()-1] += size;
    }

    private void Run() {
        for(int i = 0;i < num_of_weights;i++){
            if(isWorking[i]){
                int finalI = i;
                thread.add(new ThreadStack(graph.get(i).getFrom(),new Thread(()->Optimize(finalI))));
            }
        }
        for(int i = 0;i < thread.size();i++){
            int top = thread.get(i).getFrom();
            while(thread.get(i).getFrom() == top){
                thread.get(i).getThread().start();
                i++;
                if(i == thread.size()){
                    break;
                }
            }
            i--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
