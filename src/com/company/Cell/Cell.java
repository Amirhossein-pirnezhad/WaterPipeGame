package com.company.Cell;

import com.company.map.map.move;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.FileInputStream;
import static com.company.map.map.map_size;


public class Cell extends StackPane {
    public static class Vector{
        public int row, col;
    }
    public Vector vector = new Vector();
    public Rectangle border;
    private int cell_size = 800 / map_size;
    private ImageView pipeImage = new ImageView();
    private PipeType pipeType;
    public move[] canConnect;//build the  way of connect

    public Cell(int row, int col, int type, int matter) throws Exception {
        this.vector.row = row;
        this.vector.col = col;
        this.pipeType = new PipeType();
        this.pipeType.setPipeType(type);
        this.pipeType.setMatter(matter);
        this.canConnect = this.build_connection();
        border = new Rectangle(cell_size, cell_size);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.TRANSPARENT);
        border.setStrokeWidth(1);
        cell_shape();
        this.getChildren().addAll(border, pipeImage);
    }

    public void cell_shape() throws Exception { //for picture of cell
        if (pipeType.getPipeType() == 0) return;
        String basePath = System.getProperty("user.dir");
        String imagePath = basePath + "/src/com/company/image/Type" + this.pipeType.getPipeType() + "/" + this.pipeType.getMatter() + ".png";
        Image image = new Image(new FileInputStream(imagePath));
        pipeImage.setImage(image);
        pipeImage.setFitHeight(cell_size);
        pipeImage.setFitWidth(cell_size);
        pipeImage.setPreserveRatio(true);
    }

    public class PipeType { // ability's pipe
        private int pipeType;//Type 0 = empty cell , Type 1 = |  , Type 2 = |_ , Type 4 = start & finish , Type 3 = +
        private int[] AllOfMatter;// the matter of pipe
        private int matter;//the pipe matter at the moment

        public PipeType(){
            this.AllOfMatter = buildMatter();
        }
        private int[] buildMatter() {
            switch (pipeType) {
                case 0:
                    break;
                case 1:
                case 4:
                    AllOfMatter = new int[]{1, 2};
                    break;
                case 2:
                    AllOfMatter = new int[]{1, 2, 3, 4};
                    break;
                default:break;
            }
            return AllOfMatter;
        }

        public boolean Ability_to_turn() { // if pipe can turn or not
            if (pipeType == 0 || pipeType == 3 || pipeType == 4) return false;
            return true;
        }

        public boolean Ability_to_connect() { // if pipe can connect  with other's or not
            if (pipeType == 0) return false;
            return true;
        }

        public void setPipeType(int pipeType) {
            this.pipeType = pipeType;
        }

        public int getPipeType() {
            return pipeType;
        }

        public void setMatter(int matter) throws Exception {//for turn pipe
            this.matter = matter;
            Cell.this.canConnect = Cell.this.build_connection();
        }


        public int getMatter() {
            return matter;
        }
    }
    public move[] build_connection(){
        move CanConnect[] = new move[]{null};
        if(pipeType.Ability_to_connect()){
            switch (pipeType.pipeType){
                case 1:
                    switch (pipeType.matter){
                        case 1:
                            CanConnect = new move[]{move.top , move.down};
                            break;
                        case 2:
                            CanConnect = new move[]{move.left , move.right};
                        default:break;
                    }
                    break;
                case 2:
                    switch (pipeType.matter){
                        case 1:
                            CanConnect = new move[]{move.top , move.right};
                            break;
                        case 2:
                            CanConnect = new move[]{move.right , move.down};
                            break;
                        case 3:
                            CanConnect = new move[]{move.left , move.down};
                            break;
                        case 4:
                            CanConnect = new move[]{move.top , move.left};
                            break;
                    }
                    break;
                case 3:
                    CanConnect = new move[]{move.top , move.down , move.right , move.left};
                    break;
                case 4:
                    switch (pipeType.matter){
                        case 1:
                            CanConnect = new move[]{move.down};
                            break;
                        case 2:
                            CanConnect = new move[]{move.left};
                        default:break;
                    }
                    break;

                default:break;
            }
        }
        return CanConnect;
    }
    public PipeType getPipe(){
        return this.pipeType;
    }
}
