package org.example.firstyearproject;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class TestNode {

    @Test
    void testConstructor() {
        Node node = new Node(2, 2);
        assertNotNull(node);
    }
}
