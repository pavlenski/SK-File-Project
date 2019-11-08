package dropbox;

import model.User;
import model.UserPriority;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CloudStroage extends AbstractDropBoxProvider {

    protected static String STORAGE_ROOT_PATH = "/";

    private String users_file;
    private List<DropBoxUser> users;
    private DropBoxUser current_user;

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

    public List<DropBoxUser> getUsers() {
        return users;
    }

    public DropBoxUser getCurrent_user() {
        return current_user;
    }

    public void setUsers_file(String users_file) {
        this.users_file = users_file;
    }

    public void setUsers(List<DropBoxUser> users) {
        this.users = users;
    }

    public void setCurrent_user(DropBoxUser current_user) {
        this.current_user = current_user;
    }


}
