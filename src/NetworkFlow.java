import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by Team El Camino
 */
// Source: http://bit.ly/1Mram1V
// Note: The source code was modified to be zero-indexed.
public class NetworkFlow
{
    private int[] parent;
    private Queue<Integer> queue;
    private int numberOfVertices;
    private boolean[] visited;
 
    public NetworkFlow(int numberOfVertices)
    {
        this.numberOfVertices = numberOfVertices;
        this.queue = new LinkedList<Integer>();
        parent = new int[numberOfVertices];
        visited = new boolean[numberOfVertices];        
    }
 
    public boolean runBFS(int source, int goal, int graph[][])
    {
        boolean pathFound = false;
        int destination, element;
 
        for(int vertex = 0; vertex < numberOfVertices; vertex++)
        {
            parent[vertex] = -1;
            visited[vertex] = false;
        }
 
        queue.add(source);
        parent[source] = -1;
        visited[source] = true;
 
        while (!queue.isEmpty())
        { 
            element = queue.remove();
            destination = 0;
 
            while (destination < numberOfVertices)
            {
                if (graph[element][destination] > 0 &&  !visited[destination])
                {
                    parent[destination] = element;
                    queue.add(destination);
                    visited[destination] = true;
                }
                destination++;
            }
        }
        if(visited[goal])
        {
            pathFound = true;
        }
        return pathFound;
    }
 
    public int findMaxFlow(int graph[][], int source, int destination)
    {
        int u, v;
        int maxFlow = 0;
        int pathFlow;
 
        int[][] resGraph = new int[numberOfVertices][numberOfVertices];
        for (int sVert = 0; sVert < numberOfVertices; sVert++)
        {
            for (int dVert = 0; dVert < numberOfVertices; dVert++)
            {
                resGraph[sVert][dVert] = graph[sVert][dVert];
            }
        }
 
        while (runBFS(source ,destination, resGraph))
        {
            pathFlow = Integer.MAX_VALUE;
            for (v = destination; v != source; v = parent[v])
            {
                u = parent[v];
                pathFlow = Math.min(pathFlow, resGraph[u][v]);
            }
            for (v = destination; v != source; v = parent[v])
            {
                u = parent[v];
                resGraph[u][v] -= pathFlow;
                resGraph[v][u] += pathFlow;
            }
            maxFlow += pathFlow;    
        }
 
        return maxFlow;
    }
 
    public static void main(String[] arg)
    {
        int[][] graph;
        int numberOfNodes;
        int source;
        int sink;
        int maxFlow;
 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of nodes");
        numberOfNodes = scanner.nextInt();
        graph = new int[numberOfNodes][numberOfNodes];
 
        System.out.println("Enter the graph matrix");
        for (int sVert = 0; sVert < numberOfNodes; sVert++)
        {
           for (int dVert = 0; dVert < numberOfNodes; dVert++)
           {
               graph[sVert][dVert] = scanner.nextInt();
           }
        }
 
        System.out.println("Enter the source of the graph");
        source= scanner.nextInt();
 
        System.out.println("Enter the sink of the graph");
        sink = scanner.nextInt();
 
        NetworkFlow fordFulkerson = new NetworkFlow(numberOfNodes);
        maxFlow = fordFulkerson.findMaxFlow(graph, source, sink);
        System.out.println("The Max Flow is " + maxFlow);
        scanner.close();        
    }   
}
