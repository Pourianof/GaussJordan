package io.github.pourianof.gaussjordan;

import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GuideStage extends Stage {

    static final double STAGE_WIDTH = 380;
    VBox vb = new VBox();

    private void setImage(String imageName) {
        Image image = new Image("images/" + imageName + ".png");
        ImageView iv = new ImageView(image);

        double ratio = image.getHeight() / image.getWidth();
        iv.setFitWidth(STAGE_WIDTH);
        iv.setFitHeight(ratio * STAGE_WIDTH);
        vb.getChildren().add(iv);
    }

    private void setSomeText(String... texts) {
        for (String txt : texts) {
            Text t = new Text(txt);
            t.setWrappingWidth(STAGE_WIDTH);
            vb.getChildren().add(t);
        }
    }

    private void eqGuide() {
        Label lbl = new Label("1-" + LocaleManager.getManager().get("guideManTitle"));
        Text txt = new Text(LocaleManager.getManager().get("guideManDesc"));
        txt.setWrappingWidth(STAGE_WIDTH);

        vb.getChildren().addAll(lbl, txt);

        this.setImage("image1");
        StringBuilder sb = new StringBuilder();
        setSomeText(
                "1.1- " + LocaleManager.getManager().get("guideMan1"),
                "1.2- " + LocaleManager.getManager().get("guideMan2"),
                "1.3- " + LocaleManager.getManager().get("guideMan3"),
                "1.4- " + LocaleManager.getManager().get("guideMan4"),
                "** " + LocaleManager.getManager().get("guideManNote1"),
                "** " + LocaleManager.getManager().get("guideManNote2"),
                "** " + LocaleManager.getManager().get("guideManNote3")
                );

    }

    private void importGuide() {
        Label lbl = new Label("2- " + LocaleManager.getManager().get("guideFileTitle"));

        vb.getChildren().addAll(lbl);
        setSomeText(
                "2.1- " + LocaleManager.getManager().get("guideFile1"),
                "2.2- " + LocaleManager.getManager().get("guideFile2"),
                "2.3- " + LocaleManager.getManager().get("guideFile3")
        );

        this.setImage("image2");
        this.setSomeText(
                "1- " + LocaleManager.getManager().get("guideFileNote1"),
                "2- " + LocaleManager.getManager().get("guideFileNote2"),
                "** " + LocaleManager.getManager().get("guideFileSampleValidMatrix") + ":"
        );
        this.setImage("image3");

    }

    private void finalPart() {
        setSomeText(LocaleManager.getManager().get("guideCalculate"));
        this.setImage("image4");
        setSomeText(LocaleManager.getManager().get("guideCalculateExample"));
    }

    private void overview() {
        Label programLbl = new Label(LocaleManager.getManager().get("appGuide"));
        Text overview = new Text(LocaleManager.getManager().get("waysToInsertMatrix") + ":");
        overview.setWrappingWidth(STAGE_WIDTH);
        vb.getChildren().addAll(programLbl, overview);
    }

    public GuideStage() {
        this.overview();
        this.eqGuide();
        this.importGuide();
        this.finalPart();
        this.vb.setSpacing(10);
        ScrollPane sp = new ScrollPane(vb);
        Scene scene = new Scene(sp, 400, 400);
        sp.setPannable(true);
        scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        this.setTitle(LocaleManager.getManager().get("appGuide"));
        this.setScene(scene);
        this.setResizable(false);
        this.setAlwaysOnTop(true);

    }

}
