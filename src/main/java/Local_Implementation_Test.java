import exceptions.Invalid_Path_Exception;
import model.UserPriority;
import models.Local_Directory;
import models.Local_File;
import models.Local_User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Local_Implementation_Test {

    private static boolean check_extension(Local_Directory local_dir_obj, String file_name){
        for (String s : local_dir_obj.getExtension_blacklist()){
            if (file_name.endsWith("." + s)) return false;
        }
        return true;
    }

    public static void run() throws Exception {
        System.out.println("Enter storage path: ");
        Scanner scanner = new Scanner(System.in);
        String storage_path = scanner.nextLine();

        File storage = null;
        File storage_meta_file = null;
        File users_meta_file = null;

        if (!storage_path.isEmpty()) {

            storage = new File(storage_path);

            if (!storage.exists()) {
                storage.mkdir();

                storage_meta_file = new File(storage_path + File.separator + "storage.meta");
                storage_meta_file.createNewFile();
                users_meta_file = new File(storage_path + File.separator + "users.meta");
                users_meta_file.createNewFile();

                try {
                    FileWriter fw = new FileWriter(storage_meta_file);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(storage_path);
                    pw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileWriter fw = new FileWriter(users_meta_file);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println("admin:admin:ADMIN");
                    pw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Storage created at path: \"" + storage_path + "\".");

            } else {
                if (!storage.isDirectory()) {
                    System.out.println("Path you entered is not directory!");
                    throw new Invalid_Path_Exception();
                } else {
                    storage_meta_file = new File(storage_path + File.separator + "storage.meta");
                    users_meta_file = new File(storage_path + File.separator + "users.meta");
                    /*if (!storage_meta_file.exists()) {
                        System.out.println("No storage_meta_file found! Creating one...");
                        storage_meta_file.createNewFile();
                        try {
                            FileWriter fw = new FileWriter(storage_meta_file);
                            PrintWriter pw = new PrintWriter(fw);
                            pw.println(storage_path);
                            pw.close();
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!users_meta_file.exists()) {
                        System.out.println("No users_meta_file found! Creating one...");
                        users_meta_file.createNewFile();
                        try {
                            FileWriter fw = new FileWriter(users_meta_file);
                            PrintWriter pw = new PrintWriter(fw);
                            pw.println("admin:admin:ADMIN");
                            pw.close();
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                    if (!storage_meta_file.exists() || !users_meta_file.exists()) throw new Invalid_Path_Exception();
                    else System.out.println("Storage found at path: \"" + storage_path + "\".");
                }
            }

        } else {
            throw new Invalid_Path_Exception();
        }

        if (storage == null || storage_meta_file == null || users_meta_file == null) return;

        Local_User local_user_obj = new Local_User();
        Local_File local_file_obj = new Local_File();
        Local_Directory local_directory_obj = new Local_Directory();

        local_user_obj.setUsers_file(users_meta_file.getAbsolutePath());
        local_file_obj.setStorage_path(storage.getAbsolutePath());
        local_directory_obj.setStorage_path(storage.getAbsolutePath());

        local_user_obj.init_users();
        local_directory_obj.init_blacklist();

        while (true) {
            //System.out.print("[Local storage]: ");

            Scanner option_scanner = new Scanner(System.in);
            String option = option_scanner.nextLine();
            option = option.toLowerCase();

            switch (option) {

                case "log in":
                    if (local_user_obj.getCurrent_user() == null) {
                        System.out.println("Please enter your username:");
                        Scanner sc = new Scanner(System.in);
                        String username = sc.nextLine();
                        System.out.println("Please enter your password:");
                        String password = sc.nextLine();
                        local_user_obj.log_in(username, password);
                    } else {
                        System.out.println("You need to log out first!");
                    }
                    break;

                case "log out":
                    if (local_user_obj.getCurrent_user() != null) local_user_obj.log_out();
                    else System.out.println("You have to be loged in first!");
                    break;

                case "create user":
                    if (local_user_obj.getCurrent_user() != null ){
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN){
                            System.out.println("Please enter new user username:");
                            Scanner sc = new Scanner(System.in);
                            String username = sc.nextLine();
                            System.out.println("Please enter new user password:");
                            String password = sc.nextLine();
                            System.out.println("Please enter new user priority:");
                            String priority = sc.nextLine();
                            priority = priority.toLowerCase();
                            switch (priority){
                                case "admin":
                                    local_user_obj.create_user(username, password, UserPriority.ADMIN);
                                    break;
                                case "basic":
                                    local_user_obj.create_user(username, password, UserPriority.BASIC);
                                    break;
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be loged in first!");
                    }
                    break;

                case "delete user":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter username of user you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String username = sc.nextLine();
                            local_user_obj.delete_user(username);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter new file name:");
                            Scanner sc = new Scanner(System.in);
                            String name = sc.nextLine();
                            if (check_extension(local_directory_obj, name)) {
                                System.out.println("Please enter path where you want to create this new file:");
                                String path = sc.nextLine();
                                local_file_obj.create_file(path, name);
                            } else {
                                System.out.println("You can't use that extension (it's on a blacklist). File not created.");
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create multiple files":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter path where you want to create these new files:");
                            String path = sc.nextLine();
                            System.out.println("Please enter common name for these new files:");
                            String common_name = sc.nextLine();
                            System.out.println("Please enter how many similar files you want to create:");
                            int no_files = sc.nextInt();
                            local_file_obj.create_multiple_files(path, common_name, no_files);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "delete file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            local_file_obj.delete_file(path);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "copy file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to copy:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            if (check_extension(local_directory_obj, source)) {
                                System.out.println("Please enter path where you want to paste copied file:");
                                String destination = sc.nextLine();
                                local_file_obj.download_file(source, destination);
                            } else {
                                System.out.println("You can't copy a file with that extension (it's on a blacklist). File not copied.");
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "copy multiple files":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter paths of files you want to copy: (separate paths with ';')");
                            String paths = sc.nextLine();
                            String[] parse_paths = paths.split(";");
                            List<File> files = new ArrayList<>();
                            for (String s:parse_paths){
                                if (check_extension(local_directory_obj, s)) {
                                    File f = new File(s);
                                    files.add(f);
                                } else {
                                    System.out.println("File on path: \"" + s + "\" is on extension blacklist. File not copied.");
                                }
                            }
                            System.out.println("Please enter path where you want to paste copied file:");
                            String destination = sc.nextLine();
                            local_file_obj.download_multiple_files(files, "", destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "archive file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to archive:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this archive:");
                            String name = sc.nextLine();
                            if (check_extension(local_directory_obj, name)) {
                                local_file_obj.generate_archive_file(path, name);
                            } else {
                                System.out.println("You can't archive a file with that extension (it's on a blacklist). File not archieved.");
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "archive multiple files":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter paths of files you want to archive: (separate paths with ';')");
                            String paths = sc.nextLine();
                            String[] parse_paths = paths.split(";");
                            List<File> files = new ArrayList<>();
                            for (String s:parse_paths){
                                File f = new File(s);
                                files.add(f);
                            }
                            System.out.println("Please enter how you want to name this archive:");
                            String archive_name = sc.nextLine();
                            if (check_extension(local_directory_obj, archive_name)) {
                                System.out.println("Please enter path where you want to save this archive:");
                                String destination = sc.nextLine();
                                local_file_obj.generate_archive_from_multiple_files(files, archive_name, destination);
                            } else {
                                System.out.println("You can't archive files with that extension (it's on a blacklist). Files not archieved.");
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "move file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to move:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            if (check_extension(local_directory_obj, source)) {
                                System.out.println("Please enter path where you want to move selected file:");
                                String destination = sc.nextLine();
                                local_file_obj.move_file(source, destination);
                            } else {
                                System.out.println("You can't move file with that extension (it's on a blacklist). File not moved.");
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "rename file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to rename:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter this file's new name:");
                            String new_name = sc.nextLine();
                            if (check_extension(local_directory_obj, new_name)) {
                                local_file_obj.rename_file(path, new_name);
                            } else {
                                System.out.println("You can't reneme file with that extension (it's on a blacklist). File not renamed.");
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of where you want to create a directory:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this directory:");
                            String name = sc.nextLine();
                            local_directory_obj.create_directory(path, name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create multiple dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter path where you want to create these new directories:");
                            String path = sc.nextLine();
                            System.out.println("Please enter common name for these new directories:");
                            String common_name = sc.nextLine();
                            System.out.println("Please enter how many similar directories you want to create:");
                            int no_directories = sc.nextInt();
                            local_directory_obj.create_multiple_directories(path, common_name, no_directories);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "delete dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            local_directory_obj.delete_directory(path);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "copy dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to copy:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            System.out.println("Please enter path where you want to paste copied directory:");
                            String destination = sc.nextLine();
                            local_directory_obj.download_directory(source, destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "archive dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to archive:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this archive:");
                            String name = sc.nextLine();
                            if (check_extension(local_directory_obj, name)) {
                                local_directory_obj.generate_archive_directory(path, name);
                            } else {
                                System.out.println("You can't archive a directory with that extension (it's on a blacklist). Directory not archieved.");
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "move dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to move:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            System.out.println("Please enter path where you want to move selected directory:");
                            String destination = sc.nextLine();
                            local_directory_obj.move_directory(source, destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "rename dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to rename:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter this directory's new name:");
                            String new_name = sc.nextLine();
                            local_directory_obj.rename_directory(path, new_name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        local_directory_obj.list_files(path, null, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files r":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        local_directory_obj.list_files(path, null, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files with":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        System.out.println("Please enter extensions of files you want to list: (separate them with ';')");
                        String extension_str = sc.nextLine();
                        String[] extensions = extension_str.split(";");
                        local_directory_obj.list_files(path, extensions, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files with r":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        System.out.println("Please enter extensions of files you want to list: (separate them with ';')");
                        String extension_str = sc.nextLine();
                        String[] extensions = extension_str.split(";");
                        local_directory_obj.list_files(path, extensions, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list dirs":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose directories you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        local_directory_obj.list_directories(path, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list dirs r":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose directories you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        local_directory_obj.list_directories(path, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create blacklist":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter extensions of files you want to blacklist: (separate paths with ';')");
                            String extensions = sc.nextLine();
                            String[] new_extensions = extensions.split(";");
                            local_directory_obj.create_extension_blacklist(storage_path, new_extensions);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "search":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter name of a file you want to search:");
                        Scanner sc = new Scanner(System.in);
                        String file_name = sc.nextLine();
                        local_directory_obj.search_files(file_name);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                default:
                    System.out.println("Command \"" + option + "\" doens't exist.");
                    break;

            }


        }
    }

}
