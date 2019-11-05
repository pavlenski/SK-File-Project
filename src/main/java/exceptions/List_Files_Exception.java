package exceptions;

public class List_Files_Exception extends Exception {

    public List_Files_Exception() {
        super("Error while trying to list files.");
    }
}
