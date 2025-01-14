package io.github.pourianof.gaussjordan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class GaussJordan extends Application implements Observer {

    Stage stage;
    @Override
    public void update(Observable o, Object arg) {
        stage.setScene(provideScene());
    }

    private Scene provideScene(){
        BorderPane template = new BorderPane();
        DescriptionsPane dp = new DescriptionsPane();
        dp.setRightDesc(stage);
        template.setTop(dp);

        template.setCenter(new EquationPaneWrapper());
        return new Scene(template, 800, 500);
    }

    public void start(Stage stage) {
        StageProvider.setRootStage(stage);
        StageProvider.setSecondaryStage();

        Image icon = new Image("images/gauss.png");

        stage.getIcons().add(icon);
        stage.setScene(provideScene());
        stage.setTitle("Gauss Jordan Solver");
        stage.show();
        this.stage = stage;
        LocaleManager.getManager().addObserver(this);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
