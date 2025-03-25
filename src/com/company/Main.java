package com.company;

import com.company.map.map;
import com.company.Gameplay.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import static sun.misc.PostVMInitHook.run;

public class Main extends Application {
    private final int cell_size = 100;//havaset bashe to map ham tarif kardi
    private int HEIGHT;
    private int WIDTH;
    private int map_size = 7;

    @Override
    public void start(Stage stage) throws Exception {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = (int) screenBounds.getWidth();
        HEIGHT = (int) screenBounds.getHeight();
        stage.setHeight(HEIGHT);
        stage.setWidth(WIDTH);
        map Map = new map();
        Map.Draw_map(HEIGHT, WIDTH);

        Scene scene = new Scene(Map.getGridPane());
        //     Timeline tl = new Timeline(new KeyFrame(Duration.millis(10)), e -> run());
        scene.setFill(Color.WHEAT);
        stage.setScene(scene);
        stage.show();


    }
}

  /*  private void run(){

    }
}*/
