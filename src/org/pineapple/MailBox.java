package org.pineapple;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailBox {
    
    private String name;
    private String password;
    private MBXManager fileManager;

    public MailBox(String name, String password) {
        this.name = name;
        this.password = password;
        this.fileManager = new MBXManager(name);
    }
    
   //GETTERS
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public ArrayList<Message> getMessages() {
        return fileManager.read();
    }
    
    /**
     * Add the messages to the mailbox file.
     * @param messages The list of messages to append to the file.
     */
    public void addMessages(@NotNull Message... messages) {
        fileManager.write(Arrays.asList(messages));
    }
    
    /**
     * Set the messages to the mailbox. If the file contains old messages, they will be deleted.
     * @param messages The messages to set in the mailbox. Warning: It will delete the old messages.
     */
    public void setMessages(@NotNull List<Message> messages) {
        fileManager.write(messages, true);
    }
    
    public int getMessagesNumber() {
        return getMessages().size();
    }
    
    /**
     * Get the file size in Bytes.
     * @return The file size (Bytes).
     */
    public long getTotalSize() {
        return fileManager.getFileSize();
    }
}
