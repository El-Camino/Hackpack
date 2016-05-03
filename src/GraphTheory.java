import java.util.*;


/**
 * Everything here was created by Team El Camino
 */

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
    public boolean visited = false;
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
        /*
            Test edge list with weights:
            4 4
            0 1 1
            1 2 1
            2 3 1
            1 3 1
         */

        // Test Bellman Ford
//        int[][] matrix = BuildMatrixFromInput(true, true);
//        System.out.println(Arrays.toString(RunBellmanFord(matrix, 0, 3)));
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
                minDistances[i] = Math.min(minDistances[i], minDistances[next] + graph[next][i]);
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

    // Run dfs to mark all children of rootVertex as visited. Also makes rootVertex as visited.
    // Inputs: The root vertex of where the search begins.
    // Nothing is returned but the graph is mutated.
    public static void RunDFSAdjacencyList(Vertex rootVertex){
        Stack<Vertex> stack = new Stack<>();
        stack.add(rootVertex);

        while(!stack.empty()){
            Vertex v = stack.pop();

            for (int i = 0; i < v.children.size(); i++) {
                if(!v.children.get(i).visited){
                    v.children.get(i).visited = true;
                    stack.push(v.children.get(i));
                }
            }
            rootVertex.visited = true;
        }
    }

    // Run bfs to mark all children of rootVertex as visited. Also makes rootVertex as visited.
    // Inputs: The root vertex of where the search begins.
    // Nothing is returned but the graph is mutated.
    public static void RunBFSAdjacencyList(Vertex rootVertex){
        ArrayDeque<Vertex> queue = new ArrayDeque<>();
        queue.add(rootVertex);
        rootVertex.visited = true;

        while(!queue.isEmpty()){
            Vertex v = queue.poll();

            for (int i = 0; i < v.children.size(); i++) {
                if(!v.children.get(i).visited){
                    v.children.get(i).visited = true;
                    queue.add(v.children.get(i));
                }
            }
        }
    }

    // Run Bellman-Ford's algorithm to determine shortest distance from a start vertex
    // to all other vertices in the graph.
    // Runtime: O(n^2)
    public static int[] RunBellmanFord(int[][] matrix, int start){
        int[] dist = new int[matrix.length];
        int[] pred = new int[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            if(i == start){
                dist[i] = 0;
            }else{
                dist[i] = inf;
                pred[i] = -1;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j] != inf){
                    if(dist[i] + matrix[i][j] < dist[j]){
                        dist[j] = dist[i] + matrix[i][j];
                        pred[j] = i;
                    }
                }
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j] != inf){
                    if(dist[i] + matrix[i][j] < dist[j]){
                        System.out.println("Cycles detected");
                    }
                }
            }
        }

        //Dist contains distance to each vertex
        return dist;
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
