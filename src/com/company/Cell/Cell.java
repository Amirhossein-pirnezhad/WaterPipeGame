package com.company.Cell;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell  extends Pane {
    private int row, col;
    private Rectangle border;
    private final int cell_size = 100;

    public void Cell(int  row , int  col , int type){
        this.row = row;
        this.col = col;
        border = new Rectangle(cell_size , cell_size);
        border.setFill(Color.LIGHTBLUE);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(1);
        this.getChildren().addAll(border);
        cell_shape(type);

    }
    public void cell_shape(int type){
        switch (type){
            case 0 :
                break;
            case 1 ://ofoghi
                Rectangle r = new Rectangle(cell_size-((cell_size/100)*20) , cell_size-((cell_size/100)*70) , Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setX(cell_size/10);  r.setY((cell_size - r.getHeight())/2);
                Rectangle r1 = new Rectangle(
                        10,          // عرض: 10 پیکسل
                        cell_size/2, // ارتفاع: 50% سلول
                        Color.DARKGRAY
                );
                r1.setLayoutX(0); // چسبیده به لبه چپ
                r1.setLayoutY((cell_size - r1.getHeight()) / 2); // وسط عمودی

                // مستطیل راست (خروجی لوله)
                Rectangle r2 = new Rectangle(
                        10,          // عرض: 10 پیکسل
                        cell_size/2, // ارتفاع: 50% سلول
                        Color.DARKGRAY
                );
                r2.setLayoutX(cell_size - r2.getWidth()); // چسبیده به لبه راست
                r2.setLayoutY((cell_size - r2.getHeight()) / 2);
                this.getChildren().addAll(r, r1 , r2);
                break;
        }
    }
}
