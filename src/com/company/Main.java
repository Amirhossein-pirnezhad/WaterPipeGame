package com.company;

import com.company.map.map;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

public class Main extends Application {
    private int HEIGHT;
    private int WIDTH;
    private map Map;
    private Stage primaryStage;
    private AnimationTimer gameUpdate;

    @Override
    public void start(Stage stage) {
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
                        Map.restartLevel();
                        Map.updateGame();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    primaryStage.show();
                });
                Map.undo.setOnAction(e ->{
                    try {
                        Map.getUNDO().undoLastMove();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        };
        gameUpdate.start();
        primaryStage.show();
    }

    private map setGameStructures() throws Exception {
        map m = new map();
        m.Build_map(HEIGHT,WIDTH);
        return m;
    }

    private void showLevelChoice() {
        Stage choice = new Stage();
        Map = new map();
        BorderPane borderPane = new BorderPane();
        Button l1 = new Button("Level 1");
        Button l2 = new Button("Level 2");
        Button l3 = new Button("Level 3");
        Button l4 = new Button("Level 100");
        VBox levels = new VBox(10, l1, l2 , l3 ,l4);
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

        l3.setOnAction(event -> {
            Map.setLevelGame(3);
            choice.close();
            try {
                startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        l4.setOnAction(event -> {
            Map.setLevelGame(100);
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