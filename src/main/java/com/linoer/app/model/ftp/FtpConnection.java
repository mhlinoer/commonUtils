package com.linoer.app.model.ftp;

public class FtpConnection {
    private String username;
    private String host;
    private String password;

    public FtpConnection(String username, String host, String password) {
        this.username = username;
        this.host = host;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
