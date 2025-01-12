package org.example.firstyearproject;

import org.example.firstyearproject.Node;
import org.junit.jupiter.api.Test;
import org.example.firstyearproject.ShortestPath.CordtoKM;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CordtoKMTest {

    @Test
    public void testCordToKM() {
        // Create two nodes with known coordinates
        Node node1 = new Node(10, 10, 1L);
        Node node2 = new Node(20, 20, 2L);

        // Create an instance of CordtoKM
        CordtoKM cordtoKM = new CordtoKM();

        // Calculate the distance between the two nodes
        double distance = cordtoKM.cordToKM(node1, node2);

        // Expected distance between the two nodes (in kilometers)
        double expectedDistance = 1544.0;

        // Assert that the calculated distance matches the expected distance
        assertEquals(expectedDistance, distance, 1.0); // Delta is set to 1.0 for some margin of error
    }
}
