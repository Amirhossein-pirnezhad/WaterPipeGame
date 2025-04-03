package com.company;

import com.company.map.map;
import com.company.Gameplay.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;




public class Main extends Application {
    private int HEIGHT;
    private int WIDTH;
    private map Map = new map();

    @Override
    public void start(Stage stage) throws Exception {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = (int) screenBounds.getWidth();
        HEIGHT = (int) screenBounds.getHeight();
        stage.setHeight(HEIGHT);
        stage.setWidth(WIDTH);
        stage.setTitle("Water Pipe Game");

        this.Map = setGameStructures();
        Gameplay gameplay = new Gameplay();
        AnimationTimer gameUpdate = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Map.updateGame();
                stage.setScene(Map.scene);
                Map.exitButton.setOnAction(e -> stage.close());
                Map.restart.setOnAction(e ->{
                    stage.close();
                    Map.setAvailableMoves(30);
                    try {
                        Map = setGameStructures();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    stage.show();
                });
            }
        };
        gameUpdate.start();
        stage.show();
    }
    private map setGameStructures() throws Exception {
        Map.Build_map(HEIGHT, WIDTH);
        return Map;
    }
}
