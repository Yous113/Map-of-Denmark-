package org.example.firstyearproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;
import org.example.firstyearproject.ShortestPath.Astar;
import org.example.firstyearproject.KdTree.RectHV;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class View implements Serializable {

    Node startnode;
    Node endnode;
    boolean colorBlindMode = false;
    Color color = Color.web("#7fcdff");

    //Added to make the buffer bigger
    private static final double bufferBouding = 200;
    Canvas canvas = new Canvas(1600, 900);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    Label zoomLabel = new Label();
    ListView<String> listView;
    ListView<String> listView2;
    TextField textField;
    TextField textField2;
    ObservableList<String> suggestions;
    ObservableList<String> suggestions2;


    Map<String, Double> lineWidths = Map.ofEntries(
            Map.entry("tertiary_link", 3.0),
            Map.entry("motorway_junction", 2.0),
            Map.entry("bridleway", 2.0),
            Map.entry("pedestrian", 3.0),
            Map.entry("secondary_link", 2.0),
            Map.entry("trunk_link", 4.0),
            Map.entry("secondary", 4.0),
            Map.entry("trunk", 5.0),
            Map.entry("motorway", 6.0),
            Map.entry("primary_link", 2.0),
            Map.entry("tertiary", 5.5),
            Map.entry("crossing", 1.0),
            Map.entry("steps", 2.0),
            Map.entry("platform", 1.0),
            Map.entry("path", 1.0),
            Map.entry("raceway", 2.0),
            Map.entry("road", 2.0),
            Map.entry("residential", 5.5),
            Map.entry("turning_circle", 2.0),
            Map.entry("service", 3.0),
            Map.entry("footway", 1.0),
            Map.entry("track", 1.0),
            Map.entry("cycleway", 1.0),
            Map.entry("primary", 15.0),
            Map.entry("living_street", 7.0)
    );

    Scene scene;
    GridPane gridPane;

    Affine trans = new Affine();
    Model model;

    CheckBox colorBlindCheckBox;
    Button carButton;
    Button bikeButton;

    Button clearButton;

    boolean routeType = false;
    RectHV rectHV;
    Iterable<org.example.firstyearproject.KdTree.Point2D> nodesInRange;
    Iterable<org.example.firstyearproject.KdTree.Point2D> housesInRange;

    List<Long> shortestPathCarList;
    List<Long> shortestPathBikeList;

    public View(Model model, Stage primaryStage) {
        this.model = model;
        primaryStage.setTitle("Map of Denmark");
        BorderPane pane = new BorderPane(canvas);


        scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
        redraw();
        pan(-0.56 * model.minlon, model.maxlat);
        zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));

        // Bind canvas size to scene size
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());

        //a listener to redraw the canvas when its size changes
        canvas.widthProperty().addListener(evt -> {
            redraw();
        });


        //Label for searching
        Label label = new Label("Search for an address");

        //TextField to search for addresses. The textFields FocusTraversable is set as false sine it should only be
        //traversable when we press on the textField itself meaning the line when blinking when pressing the textfield
        // will not be there until we press it.
        textField = new TextField();
        textField.setFocusTraversable(false);
        textField.setMaxWidth(300);

        textField2 = new TextField();
        textField2.setFocusTraversable(false);
        textField2.setMaxWidth(300);

        HBox hBox = new HBox();
        carButton = new Button("Search here car route");
        bikeButton = new Button("Search here for a bike route");
        clearButton = new Button("Clear route");

        hBox.getChildren().addAll(carButton, bikeButton, clearButton);

        hBox.setSpacing(10);


        gridPane = new GridPane();

        //Listview which will hold the observable arraylist of the suggestions of addresses
        listView = new ListView<>();
        listView.setVisible(false);
        listView.setMaxWidth(300);

        listView2 = new ListView<>();
        listView2.setVisible(false);
        listView2.setMaxWidth(300);


        colorBlindCheckBox = new CheckBox("Color Blind Mode");
        colorBlindCheckBox.setSelected(colorBlindMode); // set initial state

        gridPane.add(colorBlindCheckBox, 0, 0);
        gridPane.add(label, 0, 1);
        gridPane.add(textField, 0, 2);
        gridPane.add(textField2, 0, 4);
        gridPane.add(listView, 0, 3);
        gridPane.add(listView2, 0, 5);
        gridPane.add(hBox, 0, 6);
        gridPane.add(zoomLabel, 0, 30);


        gridPane.setVgap(10);
        gridPane.setHgap(20);
        BorderPane.setMargin(gridPane, new Insets(10, 10, 10, 10));



        pane.setLeft(gridPane);
        primaryStage.setScene(gridPane.getScene());
        primaryStage.show();

        suggestions = FXCollections.observableArrayList();



        suggestions2 = FXCollections.observableArrayList();


    }


    void redraw() {
        gc.setTransform(new Affine());
        color = Color.web("#7fcdff");
        gc.setFill(color);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        double currentZoomLvL = calculateZoomLevel();


        // Get the width and height of the canvas
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // Get the range of the kdTree points
        double kdTreeWidth = model.maxlon - model.minlon;
        double kdTreeHeight = model.maxlat - model.minlat;

        // Calculate the ratio between the canvas size and the kdTree range
        double widthRatio = canvasWidth / kdTreeWidth;
        double heightRatio = canvasHeight / kdTreeHeight;


        try {
            // Transform the kdTree points to the canvas scale
            Point2D minPoint2D = trans.inverseTransform(-bufferBouding * widthRatio, -bufferBouding * heightRatio);
            Point2D maxPoint2D = trans.inverseTransform((canvas.getWidth() + bufferBouding) * widthRatio, (canvas.getHeight() + bufferBouding) * heightRatio);
            RectHV rectHV = new RectHV(minPoint2D.getX(), minPoint2D.getY(), maxPoint2D.getX(), maxPoint2D.getY());
            nodesInRange = model.kdTree.range(rectHV);
            housesInRange = model.KdHouseTree.range(rectHV);


        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
            return;
        }



        for (var relation : model.relations) {
            relation.draw(gc, colorBlindMode);
        }

        for (org.example.firstyearproject.KdTree.Point2D KdNode : housesInRange) {
            Way way = KdNode.getWay();
            if (way != null && (!way.landuse.isEmpty() || !way.natural.isEmpty())) {
                gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
                way.draw(gc, colorBlindMode);

            }

        }

        if (currentZoomLvL <= 8000) {
            for (org.example.firstyearproject.KdTree.Point2D KdNode : nodesInRange) {
                Way way = KdNode.getWay();
                if (way != null) {
                    if (way.wayType.equals("motorway") || way.wayType.equals("trunk") || way.wayType.equals("primary") ||
                            way.wayType.equals("secondary") || way.wayType.equals("tertiary")) {
                        gc.setLineWidth(1 / Math.sqrt(trans.determinant()) * 0.9);
                        way.draw(gc, colorBlindMode);
                    }
                }

            }

        }   else if (currentZoomLvL <= 70000) {
            for (org.example.firstyearproject.KdTree.Point2D KdNode : nodesInRange) {
                Way way = KdNode.getWay();
                if (way != null) {
                    if (way.wayType.equals("motorway") || way.wayType.equals("trunk") || way.wayType.equals("primary") ||
                            way.wayType.equals("secondary") || way.wayType.equals("tertiary") || way.wayType.equals("residential")
                            || way.wayType.equals("secondary_link") || way.wayType.equals("primary_link")
                            || way.wayType.equals("trunk_link") || way.wayType.equals("motorway_link")) {
                        Double lineWidth = lineWidths.getOrDefault(way.wayType, 0.0001);
                        gc.setLineWidth(lineWidth/ Math.sqrt(trans.determinant()) * 0.3);
                        way.draw(gc, colorBlindMode);
                    }
                }

            }

        } else if (currentZoomLvL > 70000) {
            for (org.example.firstyearproject.KdTree.Point2D KdNode : nodesInRange) {
                Way way = KdNode.getWay();
                if (way != null) {
                    Double lineWidth = lineWidths.getOrDefault(way.wayType, 1.0);
                    gc.setLineWidth(lineWidth / Math.sqrt(trans.determinant()));
                    way.draw(gc, colorBlindMode);
                }
            }

        }
        if (currentZoomLvL >= 37000) {
            for (org.example.firstyearproject.KdTree.Point2D KdNode : housesInRange) {
                Way way = KdNode.getWay();
                if (way != null && way.landuse.isEmpty() && way.natural.isEmpty()) {
                    gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
                    way.draw(gc, colorBlindMode);

                }

            }
        }
        Image markedImage = new Image("file:C:/Users/denni/OneDrive - ITU/GitHub/BFST2024Group5/First-year-project/src/main/resources/9356230.png");


        //test(model.id2node, model.G, gc);
        for (Node nd: model.addressXnode.values()) {
            if (nd.getMarked()) {
                gc.drawImage(markedImage, nd.getX(), nd.getY(), 0.00004, 0.00004);

            }

        }
        if (startnode != null && endnode != null) {
            if (routeType) {
                if (!shortestPathBikeList.isEmpty()) {
                    drawShortestPath(shortestPathBikeList);
                }
            } else {
                if (!shortestPathCarList.isEmpty()) {
                    drawShortestPath(shortestPathCarList);
                }
            }
        }

    }

    public void drawShortestCarPath () {
        Astar a = new Astar();
        shortestPathCarList = a.findNeighborsAStar(startnode.getID(), endnode.getID(), model.G, model.id2node);

    }

    public void drawShortestBikePath () {
        Astar a = new Astar();
        shortestPathBikeList = a.findNeighborsAStar(startnode.getID(), endnode.getID(), model.GCycle, model.id2node);
    }

    public void drawShortestPath (List<Long> shortest) {
        gc.beginPath();
        gc.moveTo(0.56 * startnode.getLong(), -startnode.getLat());
        assert shortest != null;
        for (Long l : shortest) {
            gc.lineTo(0.56 * model.id2node.get(l).getLong(), -model.id2node.get(l).getLat());
            gc.setLineWidth(0.00015); //eller 0.00001 da den nuværende værdi er stor
            Color color = Color.BLUE;
            gc.setStroke(color);
        }
        routeType = false;
        gc.stroke();

    }


    public void setStartnode (Node n) {
        startnode = n;
    }
    public void setEndnode (Node n) {
        endnode = n;
    }


    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        try {
            redraw();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    void maxZoom(double dx, double dy, double factor) {
        double newZoomLevel = calculateZoomLevel() * factor;
        double maxZoomLevel = 1000000.0; // vores max zoom sættes her

        //vi ser om vi kommer over zoom lvl
        if (newZoomLevel > maxZoomLevel) {
            factor = maxZoomLevel / calculateZoomLevel();
        }

        // her er vores zoom operations indhold. Zoom metoden er ændret til bare at kalde maxzoom
        // som så står får det
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);
        redraw();
        updateZoomLabel();
    }

    void updateZoomLabel() {
        double zoomLevelValue = calculateZoomLevel();
        zoomLabel.setText("Zoom level: " + zoomLevelValue);
    }

    private double calculateZoomLevel() {
        return Math.sqrt(trans.getMxx() * trans.getMxx() + trans.getMyy() * trans.getMyy());
    }

    void zoom(double dx, double dy, double factor) {
        maxZoom(dx, dy, factor);
    }



    public Point2D mouseToModel(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
        } catch (NonInvertibleTransformException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }

    }


}
