package exceptions;

public class Search_Files_Exception extends Exception {

    public Search_Files_Exception() {
        super("Error while trying to search for a file.");
    }
}
