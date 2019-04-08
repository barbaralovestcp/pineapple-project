package org.pineapple.clientsmtp;

import javafx.application.Application;
import javafx.application.Platform;
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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Interface extends Application implements Observer {
    public static void main(String[] args) {
        launch(args);
    }

    //Style
    private Color primary = Color.rgb(198, 182, 226);
    private Color secondary = Color.rgb(128, 102, 130);
    private LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, primary), new Stop(1, secondary));
    private Background background = new Background(new BackgroundFill(secondary, CornerRadii.EMPTY, Insets.EMPTY));

    private final Button button = new Button("Send");
    private final Label notification = new Label();
    private final TextField subject = new TextField("");
    private final Label receiver = new Label();
    private final TextArea text = new TextArea("");

    private ArrayList<String> address = new ArrayList<>();
    private String name = "pdg";
    private String domain = "apple";
    private ClientSMTP client;

    private String receiversToString(ArrayList<String> address) {
        StringBuilder receivers = new StringBuilder();
        for (String a : address) {
            receivers.append(a);
            receivers.append("; ");
        }
        return receivers.toString();
    }

    @Override
    public void start(Stage stage) {

        client = new ClientSMTP(name, domain);
        stage.setTitle("Nouveau message");
        Scene scene = new Scene(new Group(), 500, 400, gradient);

        final ComboBox emailComboBox = new ComboBox();
        emailComboBox.getItems().addAll(
                "valentin@pine.apple",
                "elie@pine.apple",
                "lea@pine.apple",
                "philippine@pine.apple"

        );
        emailComboBox.setPromptText("Email address");
        emailComboBox.setEditable(true);
        emailComboBox.valueProperty().addListener((ChangeListener<String>) (ov, t, t1) ->
        {this.address.add(t1);
         receiver.setText(receiversToString(this.address));});

        button.setOnAction(e -> {
            if(this.address.size() > 0) {
                notification.setText("Your message was successfully sent to " + receiversToString(this.address));
                this.address.clear();
            }

        else {
                notification.setText("You have not selected a receiver!");
            }
        });

        GridPane grid = new GridPane();
        notification.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button.setDisable(false); //TODO remettre a true a la fin des tests
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("To: "), 0, 0);
        grid.add(receiver, 1,0);
        grid.add(emailComboBox, 1, 1);
        grid.add(new Label("Subject: "), 0, 2);
        grid.add(subject, 1, 2, 3, 1);
        grid.add(text, 0, 3, 4, 1);
        grid.add(button, 0, 4);
        grid.add(notification, 1, 4, 3, 1);

        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        stage.setScene(scene);
        stage.show();
        
        Thread t = new Thread(() -> client.connect());
        t.start();

    }

    @Override
    public void update(Observable observable, Object o) {
        if (client.isConnected()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    notification.setText(client.getServerMessage());
                    button.setDisable(false);
                }
            });
        }

    }

}