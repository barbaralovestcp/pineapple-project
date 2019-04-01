package org.pineapple.clientsmtp;

public class ClientSMTP {

    private boolean isConnected;
    private String serverMessage;

    //TODO : notify observer à chaque réception d'un message du serveur

    boolean isConnected() {
        return isConnected;
    }

    public String getServerMessage() {
        return serverMessage;
    }
}
