import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;

class Edge implements Comparable<Edge>
{
    public int source;
    public int dest;
    public int weight;
    
    public Edge ( int dest, int weight )
    {
        this.dest = dest;
        this.weight = weight;
    }
    
    public Edge ( int source, int dest, int weight )
    {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int compareTo( Edge other )
    {
        return this.weight - other.weight;
    }
}

class Vertex implements Comparable<Vertex>
{
    public int inDegree = 0;
    public ArrayList<Vertex> children = new ArrayList<Vertex>();
    
    @Override
    public int compareTo( Vertex other )
    {
        return this.inDegree - other.inDegree;
    }
}

public class GraphTheory
{
    static final int inf = (int)1e9;

    public static void main( String[] args )
    {          
    }
    
    /**
     * Input Processing
     */   
    
    // Create a 2D adjacency matrix from a list of edges.
    public static int[][] BuildMatrixFromInput( boolean directed, boolean weighted )
    {
        Scanner in = new Scanner(System.in);
        
        // Scan in the number of vertices and edges.
        int numVerts = in.nextInt();
        int numEdges = in.nextInt();
        
        // Create shell of matrix.
        int[][] graph = new int[numVerts][numVerts];
        
        // Mark all edges with "infinite" distance.
        // NOTE: An edge between a vertex and itself should remain '0'.
        for ( int i = 0; i < graph.length; i++ )
        {
            for ( int j = 0; j < graph.length; j++ )
            {
                if ( i != j )
                {
                    graph[i][j] = inf;
                }
            }
        }
        
        // The remaining input should be a list of pairs (with optional weights) representing edges.
        // Create adjacency matrix from the input.
        for ( int i = 0; i < numEdges; i++ )
        {
            int first = in.nextInt();
            int second = in.nextInt();
            int weight = 1;
            
            // If weighted, treat the third number on the line as the weight.
            if ( weighted )
            {
                weight = in.nextInt();
            }
            
            graph[first][second] = weight;
            
            // If undirected, update the weight of the reverse edge with the same value.
            if ( !directed )
            {
                graph[second][first] = weight;
            }
        }
        
        return graph;
    }
    
    // Given a list of edges, build an array of vertices to represent the graph.
    public static Vertex[] BuildVertexArrayFromInput()
    {
        Scanner scanner = new Scanner(System.in);
               
        int numVerts = scanner.nextInt();
        int numEdges = scanner.nextInt();
        
        // Create array of vertices.
        Vertex[] verts = new Vertex[numVerts];
        for ( int i = 0; i < numVerts; i++ )
        {
            verts[i] = new Vertex();
        }
        
        for ( int i = 0; i < numEdges; i++ )
        {
            int first = scanner.nextInt();
            int second = scanner.nextInt();
            
            verts[first].children.add(verts[second]);
            verts[second].inDegree++;
        }     
        
        return verts;
    }
    
    // Given a list of edges, build an array of edge lists to represent the graph.
    public static ArrayList<Edge>[] BuildEdgeListArrayFromInput( boolean directed, boolean weighted )
    {
        Scanner scanner = new Scanner(System.in);
               
        int numVerts = scanner.nextInt();
        int numEdges = scanner.nextInt();
        
        // Create array of edge lists. Each list corresponds to a vertex.
        ArrayList<Edge>[] verts = new ArrayList[numVerts];
        for ( int i = 0; i < numVerts; i++ )
        {
            verts[i] = new ArrayList<Edge>();
        }
        
        for ( int i = 0; i < numEdges; i++ )
        {
            int first = scanner.nextInt();
            int second = scanner.nextInt();
            int weight = 1;
            
            if ( weighted )
            {
                weight = scanner.nextInt();
            }
            
            verts[first].add(new Edge(first, second, weight));
            
            if ( !directed )
            {
                verts[second].add(new Edge(second, first, weight));
            }
        }     
        
        return verts;
    }
    
    /**
     * Algorithms
     */
    
    // Detect if a cycle exists in a graph. If true is returned, there is a cycle.
    public static boolean DetectCycle( Vertex[] graph )
    {
        ArrayList<Vertex> tpList = RunTopoSort(graph);
        
        return tpList.size() != graph.length;        
    }
    
    // Create a topologically sorted list of vertices.
    public static ArrayList<Vertex> RunTopoSort( Vertex[] graph )
    {
        PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
        ArrayList<Vertex> tpList = new ArrayList<Vertex>();
        
        queue.addAll(Arrays.asList(graph));
        
        while ( !queue.isEmpty() && (0 == queue.peek().inDegree) )
        {
            ArrayList<Vertex> vertsToRemove = new ArrayList<Vertex>();
            while ( !queue.isEmpty() && 0 == queue.peek().inDegree )
            {
                vertsToRemove.add(queue.remove());
            }
            
            // Update indegrees.
            for ( Vertex vert : vertsToRemove )
            {
                for ( Vertex child : vert.children )
                {
                    queue.remove(child);
                    child.inDegree--;
                    queue.add(child);
                }
            }
            
            tpList.addAll(vertsToRemove);                
        }
        
        return tpList;
    }
    
    // Determine the shortest paths between all nodes in the graph.
    // Runtime: O(n^3)
    public static int[][] RunFloyds( int[][] graph )
    {
        // Copy the graph to the shortestPaths matrix.
        int[][] shortestPaths = CopyGraph(graph);
        
        // Run the Floyd-Warshall's algorithm.
        for ( int k = 0; k < graph.length; k++ )
        {
            for ( int i = 0; i < graph.length; i++ )
            {
                for ( int j = 0; j < graph.length; j++ )
                {
                    shortestPaths[i][j] = Math.min(shortestPaths[i][j], shortestPaths[i][k] + shortestPaths[k][j]);
                }
            }
        }
        
        return shortestPaths;
    }
    
    // Run Djistra's algorithm to determine shortest distance from a source vertex
    // to all other vertices in the graph. Do not use a priority queue.
    // Runtime: O(n^2)
    public static int[] RunDijkstrasWithoutQueue( int[][] graph, int source )
    {        
        Set<Integer> unvisited = new HashSet<Integer>();
        int[] minDistances = new int[graph.length];
        
        // Populate the initial values of the minDistances array.
        // The distance from the source to itself should be 0, and the distance to
        // every other node should be infinity.
        for ( int i = 0; i < minDistances.length; i++ )
        {
            minDistances[i] = inf;
            unvisited.add(i);
        }
        minDistances[source] = 0;

        while ( !unvisited.isEmpty() )
        {
            // Find the vertex with the smalled distance to the source.
            // Source node will be selected first.
            int next = -1;
            int theMinDist = inf;
            for ( Integer i : unvisited )
            {
                if ( minDistances[i] <= theMinDist )
                {
                    next = i;
                    theMinDist = minDistances[i];
                }
            }

            // Remove the selected vertex from the unvisited set.
            unvisited.remove(next);
            
            // Update the minDistance array with new minimum values. If the sum of the min distance from the source
            // vertex to the selected vertex and the weight of the edge from the selected vertex to the vertex 'i'
            // is smaller than the min distance from the source vertex to the vertex 'i', update the min distance.
            for ( int i = 0; i < graph.length; i++ )
            {
                minDistances[i] = Integer.min(minDistances[i], minDistances[next] + graph[next][i]);
            }
        }       
        
        return minDistances;
    }
    
    // Run Djistra's algorithm to determine shortest distance from a source vertex
    // to all other vertices in the graph. Use a priority queue.
    // Runtime: O(n^2)
    public static int[] RunDijkstrasWithQueue( int[][] graph, int source )
    {        
        Set<Integer> unvisited = new HashSet<Integer>();
        int[] minDistances = new int[graph.length];
        PriorityQueue<Edge> queue = new PriorityQueue<Edge>();
        Edge[] destVerts = new Edge[graph.length];
        
        // Populate the initial values of the minDistances array.
        // The distance from the source to itself should be 0, and the distance to
        // every other node should be infinity.
        for ( int i = 0; i < minDistances.length; i++ )
        {
            minDistances[i] = inf;
            destVerts[i] = new Edge(i, inf);
            unvisited.add(i);
            queue.add(destVerts[i]);
        }
        minDistances[source] = 0;
        destVerts[source].weight = 0;

        while ( !unvisited.isEmpty() )
        {
            // Find the vertex with the smalled distance to the source.
            // Source node will be selected first.
            int next = queue.poll().dest;

            // Remove the selected vertex from the unvisited set.
            unvisited.remove(next);
            
            // Update the minDistance array with new minimum values. If the sum of the min distance from the source
            // vertex to the selected vertex and the weight of the edge from the selected vertex to the vertex 'i'
            // is smaller than the min distance from the source vertex to the vertex 'i', update the min distance.
            for ( int i = 0; i < graph.length; i++ )
            {
                int sum = minDistances[next] + graph[next][i];
                if ( sum < minDistances[i] )
                {
                    minDistances[i] = sum;

                    // Update the priority queue.
                    queue.remove(destVerts[i]);
                    destVerts[i].weight = sum;
                    queue.add(destVerts[i]);                    
                }
            }
        }       
        
        return minDistances;
    }
    
    // Run Prim's algorithm to generate the Minimum Spanning Tree for a graph.
    // Inputs: The graph (represented as array of Edge lists) and the starting vertex.
    // The sum of all weights of the MST is returned.
    // Source: Arup's notes.
    public static int RunPrims(ArrayList[] graph, int v) {

        // Mark vertex v as being in mst.
        int n = graph.length;
        boolean[] used = new boolean[n];
        used[v] = true;

        // Add all of v's edges into the priority queue.
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        for (int i=0; i<graph[v].size(); i++)
            pq.offer( ((ArrayList<Edge>)graph[v]).get(i));

        int numEdges = 0, res = 0;

        while (pq.size() > 0) {

            // Get next edge.
            Edge next = pq.poll();
            if (used[next.source] && used[next.dest]) continue;

            // Add new items to priority queue - need to check which vertex is new.
            if (!used[next.source]) {
                for (int i=0; i<graph[next.source].size(); i++)
                    pq.offer( ((ArrayList<Edge>)graph[next.source]).get(i));
                used[next.source] = true;
            }
            else {
                for (int i=0; i<graph[next.dest].size(); i++)
                    pq.offer( ((ArrayList<Edge>)graph[next.dest]).get(i));
                used[next.dest] = true;
            }

            // Bookkeeping
            numEdges++;
            res += next.weight;
            if (numEdges == n-1) break;
        }

        // -1 indicates no MST, so not connected.
        return numEdges == n-1 ? res : -1;
    }
    
    /**
     * Utility
     */
    
    public static int[][] CopyGraph( int[][] graph )
    {
        int[][] copy = new int[graph.length][graph[0].length];
        for ( int i = 0; i < graph.length; i++ )
        {
            for ( int j = 0; j < graph[0].length; j++ )
            {
                copy[i][j] = graph[i][j];
            }
        }
        
        return copy;
    }
}
