package com.company;

import com.company.map.map;
import com.company.Gameplay.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;




public class Main extends Application {
    private int HEIGHT;
    private int WIDTH;
    private int levelGame = 1;

    @Override
    public void start(Stage stage) throws Exception {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = (int) screenBounds.getWidth();
        HEIGHT = (int) screenBounds.getHeight();
        stage.setHeight(HEIGHT);
        stage.setWidth(WIDTH);
        map Map = new map();
        if(this.levelGame == 1){
            Map.setLevelGame(this.levelGame);}
        else if(this.levelGame == 2){
            Map.setLevelGame(this.levelGame);}

        Map.Build_map(HEIGHT, WIDTH);

        Gameplay gameplay = new Gameplay();
        AnimationTimer gameUpdate = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Map.updateGame();
                stage.setScene(Map.scene);
                Map.exitButton.setOnAction(e -> stage.close());
            }
        };
        gameUpdate.start();





        stage.setTitle("Water Pipe Game");
        stage.show();


    }

    private void run(){

    }
}
