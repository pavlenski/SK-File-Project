package model;

import interfaces.User_Interface;

public class User {

    private String username;
    private String password;
    private UserPriority priority;
    private String dupe;

    public User(String username, String password, UserPriority priority) {
        this.username = username;
        this.password = password;
        this.priority = priority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserPriority getPriority() {
        return priority;
    }

    public void setPriority(UserPriority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "User[" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", priority=" + priority +
                ']';
    }

}
