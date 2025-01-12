package org.example.firstyearproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * The Node class is used to represent a node in the map
 */
public class Node implements Serializable {

    double lat, lon, x, y;

    Long id;

    Boolean oneWay;
    private  Boolean bicycle;

    Boolean marked = false;

    /**
     * Constructor for the Node class
     * @param lat
     * @param lon
     * @param id
     */
    public Node(double lat, double lon, Long id) {
        this.lat = lat;
        this.lon = lon;
        this.id = id;
        this.x = 0.56 * lon;
        this.y = -lat;
        oneWay = false;
        bicycle = false;
    }

    public Node(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.id = null;
        this.x = 0.56 * lon;
        this.y = -lat;
        oneWay = false;
        bicycle = false;
    }
    public double getLat(){
        return this.lat;
    }
    public double getLong(){
        return this.lon;
    }

    public Long getID(){
        return id;
    }

    public void setOneWay(){
        oneWay = true;
    }
    public boolean isOneWay(){
         return oneWay;
    }


    /**
     * Calculates the distance between two nodes
     * @param goalNode
     * @return
     */
    // returnere afstand fra en startnode til den sidste node i ruten
    public double heuristic(Node goalNode) {

        double dx = goalNode.getLat() - this.lat;
        double dy = goalNode.getLong() - this.lon;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void isbicycle(){
        this.bicycle = true;
    }
    public boolean getBicycle(){
        return bicycle;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(boolean b) {
        marked = b;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);

        gc.fillOval(x, y, 0.00004, 0.00004);
    }
//0.00000
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


}

