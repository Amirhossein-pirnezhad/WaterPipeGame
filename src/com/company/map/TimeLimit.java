package com.company.map;

import com.company.Cell.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import static com.company.map.map.availableMoves;
import static com.company.map.map.levelGame;


public class TimeLimit{
    private int second = 5 + (levelGame-3) * 20;

    private boolean lost = false;

    public boolean isLost() {
        return lost;
    }

    public Label showTime = new Label();

    public Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
        if(second <= 20){
            this.showTime.setTextFill(Color.RED);
        }
        else this.showTime.setTextFill(Color.GREEN);

        if(second <= 0){
            lost = true;
            this.tl.stop();
        }
        else{
            second--;
            timeTable();
        }
    }));


    public void timeTable(){
        tl.play();
        String timeLabel = String.format("%02d : %02d",second/60 , second % 60);
        this.showTime.setText(timeLabel);
        this.showTime.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
    }

    public boolean check_if_player_moved(Cell cell){
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
