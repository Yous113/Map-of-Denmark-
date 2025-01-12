package org.example.firstyearproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestMultiPolygon {
    MultiPolygon multiPolygon;
    List<Double> xcoords = new ArrayList<>();
    List<Double> ycoords = new ArrayList<>();

    @BeforeEach
    void setUp() {
        xcoords.add(1.0);
        xcoords.add(3.0);
        xcoords.add(3.0);
        xcoords.add(5.0);
        xcoords.add(5.0);
        xcoords.add(7.0);
        ycoords.add(3.0);
        ycoords.add(0.0);
        ycoords.add(6.0);
        ycoords.add(0.0);
        ycoords.add(6.0);
        ycoords.add(3.0);
        multiPolygon = new MultiPolygon(xcoords,ycoords);
    }

    @Test
    void testConstructor() {
        assertNotNull(multiPolygon);
    }
    @Test
    void testGetXcoords() {
        List<Double> x = new ArrayList<>();
        for(Double d: multiPolygon.getXcoords())
            x.add(d);
        assertTrue(x.containsAll(xcoords));
    }@Test
    void testGetYcoords() {
        List<Double> y = new ArrayList<>();
        for(Double d: multiPolygon.getYcoords())
            y.add(d);
        assertTrue(y.containsAll(ycoords));
    }

}