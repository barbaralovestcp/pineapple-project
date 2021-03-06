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
import javafx.stage.WindowEvent;
import org.pineapple.CommandPOP3;
import org.pineapple.Message;

import java.util.ArrayList;
import java.util.Date;
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

    private final Button sendBtn = new Button("Send");
    private final Label notification = new Label();
    private final Label notificationServer = new Label();
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

    private String getReceiversAndAddBtns(ArrayList<String> address, GridPane grid) {
        StringBuilder receivers = new StringBuilder();
        for (int i = 0; i < address.size(); i++) {
            receivers.append(address.get(i));
            receivers.append("; ");
            Button btn = new Button(address.get(i));
            grid.add(btn, i, 0);
            final int buttonIndex = i;
            btn.setOnAction(e -> {
                btn.setVisible(false);
                this.address.remove(buttonIndex);
            });
        }

        return receivers.toString();
    }

    @Override
    public void start(Stage stage) {

        client = new ClientSMTP(name, domain);
        client.addObserver(this);
        stage.setTitle("Nouveau message");
        Scene scene = new Scene(new Group(), 600, 400, gradient);

        final ComboBox emailComboBox = new ComboBox();
        emailComboBox.getItems().addAll(
                "valentin@pine.apple",
                "elie@pine.apple",
                "lea@pine.apple",
                "philippine@pine.apple"
        );
        emailComboBox.setPromptText("Email address");
        emailComboBox.setEditable(true);
//        emailComboBox.valueProperty().addListener((ChangeListener<String>) (ov, t, t1) ->
//        {this.address.add(t1);
//         receiver.setText(getReceiversAndAddBtns(this.address));});


        GridPane grid = new GridPane();
        notification.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        notificationServer.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        sendBtn.setDisable(true);
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("To: "), 0, 0);
        grid.add(emailComboBox, 1, 1);
        grid.add(new Label("Subject: "), 0, 2);
        grid.add(subject, 1, 2, 3, 1);
        grid.add(text, 0, 3, 4, 1);
        grid.add(sendBtn, 0, 4);
        grid.add(notification, 1, 4, 3, 1);
        grid.add(notificationServer, 1, 5, 3, 1);

        emailComboBox.valueProperty().addListener((ChangeListener<String>) (ov, t, t1) ->
        {
            this.address.add(t1);
            receiver.setText(getReceiversAndAddBtns(this.address, grid));
        });

        sendBtn.setOnAction(e -> {
            if (this.address.size() > 0) {
                notification.setText("Your message was successfully sent to " + receiversToString(this.address));
                this.client.initMailTransaction(this.getMessageToSend(), this.address);
                this.address.clear();
                grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 0 || GridPane.getRowIndex(node) == null);
            } else {
                notification.setText("You have not selected a receiver!");
            }
        });

        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        stage.setScene(scene);
        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        stage.show();

        Thread t = new Thread(() -> client.connect());
        t.start();

    }

    public Message getMessageToSend(){
        return new Message()
                .setDate(new Date().toString())
                .setSender("<" + this.name + "@" + this.domain + ">")
                .setReceiver(receiversToString(this.address))
                .setSubject(subject.getText())
                .setMessageId("<" + new Date().getTime() + ">")
                .setMessage(text.getText() + "\r\n" + ".");
    }

    private void closeWindowEvent(WindowEvent event){
        System.out.println("QUIT connexion");
        String toSend = "QUIT";
        this.client.send(toSend);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (client.isConnected()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    notificationServer.setText(client.getServerMessage());
                    notification.setText(client.getLogMessage());
                    sendBtn.setDisable(false);
                }
            });
        }

    }

}