package com.company.map;

import com.company.Cell.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
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

import java.util.Timer;


public class map {

    public static int map_size;
    public static GridPane gridPane;
    public static int levelGame = 1;
    private int[][] Level;
    public Scene scene;
    public Button exitButton , check , restart , undo , Ai;
    public static VBox textLevel , keys ;
    public enum move{
        top , right , left , down
    };
    public Way way;
    public static int availableMoves = 30;
    private Undo UNDO = new Undo();
    private TimeLimit timeLimit = new TimeLimit();

    public static Cell[][] cells;

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
    private void buildMapLevel(){
        if(this.levelGame == 1){
            this.Level = level1;
            this.map_size = 5;
        }
        else if(this.levelGame == 2){
            this.Level = level2;
            this.map_size = 7;
        }
        else if (this.levelGame == 3){
            this.Level = level3;
            this.map_size = 8;
        }
        else {
            this.map_size = (int)(Math.sqrt(3*this.levelGame) + 5);
        }
        this.cells = new Cell[map_size][map_size];
        return;
    }

    public void Build_map(int height, int width) throws Exception {
        buildMapLevel();
        this.height = height;
        this.width = width;
        this.gridPane = new GridPane();
        way = new Way();
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
        }
        else{
            generateRandomMap(map_size);
        }
        System.out.println("next level");
        Build_Button();
        cells[0][0].getPipe().setPipeType(4);
        cells[0][0].getPipe().setMatter(1);
        cells[map_size - 1][map_size - 1].getPipe().setMatter(2);
        cells[map_size - 1][map_size - 1].getPipe().setPipeType(4);

    }

    public void Build_Button(){//for buttons and text , etc.
        BorderPane root = new BorderPane(this.gridPane);

        Label text = new Label("Level"+levelGame);
        text.setFont(new Font("Arial", 50));

        this.exitButton = new Button("Exit");
        this.exitButton.setPrefWidth(150);
        this.exitButton.setFont(new Font("Arial",20));

        this.check = new Button("Check");
        this.check.setPrefWidth(150);
        this.check.setFont(new Font("Arial",20));

        this.restart = new Button("RESTART");
        this.restart.setPrefWidth(150);
        this.restart.setFont(new Font("Arial" , 20));

        this.undo = new Button("Undo");
        this.undo.setPrefWidth(150);
        this.undo.setFont(new Font("Arial",20));

        this.Ai = new Button("Ai");
        this.Ai.setPrefWidth(150);
        this.Ai.setFont(new Font("Arial",20));

        this.textLevel = new VBox(20 ,text);
        this.keys = new VBox(20,exitButton,check,Ai,undo,restart);

        root.setLeft(textLevel);
        root.setRight(keys);
        this.scene = new Scene(root);
        scene.setFill(Color.WHEAT);
    }

    public void updateGame() {
        Label movesLabel = new Label("Moves Left: " + availableMoves);
        movesLabel.setFont(new Font("Arial", 20));
        Label text = new Label("Level"+levelGame);
        text.setFont(new Font("Arial", 50));
        movesLabel.setTextFill(Color.RED);
        this.check.setOnAction(event -> {
            Win_Lost();
        });
        if(levelGame == 2){
            this.textLevel.getChildren().clear();
            this.textLevel.getChildren().addAll(text, movesLabel);
        }
        else if(levelGame == 3){
            this.textLevel.getChildren().clear();
            this.textLevel.getChildren().addAll(text , timeLimit.showTime);
            this.timeLimit.tl.setCycleCount(Timeline.INDEFINITE);
            this.timeLimit.tl.play();
            this.timeLimit.timeTable();
        }

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

        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                cells[row][col].setOnMouseClicked(event -> {
                    Cell cellClicked = (Cell) event.getSource();
                    int Row = GridPane.getRowIndex(cellClicked);
                    int Col = GridPane.getColumnIndex(cellClicked);
                    if(timeLimit.check_moves(cells[Row][Col])) {
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
            if(levelGame == 3) timeLimit.tl.pause();
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
        this.keys.getChildren().add(textMassage);
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
                    timeLimit.setSecond(90);
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
        if(this.levelGame == 100){//for end a game
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
        this.levelGame++;
        this.gridPane.getChildren().clear();
        this.cells = null;
        try {
            Build_map(height,width);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateRandomMap(int mapSize) throws Exception {
        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                int[] randomCellData = generateRandomCellType(row, col);
                int randomCellType = randomCellData[0];
                int materialType = randomCellData[1];
                Cell c;
                if (randomCellType == 1) {
                    c = new Cell(row, col, randomCellType, materialType);
                } else if (randomCellType == 2) {
                    c = new Cell(row, col, randomCellType, materialType);
                } else {
                    c = new Cell(row, col, randomCellType, 1);
                }

                gridPane.add(c, col, row);
                cells[row][col] = c;
            }
        }

        setStartAndEndPoints(mapSize);

        if (!way.find_way_Ai(cells[0][0], cells[mapSize-1][mapSize-1])) {
            rebuildMap(mapSize);
        }
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

    public void setLevelGame(int levelGame) {
        this.levelGame = levelGame;
    }

    public void setAvailableMoves(int availableMoves) {
        this.availableMoves = availableMoves;
    }

    public Undo getUNDO() {
        return UNDO;
    }
}


