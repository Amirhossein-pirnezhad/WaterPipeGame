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
    private ImageView pipeImage = new ImageView();

    public class PipeType {
        private int pipeType;
        /*
        Type 0 = empty cell , Type 1 = |  , Type 2 = |_ , Type 3 = start and finish
        */
        private int[] AllOfMatter = buildMatter();// the matter of pipe
        private int matter;

        private int[] buildMatter() {
            switch (pipeType) {
                case 0:
                    break;
                case 1:
                    AllOfMatter = new int[]{1, 2};
                    break;
                case 2:
                    AllOfMatter = new int[]{1, 2, 3, 4};
                    break;
                case 3:
                default:
                    break;
            }
            return AllOfMatter;
        }

        public boolean Ability_to_turn() {
            if (pipeType == 0 || pipeType == 3) return false;
            return true;
        }

        public void setPipeType(int pipeType) {
            this.pipeType = pipeType;
        }

        public int getPipeType() {
            return pipeType;
        }

        public void setMatter(int matter) throws Exception {
            this.matter = matter;
            cell_shape();

        }

        public int getMatter() {
            return matter;
        }
    }

    private PipeType pipeType = new PipeType();

    public void Cell(int row, int col, int type, int matter) throws Exception {
        this.row = row;
        this.col = col;
        this.pipeType.setPipeType(type);
        this.pipeType.setMatter(matter);
        border = new Rectangle(cell_size, cell_size);
        border.setFill(Color.LIGHTBLUE);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(1);
        cell_shape();
        this.getChildren().addAll(border, pipeImage);
    }

    public void cell_shape() throws Exception {
        if (pipeType.getPipeType() == 0) return;
        Image image = new Image(new FileInputStream("C:\\\\Users\\\\HZD\\\\Desktop\\\\Game\\\\src\\\\com\\\\company\\\\image\\\\Type" + this.pipeType.getPipeType() + "\\\\" + this.pipeType.getMatter() + ".png"));
        pipeImage.setImage(image);
        pipeImage.setFitHeight(cell_size);
        pipeImage.setFitWidth(cell_size);
        pipeImage.setPreserveRatio(true);
    }

    public PipeType getPipe(){
        return this.pipeType;
    }
}