package com.company.Cell;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.InputStream;

public class Cell  extends StackPane {
    private ImageView pipeImage;
    private int row, col;
    private Rectangle border;
    private final int cell_size = 100;

    public void Cell(int row, int col, int type) {
        this.row = row;
        this.col = col;
        border = new Rectangle(cell_size, cell_size);
        border.setFill(Color.LIGHTBLUE);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(1);
        this.getChildren().addAll(border);
        InputStream input = getClass().getResourceAsStream("/com/company/image/7.png");
        Image image = new Image(input);
        pipeImage.setImage(image);

    }
}
  /*  public void cell_shape(int type){
        FileInputStream input = new FileInputStream("C:\\Users\\HZD\\Desktop\\Game\\src\\com\\company\\image\\7.png");
        Image image = new Image(input);
        pipeImage(image);
      /*  switch (type){
            case 0 :
        }

        pipeImage.setImage();
    }
}
*/