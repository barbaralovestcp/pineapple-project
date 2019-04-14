package org.pineapple.serversmtp;

import org.pineapple.stateMachine.Context;

public class ContextServer extends Context {

    private String domain;
    private String mailFrom;
    private String mailRcpt;

    public ContextServer() {
        super(new StateListening());
        this.domain = "pine.apple";
    }

    public String getDomain() {
        return this.domain;
    }


    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailRcpt() {
        return mailRcpt;
    }

    public void setMailRcpt(String mailRcpt) {
        this.mailRcpt = mailRcpt;
    }
}
