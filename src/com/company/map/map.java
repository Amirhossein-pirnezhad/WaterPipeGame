package com.company.map;

import com.company.Cell.*;
import com.company.Gameplay.*;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;


public class map {
    private final int map_size = 7;

    private GridPane gridPane;

    int height , width;

    public void Draw_map (int height , int width){
        this.height = height;
        this.width = width;
        this.gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                Cell c = new Cell();
                int n =(int) ((Math.random()*100)%2);
                System.out.println(n);
                c.Cell(row , col , n);

                c.setOnMouseClicked(event -> {
                    System.out.println(event);
                });
                gridPane.add(c, col, row);
            }
        }

    }

    public GridPane getGridPane() {
        return gridPane;
    }

}
