package org.example.firstyearproject;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.example.firstyearproject.ShortestPath.EdgeWeightedDigraph;
import org.example.firstyearproject.ShortestPath.DirectedEdge;
import org.example.firstyearproject.Node;
import org.example.firstyearproject.ShortestPath.Astar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShortestPathTest {

    private Node A;
    private Node B;
    private Node C;
    private Node D;
    private Node E;
    private EdgeWeightedDigraph graph;

    HashMap<Long, Node> id2node;

    private DirectedEdge AB;
    private DirectedEdge AC;
    private DirectedEdge BE;
    private DirectedEdge CB;
    private DirectedEdge CD;
    private DirectedEdge DE;


    @BeforeEach
    public void setUp() {
        // Initialize objects before each test method
        A = new Node(0, 0, 0L);
        B = new Node(1, 1, 1L);
        C = new Node(2, 2, 2L);
        D = new Node(3, 3, 4L);
        E = new Node(5, 5, 5L);
        id2node = new HashMap<>();
        id2node.put(0L, A);
        id2node.put(1L, B);
        id2node.put(2L, C);
        id2node.put(3L, D);
        id2node.put(4L, E);
        graph = new EdgeWeightedDigraph(5);
        AB = new DirectedEdge(0L, 1L, 4.0);
        AC = new DirectedEdge(0L,2L, 1.0);
        BE = new DirectedEdge(1L, 4L, 4.0);
        CB = new DirectedEdge(2L, 1L, 2.0);
        CD = new DirectedEdge(2L, 3L, 2.0);
        DE = new DirectedEdge(3L, 4L, 3.0);
        graph.addEdge(AB);
        graph.addEdge(AC);
        graph.addEdge(BE);
        graph.addEdge(CB);
        graph.addEdge(CD);
        graph.addEdge(DE);

    }

    @Test
    void testFindNeighborsAStar() {
        Astar astar = new Astar();
        List<Long> shortestPath = astar.findNeighborsAStar(0L, 4L, graph, id2node);
        assertTrue(shortestPath.contains(4L) && shortestPath.contains(3L) && shortestPath.contains(2L));
        // testing that reconstructPath reverses to the right order:
        assertTrue(shortestPath.get(0) == 2L && shortestPath.get(1) == 3L && shortestPath.get(2) == 4L);
    }


}
