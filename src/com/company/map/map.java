package com.company.map;

import com.company.Cell.*;
import javafx.animation.FadeTransition;
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
import javafx.stage.Stage;
import javafx.util.Duration;



public class map {

    public int map_size;
    private GridPane gridPane;
    private BorderPane root;
    private int levelGame;
    private int[][] Level;
    public Scene scene;
    public Button exitButton , check;
    public VBox textLevel , keys ;
    public enum move{
        top , right , left , down
    };
    private Way way;



    public Cell[][] cells;

    int height, width;

    private final int[][] level1 =   new int[][]{
                                                {0,0,0,0,0},
                                                {3,2,2,5,0},
                                                {4,2,2,6,0},
                                                {1,0,0,0,0},
                                                {3,2,2,2,0}
                                                };
    private final int[][]level2 = new int[][]{
                                                {0,0,0,0,0,0,0},
                                                {1,0,0,0,0,0,0},
                                                {1,0,0,0,0,0,0},
                                                {3,2,2,2,5,0,0},
                                                {0,4,2,2,6,0,0},
                                                {0,1,0,0,0,0,0},
                                                {0,3,2,2,2,2,0}
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
        this.cells =  new Cell[map_size][map_size];
        return;
    }

    public void Build_map(int height, int width) throws Exception {
        buildMapLevel();
        this.height = height;
        this.width = width;
        this.gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                Cell c = new Cell();
                int MaterOfType1 = (int) ((Math.random() * 100) % 2) + 1;
                int MaterOfType2 = (int) ((Math.random() * 100) % 4) + 1;
                int RandomCell   = (int) (((Math.random() * 100) % 3));
                switch (Level[row][col]){
                    case 0:
                        if(RandomCell == 1){
                            c.Cell(row,col,RandomCell,MaterOfType1);
                        }
                        else if (RandomCell == 2){
                            c.Cell(row,col,RandomCell,MaterOfType2);
                        }
                        else {
                            c.Cell(row,col,RandomCell,1);
                        }
                        break;
                    case 1:
                    case 2: c.Cell(row,col,1,MaterOfType1); break;
                    case 3:
                    case 4:
                    case 5:
                    case 6: c.Cell(row,col,2,MaterOfType2); break;
                    default:break;
                }
                gridPane.add(c, col, row);
                cells[row][col] = c;
            }
        }
        System.out.println("next level");
        Build_Button();
        cells[0][0].getPipe().setPipeType(3);
        cells[0][0].getPipe().setMatter(1);
        cells[map_size - 1][map_size - 1].getPipe().setPipeType(3);
        cells[map_size - 1][map_size - 1].getPipe().setMatter(2);
    }

    public void Build_Button(){//for buttons and text , etc.
        this.root = new BorderPane(this.gridPane);

        Label text = new Label("Level"+levelGame);
        text.setFont(new Font("Arial", 50));

        this.exitButton = new Button("Exit");
        exitButton.setPrefWidth(150);
        exitButton.setFont(new Font("Arial",20));

        this.check = new Button("Check");
        this.check.setPrefWidth(150);
        this.check.setFont(new Font("Arial",20));
        this.textLevel = new VBox(20 ,text);
        this.keys = new VBox(20,exitButton,check);

        this.root.setLeft(textLevel);
        this.root.setRight(keys);
        this.scene = new Scene(root);
        scene.setFill(Color.WHEAT);
    }

    public void updateGame() {
        this.check.setOnAction(event -> {
            Win_Lost();
        });
        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                cells[row][col].setOnMouseClicked(event -> {
                    System.out.println(event);
                    Cell cellClicked = (Cell) event.getSource();
                    int Row = GridPane.getRowIndex(cellClicked);
                    int Col = GridPane.getColumnIndex(cellClicked);
                    try {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            turn_PRIMSRY(Row, Col);
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            turn_SECONDARY(Row, Col);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public void turn_PRIMSRY(int row, int col) throws Exception {
        int matter = cells[row][col].getPipe().getMatter();
        if (cells[row][col].getPipe().Ability_to_turn()) {
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

    public void turn_SECONDARY(int row, int col) throws Exception {
        int matter = cells[row][col].getPipe().getMatter();
        if (cells[row][col].getPipe().Ability_to_turn()) {
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
        }
    }
    /*public boolean checkLevelCorrect() {
        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                int expectedType = Level[row][col];
                Cell cell = cells[row][col];
                int realType = cell.getPipe().getPipeType();
                int realMatter = cell.getPipe().getMatter();

                switch (Level[row][col]){
                    case 0: continue;
                    case 1: if(realType != 1 || realMatter != 1) {return false;} break;
                    case 2: if(realType != 1 || realMatter != 2) {return false;} break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:  if(realType != 2 || (realMatter != Level[row][col] - 2)){return false;} break;
                    default:break;
                }
            }
        }
        return true;
    }*/

    private void Win_Lost(){
        way = new Way();
        Text textMassage = new Text();

        textMassage.setFont(new Font("Arial" , 20));
        if(way.find_way(cells[0][0] , cells[map_size-1][map_size-1])){
            Stage levelCompleteStage = new Stage();
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


    public void Build_next_Level() {
        if(this.levelGame == 2){//for end a game
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

    public class Way {
        private final int[] dx = {0, 0, 1, -1};
        private final int[] dy = {1, -1, 0, 0};
        private boolean[][] visited;

        public boolean find_way(Cell start, Cell end) {
            visited = new boolean[map_size][map_size];
            return dfs(start.vector.row, start.vector.col, end.vector.row, end.vector.col);
        }

        private boolean dfs(int x, int y, int destX, int destY) {
            if (x == destX && y == destY) return true;

            visited[x][y] = true;

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (isValid(newX, newY) && !visited[newX][newY]) {
                    System.out.println("is valid");
                    if (check_connect(cells[x][y].vector, cells[newX][newY].vector)) {
                        if (dfs(newX, newY, destX, destY)) return true;
                    }
                }
            }
            return false;
        }

        private boolean isValid(int x, int y) {
            System.out.println(""+x+" "+y);
            return x >= 0 && x < map_size && y >= 0 && y < map_size;
        }

        private boolean check_connect(Cell.Vector cell1, Cell.Vector cell2) {
            Cell c1 = cells[cell1.row][cell1.col];
            Cell c2 = cells[cell2.row][cell2.col];

            if (!c1.getPipe().Ability_to_connect() || !c2.getPipe().Ability_to_connect()) {
                System.out.println("check connect");
                return false;
            }

            move direction = what_direction(c1, c2);
            if (direction == null) {
                System.out.println("direction null");
                return false;
            }
            boolean c1HasDirection = false;
            for (move m : c1.canConnect) {
                if (m == direction) {
                    System.out.println("c1 connected");
                    c1HasDirection = true;
                    break;
                }
            }

            move oppositeDirection = getOppositeDirection(direction);
            boolean c2HasOpposite = false;
            for (move m : c2.canConnect) {
                if (m == oppositeDirection) {
                    System.out.println("c2 connected");
                    c2HasOpposite = true;
                    break;
                }
            }

            return c1HasDirection && c2HasOpposite;
        }

        private move what_direction(Cell c1, Cell c2) {
            if (c1.vector.row == c2.vector.row && c1.vector.col == c2.vector.col + 1) {
                return move.left;
            } else if (c1.vector.row == c2.vector.row && c1.vector.col == c2.vector.col - 1) {
                return move.right;
            } else if (c1.vector.row == c2.vector.row + 1 && c1.vector.col == c2.vector.col) {
                return move.top;
            } else if (c1.vector.row == c2.vector.row - 1 && c1.vector.col == c2.vector.col) {
                return move.down;
            }
            return null;
        }

        private move getOppositeDirection(move direction) {
            switch (direction) {
                case top: return move.down;
                case down: return move.top;
                case left: return move.right;
                case right: return move.left;
                default: return null;
            }
        }
    }

    public void setLevelGame(int levelGame) {
        this.levelGame = levelGame;
    }
}
