package io.github.pourianof.gaussjordan;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StageProvider {

    private static Stage primaryStage = null;
    private static Stage secondaryStage = null;
    private static final Pane secondaryStagePane = new Pane();
    private static final Scene secondaryStageScene = new Scene(secondaryStagePane);

    private StageProvider() {
    }

    public static void setRootStage(Stage stage) {
        if (primaryStage == null) {
            StageProvider.primaryStage = stage;
        }
    }

    static public Stage getRootStage() {
        return primaryStage;
    }

    public static void setSecondaryStage() {
        if (secondaryStage == null) {
            secondaryStage = new Stage();
            secondaryStage.setAlwaysOnTop(true);
            secondaryStage.setScene(secondaryStageScene);
        }

    }

    public static Pane getEmptyPane() {
        secondaryStagePane.getChildren().clear();
        return secondaryStagePane;
    }

    public static void displayStage() {
        if (!secondaryStage.isShowing()) {
            secondaryStage.show();
        }
    }

}
