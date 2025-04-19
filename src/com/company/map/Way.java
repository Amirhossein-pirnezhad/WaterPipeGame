package com.company.map;

import com.company.Cell.Cell;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


import static com.company.map.map.cells;
import static com.company.map.map.map_size;

public class Way {
    public List<Step> correctPath;
    private final int[] dx = {0, 0, 1, -1};
    private final int[] dy = {1, -1, 0, 0};
    private int[][] visited;
    private int[][] visit;
    private int[][] originalMatter;

    public class Step{
        int row;
        int col;
        int correctMatter;
        public Step(int row , int col , int targetMater){
            this.row =row;
            this.col = col;
            this.correctMatter = targetMater;
        }
    }

    public void show_correct_way(List<Step> correctPath) {
        boolean sw = true;
        for (Step m : correctPath) {
            if (cells[m.row][m.col].getPipe().getMatter() != m.correctMatter) {
                sw = false;
                Cell currentCell = cells[m.row][m.col];
                int countOfRotation = m.correctMatter - currentCell.getPipe().getMatter();
                Color originalColor = (Color) currentCell.border.getFill();
                currentCell.border.setStroke(Color.RED);

                FadeTransition fade = new FadeTransition(Duration.seconds(0.5), currentCell);
                fade.setFromValue(1.0);
                fade.setToValue(0.7);
                fade.setCycleCount(4);
                fade.setAutoReverse(true);
                fade.play();


                RotateTransition rotate = new RotateTransition(Duration.seconds(1), currentCell.lookup(".image-view"));
                rotate.setByAngle(90 * countOfRotation);
                rotate.setCycleCount(2);
                rotate.setAutoReverse(true);
                rotate.play();

                new Timeline(new KeyFrame(
                        Duration.seconds(3),
                        e -> currentCell.border.setStroke(originalColor)
                )).play();
                break;
            }
        }
        if(sw) System.out.println("Correct way");
    }

    public boolean find_way(Cell start, Cell end) {
        visited = new int[map_size][map_size];
        for (int i = 0; i < map_size; i++) {
            for (int j = 0; j < map_size; j++) {
                visited[i][j] = 0;
            }
        }
        return dfs(start.vector.row, start.vector.col, end.vector.row, end.vector.col);
    }

    public boolean find_way_Ai(Cell start, Cell finish) throws Exception {
        correctPath = new ArrayList<>();
        visit = new int[map_size][map_size];
        originalMatter = new int[map_size][map_size];
        for (int i = 0; i < map_size; i++) {
            for (int j = 0; j < map_size; j++) {
                visit[i][j] = 0;
                originalMatter[i][j] = cells[i][j].getPipe().getMatter();
            }
        }
        boolean result =  Ai(start.vector.row, start.vector.col, finish.vector.row, finish.vector.col , false);
        for (int i = 0; i < map_size; i++) {
            for (int j = 0; j < map_size; j++) {
                cells[i][j].getPipe().setMatter(originalMatter[i][j]);
            }
        }
        return result;
    }

    private boolean Ai(int x, int y, int destX, int destY , boolean show) throws Exception {
        visit[x][y]++;

        if (x == destX && y == destY) {
            System.out.println("come back to ago");
            return true;
        }

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            if (isValid(newX, newY)) {
                if (cells[newX][newY].getPipe().Ability_to_turn()) {
                    int maxTurn = cells[newX][newY].getPipe().getPipeType() == 1 ? 2 : 4;
                    for (int j = 0; j < maxTurn; j++) {
                        change_matter(newX, newY);

                        if(!(cells[newX][newY].getPipe().getPipeType() == 3)) { // if not + , visit must lower than 1
                            if(visit[newX][newY] < 1)
                                if(check_connect(cells[x][y].vector, cells[newX][newY].vector))
                                    if (Ai(newX, newY, destX, destY , show)) {
                                        correctPath.add(0 ,new Step(newX,newY,cells[newX][newY].getPipe().getMatter()));
                                        return true;
                                    }
                        }
                        else {
                            if(visit[newX][newY] < 2 && visit[x][y]<=1) // for can go on + more than one
                                if(check_connect(cells[x][y].vector, cells[newX][newY].vector)) {
                                    boolean result = checkDirectionForType3ForAi(cells[x][y], cells[newX][newY]);
                                    if(result) correctPath.add(0 , new Step(newX,newY,cells[newX][newY].getPipe().getMatter()));
                                    return result;
                                }
                        }
                    }
                }else {
                    if(!(cells[newX][newY].getPipe().getPipeType() == 3)) { // if not + , visit must lower than 1
                        if(visit[newX][newY] < 1)
                            if(check_connect(cells[x][y].vector, cells[newX][newY].vector))
                                if (Ai(newX, newY, destX, destY , show)) {
                                    correctPath.add(0 , new Step(newX,newY,cells[newX][newY].getPipe().getMatter()));
                                    return true;
                                }
                    }
                    else {
                        if(visit[newX][newY] < 2 && visit[x][y]<=1)
                            if(check_connect(cells[x][y].vector, cells[newX][newY].vector)) {
                                correctPath.add(0 , new Step(newX,newY,cells[newX][newY].getPipe().getMatter()));
                                return checkDirectionForType3ForAi(cells[x][y], cells[newX][newY]);
                            }
                    }
                }
            }
        }
        visit[x][y]--;
        return false;
    }

    private boolean dfs(int x, int y, int destX, int destY) {
        if (x == destX && y == destY) return true;

        visited[x][y]++;

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            if(isValid(newX, newY)) {
                System.out.println("is valid");
                if (!(cells[newX][newY].getPipe().getPipeType() == 3)) {
                    if (visited[newX][newY] < 1) {
                        if (check_connect(cells[x][y].vector, cells[newX][newY].vector)) {
                            if (dfs(newX, newY, destX, destY)) return true;
                        }
                    }
                } else {
                    if(visited[newX][newY] < 2 && visited[x][y] <= 1) {
                        if (check_connect(cells[x][y].vector, cells[newX][newY].vector)) {
                            return checkDirectionForType3(cells[x][y], cells[newX][newY]);
                        }
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

        map.move direction = what_direction(c1, c2);
        if (direction == null) {
            System.out.println("direction null");
            return false;
        }
        boolean c1HasDirection = false;
        for (map.move m : c1.canConnect) {
            if (m == direction) {
                System.out.println("c1 connected");
                c1HasDirection = true;
                break;
            }
        }

        map.move oppositeDirection = getOppositeDirection(direction);
        boolean c2HasOpposite = false;
        for (map.move m : c2.canConnect) {
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
        visited[x][y]++;
        visited[c.vector.row][c.vector.col]++;
        map.move m = what_direction(c , cType3);
        switch (m){
            case left: y--; break;
            case right: y++; break;
            case down: x++; break;
            case top: x--;
            default:break;
        }
        m = getOppositeDirection(m);
        if(!isValid(x,y)) return false;
        for(map.move n : cells[x][y].canConnect){
            if(n == m)  {
                visited[x][y]++;
                return dfs(x,y,map_size-1, map_size - 1);
            }
        }
        return false;
    }

    private boolean checkDirectionForType3ForAi(Cell c , Cell cType3) throws Exception {
        int x = cType3.vector.row;  int y = cType3.vector.col;
        visit[x][y]++;
        visit[c.vector.row][c.vector.col]++;
        map.move m = what_direction(c , cType3);
        switch (m){
            case left: y--; break;
            case right: y++; break;
            case down: x++; break;
            case top: x--;
            default:break;
        }
        if(!isValid(x,y)) return false;
        m = getOppositeDirection(m);
        int maxTurn = cells[x][y].getPipe().getPipeType() == 1 ? 2 : 4;
        boolean isturn = cells[x][y].getPipe().Ability_to_turn() ? true : false;
        boolean maxOfMatterToConnect[] = new boolean[]{false,false};
        int k = 0;
        int turn = correctPath.size() - 1;
        if(isturn) {
            for (int i = 0; i < maxTurn; i++) {
                change_matter(x, y);
                for (map.move n : cells[x][y].canConnect) {
                    if (n == m) {
                        visit[x][y]++;
                        maxOfMatterToConnect[k] = Ai(x, y, map_size - 1, map_size - 1 , true);
                        if (maxOfMatterToConnect[k]) {
                            correctPath.add(turn , new Step(cells[x][y].vector.row, cells[x][y].vector.col , cells[x][y].getPipe().getMatter()));
                            return true;
                        }
                        else {
                            visit[x][y]--;
                        }
                        k++;
                    }
                }
            }
        }
        else{
            for (map.move n : cells[x][y].canConnect) {
                if (n == m) {
                    visit[x][y]++;
                    maxOfMatterToConnect[k] = Ai(x, y, map_size - 1, map_size - 1 ,true);
                    if (maxOfMatterToConnect[k]) {
                        correctPath.add(turn , new Step(cells[x][y].vector.row, cells[x][y].vector.col , cells[x][y].getPipe().getMatter()));
                        return true;
                    }else visit[x][y]--;
                    k++;
                }
            }
        }

        return maxOfMatterToConnect[0] || maxOfMatterToConnect[1];
    }

    private map.move what_direction(Cell c1, Cell c2) {
        int x1 = c1.vector.row; int y1 = c1.vector.col; int x2 = c2.vector.row; int y2 = c2.vector.col;
        if (x1 == x2 && y1 == y2 + 1) {
            return map.move.left;
        } else if (x1 == x2 && y1 == y2 - 1) {
            return map.move.right;
        } else if (x1 == x2 + 1 && y1 == y2) {
            return map.move.top;
        } else if (x1 == x2 - 1 && y1 == y2) {
            return map.move.down;
        }
        return null;
    }


    private map.move getOppositeDirection(map.move direction) {
        switch (direction) {
            case top: return map.move.down;
            case down: return map.move.top;
            case left: return map.move.right;
            case right: return map.move.left;
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
    }
}