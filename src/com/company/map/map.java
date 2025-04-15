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

    public int map_size;
    private GridPane gridPane;
    public int levelGame = 1;
    private int[][] Level;
    public Scene scene;
    public Button exitButton , check , restart , undo , Ai;
    public VBox textLevel , keys ;
    public enum move{
        top , right , left , down
    };
    public Way way;
    private int availableMoves = 30;
    private int minute = 1;
    private int second = 30;
    private Undo UNDO = new Undo();
    private TimeLimit timeLimit = new TimeLimit();

    public Cell[][] cells;

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
        this.cells =  new Cell[map_size][map_size];
        return;
    }

    public void Build_map(int height, int width) throws Exception {
        buildMapLevel();
        this.height = height;
        this.width = width;
        this.gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        if(levelGame<=3) {
            for (int row = 0; row < map_size; row++) {
                for (int col = 0; col < map_size; col++) {
                    Cell c = new Cell();
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
                                c.Cell(row, col, RandomCell, MaterOfType1);
                            } else if (RandomCell == 2) {
                                c.Cell(row, col, RandomCell, MaterOfType2);
                            } else {
                                c.Cell(row, col, RandomCell, 1);
                            }
                            break;
                        case 1:
                        case 2:
                            c.Cell(row, col, 1, MaterOfType1);
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            c.Cell(row, col, 2, MaterOfType2);
                            break;
                        case 7:
                            c.Cell(row, col, 4, 1);
                            break;
                        case 8:
                            c.Cell(row, col, 4, 2);
                            break;
                        case 9:
                            c.Cell(row, col, 3, 1);
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
            for (int row = 0; row < map_size; row++) {
                for (int col = 0; col < map_size; col++) {
                    Cell c = new Cell();
                    int MaterOfType1 = (int) ((Math.random() * 100) % 2) + 1;
                    int MaterOfType2 = (int) ((Math.random() * 100) % 4) + 1;
                    int random = (int) (((Math.random() * 100) % 100));
                    int RandomCell = 0;
                    if (random <= 40)
                        RandomCell = 2;
                    else if (random <= 80)
                        RandomCell = 1;
                    else if (random <= 90)
                        RandomCell = 3;
                    else if (random < 100)
                        RandomCell = 0;
                    if (RandomCell == 1) {
                        c.Cell(row, col, RandomCell, MaterOfType1);
                    } else if (RandomCell == 2) {
                        c.Cell(row, col, RandomCell, MaterOfType2);
                    } else {
                        c.Cell(row, col, RandomCell, 1);
                    }
                    gridPane.add(c, col, row);
                    cells[row][col] = c;
                }
            }
            cells[0][0].getPipe().setPipeType(4);
            cells[0][0].getPipe().setMatter(1);
            cells[map_size - 1][map_size - 1].getPipe().setPipeType(4);
            cells[map_size - 1][map_size - 1].getPipe().setMatter(2);
            if(!way.find_way_Ai(cells[0][0], cells[map_size-1][map_size-1])){
                Build_map(height,width);
            }
        }
        System.out.println("next level");
        Build_Button();
        cells[0][0].getPipe().setPipeType(4);
        cells[0][0].getPipe().setMatter(1);
        cells[map_size - 1][map_size - 1].getPipe().setPipeType(4);
        cells[map_size - 1][map_size - 1].getPipe().setMatter(2);
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
        this.Ai.setOnAction(e->{
            try {
                if(way.find_way_Ai(cells[0][0] , cells[map_size-1][map_size-1]))System.out.println("find way!!!!!!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

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
        for (int row = 0; row < map_size; row++) {
            for (int col = 0; col < map_size; col++) {
                cells[row][col].setOnMouseClicked(event -> {
                    Cell cellClicked = (Cell) event.getSource();
                    int Row = GridPane.getRowIndex(cellClicked);
                    int Col = GridPane.getColumnIndex(cellClicked);
                    if(check_moves(cells[Row][Col])) {
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
                        timeLimit.lose_by_moves();
                    }
                });
            }
        }
    }

    public void turn_PRIMSRY(int row, int col) throws Exception {
        if (cells[row][col].getPipe().Ability_to_turn()) {
            int matter = cells[row][col].getPipe().getMatter();
            RotateTransition rotate = new RotateTransition(Duration.millis(200), cells[row][col].lookup(".image-view"));
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

    public void turn_SECONDARY(int row, int col) throws Exception {
        if (cells[row][col].getPipe().Ability_to_turn()) {
            int matter = cells[row][col].getPipe().getMatter();
            RotateTransition rotate = new RotateTransition(Duration.millis(200), cells[row][col].lookup(".image-view"));
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
        }
    }

    private void Win_Lost(){
        way = new Way();
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


    public void Build_next_Level() {
        if(this.levelGame == 12){//for end a game
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
        private boolean[][] visit;
        private int[][] originalMatter;

        public boolean find_way(Cell start, Cell end) {
            visited = new boolean[map_size][map_size];
            return dfs(start.vector.row, start.vector.col, end.vector.row, end.vector.col);
        }

        public boolean find_way_Ai(Cell start, Cell finish) throws Exception {
            visit = new boolean[map_size][map_size];
            this.originalMatter = new int[map_size][map_size];
//            animateAI(start.vector.row, start.vector.col, finish.vector.row, finish.vector.col);
            return Ai(start.vector.row, start.vector.col, finish.vector.row, finish.vector.col);
        }

        private void animateAI(int x, int y, int destX, int destY) {
            new Thread(() -> {
                try {
                    boolean result = Ai(x, y, destX, destY);
                    Platform.runLater(() -> {
                        if (result) {
                            System.out.println("Path found!");
                        } else {
                            System.out.println("Path not found!");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        private boolean Ai(int x, int y, int destX, int destY) throws Exception {
            if (x == destX && y == destY) {
                    for (int i = 0; i < map_size; i++) {
                        for (int j = 0; j < map_size; j++) {
                            if (visit[i][j]) {
                                try {
                                    cells[i][j].getPipe().setMatter(originalMatter[i][j]);
                                    cells[i][j].cell_shape();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    System.out.println("come back to ago");
                return true;
            }

            visit[x][y] = true;
            originalMatter[x][y] = cells[x][y].getPipe().getMatter();

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (isValid(newX, newY) && !visit[newX][newY]) {
                    if (cells[newX][newY].getPipe().Ability_to_turn()) {
                        int maxTurn = cells[newX][newY].getPipe().getPipeType() == 1 ? 2 : 4;
                        for (int j = 0; j < maxTurn; j++) {
                            change_matter(newX, newY);

//                            Thread.sleep(500);

                            if (check_connect(cells[x][y].vector, cells[newX][newY].vector)) {
                                if(!(cells[newX][newY].getPipe().getPipeType() == 3)) {
                                    if (Ai(newX, newY, destX, destY)) return true;
                                }
                                else {
                                    return checkDirectionForType3ForAi(cells[x][y] , cells[newX][newY]);
                                }
                            }

                        }
                    }else {
                        if (check_connect(cells[x][y].vector, cells[newX][newY].vector)) {
                            if(!(cells[newX][newY].getPipe().getPipeType() == 3)) {
                                if (Ai(newX, newY, destX, destY)) return true;
                            }
                            else {
                                return checkDirectionForType3ForAi(cells[x][y] , cells[newX][newY]);
                            }
                        }

                    }
                }
            }
            visit[x][y] = false;
            return false;
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
                        if(!(cells[newX][newY].getPipe().getPipeType() == 3)) {
                            if (dfs(newX, newY, destX, destY)) return true;
                        }
                        else {
                            return checkDirectionForType3(cells[x][y] , cells[newX][newY]);
                        }
                    }
                }
            }
            return false;
        }

        private boolean isValid(int x, int y) {
            System.out.println(""+x+" "+y);
            return x >= 0 && x < map_size && y >= 0 && y < map_size &&  cells[x][y] != null;
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

        private boolean checkDirectionForType3(Cell c , Cell cType3){
            int x = cType3.vector.row;  int y = cType3.vector.col;
            visited[x][y] = true;
            move m = what_direction(c , cType3);
            switch (m){
                case left: y--; break;
                case right: y++; break;
                case down: x++; break;
                case top: x--;
                default:break;
            }
            m = getOppositeDirection(m);
            if(!isValid(x,y)) return false;
            for(move n : cells[x][y].canConnect){
                if( n == m)  return dfs(x,y,map_size-1, map_size - 1);
            }
            return false;
        }

        private boolean checkDirectionForType3ForAi(Cell c , Cell cType3) throws Exception {
            int x = cType3.vector.row;  int y = cType3.vector.col;
            visit[x][y] = true;
            move m = what_direction(c , cType3);
            switch (m){
                case left: y--; break;
                case right: y++; break;
                case down: x++; break;
                case top: x--;
                default:break;
            }
            m = getOppositeDirection(m);
            if(!isValid(x,y)) return false;
            int maxTurn = cells[x][y].getPipe().getPipeType() == 1 ? 2 : 4;
            boolean isturn = cells[x][y].getPipe().Ability_to_turn() ? true : false;
            boolean maxOfMatterToConnect[] = new boolean[]{false,false};
            int k = 0;
            for (int i = 0; i < maxTurn && isturn; i++) {
                change_matter(x,y);
                for(move n : cells[x][y].canConnect){
                    if(n == m)  {
                        maxOfMatterToConnect[k] = Ai(x,y,map_size-1, map_size - 1);
                        if(maxOfMatterToConnect[k]) return true;
                        k++;
                    }
                }
            }

            return maxOfMatterToConnect[0] || maxOfMatterToConnect[1];
        }

        private move what_direction(Cell c1, Cell c2) {
            int x1 = c1.vector.row; int y1 = c1.vector.col; int x2 = c2.vector.row; int y2 = c2.vector.col;
            if (x1 == x2 && y1 == y2 + 1) {
                return move.left;
            } else if (x1 == x2 && y1 == y2 - 1) {
                return move.right;
            } else if (x1 == x2 + 1 && y1 == y2) {
                return move.top;
            } else if (x1 == x2 - 1 && y1 == y2) {
                return move.down;
            }
            return null;
        }

        private move what_direction(int x1 , int y1 , int x2 , int y2) {
            if (x1 == x2 && y1 == y2 + 1) {
                return move.left;
            } else if (x1 == x2 && y1 == y2 - 1) {
                return move.right;
            } else if (x1 == x2 + 1 && y1 == y2) {
                return move.top;
            } else if (x1 == x2 - 1 && y1 == y2) {
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

        private void change_matter(int row , int col) throws Exception {
            if (cells[row][col].getPipe().Ability_to_turn()) {
                int matter = cells[row][col].getPipe().getMatter();
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
//            // Update UI on JavaFX thread
//            Platform.runLater(() -> {
//                try {
//                    cells[row][col].cell_shape();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
        }
    }

    private boolean check_moves(Cell cell){
        if(this.levelGame == 2){
            if(availableMoves > 0){
                if(cell.getPipe().Ability_to_turn()) {
                    availableMoves--;
                    return true;
                }
                else return true;
            }
            else return false;
        }
        return true;
    }

    public class Undo {
        private Cell[] cell_save;
        private int[] prevMatters;
        private int count;

        public Undo() {
            cell_save = new Cell[2];
            prevMatters = new int[2];
            count = 0;
        }

        public void saveMove(Cell cell, int prevMatter) {
            if (count >= 2) {
                cell_save[0] = cell_save[1];
                prevMatters[0] = prevMatters[1];
                count = 1;
            }
            cell_save[count] = cell;
            prevMatters[count] = prevMatter;
            count++;
        }

        public void undoLastMove() throws Exception {
            if (is_can_undo()) {
                count--;
                cell_save[count].getPipe().setMatter(prevMatters[count]);
                cell_save[count].cell_shape();
                availableMoves++;
            }
            else cannot_undo();
        }

        public boolean is_can_undo() {
            return count > 0;
        }

        public void cannot_undo(){
            Text text = new Text("Can't undo");
            text.setFont(new Font("Arial" , 20));
            text.setFill(Color.RED);

            keys.getChildren().add(text);
            FadeTransition fade = new FadeTransition(Duration.seconds(2), text);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(event -> keys.getChildren().remove(text));
            fade.play();

        }
    }

    public class TimeLimit{
        private boolean lost = false;
        public Label showTime = new Label();
        public Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
            if(second == 0){
                if(minute == 0){
                    if(!lost) {
                        this.tl.stop();
                        lose_by_moves();
                    }
                    lost = true;
                }
                else{
                    minute --;
                    second = 59;
                }
            }
            else{
                second--;
            }
        }));
        private void timeTable(){
            String timeLabel = String.format("%02d:%02d",minute,second);
            this.showTime.setText(timeLabel);
            this.showTime.setFont(new Font("Arial" , 20));
            this.showTime.setTextFill(Color.RED);
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


