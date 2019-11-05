package exceptions;

public class Download_Exception extends Exception {

    public Download_Exception() {
        super("Error while trying to download an item.");
    }
}
