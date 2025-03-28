package com.company.map;

import com.company.Cell.*;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;


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
        cells[0][0].getPipe().setPipeType(3);
        cells[0][0].getPipe().setMatter(1);
        cells[map_size-1][map_size-1].getPipe().setPipeType(3);
        cells[map_size-1][map_size-1].getPipe().setMatter(2);
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

    public boolean isConnection(){
        boolean[][]visited = new boolean[map_size][map_size] ;
        return checkWay(0,1,visited);
    }

    public boolean checkWay(int row , int col , boolean[][] visited){
        for (int i = 0; i < 2; i++) {
            if(cells[row][col].mustConnect[i].getKey() == map_size - 1 && cells[row][col].mustConnect[i].getValue() == map_size - 1)
                return true;
        }
        if (row < 0 || row >= map_size || col < 0 || col >= map_size
                || visited[row][col]
                || cells[row][col].getPipe().getPipeType() == 0) {
            return false;
        }
        visited[row][col] = true;
        Pair<Integer, Integer>[] connections = cells[row][col].mustConnect;
        if (connections == null) return false;
        for (Pair<Integer, Integer> connection : connections) {
            if (connection != null) {
                int nextRow = connection.getKey();
                int nextCol = connection.getValue();

                if (is_Connected(row, col, nextRow, nextCol)) {
                    if (checkWay(nextRow, nextCol, visited)) {
                        return true;
                    }
                }
            }
        }
        System.out.println("can't connect");
        return false;
    }

    public boolean is_Connected(int row1 , int col1 , int row2, int col2){//for neighbor cells
        if (row2 < 0 || row2 >= map_size || col2 < 0 || col2 >= map_size) {
            return false;
        }

        if (cells[row2][col2].mustConnect == null) return false;

        for (int i = 0; i < 2 ; i++) {
            for (int j = 0; j < 2 ; j++) {
                if(cells[row1][col1].getPipe().canConnect[i] == 4 - cells[row2][col2].getPipe().canConnect[j]){
                    return true;
                }
            }
        }
        return false;
    }
    public GridPane getGridPane () {
        return gridPane;
    }

    }
