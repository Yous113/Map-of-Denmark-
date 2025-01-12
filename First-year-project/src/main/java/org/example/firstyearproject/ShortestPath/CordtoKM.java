package org.example.firstyearproject.ShortestPath;

import Helpers.Generated;
import org.example.firstyearproject.Node;

import java.io.Serializable;

public class CordtoKM implements Serializable {
    @Generated
    double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
    public double cordToKM(Node n1, Node n2) {
        double startLat = n1.getLat();
        double startLong = n1.getLong();
        double endLat = n2.getLat();
        double endLong = n2.getLong();

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c;
    }
}
