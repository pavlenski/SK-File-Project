package exceptions;

public class Download_Multiple_Archives_Exception extends Exception {

    public Download_Multiple_Archives_Exception() {
        super("Error while trying to download multiple archive items.");
    }
}
