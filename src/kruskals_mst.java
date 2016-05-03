import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Team El Camino
 * 
 * This class was implemented based on algorithmic descriptions from the following source:
 * http://www.geeksforgeeks.org/greedy-algorithms-set-2-kruskals-minimum-spanning-tree-mst/
 */

public class kruskals_mst {
	static int V, E;
	static edge edges[];

	static class edge implements Comparable<edge> {
		int source, dest, weight;

		public edge(int s, int d, int w) {
			source = s;
			dest = d;
			weight = w;
		}

		public int compareTo(edge o) {
			return this.weight - o.weight;
		}
	}

	static class subset {
		int parent, rank;

		public subset(int p) {
			this.parent = p;
			rank = 0;
		}
	}

	// Compresses path to find the set that element i is in
	static int find(subset subsets[], int i) {
		if (subsets[i].parent == i)
			return i;
		return subsets[i].parent = find(subsets, subsets[i].parent);
	}

	// Unions sets x and y
	static void union(subset subsets[], int x, int y) {
		int xRoot = find(subsets, x);
		int yRoot = find(subsets, y);

		// Make larger tree the new root node for both sets
		if (subsets[xRoot].rank < subsets[yRoot].rank)
			subsets[xRoot].parent = yRoot;
		else {
			subsets[yRoot].parent = xRoot;
			if (subsets[xRoot].rank == subsets[yRoot].rank)
				subsets[xRoot].rank++;
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		/*
		 * Test Case: 4 5 0 1 10 0 2 6 0 3 5 1 3 15 2 3 4
		 * 
		 * Output: Edges in the minimum spanning tree: 2 <--> 3 with weight: 4 0
		 * <--> 3 with weight: 5 0 <--> 1 with weight: 10 Weight of the minimum
		 * spanning tree: 19
		 */
		V = in.nextInt(); // Number of vertices
		E = in.nextInt(); // Number of edges
		edges = new edge[E];
		for (int i = 0; i < E; i++) {
			int source = in.nextInt();
			int dest = in.nextInt();
			int weight = in.nextInt();
			edges[i] = new edge(source, dest, weight);
		}
		kruskals();
		in.close();
	}

	// Run O(ElogE) Kruskal's algorithm to find minimum spanning tree
	static void kruskals() {
		Arrays.sort(edges);
		// Create a subset for each vertex and array to store final MST
		subset subsets[] = new subset[V];
		edge mst[] = new edge[V];
		for (int i = 0; i < V; i++) {
			subsets[i] = new subset(i);
		}

		int numE = 0; // number of edges in the final MST
		int nextIdx = 0;
		while (numE < V - 1) {
			// Take next smallest edge
			edge nextEdge = edges[nextIdx++];
			int xRoot = find(subsets, nextEdge.source);
			int yRoot = find(subsets, nextEdge.dest);
			// If these vertices aren't already connected use them for the MST
			if (xRoot != yRoot) {
				mst[numE++] = nextEdge;
				union(subsets, xRoot, yRoot);
			}
		}

		// print the edges in final MST and total weight
		int minWeight = 0;
		System.out.println("Edges in the minimum spanning tree:");
		for (int i = 0; i < numE; i++) {
			System.out.println(mst[i].source + " <--> " + mst[i].dest + " with weight: " + mst[i].weight);
			minWeight += mst[i].weight;
		}
		System.out.println("Weight of the minimum spanning tree: " + minWeight);
	}
}