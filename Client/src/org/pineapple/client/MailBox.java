package org.pineapple.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MailBox extends Application {

    private String msg;
    private String name = "client";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MailBox");
        Text title = new Text (0, 0, "\uD83C\uDF4D Bienvenue " + this.name);
        title.setStyle("-fx-font: 30 arial;");

        Button connexion = new Button();
        connexion.setText("Enter");
        connexion.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO handle tcp connexion

                //then print mailbox view
                mailbox(primaryStage);
            }
        });


        GridPane homeLayout = new GridPane();
        homeLayout.add(title,0,0);
        homeLayout.add(connexion,0,1);

        StackPane root = new StackPane();
        root.getChildren().add(homeLayout);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private void mailbox(Stage primaryStage){
        Button refresh = new Button();
        refresh.setText("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(refresh);

        Scene mail = new Scene(root, 300, 250);
        primaryStage.setScene(mail);
    }
}
