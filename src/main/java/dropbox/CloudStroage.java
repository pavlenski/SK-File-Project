package dropbox;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class CloudStroage extends AbstractDropBoxProvider {

    protected static String STORAGE_ROOT_PATH = "/";

    private String users_file;
    private List<User> users;
    private User current_user;

    public CloudStroage() {

        users = new ArrayList<>();
        current_user = null;

    }

    public static void setStorageRootPath(String storageRootPath) {
        STORAGE_ROOT_PATH = storageRootPath;
    }

    public static String getStorageRootPath() {
        return STORAGE_ROOT_PATH;
    }

    public String getUsers_file() {
        return users_file;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getCurrent_user() {
        return current_user;
    }
}
