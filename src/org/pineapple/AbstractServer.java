package org.pineapple;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is used by Server (POP3) and ServerSMTP. Do not change it just for ServerSMTP!
 */
public abstract class AbstractServer extends Application implements Runnable {
	
	public enum ServerState {
		INITIALIZED,
		STARTED,
		PAUSED,
		STOPPED;
		
		@SuppressWarnings("ConstantConditions")
		@Contract(pure = true)
		public static boolean isRunning(@NotNull ServerState state) {
			if (state == null)
				throw new NullPointerException();
			
			return state == STARTED || state == PAUSED;
		}
		@Contract(pure = true)
		public boolean isRunning() {
			return isRunning(this);
		}
	}
	
	protected ServerState serverState;
	protected Thread th_route;
	
	protected ServerSocket soc;
	protected ArrayList<Thread> connections;
	protected int port;
	
	protected BorderPane root;
	protected ToolBar tb_buttons;
	protected Button bt_pause_resume;
	protected Button bt_stop;
	protected Button bt_clear;
	protected WebView loggerView;
	protected WebEngine loggerEngine;
	protected final String headContent =
			"<html>" +
					"<head>" +
					"<meta charset=\"utf-8\"/>" +
					"<style>" +
					"p {" +
					"font-family: Calibri, Verdana, Arial, serif;" +
					"}" +
					"</style>" +
					"</head>" +
					"<body>";
	protected String currentContent;
	protected final String footContent =
			"</body>" +
					"</html>";
	protected int timeout;
	
	public AbstractServer(int port) {
		setPort(port);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		setState(ServerState.INITIALIZED);
		th_route = new Thread(this);
		setCurrentContent("");
		
		connections = new ArrayList<>();
		root = new BorderPane();
		tb_buttons = new ToolBar();
		bt_pause_resume = new Button("Pause");
		bt_stop = new Button("Stop");
		bt_clear = new Button("Clear");
		loggerView = new WebView();
		loggerEngine = loggerView.getEngine();
		
		
		
		bt_pause_resume.setOnMouseClicked(event -> {
			if (getState() == ServerState.STARTED)
				pauseServer();
			else if (getState() == ServerState.PAUSED)
				resumeServer();
		});
		
		bt_stop.setCancelButton(true);
		bt_stop.setOnMouseClicked(event -> {
			stopServer();
		});
		
		bt_clear.setOnMouseClicked(event -> {
			currentContent = "";
			loggerEngine.loadContent("");
		});
		
		tb_buttons.getItems().addAll(bt_pause_resume, bt_stop, new Separator(Orientation.VERTICAL), bt_clear);
		root.setTop(tb_buttons);
		root.setCenter(loggerView);
		
		primaryStage.setScene(new Scene(root, 640, 480));
		primaryStage.setTitle("[Server] \uD83C\uDF4D");
		primaryStage.show();
		
		initServer();
		th_route.start();
	}
	
	@Override
	public void run() {
		try {
			soc = new ServerSocket(port);
			log("Server open on port " + soc.getLocalPort());
			log("Server open on adress " + soc.getInetAddress().toString());
			if (getState() == ServerState.INITIALIZED)
				startServer();
			
			// Wait for "start"
			while (getState() == ServerState.INITIALIZED);
			
			// Main loop
			while (getState().isRunning()) {
				
				// Pause
				while (getState() == ServerState.PAUSED) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				
				try {
					// Accept a connection (it will block this thread until a connection arrived)
					Socket com_cli = soc.accept();
					
					Thread t = new Thread(instantiateConnectionHandler(com_cli));
					t.start();
					connections.add(t);
				} catch (SocketException ignored) { }
				
				for (int i = 0; i < connections.size(); i++) {
					if (connections.get(i) == null || !connections.get(i).isAlive() || connections.get(i).isInterrupted()) {
						connections.remove(i);
						i--;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		stopServer();
	}
	
	@NotNull
	protected abstract AbstractServerConnectionHandler instantiateConnectionHandler(@NotNull Socket com_cli) throws IOException;
	
	@Override
	public void stop() throws Exception {
		super.stop();
		
		stopServer();
		if (th_route != null)
			th_route.interrupt();
		
		for (Thread connection : connections)
			connection.interrupt();
		
		if (soc != null) {
			try {
				soc.close();
			} catch (IOException ignored) { }
		}
		
		log("Shutting down...");
		log("Good bye.");
		Platform.exit();
		Runtime.getRuntime().halt(0);
	}
	
	@SuppressWarnings("ConstantConditions")
	public synchronized void log(@NotNull String log) {
		if (log == null)
			throw new NullPointerException();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd'/'MM'/'yyyy 'at' HH:mm:ss");
		String message = '[' + sdf.format(new Date()) + "] " + log;
		
		System.out.println(message);
		
		message = message.replace("[", "<b>[<span style=\"color: red;\">");
		message = message.replace(" at ", "</span> at <span style=\"color: blue;\">");
		message = message.replace("]", "</span>]</b>");
		
		addCurrentContent(
				"<p>" +
						message +
						"</p>"
		);
		
		if (loggerEngine != null) {
			Platform.runLater(() -> loggerEngine.loadContent(
					headContent +
							getCurrentContent() +
							footContent
			));
		}
	}
	
	/* THREAD CONTROL */
	
	public void initServer() {
		if (getState() != ServerState.STOPPED) {
			setState(ServerState.INITIALIZED);
			bt_pause_resume.setDisable(true);
			bt_pause_resume.setText("Pause");
			bt_stop.setDisable(true);
		}
	}
	
	@SuppressWarnings("Duplicates")
	public void startServer() {
		if (getState() != ServerState.STOPPED) {
			setState(ServerState.STARTED);
			bt_pause_resume.setDisable(false);
			bt_pause_resume.setText("Pause");
			bt_stop.setDisable(false);
			log("Server ready to process!");
		}
	}
	
	public void pauseServer() {
		if (getState() != ServerState.INITIALIZED && getState() != ServerState.STOPPED) {
			setState(ServerState.PAUSED);
			bt_pause_resume.setDisable(false);
			bt_pause_resume.setText("Resume");
			bt_stop.setDisable(false);
			log("Server is paused");
		}
	}
	
	@SuppressWarnings("Duplicates")
	public void resumeServer() {
		if (getState() != ServerState.INITIALIZED && getState() != ServerState.STOPPED) {
			setState(ServerState.STARTED);
			bt_pause_resume.setDisable(false);
			bt_pause_resume.setText("Pause");
			bt_stop.setDisable(false);
			log("Server is ready!");
		}
	}
	
	public void stopServer() {
		setState(ServerState.STOPPED);
		bt_stop.setDisable(true);
		bt_pause_resume.setDisable(true);
		log("Server is stopping...");
		Platform.exit();
	}
	
	/* GETTERS & SETTERS */
	
	protected synchronized ServerState getState() {
		return serverState;
	}
	
	public synchronized void setState(@NotNull ServerState serverState) {
		this.serverState = serverState;
	}
	
	public synchronized String getCurrentContent() {
		return currentContent;
	}
	
	public synchronized void setCurrentContent(String currentContent) {
		this.currentContent = currentContent;
	}
	
	public synchronized void addCurrentContent(String currentContent) {
		setCurrentContent(getCurrentContent() + currentContent);
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
}
