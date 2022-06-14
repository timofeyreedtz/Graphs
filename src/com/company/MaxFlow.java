package com.company;

import java.util.LinkedList;
import java.util.Queue;

class MaxFlow_Ford_Fulkerson {

        public static double findMaxFlow(double [][] arr, int source, int sink,int vertices) {
            double[][] residualGraph = new double[vertices][vertices];

            for (int i = 0; i < vertices ; i++) {
                for (int j = 0; j < vertices ; j++) {
                    residualGraph[i][j] = arr[i][j];
                }
            }
            int [] parent = new int[vertices];
            double max_flow = 0;
            while(isPathExist_BFS(residualGraph, source, sink, parent,vertices)){
                double flow_capacity = Integer.MAX_VALUE;
                int t = sink;
                while(t!=source){
                    int s = parent[t];
                    flow_capacity = Math.min(flow_capacity, residualGraph[s][t]);
                    t = s;
                }
                t = sink;
                while(t!=source){
                    int s = parent[t];
                    residualGraph[s][t]-=flow_capacity;
                    residualGraph[t][s]+=flow_capacity;
                    t = s;
                }
                max_flow+=flow_capacity;
            }
            return max_flow;
        }

        static boolean isPathExist_BFS(double[][] residualGraph, int src, int dest, int[] parent,int vertices){
            boolean pathFound = false;
            boolean [] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(src);
            parent[src] = -1;
            visited[src] = true;
            while(!queue.isEmpty()){
                int u = queue.poll();
                for (int v = 0; v <vertices ; v++) {
                    if(!visited[v] && residualGraph[u][v]>0) {
                        queue.add(v);
                        parent[v] = u;
                        visited[v] = true;
                    }
                }
            }
            pathFound = visited[dest];
            return pathFound;
        }
    }
