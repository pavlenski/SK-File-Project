package models;

import exceptions.Create_User_Exception;
import exceptions.Delete_User_Exception;
import exceptions.Log_In_Exception;
import exceptions.Log_Out_Exception;
import interfaces.User_Interface;
import model.User;
import model.UserPriority;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Local_User implements User_Interface {
    private String users_file;
    private List<User> users;
    private User current_user;

    public Local_User() {
        this.users = new ArrayList();
        current_user = null;
    }

    public void init_users() throws Exception {
        File file = new File(users_file);
        if (file.exists()) {
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] parse = line.split(":");
                    User user = null;
                    switch (parse[2]) {
                        case "ADMIN":
                            user = new User(parse[0], parse[1], UserPriority.ADMIN);
                            break;
                        case "BASIC":
                            user = new User(parse[0], parse[1], UserPriority.BASIC);
                            break;
                    }
                    users.add(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void rewrite_users() {
        try {
            FileWriter fw = new FileWriter(new File(users_file));
            PrintWriter pw = new PrintWriter(fw);
            for (User u : users) {
                pw.println(u.getUsername() + ":" + u.getPassword() + ":" + u.getPriority());
            }
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean log_in(String username, String password) throws Log_In_Exception {
        for (User u:users){
            if (u.getUsername().equals(username)){
                if (u.getPassword().equals(password)){
                    current_user = u;
                    System.out.println("User \"" + username + "\" successfully logged in.");
                    return true;
                } else{
                    System.out.println("Wrong password!");
                    throw new Log_In_Exception();
                }
            }
        }
        throw new Log_In_Exception();
    }

    @Override
    public boolean log_out() throws Log_Out_Exception {
        if (current_user != null) {
            current_user = null;
            System.out.println("User successfully logged out.");
            return true;
        }
        throw new Log_Out_Exception();
    }

    @Override
    public void create_user(String username, String password, UserPriority userPriority) throws Create_User_Exception {
        User new_user = new User(username, password, userPriority);
        if (!users.contains(new_user)) {
            users.add(new_user);
            rewrite_users();
            System.out.println("User \"" + username + "\" successfully created.");
        } else throw new Create_User_Exception();
    }

    @Override
    public void delete_user(String username) throws Delete_User_Exception {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                users.remove(u);
                rewrite_users();
                System.out.println("User \"" + username + "\" successfully deleted.");
                return;
            }
        }
        throw new Delete_User_Exception();
    }

    public String getUsers_file() {
        return users_file;
    }

    public void setUsers_file(String users_file) {
        this.users_file = users_file;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getCurrent_user() {
        return current_user;
    }

    public UserPriority getCurrent_user_priority(){
        return current_user.getPriority();
    }
}
