package dropbox;

public class CloudStroage {

    protected static String STORAGE_ROOT_PATH = "";

    public CloudStroage() {

    }

    public static void setStorageRootPath(String storageRootPath) {
        STORAGE_ROOT_PATH = storageRootPath;
    }

    public static String getStorageRootPath() {
        return STORAGE_ROOT_PATH;
    }
}
