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
import javafx.animation.FadeTransition;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

import static com.company.map.map.availableMoves;
import static com.company.map.map.levelGame;


public class TimeLimit{
    private int second = 5 + (levelGame-3) * 20;

    private boolean lost = false;

    public boolean isLost() {
        return lost;
    }

    public Label showTime = new Label("00 : 00");

    public TimeLimit(){

        showTime.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        showTime.setTextFill(Color.LIMEGREEN);

        showTime.setStyle(
                "-fx-background-color: rgba(0,0,0,0.65);" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-border-color: #4CAF50;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-width: 2;"
        );
    }

    public Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1) , event -> {
        if(second <= 10){

            showTime.setTextFill(Color.RED);

            showTime.setStyle(
                    "-fx-background-color: rgba(255,0,0,0.25);" +
                            "-fx-background-radius:15;" +
                            "-fx-padding:10 20;" +
                            "-fx-border-color:red;" +
                            "-fx-border-radius:15;"
            );

        }
        else if(second <= 30){

            showTime.setTextFill(Color.ORANGE);

        }
        else{

            showTime.setTextFill(Color.LIMEGREEN);

        }

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
        String timeLabel =
                String.format("⏱ %02d : %02d",
                        second/60,
                        second%60);

        showTime.setText(timeLabel);
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
