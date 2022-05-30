package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
class NodeProcessing{
    private  double lambda,size,timeProcessing,average_time_for_one_thread,sum_of_time,queue,sum_of_size, max_size_of_queue;
    private int top,number_of_threads,number_of_working_nodes;
    private boolean isWorking;
    NodeProcessing(){

    }
    NodeProcessing(double lambda,double size,double timeProcessing,
                   int number_of_threads,double average_time_for_one_thread,int top,double sum_of_time,
                   double queue, double sum_of_size,boolean isWorking,double max_size_of_queue,int number_of_working_nodes){
        this.lambda = lambda;
        this.size = size;
        this.timeProcessing = timeProcessing;
        this.number_of_threads = number_of_threads;
        this.average_time_for_one_thread = average_time_for_one_thread;
        this.top = top;
        this.sum_of_time = sum_of_time;
        this.queue = queue;
        this.sum_of_size = sum_of_size;
        this.isWorking = isWorking;
        this.max_size_of_queue = max_size_of_queue;
        this.number_of_working_nodes = number_of_working_nodes;
    }
    public void calcTime(){
        average_time_for_one_thread = sum_of_time/number_of_threads;
        lambda = average_time_for_one_thread/number_of_threads;
        timeProcessing = getTimeProcessing() + 1 - (Math.exp(-lambda*size));
        setTimeProcessing(timeProcessing);
    }

    public int getNumber_of_threads() {
        return number_of_threads;
    }

    public void setNumber_of_threads(int number_of_threads) {
        this.number_of_threads = number_of_threads;
    }

    public double getAverage_time_for_one_thread() {
        return average_time_for_one_thread;
    }

    public void setAverage_time_for_one_thread(double average_time_for_one_thread) {
        this.average_time_for_one_thread = average_time_for_one_thread;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getTimeProcessing() {
        return timeProcessing;
    }

    public void setTimeProcessing(double timeProcessing) {
        this.timeProcessing = timeProcessing;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public double getSum_of_time() {
        return sum_of_time;
    }

    public void setSum_of_time(double sum_of_time) {
        this.sum_of_time = sum_of_time;
    }

    public double getSum_of_size() {
        return sum_of_size;
    }

    public void setSum_of_size(double sum_of_size) {
        this.sum_of_size = sum_of_size;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public double getQueue() {
        return queue;
    }

    public void setQueue(double queue) {
        this.queue = queue;
    }

    public double getMax_size_of_queue() {
        return max_size_of_queue;
    }

    public void setMax_size_of_queue(double max_size_of_queue) {
        this.max_size_of_queue = max_size_of_queue;
    }

    public int getNumber_of_working_nodes() {
        return number_of_working_nodes;
    }

    public void setNumber_of_working_nodes(int number_of_working_nodes) {
        this.number_of_working_nodes = number_of_working_nodes;
    }
}
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
    private final List<List<Integer>> adj;
    private ArrayList<GraphNode> graph = new ArrayList<>();
    private ArrayList<NodeProcessing> nodeProcessing = new ArrayList<>(num_of_tops);
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
            nodeProcessing.add(new NodeProcessing(0,0,0,0,0,
                    0,0,0,0,true,10,0));
            for (int j = 0;j < num_of_tops;j++){
                matrix[i][j] = 0;
            }
        }
    }
    private boolean isCyclicUtil(int i, boolean[] visited,boolean[] recStack)
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
    private void areOtherWaysNeedToBeBlocked(int i,int from){
        if( adj.get(graph.get(i).getFrom()-1).size() == 0 && from != num_of_tops) {
            for (int k = num_of_weights-1; k > 0; k--) {
                if (graph.get(k).getTo() == graph.get(i).getFrom()) {
                    isWorking[k] = false;
                    areOtherWaysNeedToBeBlocked(k,graph.get(i).getFrom());
                }
            }
        }
        else if(adj.get(graph.get(i).getFrom()-1).size() == 1 && from != graph.get(i).getFrom() /*&& from != num_of_tops*/ ){
            if(!isWorking[i]){
                for (int k = num_of_weights-1; k > 0; k--){
                    if (graph.get(k).getTo() == graph.get(i).getFrom() && isWorking[k]) {
                        isWorking[k] = false;
                        areOtherWaysNeedToBeBlocked(k,graph.get(i).getFrom());
                    }
                }
            }
        }
    }
    private void isCyclic() {
        boolean[] visited = new boolean[num_of_tops];
        boolean[] recStack = new boolean[num_of_tops];
        for (int k = num_of_weights-1; k > 0; k--) {
            if (graph.get(k).getFrom() == num_of_tops){
                isWorking[k] = false;
            }
        }
        fillAdj();
        for (int i = num_of_weights-1; i > 0; i--){
            if (graph.get(i).getFrom() > graph.get(i).getTo() && calcTops(i) != 1) {
                for (Integer c:adj.get(graph.get(i).getTo() - 1))
                    if(graph.get(i).getFrom()-1 == c){
                        BlockingOfNode(i);
                        break;
                    }
            }
            else if(graph.get(i).getFrom() > graph.get(i).getTo() && calcTops(i) == 1){
                if(isCyclicUtil(graph.get(i).getTo() - 1,visited,recStack)){
                    BlockingOfNode(i);
                    areOtherWaysNeedToBeBlocked(i,(graph.get(i).getFrom()));
                }
            }
        }
        for (int i = num_of_weights-1; i > 0; i--) {
            if (graph.get(i).getFrom() > graph.get(i).getTo() ) {
                if (isCyclicUtil(graph.get(i).getTo() - 1, visited, recStack) && isWorking[i] ){
                    if(isNodeNeedToBeBlocked(i,i,graph.get(i).getFrom(),graph.get(i).getTo())){
                        BlockingOfNode(i);
                    }
                    areOtherWaysNeedToBeBlocked(i,(graph.get(i).getFrom()));
                }
            }
        }
        for (int i = 0; i < num_of_weights;i++){
            boolean[] visited1 = new boolean[num_of_tops];
            boolean[] recStack1 = new boolean[num_of_tops];
            if(!isWorking[i] && graph.get(i).getTo() < graph.get(i).getFrom()){
                Integer in = graph.get(i).getTo()-1;
                adj.get(graph.get(i).getFrom()-1).add(in);
                if(!isCyclicUtil(graph.get(i).getTo() - 1,visited1,recStack1))
                    isWorking[i] = true;
                else adj.get(graph.get(i).getFrom()-1).remove(in);
            }
        }
    }
    private boolean isNodeNeedToBeBlocked(int i, int k,int from,int to){
        if (graph.get(i).getTo() == from || (graph.get(i).getTo() == to && i != k)){
            return true;
        }
        if(graph.get(i).getTo() == num_of_tops){
            return false;
        }
        for (int j = 0; j < num_of_weights; j++){
            if(graph.get(j).getFrom() == graph.get(i).getTo() && isWorking[j]){
                if(isNodeNeedToBeBlocked(j,k,from,to))
                    return true;
            }
        }
        return false;
    }
    private void BlockingOfNode(int i) {
        isWorking[i] = false;
        Integer in = graph.get(i).getTo()-1;
        adj.get(graph.get(i).getFrom()-1).remove(in);
    }

    private void fillAdj(){
        for (int j = 0;j < num_of_weights; j++){
            adj.get(graph.get(j).getFrom()-1).add(graph.get(j).getTo()-1);
        }
    }
    private void maxFlow(){
        System.out.format("Максимальный поток из начальной в конечную вершины = %.5f\n"
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
        nodeProcessing.get(0).setQueue(V);
        if(nodeProcessing.get(0).getMax_size_of_queue() < V){
            size_array[0] = nodeProcessing.get(0).getMax_size_of_queue();
        }
        for(int i =0;i<num_of_weights;i++){
            Optimize(i);
        }
        FillTopsArr(tops_array_from);
        maxFlow();
        printProcess();
        calcReliability();
    }
    public void printProcess(){
        double sum = 0;
        for (int i = 0; i < nodeProcessing.size();i++){
            sum+=nodeProcessing.get(i).getTimeProcessing();
            System.out.format("Время обработки : %.10f\n" ,nodeProcessing.get(i).getTimeProcessing());
        }
        sum = sum/num_of_tops;
        System.out.format("Cреднее время обработки = %.5f\n",sum);
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
    private void calcReliability(){
        double k1 = 0,k2 = 0;
        double p_weight = 0,p_node = 0,p = 0;
        for(int i = 0; i < num_of_weights;i++){
            if(isWorking[i]){
                k1++;
            }
        }
        p_weight = k1 / num_of_weights;
        for(NodeProcessing c: nodeProcessing){
            if (c.isWorking()){
                k2++;
            }
        }
        p_node = k2/num_of_tops;
        p = p_node*p_weight;
        System.out.format("Вероятность безотказной работы каналов = %.5f\n" +
                "Вероятность безотказной работы узлов = %.5f\n" +
                "Вероятность безотказной работы системы = %.5f\n",p_weight,p_node,p);
    }
    private  void Optimize(int i) {
        if(isWorking[i]){
            if(graph.get(i).getFrom() > graph.get(i).getTo()){
                optimizeHelp(i);
                int from = findTop(graph.get(i).getTo());
                //tops_array_from[graph.get(i).getTo()-1] = calcTops(from);
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
        nodeProcessing.get(graph.get(i).getTo()-1).setQueue((nodeProcessing.get(graph.get(i).getTo()-1).getQueue()+size));
        if(nodeProcessing.get(graph.get(i).getTo()-1).getQueue() > nodeProcessing.get(graph.get(i).getTo()-1).getMax_size_of_queue()){
            nodeProcessing.get(graph.get(i).getTo()-1).setWorking(false);
            for (int k = i; k < findTop(graph.get(i).getTo());k++){
                if(graph.get(k).getTo() == graph.get(i).getTo()){
                    isWorking[k] = false;
                }
            }
            nodeProcessing.get(graph.get(i).getTo()-1).setQueue(nodeProcessing.get(graph.get(i).getTo()-1).getMax_size_of_queue());
            matrix[graph.get(i).getFrom()-1][graph.get(i).getTo()-1] += nodeProcessing.get(graph.get(i).getTo()-1).getMax_size_of_queue();
            int k = nodeProcessing.get(graph.get(i).getTo()-1).getNumber_of_threads() + 1;
            nodeProcessing.get(graph.get(i).getTo()-1).setNumber_of_threads(k);
            nodeProcessing.get(graph.get(i).getTo()-1).setSum_of_time(nodeProcessing.get(graph.get(i).getTo()-1).getMax_size_of_queue()/graph.get(i).getWeight());
            nodeProcessing.get(graph.get(i).getTo()-1).setSize(nodeProcessing.get(graph.get(i).getTo()-1).getSize()+nodeProcessing.get(graph.get(i).getTo()-1).getMax_size_of_queue());
            size  = checkIfOverload(nodeProcessing.get(graph.get(i).getTo()-1).getMax_size_of_queue(), i,size_array);
            System.out.println("from " + graph.get(i).getFrom()  + " to " + graph.get(i).getTo()  + ": "+ nodeProcessing.get(graph.get(i).getTo()-1).getMax_size_of_queue());
            size_array[graph.get(i).getTo()-1] += nodeProcessing.get(graph.get(i).getTo()-1).getMax_size_of_queue();
            nodeProcessing.get(graph.get(i).getTo()-1).calcTime();
            nodeProcessing.get(graph.get(i).getTo()-1).setNumber_of_threads(0);
            nodeProcessing.get(graph.get(i).getTo()-1).setSum_of_time(0);
            if(i == num_of_weights-1){
                size_array[graph.get(i).getFrom()-1] = 0;
            }
            else if (graph.get(i).getFrom() != graph.get(i+1).getFrom() ){
                size_array[graph.get(i).getFrom()-1] = 0;
            }
        }
        else{
            matrix[graph.get(i).getFrom()-1][graph.get(i).getTo()-1] += size;
            int k = nodeProcessing.get(graph.get(i).getTo()-1).getNumber_of_threads() + 1;
            nodeProcessing.get(graph.get(i).getTo()-1).setNumber_of_threads(k);
            nodeProcessing.get(graph.get(i).getTo()-1).setSum_of_time(nodeProcessing.get(graph.get(i).getTo()-1).getSum_of_time() + 1/graph.get(i).getWeight());
            nodeProcessing.get(graph.get(i).getTo()-1).setSize(nodeProcessing.get(graph.get(i).getTo()-1).getSize()+size);
            size  = checkIfOverload(size, i,size_array);
            System.out.println("from " + graph.get(i).getFrom()  + " to " + graph.get(i).getTo()  + ": "+ size);
            size_array[graph.get(i).getTo()-1] += size;
            nodeProcessing.get(graph.get(i).getTo()-1).calcTime();
            nodeProcessing.get(graph.get(i).getTo()-1).setNumber_of_threads(0);
            nodeProcessing.get(graph.get(i).getTo()-1).setSum_of_time(0);
            if(i == num_of_weights-1){
                size_array[graph.get(i).getFrom()-1] = 0;
            }
            else if (graph.get(i).getFrom() != graph.get(i+1).getFrom() ){
                size_array[graph.get(i).getFrom()-1] = 0;
            }
        }
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
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        while (size > graph.get(i).getWeight()) {
            int k = nodeProcessing.get(graph.get(i).getTo() - 1).getNumber_of_threads() + 1;
            nodeProcessing.get(graph.get(i).getTo() - 1).setNumber_of_threads(k);
            nodeProcessing.get(graph.get(i).getTo() - 1).setSum_of_time(nodeProcessing.get(graph.get(i).getTo() - 1).getSum_of_time() + 1 / graph.get(i).getWeight());
            size -= graph.get(i).getWeight();
            System.out.println("from " + graph.get(i).getFrom() + " to " + graph.get(i).getTo() + ": " + graph.get(i).getWeight());
            arr[graph.get(i).getTo() - 1] += graph.get(i).getWeight();
        }
        return size;
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
