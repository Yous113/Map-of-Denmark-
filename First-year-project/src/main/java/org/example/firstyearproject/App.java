package org.example.firstyearproject;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Select which file you want to load");

        ObservableList<String> options = FXCollections.observableArrayList();
        try {
            options.addAll(Files.list(Paths.get("data"))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ComboBox<String> comboBox = new ComboBox<>(options);

        Button button = new Button("Load file");

        VBox vbox = new VBox(10, label, comboBox, button); // 10 is the spacing between elements
        vbox.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10, 10, 10, 10));

        pane.setCenter(vbox);
        Scene scene = new Scene(pane, 600, 300);
        scene.setFill(Color.LIGHTGRAY);

        primaryStage.setTitle("File input");
        primaryStage.setScene(scene);
        primaryStage.show();

        comboBox.setOnAction(e -> {
            String selectedFile = comboBox.getValue();

            // Show loading screen
            StackPane loadingPane = new StackPane();
            loadingPane.setStyle("-fx-background-color: white");

            String[] parts = selectedFile.split("\\.");
            Label loadingLabel = new Label("Loading " + parts[0] + "...");
            loadingLabel.setFont(new Font(20));
            Rectangle loadingBar = new Rectangle(0, 0, 400, 20); // x, y, width, height
            loadingBar.setFill(Color.WHITE);

            VBox loadingVBox = new VBox(10, loadingLabel, loadingBar);
            loadingVBox.setAlignment(Pos.CENTER);

            Image image = new Image(getClass().getResourceAsStream("/world.jpg"));
            ImageView imageView = new ImageView(image);

            // Bind the ImageView size to the Scene size
            imageView.fitWidthProperty().bind(primaryStage.widthProperty());
            imageView.fitHeightProperty().bind(primaryStage.heightProperty());

            loadingPane.getChildren().add(imageView);
            loadingPane.getChildren().addAll(loadingVBox);

            Scene loadingScene = new Scene(loadingPane, 600, 300);

            primaryStage.setScene(loadingScene);

            // Load file in background
            new Thread(() -> {
                try {
                    var model = Model.load("data/" + comboBox.getValue());

                    // Update the UI on the JavaFX Application Thread
                    javafx.application.Platform.runLater(() -> {
                        // Once loading is complete, create view and controller
                        View view = new View(model, primaryStage);
                        new Controller(model, view);

                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

            // Update the progress of loading
            final double[] progress = {0}; // Initialize progress
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(loadingBar.widthProperty(), 0)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(loadingBar.widthProperty(), 400))
            );
            timeline.setCycleCount(Timeline.INDEFINITE);

            timeline.play();

        });


        // Make the scene responsive to the window size
        pane.prefWidthProperty().bind(scene.widthProperty());
        pane.prefHeightProperty().bind(scene.heightProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }
}