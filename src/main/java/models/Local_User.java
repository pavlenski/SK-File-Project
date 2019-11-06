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

public class Local_User extends User implements User_Interface {

    private String username;
    private String password;
    private UserPriority priority;

    private List<User> users;

    public Local_User(String username, String password, UserPriority priority) {
        super(username, password, priority);
        this.username = username;
        this.password = password;
        this.priority = priority;
        this.users = new ArrayList();

        try {
            init_users();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init_users() throws Exception {
        File file = new File("users.txt");
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

    @Override
    public void log_in(String username, String password) throws Log_In_Exception {

    }

    @Override
    public void log_out() throws Log_Out_Exception {

    }

    @Override
    public void create_user(String username, String password, UserPriority userPriority) throws Create_User_Exception {

    }

    @Override
    public void delete_user(String username) throws Delete_User_Exception {

    }
}
