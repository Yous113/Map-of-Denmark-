package org.example.firstyearproject;

        import javafx.scene.canvas.GraphicsContext;
        import javafx.scene.canvas.Canvas;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

        import static org.junit.jupiter.api.Assertions.*;

public class TestWay {

    private Way way;
    private ArrayList<Node> nodes;

    @BeforeEach
    void setUp() {
        // Initialize some nodes
        nodes = new ArrayList<>(Arrays.asList(new Node(2, 2), new Node(3, 3), new Node(6, 5)));
    }

    @Test
    void testConstructor1() {
        way = new Way(nodes, "road");
        assertNotNull(way);
    }
    @Test
    void testConstructor2() {
        way = new Way(nodes, "road", "grassland", "wood");
        assertNotNull(way);
    }
    @Test
    void testConstructor3() {
        way = new Way(nodes, "road",10);
        assertNotNull(way);
    }
    @Test
    void testConstructor4() {
        way = new Way(nodes, "road",10, "grassland", "wood");
        assertNotNull(way);
    }

    @Test
    void testcheckForNoSpeedLimitInfo() {
        way = new Way(nodes, "motorway_junction");
        assertTrue(way.getSpeedlimit() == 80);
        way = new Way(nodes, "motorway");
        assertTrue(way.getSpeedlimit() == 110);
    }

    @Test
    void testGetYcoords() {
        way = new Way(nodes, "road");
        assertEquals(3, way.getYcoords().length);
    }

    @Test
    void testGetXcoords() {
        way = new Way(nodes, "road");
        assertEquals(3, way.getXcoords().length);
    }

    @Test
    void testGetClosestNode(){
        way = new Way(nodes, "road");
        Node referenceNode = new Node(5, 5);
        Node closestNode = way.getClosestNode(referenceNode);
        assertTrue(closestNode == nodes.get(2)); //the node that is 6,5
    }

    @Test
    void testGetNodes(){
        way = new Way(nodes, "road");
        assertTrue(way.getNodes().size() == 3);
    }

    @Test
    void testGetLanduse(){
        way = new Way(nodes, "road", "grassland", "wood");
        assertTrue(way.getLanduse().equals("wood"));
    }
    @Test
    void testGetNatural(){
        way = new Way(nodes, "road", "grassland", "wood");
        assertTrue(way.getNatural().equals("grassland"));
    }
    @Test
    void testGetWayType(){
        way = new Way(nodes, "road");
        assertTrue(way.getWayType().equals("road"));

    }


}