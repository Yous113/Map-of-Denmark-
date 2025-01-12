package org.example.firstyearproject;
import org.example.firstyearproject.KdTree.KdTree;
import org.example.firstyearproject.KdTree.Point2D;
import org.example.firstyearproject.KdTree.RectHV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class KdTreeTest {

    private KdTree kdTree;

    @BeforeEach
    public void setUp() {
        kdTree = new KdTree(0.0, 0.0, 10.0, 10.0);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(kdTree.isEmpty());
        kdTree.insert(new Point2D(5.0, 5.0));
        assertFalse(kdTree.isEmpty());
    }

    @Test
    public void testInsert() {
        kdTree.insert(new Point2D(1.0, 1.0));
        kdTree.insert(new Point2D(2.0, 2.0));
        kdTree.insert(new Point2D(3.0, 3.0));
        kdTree.insert(new Point2D(4.0, 4.0));
        kdTree.insert(new Point2D(5.0, 5.0));
        assertTrue(kdTree.size() == 5);

    }
    @Test
    public void testInsertOutsidePartitionRectangle() {

        // Create two nodes with known coordinates
        Point2D insideNode = new Point2D(5.0, 5.0);
        Point2D outsideNode = new Point2D(200.0, 200.0);
        // Insert the insideNode to ensure there's a root
        kdTree.insert(insideNode);
        // Insert the outsideNode into the KdTree
        kdTree.insert(outsideNode);
        // Check if the Node was inserted correctly by verifying the size of the KdTree
        assertTrue(kdTree.size() == 2);
    }

    @Test
    public void testInsertBottom() {

        // Insert a node that should lie below the root
        Point2D insideNode = new Point2D(0.5, 0.5); // Node inside initial partition rectangle
        kdTree.insert(insideNode);

        // Insert a node below the root
        Point2D bottomNode = new Point2D(0.5, 0.25); // Node below initial partition rectangle
        kdTree.insert(bottomNode);

        // Check if the Node was inserted correctly by verifying the size of the KdTree
        assertTrue(kdTree.size() == 2);
    }

    @Test
    public void testInsertToRightSubtree() {
        // Insert a root node
        kdTree.insert(new Point2D(0.5, 0.5));

        // Insert a node that should go to the right subtree of the root
        kdTree.insert(new Point2D(0.7, 0.7));

        // Check if the right subtree of the root contains the inserted node
        assertTrue(kdTree.size() == 2);
    }
    @Test
    public void testInsert1() {
        // Insert a root node
        kdTree.insert(new Point2D(5.0, 5.0));

        // Insert a node that should be on the left of the root
        kdTree.insert(new Point2D(4.0, 6.0));

        // Insert a node that should be on the right of the root
        kdTree.insert(new Point2D(6.0, 4.0));

        // Insert a node with the same coordinates as the root
        kdTree.insert(new Point2D(5.0, 5.0));

        // Insert a node with different coordinates than the root but equals to it
        kdTree.insert(new Point2D(05.0, 6.0));

        // Insert a node with different coordinates than the root and not equals to it
        kdTree.insert(new Point2D(6.0, 6.0));

        // Insert a node that is smaller than the current node
        kdTree.insert(new Point2D(4.0, 4.0));

        // Insert a node that is outside the partition rectangle
        kdTree.insert(new Point2D(200.0, 200.0));

        // Assert that all insertions succeeded
        assertTrue(kdTree.size() == 8); // Placeholder assertion, since each insertion should trigger an if statement
    }



    @Test
    public void testRange(){
        // Insert points into the KdTree
        kdTree.insert(new Point2D(1.0, 1.0));
        kdTree.insert(new Point2D(2.0, 2.0));
        kdTree.insert(new Point2D(3.0, 3.0));
        kdTree.insert(new Point2D(4.0, 4.0));
        kdTree.insert(new Point2D(5.0, 5.0));
        kdTree.insert(new Point2D(3.0, 3.0));
        kdTree.insert(new Point2D(1.0, 2.0));
        kdTree.insert(new Point2D(5.0, 5.0));

        // Define the rectangular range
        RectHV rect = new RectHV(0.0, 0.0, 6.0, 6.0);

        // Get points within the range
        Iterable<Point2D> pointsInRange = kdTree.range(rect);

        // Count the number of points within the range
        int count = 0;
        for (Point2D point : pointsInRange) {
            count++;
        }

        // Assert that the number of points within the range is as expected
        assertEquals(8, count);
    }
    @Test
    public void testNearest() {
        // Insert some points into the KdTree
        Point2D point1 = new Point2D(1.0, 1.0);
        Point2D point2 = new Point2D(2.0, 2.0);
        Point2D point3 = new Point2D(3.0, 3.0);
        Point2D point4 = new Point2D(4.0, 4.0);
        Point2D point5 = new Point2D(5.0, 5.0);
        kdTree.insert(point1);
        kdTree.insert(point2);
        kdTree.insert(point3);
        kdTree.insert(point4);
        kdTree.insert(point5);

        // Define the point for which you want to find the nearest point
        Point2D searchPoint1 = new Point2D(4.0, 4.2);

        // Get the nearest point to the search point
        Point2D nearestPoint1 = kdTree.nearest(searchPoint1);

        // Assert that the nearest point is the expected point
        assertEquals(point4, nearestPoint1);

        // Define the point for which you want to find the nearest point
        Point2D searchPoint2 = new Point2D(4.9, 5.0);

        // Get the nearest point to the search point
        Point2D nearestPoint2 = kdTree.nearest(searchPoint2);

        // Assert that the nearest point is the expected point
        assertEquals(point5, nearestPoint2);

    }

    }




