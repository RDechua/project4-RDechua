package graph;

/** Class Dijkstra. Implementation of Dijkstra's algorithm for finding the shortest path
 * between the source vertex and other vertices in the graph.
 *  Fill in code. It is ok to add additional helper methods / classes.
 *  To get full credit, must add a class representing a Priority queue.
 *  You can still get 90% if you do not use a priority queue.
 */


import org.w3c.dom.Node;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dijkstra {
    private Graph graph; // stores the graph of CityNode-s and edges connecting them
    private List<Integer> shortestPath = null; // nodes that are part of the shortest path

    private Object[][] table;

    /** Constructor
     *
     * @param filename name of the file that contains info about nodes and edges
     * @param graph graph
     */
    public Dijkstra(String filename, Graph graph) {
        this.graph = graph;
        graph.loadGraph(filename);
    }

    /**
     * Returns the shortest path between the origin vertex and the destination vertex.
     * The result is stored in shortestPathEdges.
     * This function is called from GUIApp, when the user clicks on two cities.
     * @param origin source node
     * @param destination destination node
     * @return the ArrayList of nodeIds (of nodes on the shortest path)
     */
    public List<Integer> computeShortestPath(CityNode origin, CityNode destination) {
        int numNodes = graph.numNodes();
        int sourceId = graph.getId(origin);
        int destId = graph.getId(destination);

        table = new Object[numNodes][3];
        for (int i = 0; i < numNodes; i++) {
            table[i][0] = Integer.MAX_VALUE;
            table[i][1] = null;
            table[i][2] = false;
        }
        table[sourceId][0] = 0;

        PriorityQueue pq = new PriorityQueue(numNodes);
        pq.insert(sourceId, 0);

        while (!pq.isEmpty()) {
            int u = pq.removeMin();
            table[u][2] = true;

            if (u == destId) {
                break;
            }

            Edge edge = graph.getAdjacencyList(u);

            while (edge != null) {
                int v = edge.getNeighbor();
                int cost = edge.getCost();
                if (!((boolean) table[v][2]) &&
                        ((int) table[u][0] + cost < (int) table[v][0])) {
                    table[v][0] = (int) table[u][0] + cost;
                    table[v][1] = u;
                    if (pq.contains(v)) {
                        pq.reduceKey(v, (int) table[v][0]);
                    } else {
                        pq.insert(v, (int) table[v][0]);
                    }
                }
                edge = edge.getNext();
            }
        }

        shortestPath = new ArrayList<Integer>();

        while (destId != sourceId) {
            shortestPath.add(0, destId);
            destId = (int) table[destId][1];
        }


        shortestPath.add(0, sourceId);


        System.out.println(table[shortestPath.get(shortestPath.size()-1)][0]);
        return shortestPath;
    }

    /**
     * Return the shortest path as a 2D array of Points.
     * Each element in the array is another array that has 2 Points:
     * these two points define the beginning and end of a line segment.
     * @return 2D array of points
     */
    public Point[][] getPath() {
        if (shortestPath == null)
            return null;
        return graph.getPath(shortestPath); // delegating this task to the Graph class
    }

    /** Set the shortestPath to null.
     *  Called when the user presses Reset button.
     */
    public void resetPath() {
        shortestPath = null;
    }

}