package dropbox;

import exceptions.Create_User_Exception;
import exceptions.Delete_User_Exception;
import exceptions.Log_In_Exception;
import exceptions.Log_Out_Exception;
import interfaces.User_Interface;
import model.User;
import model.UserPriority;

import java.util.List;

public class DropBoxUser extends User implements User_Interface {

    public DropBoxUser(String username, String password, UserPriority priority) {
        super(username, password, priority);

    }

    @Override
    public boolean log_in(String s, String s1) throws Log_In_Exception {
        return false;
    }

    @Override
    public boolean log_out() throws Log_Out_Exception {
        return false;
    }

    @Override
    public void create_user(String s, String s1, UserPriority userPriority) throws Create_User_Exception {

    }

    @Override
    public void delete_user(String s) throws Delete_User_Exception {

    }
}
