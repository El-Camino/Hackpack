import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


/**
 * Created by Team El Camino
 */
public class edmonds_karp_max_flow {
	static int MAX_V = 40, INF = (int) 1e9, mf, f, s, t;
	static int[][] res = new int[MAX_V][];
	static ArrayList<Integer> path = new ArrayList<Integer>();

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int V, k, vertex, weight;
		V = in.nextInt(); // Number of vertices
		s = in.nextInt(); // Source index
		t = in.nextInt(); // Sink index
		for (int i = 0; i < V; i++) {
			res[i] = new int[MAX_V];
			k = in.nextInt(); // Number of vertices connected to current vertex
			for (int j = 0; j < k; j++) {
				vertex = in.nextInt();
				weight = in.nextInt();
				res[i][vertex] = weight;
			}
		}
		mf = 0;
		while (true) { // run O(VE^2) Edmonds Karp
			f = 0;
			Queue<Integer> q = new LinkedList<Integer>();
			ArrayList<Integer> dist = new ArrayList<Integer>();
			dist.addAll(Collections.nCopies(V, INF));
			q.offer(s);
			dist.set(s, 0);
			path.clear();
			path.addAll(Collections.nCopies(V, -1));
			while (!q.isEmpty()) {
				int u = q.poll();
				if (u == t)
					break;
				for (int v = 0; v < MAX_V; v++)
					if (res[u][v] > 0 && dist.get(v) == INF) {
						dist.set(v, dist.get(u) + 1);
						q.offer(v);
						path.set(v, u);
					}
			}
			augment(t, INF);
			if (f == 0)
				break;
			mf += f;

		}
		System.out.println("Max Flow = " + mf);
		in.close();
	}

	static void augment(int v, int minEdge) {
		if (v == s) {
			f = minEdge;
			return;
		} else if (path.get(v) != -1) {
			augment(path.get(v), Math.min(minEdge, res[path.get(v)][v]));
			res[path.get(v)][v] -= f;
			res[v][path.get(v)] += f;
		}
	}
}
