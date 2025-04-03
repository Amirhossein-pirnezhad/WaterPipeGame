package com.company;

import com.company.map.map;
import com.company.Gameplay.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

public class Main extends Application {
    private int HEIGHT;
    private int WIDTH;
    private map Map = new map();
    private Stage primaryStage;
    private AnimationTimer gameUpdate;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = (int) screenBounds.getWidth();
        HEIGHT = (int) screenBounds.getHeight();

        showLevelChoice();
    }

    private void startGame() throws Exception {
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);
        primaryStage.setTitle("Water Pipe Game");

        this.Map = setGameStructures();
        Gameplay gameplay = new Gameplay();

        gameUpdate = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Map.updateGame();
                primaryStage.setScene(Map.scene);
                Map.exitButton.setOnAction(e -> primaryStage.close());
                Map.restart.setOnAction(e -> {
                    primaryStage.close();
                    Map.setAvailableMoves(30);
                    try {
                        Map = setGameStructures();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    primaryStage.show();
                });
            }
        };
        gameUpdate.start();
        primaryStage.show();
    }

    private map setGameStructures() throws Exception {
        Map.Build_map(HEIGHT, WIDTH);
        return Map;
    }

    private void showLevelChoice() {
        Stage choice = new Stage();
        BorderPane borderPane = new BorderPane();
        Button l1 = new Button("Level 1");
        Button l2 = new Button("Level 2");
        VBox levels = new VBox(10, l1, l2);
        levels.setAlignment(Pos.CENTER);
        Text text = new Text("Please choose your level that you want it:");
        text.setFont(new Font("Arial", 20));
        borderPane.setTop(text);
        borderPane.setCenter(levels);
        Scene scene = new Scene(borderPane);
        choice.setScene(scene);

        l1.setOnAction(event -> {
            Map.setLevelGame(1);
            choice.close();
            try {
                startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        l2.setOnAction(event -> {
            Map.setLevelGame(2);
            choice.close();
            try {
                startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        choice.setOnCloseRequest(event -> {
            if (gameUpdate != null) {
                gameUpdate.stop();
            }
            primaryStage.close();
        });

        choice.showAndWait();
    }
}