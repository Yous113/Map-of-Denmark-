package org.example.firstyearproject;

import java.io.Serializable;
import java.util.List;

public class MultiPolygon implements Serializable {
    double[] xcoords;
    double[] ycoords;
    MultiPolygon(List<Double> xcoords, List<Double> ycoords){
        this.xcoords = xcoords.stream().mapToDouble(Double::doubleValue).toArray();
        this.ycoords = ycoords.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public double[] getXcoords() {
        return xcoords;
    }

    public double[] getYcoords() {
        return ycoords;
    }
}
