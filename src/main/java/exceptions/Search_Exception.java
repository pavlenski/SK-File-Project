package exceptions;

public class Search_Exception extends Exception {
    public Search_Exception() {
        System.out.println("Error while trying to search for the desired item.");
        System.err.println("Error while trying to search for the desired item.");
    }
}
