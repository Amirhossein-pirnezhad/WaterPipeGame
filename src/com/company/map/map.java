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
                int n = (int) ((Math.random() * 100) % 3);
                int m = (int) ((Math.random()*100) % 2)+1;
                c.Cell(row, col, n , m);
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
    public void turn (int row , int col) throws Exception {
        System.out.println("Turn method called for cell at row " + row + ", col " + col);
        int matter = cells[row][col].getPipe().getMatter();
        if(cells[row][col].getPipe().Ability_to_turn()){
            switch (cells[row][col].getPipe().getPipeType()){
                case 1 :
                    if(cells[row][col].getPipe().getMatter() != 2){
                        matter++;
                        cells[row][col].getPipe().setMatter(matter);
                    }
                    else{
                        cells[row][col].getPipe().setMatter(1);
                    }
                    break;
                case 2 :
                    if(cells[row][col].getPipe().getMatter() != 4){
                        matter++;
                        cells[row][col].getPipe().setMatter(matter);
                    }
                    else{
                        cells[row][col].getPipe().setMatter(1);
                    }
            }
        }
        else{
            return;
        }
    }
    public GridPane getGridPane () {
        return gridPane;
    }

    }
