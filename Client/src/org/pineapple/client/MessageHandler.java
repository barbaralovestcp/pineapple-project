package org.pineapple.client;

import org.pineapple.Message;

import java.io.*;
import java.util.ArrayList;

public class MessageHandler {

    private static String FILEPATH = "./Client/data/messages.txt";
    private static String SEPARATOR = "******";

    public void addMessage(String data) {
        try {
            FileWriter fileWriter =
                    new FileWriter(FILEPATH);

            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            bufferedWriter.write(data);
            bufferedWriter.write(SEPARATOR);
            bufferedWriter.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            FILEPATH + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error writing file '"
                            + FILEPATH + "'");
        }

    }

    public ArrayList<Message> getMessagesFromFileBox() {
        String line = null;
        ArrayList<Message> messages = new ArrayList<>();

        try {
            FileReader fileReader =
                    new FileReader(FILEPATH);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            Message message = new Message();
            boolean isMessageText = false;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(SEPARATOR)) {
                    messages.add(message);
                    message = new Message();
                    isMessageText = false;
                } else if (isMessageText) {
                    message.setMessage(message.getMessage() + line);
                }
                else if (line.toLowerCase().contains("from:")) {
                    message.setSender(line.split(":")[1]);
                }
                else if (line.toLowerCase().contains("to:")) {
                    message.setReceiver(line.split(":")[1]);
                }
                else if (line.toLowerCase().contains("subject:")) {
                    message.setSubject(line.split(":")[1]);
                }
                else if (line.toLowerCase().contains("date:")) {
                    message.setDate(line.split(":")[1]);
                }
                else if (line.toLowerCase().contains("message-id:")) {
                    message.setMessageId(line.split(":")[1]);
                }
                else {
                    isMessageText = true;
                }
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex){
                System.out.println(
                        "Unable to open file '" +
                                FILEPATH + "'");
            }
        catch(IOException ex){
            ex.printStackTrace();
                System.out.println(
                        "Error reading file '"
                                + FILEPATH + "'");
            }

            return messages;
        }

    }
