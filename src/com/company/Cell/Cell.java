package com.company.Cell;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;


public class Cell  extends StackPane {
    private int row, col;
    private Rectangle border;
    private final int cell_size = 100;
    private int pipeType;
    private ImageView pipeImage = new ImageView();



    public void Cell(int  row , int  col , int type) throws Exception {
        this.row = row;
        this.col = col;
        border = new Rectangle(cell_size , cell_size);
        border.setFill(Color.LIGHTBLUE);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(1);
        cell_shape(type);
        this.getChildren().addAll(border , pipeImage);
    }

    public void cell_shape(int type) throws Exception {
        int i = 0;
        switch (type) {
            case 0:        break;
            case 1: i = 1; break;
            case 2: i = 2; break;
            case 3: i = 3; break;
            case 4: i = 4; break;
            case 5: i = 5; break;
            case 6: i = 6; break;
            case 7: i = 7; break;
            case 8: i = 8; break;
            default: i = 0;
        }
        if(i==0) return;
        Image image = new Image(new FileInputStream("C:\\\\Users\\\\HZD\\\\Desktop\\\\Game\\\\src\\\\com\\\\company\\\\image\\\\"+i+".png"));
        pipeImage.setImage(image);
        pipeImage.setFitHeight(cell_size);
        pipeImage.setFitWidth(cell_size);
        pipeImage.setPreserveRatio(true);
    }
    public void setPipeType(int pipeType) throws Exception {
        this.pipeType = pipeType;
        cell_shape(pipeType);
    }
    public int getPipeType() {
        return pipeType;
    }
}