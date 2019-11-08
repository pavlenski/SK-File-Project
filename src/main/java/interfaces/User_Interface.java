package interfaces;

import exceptions.Create_User_Exception;
import exceptions.Delete_User_Exception;
import exceptions.Log_In_Exception;
import exceptions.Log_Out_Exception;
import model.UserPriority;

public interface User_Interface {

    /**
     * Logs in and sets current user.
     * @param username Unique username of a user.
     * @param password Password of a user.
     */
    boolean log_in(String username, String password) throws Log_In_Exception;

    /**
     * Logs out and sets current user to null.
     */
    boolean log_out() throws Log_Out_Exception;

    /**
     * Creates new user. Only admin can call this.
     * @param username Unique username of a new user.
     * @param password Password of a new user.
     * @param priority Priority of a new user.
     */
    void create_user(String username, String password, UserPriority priority) throws Create_User_Exception;

    /**
     * Deletes user. Only admin can call this.
     * @param username Unique username of a user we want to delete.
     */
    void delete_user(String username) throws Delete_User_Exception;

}
