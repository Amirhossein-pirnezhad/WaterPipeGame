package com.company.map;

import com.company.Cell.*;
import com.company.Gameplay.*;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class map {
    private final int map_size = 7;
    private GridPane gridPane;
    public Cell[][] cells = new Cell[map_size][map_size];

    int height , width;

    public void Draw_map (int height , int width) throws Exception {
        this.height = height;
        this.width = width;
        this.gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                Cell c = new Cell();
                int n =(int) ((Math.random()*100)%9);
                System.out.println(n);
                c.Cell(row , col , n);
                c.setOnMouseClicked(event -> {
                    System.out.println(event);
                });
                gridPane.add(c, col, row);
                cells[row][col] = c;
            }
        }
    }
    public GridPane getGridPane() {
        return gridPane;
    }

}