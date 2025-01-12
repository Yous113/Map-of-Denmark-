package org.example.firstyearproject;

import javafx.beans.binding.Bindings;
import org.example.firstyearproject.KdTree.Point2D;

import java.util.List;

public class Controller {
    double lastX;
    double lastY;

    Node from;
    Node to;

    public Controller(Model model, View view) {

        //Resizing the canvas of the map where the width subtracts the width of the gridPane to make it fit and the
        //height remains the same.
        view.canvas.widthProperty().bind(Bindings.subtract(view.scene.widthProperty(), view.gridPane.widthProperty()));
        view.canvas.heightProperty().bind(view.scene.heightProperty());


        //a listener to redraw the canvas when its size changes
        view.canvas.widthProperty().addListener(evt -> {
            view.redraw();
        });

        view.canvas.setOnMousePressed(e -> {
            javafx.geometry.Point2D modelPoint = view.mouseToModel(e.getX(), e.getY());
            lastX = e.getX();
            lastY = e.getY();

            if (e.isSecondaryButtonDown()) {
                System.out.println("brrrrrr");
                org.example.firstyearproject.KdTree.Point2D closest = model.kdTree.nearest(new org.example.firstyearproject.KdTree.Point2D(modelPoint.getX(), modelPoint.getY()));
                if (closest != null) {

                }
            }
        });
        view.canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);

            }
            lastX = e.getX();
            lastY = e.getY();


        });
        view.canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        });

        // Handle the fillcolor blind button click event
        view.colorBlindCheckBox.setOnAction(event -> {
            view.colorBlindMode = view.colorBlindCheckBox.isSelected();
            view.redraw(); // redraw the view with the new mode
        });


        //For
        view.textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                view.listView.setVisible(false);
                view.listView.setFocusTraversable(false);
            } else {
                var searchInput = view.textField.getText();

                List<String> listOfSuggestedAddresses = model.trie.suggest(searchInput);

                view.suggestions.clear();
                view.listView.getItems().clear();

                view.suggestions.addAll(listOfSuggestedAddresses);

                if (!listOfSuggestedAddresses.isEmpty()) {
                    view.listView.setItems(view.suggestions);
                    view.listView.setVisible(true);
                }


            }
        });

        view.listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ( from != null) {
                from.setMarked(false);

            }
            if (newValue != null && !view.listView.getItems().isEmpty()) {
                view.textField.setText(newValue);
                from = model.addressXnode.get(newValue);
                from.setMarked(true);
                view.listView.setVisible(false);
                view.redraw();
            }
        });

        view.textField2.textProperty().addListener((observable2, oldValue2, newValue2) -> {
            if (newValue2.isEmpty()) {
                view.listView2.setVisible(false);
                view.listView2.setFocusTraversable(false);
            } else {
                var searchInput2 = view.textField2.getText();

                List<String> listOfSuggestedAddresses2 = model.trie.suggest(searchInput2);

                view.suggestions2.clear();
                view.listView2.getItems().clear();

                view.suggestions2.addAll(listOfSuggestedAddresses2);

                view.listView2.setItems(view.suggestions2);
                view.listView2.setVisible(true);
            }
        });


        view.listView2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ( to != null) {
                to.setMarked(false);
            }
            if (newValue != null && !view.listView2.getItems().isEmpty()) {
                view.textField2.setText(newValue);
                to = model.addressXnode.get(newValue);
                to.setMarked(true);
                view.listView2.setVisible(false);
                view.redraw();
            }
        });

        view.carButton.setOnAction(actionEvent -> {
            Point2D node = model.kdTree.nearest(new Point2D(from.getX(), from.getY()));
            Way w = node.getWay();
            Node j = w.getClosestNode(from);

            view.setStartnode(j);

            Point2D node2 = model.kdTree.nearest(new Point2D(to.getX(), to.getY()));


            Way w2 = node2.getWay();
            Node j2 = w2.getClosestNode(to);

            view.setEndnode(j2);

            view.drawShortestCarPath();
            view.drawShortestPath(view.shortestPathCarList);
            view.redraw();

        });

        view.bikeButton.setOnAction(actionEvent -> {
            Point2D node = model.kdTree.nearest(new Point2D(from.getX(), from.getY()));
            Way w = node.getWay();
            Node j = w.getClosestNode(from);

            view.setStartnode(j);

            Point2D node2 = model.kdTree.nearest(new Point2D(to.getX(), to.getY()));


            Way w2 = node2.getWay();
            Node j2 = w2.getClosestNode(to);

            view.setEndnode(j2);

            view.drawShortestBikePath();
            view.drawShortestPath(view.shortestPathBikeList);
            view.redraw();

        });

        view.clearButton.setOnAction(actionEvent -> {
            view.startnode = null;
            view.endnode = null;
            view.textField.setText("");
            view.textField2.setText("");
            view.redraw();

        });


    }

}