package org.example.firstyearproject;

import Helpers.Generated;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * The Relation class is used to represent a relation in the map
 */
public class Relation implements Serializable {

    transient Color fillcolor;
    transient Color strokecolor;
    ArrayList<ArrayList<Way>> closedLoops;
    ArrayList<MultiPolygon> multiPolygons;
    ArrayList<Way> closed;
    String type;
    String natural = "";
    ArrayList<Way> outer;
    String landuse = "";
    boolean building = false;
    Boolean ColorBlindMode = false;
    /**
     * Constructor for the Relation class
     * @param type
     */
    public Relation(ArrayList<Way> outer, ArrayList<Way> inner, String type) {
        this.outer = outer;
        this.multiPolygons = new ArrayList<>();
        closed = new ArrayList<>();
        this.closedLoops = new ArrayList<>();

        Iterator<Way> iterator = outer.iterator();

        while (iterator.hasNext()) {
            Way line = iterator.next();
            // Check if the current way is closed
            if (line.isClosed()) {
                closed.add(line);  // Add to closed list
                iterator.remove(); // Safely remove the element from the outer list using the iterator
            }
        }
        closed.addAll(inner);
        relationCloser();

        for (ArrayList<Way> ways: closedLoops) {
            MultiPolygon mp = constructPolygon(ways);
            multiPolygons.add(mp);
        }
        this.type = type;
        setToNormalColors();
    }

    /**
     * Constructor for the Relation class
     * @param type
     * @param natural
     */
    public Relation(ArrayList<Way> outer,ArrayList<Way> inner, String type, String natural, String landuse) {
        this.outer = outer;
        closed = new ArrayList<>();
        this.multiPolygons = new ArrayList<>();
        this.closedLoops = new ArrayList<>();
        this.landuse = landuse;

        Iterator<Way> iterator = outer.iterator();

        while (iterator.hasNext()) {
            Way line = iterator.next();
            // Check if the current way is closed
            if (line.isClosed()) {
                closed.add(line);  // Add to closed list
                iterator.remove(); // Safely remove the element from the outer list using the iterator
            }
        }
        closed.addAll(inner);
        relationCloser();

        for (ArrayList<Way> ways: closedLoops) {
            MultiPolygon mp = constructPolygon(ways);
            multiPolygons.add(mp);
        }

        this.type = type;
        this.natural = natural;

    }

    private MultiPolygon constructPolygon(ArrayList<Way> ways) {
        ArrayList<Double> xcoordsList = new ArrayList<>();
        ArrayList<Double> ycoordsList = new ArrayList<>();
        for (Way w: ways) {
            for (Node nd: w.getNodes()) {
                xcoordsList.add(nd.getX());
                ycoordsList.add(nd.getY());
            }
        }
       return new MultiPolygon(xcoordsList, ycoordsList);
    }

    public void changeColorBlindMode(boolean colorBlindModeinput) {
        if (!colorBlindModeinput && ColorBlindMode) {
            setToColorBlind();
        } else if (colorBlindModeinput && !ColorBlindMode) {
            setToNormalColors();
        }

    }

    private void setToNormalColors() {
        colorClosedPolygons(natural, landuse);
    }

    private void setToColorBlind() {

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
                    fillcolor = Color.web("#7fcdff");
                    strokecolor = Color.web("#7fcdff");
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
                    fillcolor = Color.web("#7fcdff");
                    strokecolor = Color.web("#7fcdff");
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
     * Draw the relation on the canvas
     * @param gc
     * @param colorBlindMode
     */
    @Generated
    public void draw (GraphicsContext gc, boolean colorBlindMode) {

        if (type.equals("multipolygon")){

            changeColorBlindMode(colorBlindMode);
            gc.setFill(fillcolor);
            gc.setStroke(strokecolor);
            for (MultiPolygon polygon: multiPolygons) {
                gc.fillPolygon(polygon.getXcoords(), polygon.getYcoords(), polygon.getXcoords().length);
                gc.strokePolygon(polygon.getXcoords(), polygon.getYcoords(), polygon.getXcoords().length);
            }

            for (Way closedWay: closed) {
                if (closedWay.isClosed()) {
                    colorClosedPolygons(closedWay.getNatural(), closedWay.getLanduse());
                    gc.setFill(fillcolor);
                    gc.setStroke(strokecolor);
                    closedWay.fillClosedWays(gc);
                } else {
                    closedWay.draw(gc, colorBlindMode);
                }

            }
        }
    }

    @Generated
    public void relationCloser() {
        while (!outer.isEmpty()) {
            ArrayList<Way> orderedOuter = new ArrayList<>();
            Way startingWay = outer.get(0);
            Node currentNode = startingWay.getNodes().get(startingWay.getNodes().size() - 1);

            orderedOuter.add(startingWay);
            outer.remove(0);

            boolean found;
            do {
                found = false;
                Iterator<Way> it = outer.iterator();
                while (it.hasNext()) {
                    Way way = it.next();
                    ArrayList<Node> nodes = way.getNodes();
                    Node firstNode = nodes.get(0);
                    Node lastNode = nodes.get(nodes.size() - 1);

                    if (firstNode.equals(currentNode)) {
                        orderedOuter.add(way);
                        currentNode = lastNode;
                        it.remove();
                        found = true;
                        break;
                    } else if (lastNode.equals(currentNode)) {
                        Collections.reverse(nodes);
                        orderedOuter.add(way);
                        currentNode = firstNode;
                        it.remove();
                        found = true;
                        break;
                    }
                }
            } while (found && !outer.isEmpty());

            // Check if the last node of the last way matches the first node of the first way
            if (currentNode.equals(orderedOuter.get(0).getNodes().get(0))) {
                // A closed loop has been formed, save it
                closedLoops.add(orderedOuter);
            } else {
                System.out.println("Error: The ways do not form a closed loop");
                // Optionally handle the unclosed loop, e.g., break or continue trying with remaining ways
            }
        }

    }

    public void isBuilding() {
        building = true;
    }


}