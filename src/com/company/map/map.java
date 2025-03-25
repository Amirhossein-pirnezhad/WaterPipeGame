package com.company.map;

import com.company.Cell.*;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;



public class map {
    private final int map_size = 7;
    private GridPane gridPane;
    public Cell[][] cells = new Cell[map_size][map_size];

    int height, width;

    public void Build_map(int height, int width) throws Exception {
        this.height = height;
        this.width = width;
        this.gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                Cell c = new Cell();
                int n = (int) ((Math.random() * 100) % 9);
                System.out.println(n);
                c.Cell(row, col, n);
                gridPane.add(c, col, row);
                cells[row][col] = c;
            }
        }
    }

    public void updateGame(){
        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                cells[row][col].setOnMouseClicked(event -> {
                    System.out.println(event);
                    Cell cellClicked = (Cell) event.getSource();
                    int Row = GridPane.getRowIndex(cellClicked);
                    int Col = GridPane.getColumnIndex(cellClicked);
                    System.out.println("Clicked on: Row=" + Row + ", Col=" + Col);
                    try {
                        turn(Row , Col);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
    private void turn (int row , int col) throws Exception {
        int type = cells[row][col].getPipeType();
        type++;
        cells[row][col].setPipeType(type);
    }

        public GridPane getGridPane () {
            return gridPane;
        }

    }
