package models;

import exceptions.Create_User_Exception;
import exceptions.Invalid_Path_Exception;
import exceptions.Log_In_Exception;
import exceptions.Log_Out_Exception;
import model.UserPriority;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Testing {

    private static void init(File storage_file, File users_file, File storage_final) throws Invalid_Path_Exception, IOException {
        System.out.println("Enter storage path: ");
        Scanner sc = new Scanner(System.in);
        String storage_path = sc.nextLine();

        if (!storage_path.isEmpty()) {

            File storage = new File(storage_path);

            if (!storage.exists()) {
                storage.mkdir();

                File storage_meta_file = new File(storage_path + File.separator + "storage.meta");
                storage_meta_file.createNewFile();
                File users_meta_file = new File(storage_path + File.separator + "users.meta");
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

                storage_file = storage_meta_file;
                users_file = users_meta_file;
                storage_final = storage;
                System.out.println("Storage created at path: \"" + storage_path + "\".");

            } else {
                if (!storage.isDirectory()) {
                    System.out.println("Path you entered is not directory!");
                    throw new Invalid_Path_Exception();
                } else {
                    File storage_meta_file = new File(storage_path + File.separator + "storage.meta");
                    File users_meta_file = new File(storage_path + File.separator + "users.meta");
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
                    else {
                        storage_file = storage_meta_file;
                        users_file = users_meta_file;
                        storage_final = storage;
                        System.out.println("Storage found at path: \"" + storage_path + "\".");
                    }
                }
            }

        } else {
            throw new Invalid_Path_Exception();
        }
    }

    private static void local_implementation() throws Exception {
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
                            System.out.println("Please enter path where you want to create this new file:");
                            String path = sc.nextLine();
                            local_file_obj.create_file(path, name);
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
                            System.out.println("Path: " + path + " Common name: " + common_name + " No files: " + no_files);
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
                            System.out.println("Please enter path where you want to paste copied file:");
                            String destination = sc.nextLine();
                            local_file_obj.download_file(source, destination);
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
                                File f = new File(s);
                                files.add(f);
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

                case "generate archive":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to archive:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this archive:");
                            String name = sc.nextLine();
                            local_file_obj.generate_archive_file(path, name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "generate multiple archive":
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
                            System.out.println("Please enter path where you want to save this archive:");
                            String destination = sc.nextLine();
                            local_file_obj.generate_archive_from_multiple_files(files, archive_name, destination);
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
                            System.out.println("Please enter path where you want to move selected file:");
                            String destination = sc.nextLine();
                            local_file_obj.move_file(source, destination);
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
                            local_file_obj.rename_file(path, new_name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                /*case "":
                    break;*/

            }


        }
    }

    public static void main(String[] args) {
        try {
            local_implementation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
