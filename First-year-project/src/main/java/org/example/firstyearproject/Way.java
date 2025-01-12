package org.example.firstyearproject;

import Helpers.Generated;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.firstyearproject.ShortestPath.CordtoKM;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * The Way class is used to represent a way in the map
 */
public class Way implements Serializable {

    boolean closed;

    double[] xcoords;
    double[] ycoords;
    final String wayType;
    int speedlimit;
    ArrayList<Node> nodes;

    String natural = "";
    String landuse = "";
    Boolean building = false;

    transient Color fillcolor;
    transient Color strokecolor;

    boolean ColorBlindMode = false;
    transient Map<String, Color> wayTypesAndColor;

    /**
     * Constructor for the Way class
     * @param way
     * @param wayType
     */
    public Way(ArrayList<Node> way, String wayType) {
        if (way.get(0).equals(way.get(way.size() - 1))) {
            closed = true;
        }
        this.nodes = way;
        this.wayType = wayType;
        xcoords = new double[way.size()];
        ycoords = new double[way.size()];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            xcoords[i] = node.getX();
            ycoords[i] = node.getY();
        }
        setToNormalColors();
        checkForNoSpeedLimitInfo();
    }

    public Way(ArrayList<Node> way, String wayType, String natural, String landuse) {
        if (way.get(0).equals(way.get(way.size() - 1))) {
            closed = true;
        }
        this.nodes = way;
        this.wayType = wayType;
        xcoords = new double[way.size()];
        ycoords = new double[way.size()];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            xcoords[i] = node.getX();
            ycoords[i] = node.getY();
        }
        this.natural = natural;
        this.landuse = landuse;
        setToNormalColors();
        checkForNoSpeedLimitInfo();
    }

    /**
     * Constructor for the Way class
     * @param way
     * @param wayType
     * @param speedlimit
     */
    public Way(ArrayList<Node> way, String wayType, int speedlimit) {
        if (way.get(0).equals(way.get(way.size() - 1))) {
            closed = true;
        }
        this.nodes = way;
        this.wayType = wayType;
        this.speedlimit = speedlimit;
        xcoords = new double[way.size()];
        ycoords = new double[way.size()];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            xcoords[i] = node.getX();
            ycoords[i] = node.getY();
        }
        setToNormalColors();
    }

    public Way(ArrayList<Node> way, String wayType, int speedlimit, String natural, String landuse) {
        if (way.get(0).equals(way.get(way.size() - 1))) {
            closed = true;
        }
        this.nodes = way;
        this.wayType = wayType;
        this.speedlimit = speedlimit;
        xcoords = new double[way.size()];
        ycoords = new double[way.size()];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            xcoords[i] = node.getX();
            ycoords[i] = node.getY();
        }
        this.natural = natural;
        this.landuse = landuse;
        setToNormalColors();
    }
    /**
     * Change the colors of the ways to fillcolor blind friendly colors
     * @param colorBlindModeinput
     */
    public void changeColorBlindMode(boolean colorBlindModeinput) {
        if (!colorBlindModeinput && ColorBlindMode) {
            setToColorBlind();
        } else if (colorBlindModeinput && !ColorBlindMode) {
            setToNormalColors();
        }

    }
    /**
     * Set the colors to fillcolor blind friendly colors
     */
    @Generated
    private void setToColorBlind() {
        // Change the colors to fillcolor blind friendly colors
        wayTypesAndColor = Map.ofEntries(
                Map.entry("tertiary_link", Color.BLACK),
                Map.entry("motorway_link", Color.DARKSLATEBLUE),
                Map.entry("unclassified", Color.LIGHTGRAY),
                Map.entry("road", Color.DARKGRAY),
                Map.entry("motorway_junction", Color.DEEPPINK),
                Map.entry("mini_roundabout", Color.LIGHTGRAY),
                Map.entry("construction", Color.SLATEGRAY),
                Map.entry("bridleway", Color.LIMEGREEN),
                Map.entry("bus_guideway", Color.SKYBLUE),
                Map.entry("pedestrian", Color.GRAY.TRANSPARENT),
                Map.entry("secondary_link", Color.LIGHTSTEELBLUE),
                Map.entry("trunk_link", Color.SANDYBROWN),
                Map.entry("secondary", Color.LIGHTSTEELBLUE),
                Map.entry("trunk", Color.SANDYBROWN),
                Map.entry("motorway", Color.STEELBLUE),
                Map.entry("primary_link", Color.GOLDENROD),
                Map.entry("tertiary", Color.LIGHTGRAY),
                Map.entry("living_street", Color.SILVER),
                //Map.entry("crossing", Color.CRIMSON),
                //Map.entry("steps", Color.CRIMSON),
                Map.entry("platform", Color.DARKGRAY),
                //Map.entry("path", Color.CRIMSON),
                Map.entry("raceway", Color.THISTLE),
                Map.entry("residential", Color.WHITE),
                Map.entry("turning_circle", Color.WHITE),
                Map.entry("service", Color.WHITE),
                Map.entry("footway", Color.CRIMSON),
                Map.entry("track", Color.PERU),
                Map.entry("cycleway", Color.DODGERBLUE),
                Map.entry("primary", Color.GOLDENROD)
        );

    }

    /**
     * Set the colors to normal colors
     */
    @Generated
    private void setToNormalColors() {
        wayTypesAndColor =  Map.ofEntries(
                Map.entry("tertiary_link", Color.WHITE),
                Map.entry("motorway_link", Color.RED),
                Map.entry("unclassified", Color.WHITE),
                Map.entry("road", Color.GRAY),
                Map.entry("motorway_junction", Color.web("#ff80bf")),
                Map.entry("mini_roundabout", Color.WHITE),
                Map.entry("construction", Color.GRAY),
                Map.entry("bridleway", Color.GREEN),
                Map.entry("bus_guideway", Color.BLUE),
                Map.entry("pedestrian", Color.LIGHTGRAY),
                Map.entry("secondary_link", Color.web("#c7f1c7")),
                Map.entry("trunk_link", Color.web("#ff6826")),
                Map.entry("secondary", Color.web("#c7f1c7")),
                Map.entry("trunk", Color.web("#ff6826")),
                Map.entry("motorway", Color.web("#FF6478")),
                Map.entry("primary_link", Color.web("#fa6")),
                Map.entry("tertiary", Color.WHITE),
                Map.entry("living_street", Color.web("e0e0e0")),
                //Map.entry("crossing", Color.RED),
                //Map.entry("steps", Color.RED),
                Map.entry("platform", Color.GRAY),
                //Map.entry("path", Color.RED),
                Map.entry("raceway", Color.web("#F8C8DC")),
                Map.entry("residential", Color.WHITE),
                Map.entry("turning_circle", Color.WHITE),
                Map.entry("service", Color.WHITE),
                Map.entry("footway", Color.RED),
                Map.entry("track", Color.web("#775917")),
                Map.entry("cycleway", Color.BLUE),
                Map.entry("primary", Color.web("#fa6"))
        );
    }

    @Generated
    void colorClosedPolygons(String natural, String landuse) {
        if (building) {
            fillcolor = Color.web("#D9D4CF");
            strokecolor = Color.web("#b7aea4");
            return;
        }
        if (!natural.isBlank()) {
            switch (natural){
                case "grassland", "tundra":
                    fillcolor = Color.web("#CEECB2");
                    strokecolor = Color.web("#A3C893");
                    break;
                case "wood":
                    fillcolor = Color.web("#B5C398");
                    strokecolor = Color.web("#A3C893");
                    break;
                case "water":
                    fillcolor = Color.web("#ABD4E0");
                    strokecolor = Color.web("#556A70");
                    break;
                case "meadow":
                    fillcolor = Color.web("#B6B690");
                    strokecolor = Color.web("#ACE0A1");
                    break;
                case "scrub":
                    fillcolor = Color.web("#C9D8AD");
                    strokecolor = Color.web("#B5C398");
                    break;
                case "beach", "sand", "shoal":
                    fillcolor = Color.web("#FFF1BB");
                    strokecolor = Color.web("#F5EAC7");
                    break;
                case "heath":
                    fillcolor = Color.web("#A3C893");
                    strokecolor = Color.web("#B6B690");
                    break;
                case "bare_rock":
                    fillcolor = Color.web("#D9D4CF");
                    strokecolor = Color.rgb(214,217,159);
                    break;
                case "scree", "blockfield":
                    fillcolor = Color.web("#E5DDD6");
                    strokecolor = Color.web("#DED9D3");
                    break;
                case "shingle":
                    fillcolor = Color.web("#ECE4DB");
                    strokecolor = Color.web("#C4C2C2");
                    break;
                case "wetland":
                    fillcolor = Color.web("#E9F0F7");
                    strokecolor = Color.web("#B2D3F3");
                    break;
                case "mud":
                    fillcolor = Color.web("#EADED2");
                    strokecolor = Color.web("#756F6A");
                    break;
                case "glacier":
                    fillcolor = Color.web("#756F6A");
                    strokecolor = Color.web("#A3CFDE");
                    break;
                case "reef":
                    fillcolor = Color.web("#A3CFDE");
                    strokecolor = Color.web("#A3CFDE");
                    break;
                case "forest", "shrubbery":
                    fillcolor = Color.rgb(74,103,65);
                    strokecolor = Color.rgb	(48,69,41);
                    break;
                case "quarry":
                    fillcolor = Color.web("#C4C2C2");
                    strokecolor = Color.web("#626161");
                    break;
                case "grass":
                    fillcolor = Color.web("#CEECB2");
                    strokecolor = Color.web("#ACE0A1");
                    break;
                case "orchard":
                    fillcolor = Color.web("#ABDFA0");
                    strokecolor = Color.web("#A3C893");
                    break;
                case "vineyard":
                    fillcolor = Color.web("#A9DE9E");
                    strokecolor = Color.web("#A3C893");
                    break;
                case "farmland", "farmyard":
                    fillcolor = Color.web("#EFF0D6");
                    strokecolor = Color.web("#D7D9A0");
                    break;
                case "salt_pond", "basin":
                    fillcolor = Color.web("#ABD4E0");
                    strokecolor = Color.web("#D7D9A0");
                    break;
                case "landfill":
                    fillcolor = Color.web("#B6B690");
                    strokecolor = Color.web("#B6B690");
                    break;
            }
        } else if (!landuse.isBlank()) {
            switch (landuse) {
                case "residential", "industrial", "military", "allotments":
                    fillcolor = Color.web("#D9D0C9");
                    strokecolor = Color.web("#a09588");
                    break;
                case "grassland", "tundra":
                    fillcolor = Color.web("#CEECB2");
                    strokecolor = Color.web("#A3C893");
                    break;
                case "wood":
                    fillcolor = Color.web("#B5C398");
                    strokecolor = Color.web("#A3C893");
                    break;
                case "water":
                    fillcolor = Color.web("#ABD4E0");
                    strokecolor = Color.web("#556A70");
                    break;
                case "meadow":
                    fillcolor = Color.web("#B6B690");
                    strokecolor = Color.web("#ACE0A1");
                    break;
                case "scrub":
                    fillcolor = Color.web("#C9D8AD");
                    strokecolor = Color.web("#B5C398");
                    break;
                case "beach", "sand", "shoal":
                    fillcolor = Color.web("#FFF1BB");
                    strokecolor = Color.web("#F5EAC7");
                    break;
                case "heath":
                    fillcolor = Color.web("#A3C893");
                    strokecolor = Color.web("#B6B690");
                    break;
                case "bare_rock":
                    fillcolor = Color.web("#D9D4CF");
                    strokecolor = Color.rgb(214,217,159);
                    break;
                case "scree", "blockfield":
                    fillcolor = Color.web("#E5DDD6");
                    strokecolor = Color.web("#DED9D3");
                    break;
                case "shingle":
                    fillcolor = Color.web("#ECE4DB");
                    strokecolor = Color.web("#C4C2C2");
                    break;
                case "wetland":
                    fillcolor = Color.web("#E9F0F7");
                    strokecolor = Color.web("#B2D3F3");
                    break;
                case "mud":
                    fillcolor = Color.web("#EADED2");
                    strokecolor = Color.web("#756F6A");
                    break;
                case "glacier":
                    fillcolor = Color.web("#756F6A");
                    strokecolor = Color.web("#A3CFDE");
                    break;
                case "reef":
                    fillcolor = Color.web("#A3CFDE");
                    strokecolor = Color.web("#A3CFDE");
                    break;
                case "forest", "shrubbery":
                    fillcolor = Color.rgb(74,103,65);
                    strokecolor = Color.rgb	(48,69,41);
                    break;
                case "quarry":
                    fillcolor = Color.web("#C4C2C2");
                    strokecolor = Color.web("#626161");
                    break;
                case "grass":
                    fillcolor = Color.web("#CEECB2");
                    strokecolor = Color.web("#ACE0A1");
                    break;
                case "orchard":
                    fillcolor = Color.web("#ABDFA0");
                    strokecolor = Color.web("#A3C893");
                    break;
                case "vineyard":
                    fillcolor = Color.web("#A9DE9E");
                    strokecolor = Color.web("#A3C893");
                    break;
                case "farmland":
                    fillcolor = Color.web("#EFF0D6");
                    strokecolor = Color.web("#D7D9A0");
                    break;
                case "farmyard":
                    fillcolor = Color.web("#F5DCBA");
                    strokecolor = Color.web("#E3D1B0");
                    break;

                case "salt_pond", "basin":
                    fillcolor = Color.web("#ABD4E0");
                    strokecolor = Color.web("#D7D9A0");
                    break;
                case "landfill":
                    fillcolor = Color.web("#B6B690");
                    strokecolor = Color.web("#B6B690");
                    break;
            }
        } else {
            fillcolor = Color.web("#bbdaa4");
            strokecolor = Color.web("#bbdaa4");
        }
    }
    /**
     * Check for ways that do not have a speed limit
     */
    public void checkForNoSpeedLimitInfo() {
        switch (wayType) {
            case "tertiary_link":
                speedlimit = 50;
                break;

            case "unclassified":
                speedlimit = 50;
                break;

            case "road":
                speedlimit = 50;
                break;

            case "motorway_junction":
                speedlimit = 80;
                break;

            case "mini_roundabout":
                speedlimit = 30;
                break;

            case "secondary_link":
                speedlimit = 50;
                break;

            case "trunk_link":
                speedlimit = 80;
                break;

            case "secondary":
                speedlimit = 50;
                break;

            case "trunk":
                speedlimit = 80;
                break;

            case "motorway":
                speedlimit = 110;
                break;

            case "primary_link":
                speedlimit = 50;
                break;

            case "tertiary":
                speedlimit = 50;
                break;

            case "living_street":
                speedlimit = 50;
                break;

            case "residential":
                speedlimit = 50;
                break;
        }
    }

    public double[] getXcoords() {
        return xcoords;
    }

    public double[] getYcoords() {
        return ycoords;
    }

    public int getSpeedlimit() {
        return speedlimit;
    }


    /**
     * Draw the way on the canvas
     * @param gc
     * @param colorBlindMode
     */
    @Generated
    public void draw(GraphicsContext gc, boolean colorBlindMode) {



        if (isClosed()) {
            colorClosedPolygons(natural, landuse);
            gc.setFill(fillcolor);
            gc.setStroke(strokecolor);
            fillClosedWays(gc);
        } else {
            changeColorBlindMode(colorBlindMode);
            gc.beginPath();
            gc.moveTo(xcoords[0], ycoords[0]);

            for (int i = 0; i < xcoords.length; i++) {
                gc.lineTo(xcoords[i], ycoords[i]);

                Color color = wayTypesAndColor == null ? Color.BLACK : wayTypesAndColor.getOrDefault(wayType, Color.BLACK);
                gc.setStroke(color);
            }
            gc.stroke();
        }

    }

    public Node getClosestNode(Node node) {
        Node closest = nodes.get(0);
        for (Node nd: nodes) {
            CordtoKM cTKM = new CordtoKM();
            if (cTKM.cordToKM(closest, node) > cTKM.cordToKM(nd, node)) {
                closest = nd;
            }
        }
        return closest;
    }

    @Generated
    void fillClosedWays(GraphicsContext gc) {
        gc.fillPolygon(xcoords, ycoords, xcoords.length);
        gc.strokePolygon(xcoords, ycoords, xcoords.length);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public String getLanduse() {
        return landuse;
    }

    public String getNatural() {
        return natural;
    }
    @Generated
    public void isBuilding() {
        building = true;
    }
    @Generated
    public boolean isClosed() {
        return closed;
    }
    public String getWayType() {
        return wayType;
    }
}