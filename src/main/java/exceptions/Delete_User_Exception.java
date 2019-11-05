package exceptions;

public class Delete_User_Exception extends Exception {

    public Delete_User_Exception() {
        super("Error while trying to delete a user.");
    }
}
