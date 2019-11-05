package models;

import exceptions.Create_User_Exception;
import exceptions.Delete_User_Exception;
import exceptions.Log_In_Exception;
import exceptions.Log_Out_Exception;
import interfaces.User_Interface;
import model.User;
import model.UserPriority;

public class Local_User extends User implements User_Interface {

    private String username;
    private String password;
    private UserPriority priority;

    public Local_User(String username, String password, UserPriority priority) {
        super(username, password, priority);
        this.username = username;
        this.password = password;
        this.priority = priority;
    }

    @Override
    public void log_in(String s, String s1) throws Log_In_Exception {

    }

    @Override
    public void log_out() throws Log_Out_Exception {

    }

    @Override
    public void create_user(String s, String s1, UserPriority userPriority) throws Create_User_Exception {

    }

    @Override
    public void delete_user(String s) throws Delete_User_Exception {

    }
}
