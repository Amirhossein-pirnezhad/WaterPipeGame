package com.company;

import com.company.map.map;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class Main extends Application {
    private int HEIGHT;
    private int WIDTH;
    private map Map;
    private Stage primaryStage;
    private AnimationTimer gameUpdate;

    @Override
    public void start(Stage stage) {
        try {

            primaryStage = stage;

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            WIDTH = (int) screenBounds.getWidth();
            HEIGHT = (int) screenBounds.getHeight();

            showLevelChoice();

        } catch (Exception e) {
            e.printStackTrace();

            try {
                java.nio.file.Files.writeString(
                        java.nio.file.Paths.get("error.txt"),
                        e.toString()
                );
            } catch (Exception ignored) {}
        }
    }

    private void startGame() throws Exception {
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);
        primaryStage.setTitle("Water Pipe Game");

        this.Map = setGameStructures();

        gameUpdate = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Map.updateGame();
                primaryStage.setScene(Map.scene);
                Map.exitButton.setOnAction(e -> primaryStage.close());
                Map.restart.setOnAction(e -> {
                    primaryStage.close();
                    Map.setAvailableMoves(30);
                    try {
                        Map.restartLevel();
                        Map.updateGame();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    primaryStage.show();
                });
                Map.undo.setOnAction(e ->{
                    try {
                        Map.getUNDO().undoLastMove();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        };
        gameUpdate.start();
        primaryStage.show();
    }

    private map setGameStructures() throws Exception {
        map m = new map();
        m.Build_map(HEIGHT,WIDTH);
        return m;
    }

    private void showLevelChoice() {
        Stage choice = new Stage();
        Map = new map();

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom,#0f2027,#203a43,#2c5364);"
        );

        Text title = new Text("WATER PIPE");
        title.setFont(Font.font("Arial", 42));
        title.setStyle("-fx-fill:white;-fx-font-weight:bold;");

        Text subtitle = new Text("Choose a Level");
        subtitle.setFont(Font.font("Arial", 20));
        subtitle.setStyle("-fx-fill:#dddddd;");

        VBox header = new VBox(10, title, subtitle);
        header.setAlignment(Pos.CENTER);

        Button l1 = createMenuButton("Level 1");
        Button l2 = createMenuButton("Level 2");
        Button l3 = createMenuButton("Level 3");

        TextField levelField = new TextField();
        levelField.setPromptText("Custom Level (1-100)");
        levelField.setMaxWidth(250);
        levelField.setStyle(
                "-fx-font-size:16;" +
                        "-fx-background-radius:10;" +
                        "-fx-alignment:center;"
        );

        // فقط اجازه ورود عدد
        levelField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                levelField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        Button customLevel = createMenuButton("Play Selected Level");

        VBox levels = new VBox(
                18,
                l1,
                l2,
                l3,
                levelField,
                customLevel
        );
        levels.setAlignment(Pos.CENTER);

        Text footer = new Text("© Water Pipe Game");
        footer.setStyle("-fx-fill:lightgray;");

        VBox bottom = new VBox(footer);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPrefHeight(50);

        root.setTop(header);
        root.setCenter(levels);
        root.setBottom(bottom);

        BorderPane.setAlignment(header, Pos.CENTER);
        BorderPane.setAlignment(bottom, Pos.CENTER);

        Scene scene = new Scene(root, 600, 700);

        l1.setOnAction(event -> {
            Map.setLevelGame(1);
            choice.close();
            try {
                startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        l2.setOnAction(event -> {
            Map.setLevelGame(2);
            choice.close();
            try {
                startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        l3.setOnAction(event -> {
            Map.setLevelGame(3);
            choice.close();
            try {
                startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        customLevel.setOnAction(event -> {

            try {
                int level = Integer.parseInt(levelField.getText());

                if (level < 1 || level > 1000) {
                    throw new NumberFormatException();
                }

                Map.setLevelGame(level);
                choice.close();
                startGame();

            } catch (NumberFormatException ex) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Level");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a level between 1 and 1000.");
                alert.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        choice.setTitle("Water Pipe");
        choice.setScene(scene);

        choice.setOnCloseRequest(event -> {
            if (gameUpdate != null) {
                gameUpdate.stop();
            }
            primaryStage.close();
        });

        choice.showAndWait();
    }

    private Button createMenuButton(String text){

        Button btn = new Button(text);

        btn.setPrefWidth(250);
        btn.setPrefHeight(55);

        btn.setStyle(
                "-fx-background-color:#2196F3;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:18;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:12;" +
                        "-fx-cursor:hand;"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-color:#42A5F5;" +
                                "-fx-text-fill:white;" +
                                "-fx-font-size:18;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:12;" +
                                "-fx-cursor:hand;" +
                                "-fx-scale-x:1.05;" +
                                "-fx-scale-y:1.05;"
                )
        );

        btn.setOnMouseExited(e ->
                btn.setStyle(
                        "-fx-background-color:#2196F3;" +
                                "-fx-text-fill:white;" +
                                "-fx-font-size:18;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:12;" +
                                "-fx-cursor:hand;"
                )
        );

        return btn;
    }
}