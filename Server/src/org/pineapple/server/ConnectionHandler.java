package org.pineapple.server;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

/**
 * Class to manage a connection for the server.
 * This class has been made with the help of Stack Overflow community
 * Source: https://stackoverflow.com/questions/28629669/java-tcp-simple-webserver-problems-with-response-codes-homework?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 */
public class ConnectionHandler implements Runnable {

    private Socket so_client;
    private DataInputStream in_data;
    private PrintStream out_data;

    @Nullable
    private Function<String, Void> onLog;

    public ConnectionHandler(@NotNull Socket so_client, @Nullable Function<String, Void> onLog) throws IOException {
        setClient(so_client);

        in_data = new DataInputStream(so_client.getInputStream());
        out_data = new PrintStream(so_client.getOutputStream());
        setOnLog(onLog);

        tryLog("New connection: " + getClientName());
    }
    public ConnectionHandler(@NotNull Socket so_client) throws IOException {
        this(so_client, null);
    }

    @Override
    public void run() {
        if (in_data == null)
            throw new NullPointerException();

        boolean keepRunning = true;
        String line = "";
        try {
            line = in_data.readLine();
            if(line != null){
                String[] parts = line.split(" ");

                //TODO redirect to right process

                try {
                    while ((line = in_data.readLine()) != null) {
                        if (line.equals("Connection: close")) {
                            so_client.close();
                            tryLog("Connection closed with " + getClientName());
                            break;
                        }
                    }
                } catch (SocketException ignored) {
                    tryLog("Connection closed by " + getClientName());
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public String getClientName() {
        return so_client.getInetAddress().getHostAddress() + ":" + so_client.getPort() + " (\"" + so_client.getInetAddress().getCanonicalHostName() + "\")";
    }

    /* CONNECTION HANDLER METHODS */

    public void sendMessage(@NotNull String message){
        //TODO send message logic
        SimpleDateFormat sdf = new SimpleDateFormat("dd'/'MM'/'yyyy 'at' HH:mm:ss");
        if(out_data != null){
            out_data.print("From : POP3 Server \r\n");
            out_data.print("To: " + this.getClientName() + "\r\n");
            out_data.print("Date: " + sdf.format(new Date())  + "\r\n");
            out_data.print("\r\n\r\n");
            out_data.print(message + "\r\n");
            out_data.flush();
            tryLog("Message sent");
        }
    }

    @Nullable
    public byte[] getFileData(@NotNull File file) {
        if (file == null)
            throw new NullPointerException();

        FileInputStream in = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] raw = new byte[4096];
        int size;

        try {
            in = new FileInputStream(file);

            while ((size = in.read(raw)) >= 0)
                baos.write(raw, 0, size);

            baos.flush();
            baos.close();

            return baos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* GETTERS & SETTERS */

    @Contract(pure = true)
    public Socket getClient() {
        return so_client;
    }

    public void setClient(Socket so_client) {
        this.so_client = so_client;
    }

    @Nullable
    @Contract(pure = true)
    public Function<String, Void> getOnLog() {
        return onLog;
    }

    public void setOnLog(@Nullable Function<String, Void> onLog) {
        this.onLog = onLog;
    }

    public void tryLog(String message) {
        if (getOnLog() != null)
            getOnLog().apply(message);
    }
}
