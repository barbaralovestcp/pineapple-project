package org.pineapple.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class MailBox extends Application {

    private String msg;
    private String name = "client";
    private Color primary = Color.rgb(138,43,226);
    private Color secondary = Color.rgb(75,0,130);
    private LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, primary), new Stop(1, secondary));
    private Background background = new Background(new BackgroundFill(secondary,CornerRadii.EMPTY,Insets.EMPTY));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mail Box");

        //MAIN TITLE
        Text title = new Text (0, 0, "\uD83C\uDF4D Bienvenue " + this.name);
        title.setStyle("-fx-font: 30 arial;");
        title.setFill(Color.WHITE);

        //MAIN BUTTON ENTER
        Button connexionbtn = new Button();
        this.initButtonStyle(connexionbtn,"Enter");
        connexionbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO handle tcp connexion

                //Client client = new Client();
//                while(!client.isConnected()){
//
//                }
                //then print mailbox view
                mailbox(primaryStage);
            }
        });

        //GRIDLAYOUT
        GridPane homegridpane = new GridPane();
        homegridpane.add(title,0,0);
        homegridpane.add(connexionbtn,0,1);
        homegridpane.setAlignment(Pos.CENTER);

        //INIT WINDOW
        StackPane root = new StackPane();
        root.setBackground(Background.EMPTY);
        root.getChildren().add(homegridpane);
        Scene scene = new Scene(root, 500, 500,gradient);
        primaryStage.setScene(scene);

//        primaryStage.getIcons().add(new Image("./Client/data/pineapple.png"));
        primaryStage.show();
    }

    private void mailbox(Stage primaryStage){
        Button refresh = new Button();
        this.initButtonStyle(refresh, "Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.setBackground(Background.EMPTY);
        root.getChildren().add(refresh);
        Scene mail = new Scene(root, 500, 500, primary);
        primaryStage.setScene(mail);
    }

    private void printMessages(){
        MessagesReader fileReader = new MessagesReader();
    }

    private void initButtonStyle(Button btn, String text){
        btn.setText(text);
        btn.setTextFill(Color.WHITE);
        btn.setBackground(background);

        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
                btn.setTextFill(secondary);
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setBackground(new Background(new BackgroundFill(secondary,CornerRadii.EMPTY,Insets.EMPTY)));
                btn.setTextFill(Color.WHITE);
            }
        });
    }
}
