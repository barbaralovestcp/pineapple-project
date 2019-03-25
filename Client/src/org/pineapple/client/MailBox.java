package org.pineapple.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.pineapple.CommandPOP3;
import org.pineapple.Message;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class MailBox extends Application implements Observer {

    private String name = "lea";
    private Client client;
    private HashMap<Integer, Message> messagesList = new HashMap<>();
    private boolean isMailboxOpened = false;

    //Style
    private Color primary = Color.rgb(138, 43, 226);
    private Color secondary = Color.rgb(75, 0, 130);
    private LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, primary), new Stop(1, secondary));
    private Background background = new Background(new BackgroundFill(secondary, CornerRadii.EMPTY, Insets.EMPTY));

    //View elements
    private Stage primaryStage;
    private Button refresh = new Button();
    private ListView<Message> messageView = new ListView<>();
    private ListView<Box> mailboxView = new ListView<>();
    private HBox toolbar = new HBox();
    private TextArea messageBody = new TextArea();
    private TextArea messageInfo = new TextArea();
    private TextArea messageState = new TextArea();
    private VBox viewInfo = new VBox();
    private VBox viewMessage = new VBox();
    private Box inbox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Mail Box");
        openMailbox(primaryStage, this);

        //MAIN TITLE
        Text title = new Text(0, 0, "\uD83C\uDF4D Bienvenue " + this.name);
        title.setStyle("-fx-font: 30 arial;");
        title.setFill(Color.WHITE);

        //MAIN BUTTON ENTER
        Button connexionbtn = new Button();
        this.initButtonStyle(connexionbtn, "Enter");
        connexionbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.sendAPOP();
            }
        });

        //Main image
//        Image image = null;
//        try {
//            image = new Image(new FileInputStream("./data/mail.png"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        ImageView view = new ImageView();
//        view.setImage(image);

        //GRIDLAYOUT
        GridPane homegridpane = new GridPane();
        homegridpane.add(title, 0, 0);
//        homegridpane.add(view, 0, 1);
        homegridpane.add(connexionbtn, 0, 2);
        homegridpane.setAlignment(Pos.CENTER);

        //INIT WINDOW
        StackPane root = new StackPane();
        root.setBackground(Background.EMPTY);
        root.getChildren().add(homegridpane);
        Scene scene = new Scene(root, 500, 500, gradient);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.initMailbox();
    }

    private void openMailbox(Stage stage, MailBox that) {
        client = new Client(name);
        client.addObserver(that);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    client.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initMailbox() {
        Stage mailview = new Stage();

        ArrayList<Message> inboxList = this.getMessages();

        ObservableList<Message> inboxMessages = FXCollections
                .observableArrayList(inboxList);
        inbox = new Box("Inbox", inboxMessages);
        messageView.setItems(inboxMessages);

        BorderPane borderPane = new BorderPane();
        borderPane.setBackground(Background.EMPTY);
        ObservableList<Box> obsMailboxes = mailboxView.getItems();
        obsMailboxes.add(inbox);

        mailboxView.setItems(obsMailboxes);
        this.initButtonStyle(refresh, "Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.sendMessage(CommandPOP3.STAT);
            }
        });

        toolbar.setSpacing(25);
        toolbar.getChildren().addAll(refresh);
        messageInfo.setPrefHeight(100);
        messageInfo.setPrefWidth(350);
        messageInfo.setEditable(false);
        messageBody.setPrefHeight(400);
        messageBody.setPrefWidth(350);
        messageBody.setEditable(false);
        viewMessage.getChildren().addAll(messageInfo);
        viewInfo.getChildren().addAll(viewMessage, messageBody);

        //Message openMailbox view
        mailboxView.setOnMouseClicked(e -> {
            Box currentMailbox = mailboxView.getSelectionModel()
                    .getSelectedItem();
            if (currentMailbox != null) {
                int index = messageView.getSelectionModel()
                        .getSelectedIndex();
                messageView.getSelectionModel().clearSelection(index);
                ObservableList<Message> newList = currentMailbox.getMessages();
                currentMailbox.addAll(newList);
            }
        });
        mailboxView.setOnMouseClicked(e -> {
            //change current openMailbox
            Box currentMailbox = mailboxView.getSelectionModel()
                    .getSelectedItem();
            if (currentMailbox != null) {
                ObservableList<Message> obsMessages = currentMailbox
                        .getMessages();
                messageView.setItems(obsMessages);
            }
        });
        messageView.setOnMouseClicked(e -> {
            //change message selected
            Message currentMessage = messageView.getSelectionModel()
                    .getSelectedItem();
            if (currentMessage != null) {
                messageInfo.setText(currentMessage.getInfos());
                messageBody.setText(currentMessage.getMessage());
            }
        });

        borderPane.setLeft(mailboxView);
        borderPane.setTop(toolbar);
        borderPane.setRight(viewInfo);
        borderPane.setCenter(messageView);

        Scene scene = new Scene(borderPane, 800, 500, primary);
        mailview.setTitle("Mail Box");
        mailview.setScene(scene);
       scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

        mailview.show();
    }

    private void closeWindowEvent(WindowEvent event){
        System.out.println("QUIT connexion");
        this.client.sendMessage(CommandPOP3.QUIT);
//        event.consume();
    }

    private ArrayList<Message> getMessages() {
        return this.client.getMessageHandler().getMessages();
    }

    public void remove(int m) {
        this.messagesList.remove(m);
    }

    public void add(Message m, int number) {
        this.messagesList.put(number, m);
    }

    private void initButtonStyle(Button btn, String text) {
        btn.setText(text);
        btn.setTextFill(Color.WHITE);
        btn.setBackground(background);

        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                btn.setTextFill(secondary);
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setBackground(new Background(new BackgroundFill(secondary, CornerRadii.EMPTY, Insets.EMPTY)));
                btn.setTextFill(Color.WHITE);
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        //Get Mailbox messages
        ArrayList<Message> newMessages = this.getMessages();
        ObservableList<Message> newInboxMessages = FXCollections
                .observableArrayList(newMessages);

        inbox = new Box("Inbox", newInboxMessages);
    }
}
