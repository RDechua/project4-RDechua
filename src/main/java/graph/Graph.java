package graph;


/** A class that represents a graph where nodes are cities (CityNode).
 * The cost of each edge connecting two cities is the distance between the cities.
 * Fill in code in this class. You may add additional methods and variables.
 */

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    public final int EPS_DIST = 5;
    private int numNodes;     // total number of nodes
    private int numEdges; // total number of edges
    private CityNode[] nodes; // array of nodes of the graph
    private Edge[] adjacencyList; // adjacency list; for each vertex stores a linked list of edges
    private Map<String, Integer> labelsToIndices; // a HashMap that maps each city to the corresponding node id

    /**
     * Read graph info from the given file, and create nodes and edges of
     * the graph.
     *
     * @param filename name of the file that has nodes and edges
     */
    public void loadGraph(String filename) {
        // FILL IN CODE
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) { // Go through each line in the file and add it into the tree
            String str;
            int i = 0;
            numEdges = 0;
            labelsToIndices = new HashMap<String, Integer>();

            if((str = br.readLine()) != null){
                if(str.equals("NODES")){
                    numNodes = Integer.parseInt(br.readLine());
                }
            }
            nodes = new CityNode[numNodes];
            adjacencyList = new Edge[numNodes];

            while ((str = br.readLine()) != null && !str.equals("ARCS")) {
                String[] temp = str.split(" ");
                CityNode node = new CityNode(temp[0], Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
                labelsToIndices.put(temp[0], i++);
                addNode(node);
            }


            while((str = br.readLine()) != null){
                String[] temp = str.split(" ");
                Edge edge1 = new Edge(labelsToIndices.get(temp[1]), Integer.parseInt(temp[2]));
                Edge edge2 = new Edge(labelsToIndices.get(temp[0]), Integer.parseInt(temp[2]));
                addEdge(labelsToIndices.get(temp[0]), edge1);
                addEdge(labelsToIndices.get(temp[1]), edge2);
                numEdges += 2;
            }
            System.out.println(numEdges);


        }catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Add a node to the array of nodes.
     * Increment numNodes variable.
     * Called from loadGraph.
     *
     * @param node a CityNode to add to the graph
     */
    public void addNode(CityNode node) {
        // FILL IN CODE
        nodes[getId(node)] = node;
    }

    /**
     * Return the number of nodes in the graph
     * @return number of nodes
     */
    public int numNodes() {
        return numNodes;
    }

    /**
     * Adds the edge to the linked list for the given nodeId
     * Called from loadGraph.
     *
     * @param nodeId id of the node
     * @param edge edge to add
     */
    public void addEdge(int nodeId, Edge edge) {
        // FILL IN CODE
        Edge head = adjacencyList[nodeId];
        adjacencyList[nodeId] = edge; // insert in front
        if (head != null) {
            edge.setNext(head);
        }
    }

    /**
     * Returns an integer id of the given city node
     * @param city node of the graph
     * @return its integer id
     */
    public int getId(CityNode city) {
        return labelsToIndices.get(city.getCity()); // Don't forget to change this
    }

    /**
     * Return the edges of the graph as a 2D array of points.
     * Called from GUIApp to display the edges of the graph.
     *
     * @return a 2D array of Points.
     * For each edge, we store an array of two Points, v1 and v2.
     * v1 is the source vertex for this edge, v2 is the destination vertex.
     * This info can be obtained from the adjacency list
     */
    public Point[][] getEdges() {
        if (adjacencyList == null || adjacencyList.length == 0) {
            // System.out.println("Adjacency list is empty. Load the graph first.");
            return null;
        }
        Point[][] edges2D = new Point[numEdges][2];
        // FILL IN CODE
        int j = 0;
        for(int i = 0; i < adjacencyList.length; i++){
            Edge curr = adjacencyList[i];
            while(curr != null){
                edges2D[j][0] = new Point(nodes[i].getLocation());
                edges2D[j][1] = new Point(nodes[curr.getNeighbor()].getLocation());
                curr = curr.getNext();
                j++;
            }
        }

        return edges2D;
    }

    /**
     * Get the nodes of the graph as a 1D array of Points.
     * Used in GUIApp to display the nodes of the graph.
     * @return a list of Points that correspond to nodes of the graph.
     */
    public Point[] getNodes() {
        if (nodes == null) {
            // System.out.println("Array of nodes is empty. Load the graph first.");
            return null;
        }
        Point[] nodes = new Point[this.nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = this.nodes[i].getLocation();
        }
        return nodes;
    }

    /**
     * Used in GUIApp to display the names of the airports.
     * @return the list that contains the names of cities (that correspond
     * to the nodes of the graph)
     */
    public String[] getCities() {
        if (this.nodes == null) {
            System.out.println("Graph has no nodes. Write loadGraph method first. ");
            return null;
        }
        String[] labels = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            labels[i] = nodes[i].getCity();
        }
        return labels;
    }

    /** Take a list of node ids on the path and return an array where each
     * element contains two points (an edge between two consecutive nodes)
     * @param pathOfNodes A list of node ids on the path
     * @return array where each element is an array of 2 points
     */
    public Point[][] getPath(List<Integer> pathOfNodes) {
        int i = 0;
        Point[][] edges2D = new Point[pathOfNodes.size()-1][2];
        // Each "edge" is an array of size two (one Point is origin, one Point is destination)
        // FILL IN CODE

        return edges2D;
    }

    /**
     * Return the CityNode for the given nodeId
     * @param nodeId id of the node
     * @return CityNode
     */
    public CityNode getNode(int nodeId) {
        return nodes[nodeId];
    }

    /**
     * Take the location of the mouse click as a parameter, and return the node
     * of the graph at this location. Needed in GUIApp class. No need to modify.
     * @param loc the location of the mouse click
     * @return reference to the corresponding CityNode
     */
    public CityNode getNode(Point loc) {
        if (nodes == null) {
            System.out.println("No node at this location. ");
            return null;
        }
        for (CityNode v : nodes) {
            Point p = v.getLocation();
            if ((Math.abs(loc.x - p.x) < EPS_DIST) && (Math.abs(loc.y - p.y) < EPS_DIST))
                return v;
        }
        return null;
    }

}