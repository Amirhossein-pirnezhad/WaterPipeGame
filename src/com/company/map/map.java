package com.company.map;

import com.company.Cell.*;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public class map {

    public static int map_size;
    public static GridPane gridPane;
    public static int levelGame = 1;
    private int[][] Level;
    public Scene scene;
    public Button exitButton , check , restart , undo , Ai , help;
    public static VBox textLevel , keys ;
    public enum move{
        top , right , left , down
    }
    public Way way;
    public static int availableMoves = 30;
    private Undo UNDO = new Undo();
    private TimeLimit timeLimit;

    public static Cell[][] cells;
    private CellData[][] cellData;
    private List<Way.Step> correctPath;

    int height, width;

    private final int[][] level1 =   new int[][]{
            {7,0,0,0,0},
            {3,2,2,5,0},
            {4,2,2,6,0},
            {9,0,0,0,0},
            {3,2,2,2,8}
    };
    private final int[][]level2 = new int[][]{
            {7,0,0,0,0,0,0},
            {1,0,0,0,0,0,0},
            {1,0,0,0,6,6,0},
            {3,2,2,2,9,6,0},
            {0,4,2,2,6,0,0},
            {0,1,0,0,0,0,0},
            {0,3,2,2,2,2,8}
    };

    private final int[][]level3 = new int[][]{
            {7,0,0,0,0,0,0,0},
            {3,2,5,0,0,0,0,0},
            {4,2,6,0,0,0,0,0},
            {1,0,0,0,0,0,0,0},
            {1,0,0,0,0,0,0,0},
            {3,2,2,2,5,0,0,0},
            {0,4,2,2,6,0,0,0},
            {0,3,2,2,2,2,2,8}
    };

    public class CellData {//for save a game
        public int row, col, pipeType, matter;

        public CellData(int row, int col, int pipeType, int matter) {
            this.row = row;
            this.col = col;
            this.pipeType = pipeType;
            this.matter = matter;
        }
    }

    private void buildMapLevel(){
        if(levelGame == 1){
            this.Level = level1;
            map_size = 5;
        }
        else if(levelGame == 2){
            this.Level = level2;
            map_size = 7;
        }
        else if (levelGame == 3){
            Level = level3;
            map_size = 8;
        }
        else {
            map_size = (int)(Math.sqrt(3 * levelGame) + 5);
        }
        cells = new Cell[map_size][map_size];
        this.cellData = new CellData[map_size][map_size];
        this.way = new Way();
        this.UNDO = new Undo();
        timeLimit = new TimeLimit();
        gridPane = new GridPane();
    }

    public void Build_map(int height, int width) throws Exception {
        buildMapLevel();
        this.height = height;
        this.width = width;

        gridPane.setAlignment(Pos.CENTER);
        if(levelGame<=3) {
            for (int row = 0; row < map_size; row++) {
                for (int col = 0; col < map_size; col++) {
                    Cell c = new Cell(row,col,0,0);
                    int MaterOfType1 = (int) ((Math.random() * 100) % 2) + 1;
                    int MaterOfType2 = (int) ((Math.random() * 100) % 4) + 1;
                    int random = (int) (((Math.random() * 100) % 100));
                    int RandomCell = 0;
                    if (random <= 40)
                        RandomCell = 2;
                    else if (random <= 70)
                        RandomCell = 1;
                    else if (random <= 80)
                        RandomCell = 3;
                    else if (random < 100)
                        RandomCell = 0;
                    switch (Level[row][col]) {
                        case 0:
                            if (RandomCell == 1) {
                                c = new Cell(row, col, RandomCell, MaterOfType1);
                            } else if (RandomCell == 2) {
                                c = new Cell(row, col, RandomCell, MaterOfType2);
                            } else {
                                c = new Cell(row, col, RandomCell, 1);
                            }
                            break;
                        case 1:
                        case 2:
                            c = new Cell(row, col, 1, MaterOfType1);
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            c = new Cell(row, col, 2, MaterOfType2);
                            break;
                        case 7:
                            c = new Cell(row, col, 4, 1);
                            break;
                        case 8:
                            c = new Cell(row, col, 4, 2);
                            break;
                        case 9:
                            c = new Cell(row, col, 3, 1);
                            break;
                        default:
                            break;
                    }
                    gridPane.add(c, col, row);
                    cells[row][col] = c;
                }
            }
            setStartAndEndPoints(map_size);
            if(way.find_way_Ai(cells[0][0] , cells[map_size-1][map_size-1])){
                this.correctPath = new ArrayList<>(way.correctPath);
            }
        }
        else{
            generateRandomMap(map_size);
        }
        System.out.println("next level");
        Build_Button();
        for (int i = 0; i < map_size; i++) {// save initial map
            for (int j = 0; j < map_size; j++) {
                cellData[i][j] = new CellData(cells[i][j].vector.row , cells[i][j].vector.col , cells[i][j].getPipe().getPipeType() , cells[i][j].getPipe().getMatter());
            }
        }

    }

    public void Build_Button() {

        BorderPane root = new BorderPane(gridPane);

        root.setStyle("-fx-background-color: linear-gradient(to bottom,#0f2027,#203a43,#2c5364);");

        Label text = new Label("LEVEL " + levelGame);
        text.setFont(Font.font("Verdana", 40));
        text.setTextFill(Color.web("#FFD54F"));
        text.setStyle("-fx-font-weight:bold;");

        this.exitButton = new Button("Exit");
        this.check = new Button("Check");
        this.restart = new Button("Restart");
        this.undo = new Button("Undo");
        this.Ai = new Button("AI");
        this.help = new Button("Help");

        styleButton(exitButton);
        styleButton(check);
        styleButton(restart);
        styleButton(undo);
        styleButton(Ai);
        styleButton(help);

        textLevel = new VBox(30, text);
        textLevel.setAlignment(Pos.TOP_CENTER);
        textLevel.setPadding(new Insets(30));

        keys = new VBox(20,
                check,
                Ai,
                help,
                undo,
                restart,
                exitButton);

        keys.setAlignment(Pos.CENTER);
        keys.setPadding(new Insets(20));

        root.setLeft(textLevel);
        root.setRight(keys);

        BorderPane.setMargin(gridPane, new Insets(20));
        BorderPane.setMargin(keys, new Insets(20));
        BorderPane.setMargin(textLevel, new Insets(20));

        this.scene = new Scene(root);
    }

    public void updateGame() {
        Label movesLabel = new Label("Moves Left: " + availableMoves);
        movesLabel.setFont(new Font("Arial", 20));
        Label text = new Label("LEVEL " + levelGame);
        text.setFont(Font.font("Verdana", 42));
        text.setTextFill(Color.web("#FFD54F"));
        text.setStyle("-fx-font-weight:bold;");
        movesLabel.setTextFill(Color.RED);
        this.check.setOnAction(event -> {
            Win_Lost();
        });
        if(levelGame == 2){
            textLevel.getChildren().clear();
            textLevel.getChildren().addAll(text, movesLabel);
        }
        else if(levelGame >= 3){
            textLevel.getChildren().clear();
            textLevel.getChildren().addAll(text , timeLimit.showTime);
            timeLimit.tl.setCycleCount(Timeline.INDEFINITE);
            timeLimit.timeTable();
        }

        timeLimit.tl.setOnFinished(event -> {
            lose_by_moves();
        });

        this.Ai.setOnAction(e->{
            try {
                if(way.find_way_Ai(cells[0][0], cells[map_size-1][map_size-1])) {
                    System.out.println("Path found:");
                    for (Way.Step step : way.correctPath) {
                        System.out.println("Row: " + step.row + ", Col: " + step.col + ", Matter: " + step.correctMatter);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        this.help.setOnAction(event -> {
            way.show_correct_way(this.correctPath);
        });

        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                cells[row][col].setOnMouseClicked(event -> {
                    Cell cellClicked = (Cell) event.getSource();
                    int Row = GridPane.getRowIndex(cellClicked);
                    int Col = GridPane.getColumnIndex(cellClicked);
                    if(timeLimit.check_if_player_moved(cells[Row][Col])) {
                        System.out.println(event);
                        UNDO.saveMove(cells[Row][Col] , cells[Row][Col].getPipe().getMatter());
                        try {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                turn_PRIMSRY(Row, Col);
                            } else if (event.getButton() == MouseButton.SECONDARY) {
                                turn_SECONDARY(Row, Col);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        lose_by_moves();
                    }
                });
            }
        }
    }

    public static void turn_PRIMSRY(int row, int col) throws Exception {
        if (cells[row][col].getPipe().Ability_to_turn()) {
            int matter = cells[row][col].getPipe().getMatter();
            RotateTransition rotate = new RotateTransition(Duration.millis(100), cells[row][col].lookup(".image-view"));
            rotate.setByAngle(90);
            rotate.setCycleCount(1);
            rotate.setAutoReverse(false);
            rotate.play();

            switch (cells[row][col].getPipe().getPipeType()) {
                case 1:
                    if (cells[row][col].getPipe().getMatter() != 2) {
                        matter++;
                        cells[row][col].getPipe().setMatter(matter);
                    } else {
                        cells[row][col].getPipe().setMatter(1);
                    }
                    break;
                case 2:
                    if (cells[row][col].getPipe().getMatter() != 4) {
                        matter++;
                        cells[row][col].getPipe().setMatter(matter);
                    } else {
                        cells[row][col].getPipe().setMatter(1);
                    }
            }
        } else {
            return;
        }

    }

    public static void turn_SECONDARY(int row, int col) throws Exception {
        if (cells[row][col].getPipe().Ability_to_turn()) {
            int matter = cells[row][col].getPipe().getMatter();
            RotateTransition rotate = new RotateTransition(Duration.millis(100), cells[row][col].lookup(".image-view"));
            rotate.setByAngle(-90);
            rotate.setCycleCount(1);
            rotate.setAutoReverse(false);
            rotate.play();

            switch (cells[row][col].getPipe().getPipeType()) {
                case 1:
                    if (cells[row][col].getPipe().getMatter() != 1) {
                        matter--;
                        cells[row][col].getPipe().setMatter(matter);
                    } else {
                        cells[row][col].getPipe().setMatter(2);
                    }
                    break;
                case 2:
                    if (cells[row][col].getPipe().getMatter() != 1) {
                        matter--;
                        cells[row][col].getPipe().setMatter(matter);
                    } else {
                        cells[row][col].getPipe().setMatter(4);
                    }
            }
        }else {
            return;
        }

    }

    private void Win_Lost(){
        Text textMassage = new Text();

        textMassage.setFont(new Font("Arial" , 20));
        if(way.find_way(cells[0][0] , cells[map_size-1][map_size-1])){

            if(levelGame >= 3) timeLimit.tl.pause();
            Stage levelCompleteStage = new Stage();
            levelCompleteStage.initModality(Modality.APPLICATION_MODAL);
            levelCompleteStage.setTitle("Level Complete!");
            VBox layout = new VBox(20);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));

            Label message = new Label("Congratulations! You won level " + levelGame);
            message.setFont(new Font("Arial", 18));

            Button nextLevelBtn = new Button("Next Level");

            nextLevelBtn.setOnAction(e -> {
                levelCompleteStage.close();

                FadeTransition fade = new FadeTransition(Duration.seconds(0.5), gridPane);
                fade.setFromValue(1);
                fade.setToValue(0);

                fade.setOnFinished(event -> {
                    Build_next_Level();

                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), gridPane);
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();
                });

                fade.play();
            });
            Button exitBtn = new Button("Exit");
            exitBtn.setOnAction(e -> {
                levelCompleteStage.close();
                Stage stage = (Stage) gridPane.getScene().getWindow();
                stage.close();
            });

            HBox buttons = new HBox(15, nextLevelBtn, exitBtn);
            buttons.setAlignment(Pos.CENTER);

            layout.getChildren().addAll(message, buttons);

            Scene scene = new Scene(layout, 500, 200);
            levelCompleteStage.setScene(scene);
            levelCompleteStage.showAndWait();

            Build_next_Level();
        }
        else{
            textMassage.setText("Wrong way");
            textMassage.setFill(Color.RED);
            System.out.println("You don't win");
        }
        keys.getChildren().add(textMassage);
        FadeTransition fade = new FadeTransition(Duration.seconds(2), textMassage);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(event -> keys.getChildren().remove(textMassage));
        fade.play();
    }
    public void lose_by_moves() {
        Platform.runLater(() -> {
            Stage loser = new Stage();
            loser.initModality(Modality.APPLICATION_MODAL);
            loser.setTitle("You lost!");
            VBox choice = new VBox(20);
            choice.setAlignment(Pos.CENTER);
            choice.setPadding(new Insets(20));

            Label message = new Label("You lost! If you want to try again, press RESTART.");
            message.setFont(new Font("Arial", 18));

            Button exitBtn = new Button("Exit");
            exitBtn.setFont(new Font("Arial", 16));
            exitBtn.setOnAction(e -> {
                loser.close();
                Stage stage = (Stage) gridPane.getScene().getWindow();
                stage.close();
            });

            Button restartBtn = new Button("RESTART");
            restartBtn.setFont(new Font("Arial", 16));
            restartBtn.setOnAction(e -> {
                loser.close();
                try {
                    availableMoves = 30;
                    gridPane.getChildren().clear();
                    Build_map(height, width);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });


            choice.getChildren().addAll(message, restartBtn, exitBtn);

            Scene scene = new Scene(choice, 400, 200);
            loser.setScene(scene);
            loser.showAndWait();
        });
    }

    public void Build_next_Level() {
        if(levelGame == 100){//for end a game
            Stage levelCompleteStage = new Stage();
            levelCompleteStage.setTitle("Level Complete!");
            VBox layout = new VBox(20);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));

            Label message = new Label("You Win! Thanks for playing ");
            message.setFont(new Font("Arial", 18));

            Button exitBtn = new Button("Exit");
            exitBtn.setOnAction(e -> {
                levelCompleteStage.close();
                Stage stage = (Stage) gridPane.getScene().getWindow();
                stage.close();
            });

            HBox buttons = new HBox(exitBtn);
            buttons.setAlignment(Pos.CENTER);

            layout.getChildren().addAll(message, buttons);

            Scene scene = new Scene(layout, 500, 200);
            levelCompleteStage.setScene(scene);
            levelCompleteStage.showAndWait();
        }
        levelGame++;
        gridPane.getChildren().clear();
        cells = null;
        this.way = null;
        this.undo = null;
        this.correctPath = null;
        timeLimit = new TimeLimit();
        try {
            Build_map(height,width);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateRandomMap(int mapSize) throws Exception {
        boolean[][] used = new boolean[mapSize][mapSize];
        used[0][0] = true;
        used[mapSize - 1][mapSize - 1] = true;

        List<int[]> innerPath = new ArrayList<>();
        if (!buildRandomPath(1, 0, mapSize - 1, mapSize - 2, mapSize, used, innerPath)) {
            throw new Exception("Could not build a solvable path for mapSize=" + mapSize);
        }

        List<int[]> pathCells = new ArrayList<>();
        pathCells.add(new int[]{0, 0});
        pathCells.addAll(innerPath);
        pathCells.add(new int[]{mapSize - 1, mapSize - 1});

        Map<String, int[]> pathShapes = new HashMap<>();
        for (int i = 1; i < pathCells.size() - 1; i++) {
            int[] prev = pathCells.get(i - 1);
            int[] cur = pathCells.get(i);
            int[] next = pathCells.get(i + 1);

            move dirIn = directionBetween(prev, cur);
            move dirOut = directionBetween(cur, next);
            move dirBack = opposite(dirIn);

            pathShapes.put(cur[0] + "," + cur[1], shapeFromDirections(dirBack, dirOut));
        }

        boolean[][] onPath = new boolean[mapSize][mapSize];
        for (int[] p : pathCells) onPath[p[0]][p[1]] = true;

        correctPath = new ArrayList<>();
        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                Cell c;
                if (row == 0 && col == 0) {
                    c = new Cell(row, col, 4, 1);
                } else if (row == mapSize - 1 && col == mapSize - 1) {
                    c = new Cell(row, col, 4, 2);
                } else if (onPath[row][col]) {
                    int[] shape = pathShapes.get(row + "," + col); // {pipeType, correctMatter}
                    int startMatter = randomMatterForType(shape[0]);
                    c = new Cell(row, col, shape[0], startMatter);
                    correctPath.add(way.new Step(row, col, shape[1]));
                } else {
                    int[] randomCellData = generateRandomCellType(row, col);
                    c = new Cell(row, col, randomCellData[0], randomCellData[1]);
                }
                gridPane.add(c, col, row);
                cells[row][col] = c;
            }
        }
    }

    private boolean buildRandomPath(int row, int col, int destRow, int destCol,
                                    int mapSize, boolean[][] used, List<int[]> path) {
        used[row][col] = true;
        path.add(new int[]{row, col});

        if (row == destRow && col == destCol) return true;

        int[] dRow = {1, -1, 0, 0};
        int[] dCol = {0, 0, 1, -1};
        List<Integer> order = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(order);

        for (int idx : order) {
            int newRow = row + dRow[idx];
            int newCol = col + dCol[idx];
            if (newRow >= 0 && newRow < mapSize && newCol >= 0 && newCol < mapSize && !used[newRow][newCol]) {
                if (buildRandomPath(newRow, newCol, destRow, destCol, mapSize, used, path)) return true;
            }
        }

        used[row][col] = false;
        path.remove(path.size() - 1);
        return false;
    }

    private move directionBetween(int[] a, int[] b) {
        if (b[0] == a[0] + 1 && b[1] == a[1]) return move.down;
        if (b[0] == a[0] - 1 && b[1] == a[1]) return move.top;
        if (b[1] == a[1] + 1 && b[0] == a[0]) return move.right;
        if (b[1] == a[1] - 1 && b[0] == a[0]) return move.left;
        return null;
    }

    private move opposite(move m) {
        switch (m) {
            case top: return move.down;
            case down: return move.top;
            case left: return move.right;
            case right: return move.left;
            default: return null;
        }
    }

    private int[] shapeFromDirections(move d1, move d2) {
        if (isPair(d1, d2, move.top, move.down)) return new int[]{1, 1};
        if (isPair(d1, d2, move.left, move.right)) return new int[]{1, 2};
        if (isPair(d1, d2, move.top, move.right)) return new int[]{2, 1};
        if (isPair(d1, d2, move.right, move.down)) return new int[]{2, 2};
        if (isPair(d1, d2, move.left, move.down)) return new int[]{2, 3};
        if (isPair(d1, d2, move.top, move.left)) return new int[]{2, 4};
        throw new IllegalStateException("Unexpected direction pair: " + d1 + ", " + d2);
    }

    private boolean isPair(move d1, move d2, move a, move b) {
        return (d1 == a && d2 == b) || (d1 == b && d2 == a);
    }

    private int randomMatterForType(int pipeType) {
        if (pipeType == 1) return (int) (Math.random() * 2) + 1; // 1 or 2
        if (pipeType == 2) return (int) (Math.random() * 4) + 1; // 1..4
        return 1;
    }

    private int[] generateRandomCellType(int row, int col) {
        int MaterOfType1 = (int) (Math.random() * 2) + 1;
        int MaterOfType2 = (int) (Math.random() * 4) + 1;
        int random = (int) (Math.random() * 100);
        int RandomCell = 0;

        int maxAttempts = 5;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            random = (int) (Math.random() * 100);
            if (random <= 40) RandomCell = 2;
            else if (random <= 80) RandomCell = 1;
            else if (random <= 90) RandomCell = 3;
            else RandomCell = 0;

            if (RandomCell != 3 || !hasAdjacentCross(row, col)) {
                break;
            }

            if (attempt == maxAttempts - 1) {
                RandomCell = (random <= 50) ? 1 : 2;
            }
        }

        int material = (RandomCell == 1) ? MaterOfType1 :
                (RandomCell == 2) ? MaterOfType2 : 1;

        return new int[]{RandomCell, material};
    }

    private boolean hasAdjacentCross(int row, int col) {
        return (row > 0 && cells[row-1][col].getPipe().getPipeType() == 3) ||
                (col > 0 && cells[row][col-1].getPipe().getPipeType() == 3);
    }

    private void setStartAndEndPoints(int mapSize) throws Exception {
        cells[0][0].getPipe().setPipeType(4);
        cells[0][0].getPipe().setMatter(1);
        cells[0][0].cell_shape();
        cells[mapSize-1][mapSize-1].getPipe().setPipeType(4);
        cells[mapSize-1][mapSize-1].getPipe().setMatter(2);
        cells[mapSize-1][mapSize-1].cell_shape();
    }

    private void rebuildMap(int mapSize) throws Exception {
        gridPane.getChildren().clear();
        cells = new Cell[mapSize][mapSize];
        generateRandomMap(mapSize);
    }

    public void restartLevel() throws Exception {
        way = new Way();
        UNDO = new Undo();
        timeLimit = new TimeLimit();
        gridPane.getChildren().clear();
        cells = new Cell[map_size][map_size];
        for (int i = 0; i < map_size; i++) {
            for (int j = 0; j < map_size; j++) {
               cells[i][j] = new Cell(cellData[i][j].row ,cellData[i][j].col , cellData[i][j].pipeType , cellData[i][j].matter);
               cells[i][j].cell_shape();
               gridPane.add(cells[i][j], j, i);
            }
        }
        if(way.find_way_Ai(cells[0][0] , cells[map_size-1][map_size-1])){
            this.correctPath = new ArrayList<>(way.correctPath);
        }
    }

    private void styleButton(Button button) {

        button.setPrefWidth(180);
        button.setPrefHeight(50);

        button.setStyle(
                "-fx-background-color: linear-gradient(#4facfe,#00c6fb);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {
            button.setScaleX(1.08);
            button.setScaleY(1.08);

            button.setStyle(
                    "-fx-background-color: linear-gradient(#43e97b,#38f9d7);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 2;" +
                            "-fx-cursor: hand;"
            );
        });

        button.setOnMouseExited(e -> {
            button.setScaleX(1);
            button.setScaleY(1);

            button.setStyle(
                    "-fx-background-color: linear-gradient(#4facfe,#00c6fb);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 2;" +
                            "-fx-cursor: hand;"
            );
        });
    }

    public void setLevelGame(int levelGame) {
        this.levelGame = levelGame;
    }

    public void setAvailableMoves(int availableMoves) {
        availableMoves = availableMoves;
    }

    public Undo getUNDO() {
        return UNDO;
    }
}


