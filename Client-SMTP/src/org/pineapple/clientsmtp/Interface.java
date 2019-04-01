package org.pineapple.clientsmtp;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

public class Interface extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //Style
    private Color primary = Color.rgb(198, 182, 226);
    private Color secondary = Color.rgb(128, 102, 130);
    private LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, primary), new Stop(1, secondary));
    private Background background = new Background(new BackgroundFill(secondary, CornerRadii.EMPTY, Insets.EMPTY));

    private final Button button = new Button ("Send");
    private final Label notification = new Label ();
    private final TextField subject = new TextField("");
    private final TextArea text = new TextArea ("");

    private String address = " ";

    @Override public void start(Stage stage) {

        stage.setTitle("Nouveau message");
        Scene scene = new Scene(new Group(), 500, 340, gradient);

        final ComboBox emailComboBox = new ComboBox();
        emailComboBox.getItems().addAll(
                "valentin@pine.apple",
                "elie@pine.apple",
                "lea@pine.apple",
                "philippine@pine.apple"

        );
        emailComboBox.setPromptText("Email address");
        emailComboBox.setEditable(true);
        emailComboBox.valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> address = t1);


        button.setOnAction(e -> {
            if (emailComboBox.getValue() != null &&
                    !emailComboBox.getValue().toString().isEmpty()){
                notification.setText("Your message was successfully sent"
                        + " to " + address);
                emailComboBox.setValue(null);
                subject.clear();
                text.clear();
            }
            else {
                notification.setText("You have not selected a receiver!");
            }
        });

        GridPane grid = new GridPane();
        notification.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("To: "), 0, 0);
        grid.add(emailComboBox, 1, 0);
        grid.add(new Label("Subject: "), 0, 1);
        grid.add(subject, 1, 1, 3, 1);
        grid.add(text, 0, 2, 4, 1);
        grid.add(button, 0, 3);
        grid.add (notification, 1, 3, 3, 1);

        Group root = (Group)scene.getRoot();
        root.getChildren().add(grid);
        stage.setScene(scene);
        stage.show();

    }
}