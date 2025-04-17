package com.company.map;

import com.company.Cell.Cell;
import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.company.map.map.*;

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
            int nowMatter = cell_save[count].getPipe().getMatter();
            if(nowMatter != 1) {
                if (nowMatter > prevMatters[count]) {
                    turn_SECONDARY(cell_save[count].vector.row, cell_save[count].vector.col);
                } else {
                    turn_PRIMSRY(cell_save[count].vector.row, cell_save[count].vector.col);
                }
            }
            else turn_SECONDARY(cell_save[count].vector.row, cell_save[count].vector.col);

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