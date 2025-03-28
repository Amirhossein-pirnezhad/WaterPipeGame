package com.company.Cell;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.io.FileInputStream;


public class Cell  extends StackPane {
    private int row, col;
    private Rectangle border;
    private final int cell_size = 100;
    private ImageView pipeImage = new ImageView();
    private PipeType pipeType = new PipeType();
    public Pair<Integer, Integer>[] mustConnect;

    public void Cell(int row, int col, int type, int matter) throws Exception {
        this.row = row;
        this.col = col;
        this.pipeType.setPipeType(type);
        this.pipeType.setMatter(matter);
        this.mustConnect = next_pipe_toConnect();
        border = new Rectangle(cell_size, cell_size);
        border.setFill(Color.LIGHTBLUE);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(1);
        cell_shape();
        this.getChildren().addAll(border, pipeImage);
    }

    public void cell_shape() throws Exception { //for picture of cell
        if (pipeType.getPipeType() == 0) return;
        Image image = new Image(new FileInputStream(
                "C:\\\\Users\\\\HZD\\\\Desktop\\\\Game\\\\src\\\\com\\\\company\\\\image\\\\Type" + this.pipeType.getPipeType() + "\\\\" + this.pipeType.getMatter() + ".png"
        ));
        pipeImage.setImage(image);
        pipeImage.setFitHeight(cell_size);
        pipeImage.setFitWidth(cell_size);
        pipeImage.setPreserveRatio(true);
    }

    private Pair<Integer , Integer>[] next_pipe_toConnect(){
        int[] canConnect = this.pipeType.canConnect;
        if (!pipeType.Ability_to_connect()) return null;
        Pair<Integer, Integer>[] connections = new Pair[2];
        for(int i = 0 ; i<2 ; i++){
            switch (canConnect[i]){
                case 0:
                    connections[i] = new Pair<>(this.row - 1, this.col);
                    break;
                case 1:
                    connections[i] = new Pair<>(this.row, this.col + 1);
                    break;
                case 2:
                    connections[i] = new Pair<>(this.row + 1, this.col);
                    break;
                case 3:
                    connections[i] = new Pair<>(this.row, this.col - 1);
                    break;
            }
        }
        return connections;
    }

    public class PipeType { // ability's pipe
        private int pipeType;//Type 0 = empty cell , Type 1 = |  , Type 2 = |_ , Type 3 = start , Type 4 = finish
        private int[] AllOfMatter = buildMatter();// the matter of pipe
        private int matter;//the pipe matter at the moment
        public int[] canConnect = build_connection();//build the  way of connect

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

        public boolean Ability_to_turn() { // if pipe can turn or not
            if (pipeType == 0 || pipeType == 3) return false;
            return true;
        }

        public int[] build_connection(){
            if(Ability_to_connect()){
                switch (this.pipeType){
                    case 1:
                        switch (this.matter){
                            case 1:
                                canConnect = new int[] {0 , 2};
                                break;
                            case 2:
                                canConnect = new int[]{1 , 3};
                            default:break;
                        }
                        break;
                    case 2:
                        switch (this.matter){
                            case 1:
                                canConnect = new int[] {0 , 1};
                                break;
                            case 2:
                                canConnect = new int[] {1 , 2};
                                break;
                            case 3:
                                canConnect = new int[] {2 , 3};
                                break;
                            case 4:
                                canConnect = new int[] {3 , 0};
                                break;
                            default:break;
                        }
                    default:break;
                }
            }
            return canConnect;
        }

        public boolean Ability_to_connect() { // if pipe can connect  with other's or not
            if (pipeType == 0 || pipeType == 3) return false;
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
            build_connection();
            cell_shape();
        }
        public int getMatter() {
            return matter;
        }
    }

    public PipeType getPipe(){
        return this.pipeType;
    }
}