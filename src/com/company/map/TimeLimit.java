package com.company.map;

import com.company.Cell.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import static com.company.map.map.availableMoves;
import static com.company.map.map.levelGame;


public class TimeLimit{
    private int second = 90;

    public void setSecond(int second) {
        this.second = second;
    }

    private boolean lost = false;
    public Label showTime = new Label();
    public Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
        if(second == 0){
            lost = true;
        }
        else{
            second--;
        }
    }));
    public void timeTable(){
        String timeLabel = String.format("%02d:%02d",second/60,second -(second/60)*60);
        this.showTime.setText(timeLabel);
        this.showTime.setFont(new Font("Arial" , 20));
        this.showTime.setTextFill(Color.RED);
    }

    public boolean check_moves(Cell cell){
        if(levelGame == 2){
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


}
