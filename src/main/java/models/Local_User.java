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

    public Local_User() {
        this.users = new ArrayList();
        try {
            init_users();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init_users() throws Exception {
        File file = new File(users_file);
        if (file.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
    public void log_in(String username, String password) throws Log_In_Exception {

    }

    @Override
    public void log_out() throws Log_Out_Exception {

    }

    @Override
    public void create_user(String username, String password, UserPriority userPriority) throws Create_User_Exception {
        User new_user = new User(username, password, userPriority);
        if (users.contains(new_user)) {
            users.add(new_user);
            rewrite_users();
        } else throw new Create_User_Exception();
    }

    @Override
    public void delete_user(String username) throws Delete_User_Exception {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                users.remove(u);
                System.out.println("User with username \"" + username + "\" successfully deleted.");
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
}
