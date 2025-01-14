package io.github.pourianof.gaussjordan;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;


class DescriptionsPane extends BorderPane  {
    static final String[] leftData = {
            "Programmer", "Pooriya Amini", "Professor", "Dr. Hosseinzadeh Mohammadali", "UNI", "AUSMT", "Lesson", "Linear Algebra",};

    DescriptionsPane() {
        super();
        this.setLeftDesc();
        this.setCenterDesc();

    }

    final void setLeftDesc() {
        GridPane leftGrid = new GridPane();
        leftGrid.setVgap(10);
        leftGrid.setHgap(5);
        for (int i = 0; i < DescriptionsPane.leftData.length; i++) {
            int col = i % 2;
            int row = col == 0 ? i : i - 1;

            leftGrid.add(new Label(DescriptionsPane.leftData[i] + (col == 0 ? " : " : "")), col, row);

        }
        leftGrid.setPadding(new Insets(10));
        setLeft(leftGrid);
    }

    final void setCenterDesc() {
        StackPane stack = new StackPane(new Label("Gauss Jordan Solver Program"));
        stack.setPadding(new Insets(10));
        this.setCenter(stack);
    }

    private HBox provideLanguageBox(String lan){
        var img = new ImageView(new Image("images/"+ lan + "_flag.png"));
        img.setFitWidth(25);
        img.setPreserveRatio(true);
        HBox box = new HBox(new Label(lan.toUpperCase()), img);
        box.setOnMouseClicked(
                (e)->{
                    LocaleManager.getManager().changeLanguage(new Locale(lan));
                }
        );

        box.setStyle("-fx-background-color: lightgray; -fx-padding: 5; -fx-margin: 5;");
        box.setOnMouseEntered(event -> box.setCursor(javafx.scene.Cursor.HAND));
        box.setOnMouseExited(event -> box.setCursor(Cursor.DEFAULT));

        return box;
    }

    final void setRightDesc(Stage owner) {
        final Button guideBtn = new Button(LocaleManager.getManager().get("guide"));

        HBox languages = new HBox(provideLanguageBox("en"), provideLanguageBox("fa"));

        StackPane vb = new StackPane();
        VBox wrapper = new VBox(languages, guideBtn);
        vb.getChildren().add(wrapper);
        vb.setMinWidth(120);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10));

        final GuideStage gs = new GuideStage();

        this.setRight(vb);
        guideBtn.setOnAction(e -> {
            gs.show();
        });
    }

}
