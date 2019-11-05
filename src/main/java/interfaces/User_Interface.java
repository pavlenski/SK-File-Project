package interfaces;

import exceptions.Create_User_Exception;
import exceptions.Delete_User_Exception;
import exceptions.Log_In_Exception;
import exceptions.Log_Out_Exception;
import model.UserPriority;

public interface User_Interface {

    void log_in(String username, String password) throws Log_In_Exception;

    void log_out() throws Log_Out_Exception;

    void create_user(String username, String password, UserPriority priority) throws Create_User_Exception;

    void delete_user(String username) throws Delete_User_Exception;

}
