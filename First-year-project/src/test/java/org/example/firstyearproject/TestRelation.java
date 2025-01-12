package org.example.firstyearproject;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestRelation {

    private Relation relation;
    ArrayList<Way> outer;
    ArrayList<Way> inner;

    @BeforeEach
    void setUp() {
        Node nodes1 = new Node(2,2);
        Node nodes2 = new Node(3,3);
        Node nodes3 = new Node(6,5);
        Node nodes4 = new Node(1,1);
        Node nodes5 = new Node(4,4);
        Node nodes6 = new Node(5,5);
        ArrayList<Node> nodeList1 = new ArrayList<>();
        ArrayList<Node> nodeList2 = new ArrayList<>();
        nodeList1.add(nodes1);
        nodeList1.add(nodes2);
        nodeList1.add(nodes3);
        nodeList2.add(nodes4);
        nodeList2.add(nodes5);
        nodeList2.add(nodes6);
        Way way1 = new Way(nodeList1, "road");
        Way way2 = new Way(nodeList2, "road");
        outer = new ArrayList<>();
        inner = new ArrayList<>();
        outer.add(way1);
        inner.add(way2);

    }

    @Test
    void testConstructor1() {
        relation = new Relation(outer, inner, "multipolygon");
        assertTrue(relation != null);
    }

    @Test
    void testSecondConstructor2() {
        relation = new Relation(outer, inner, "multipolygon", "grassland", "wood");
        assertTrue(relation != null);
    }

    @Test
    void testColorBlind() {
        //pending
    }

}