import com.dropbox.core.v2.DbxClientV2;
import dropbox.CloudStroage;
import dropbox.DropBoxDirectory;
import dropbox.DropBoxFile;
import dropbox.DropBoxUser;
import exceptions.Invalid_Path_Exception;
import model.DropBoxChecker;
import model.UserPriority;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cloud_Implementation_Test {

    private static boolean check_extension(DropBoxDirectory db_dir, String file_name){
        for (String s : db_dir.getExtension_blacklist()){
            if (file_name.endsWith("." + s)) return false;
        }
        return true;
    }

    public static void run() throws Exception {

        System.out.println("Enter storage path: ");
        Scanner scanner = new Scanner(System.in);
        String storage_path = scanner.nextLine();

        String storage_split[] = storage_path.split("/");
        String storage_name = storage_split[storage_split.length - 1];
        String storage_root = "";
        for(int i = 0 ; i < storage_split.length - 1 ; i++) {
            storage_root = storage_root + storage_split[i] + "/";
        }
        if(storage_root.length() > 1) storage_root = storage_root.substring(0, storage_root.length() - 1);

        //File storage = null;
        File storage_meta_file = null;
        File users_meta_file = null;

        CloudStroage cloud_storage = new CloudStroage();
        DbxClientV2 client = cloud_storage.getClient();

        DropBoxDirectory db_dir = new DropBoxDirectory();
        DropBoxFile db_file = new DropBoxFile();
        DropBoxUser db_user = new DropBoxUser();

        if (!DropBoxChecker.check_path(client, storage_path)) {
//            System.out.println("root: " + storage_root);
//            System.out.println("name: " + storage_name);
//            if(storage_root.length() > 1)
//                System.out.println(storage_root + "/" + storage_name);
//            else
//                System.out.println(storage_root + storage_name);

            storage_meta_file = new File("scramble" + File.separator + "storage.meta");
            storage_meta_file.createNewFile();
            storage_meta_file.deleteOnExit();;
            users_meta_file = new File("scramble" + File.separator + "users.meta");
            users_meta_file.createNewFile();
            users_meta_file.deleteOnExit();

            try {
                FileWriter fw = new FileWriter(storage_meta_file);
                PrintWriter pw = new PrintWriter(fw);
                if(storage_root.length() > 1) pw.println(storage_root + "/" + storage_name);        /// ovde treba da promenim upis u meta fajl
                else pw.println(storage_root + storage_name);
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


            CloudStroage.setStorageRootPath(storage_root);
            //System.out.println(CloudStroage.getStorageRootPath());

            db_dir.create_directory(storage_root, storage_name);  //fiksno za sad treba da uzima skener

            if(storage_root.length() > 1)
                CloudStroage.setStorageRootPath(storage_root + "/" + storage_name);
            else
                CloudStroage.setStorageRootPath(storage_root + storage_name);

            db_dir.init_blacklist();
            db_file.upload_file(storage_meta_file.getAbsolutePath(), CloudStroage.getStorageRootPath());
            db_file.upload_file(users_meta_file.getAbsolutePath(), CloudStroage.getStorageRootPath());

            System.out.println("Storage created at path: '" + CloudStroage.getStorageRootPath() + "'.");

        } else {
            if (!DropBoxChecker.check_folder_meta_data(client, storage_path)) {
                System.out.println("Path you entered is not directory!");
                throw new Invalid_Path_Exception();
            } else {                                                                                                                                    //missing meta files !!!
                if (!DropBoxChecker.check_file_meta_data(client, storage_path + "/storage.meta") || !DropBoxChecker.check_file_meta_data(client, storage_path + "/users.meta")) throw new Invalid_Path_Exception();
                else {
                    if(storage_root.length() > 1)
                        CloudStroage.setStorageRootPath(storage_root + "/" + storage_name);
                    else
                        CloudStroage.setStorageRootPath(storage_root + storage_name);

                    db_file.download_file(CloudStroage.getStorageRootPath() + "/storage.meta", "scramble");
                    db_file.download_file(CloudStroage.getStorageRootPath() + "/users.meta", "scramble");
                    db_dir.init_blacklist();
                    storage_meta_file = new File("scramble" + File.separator + "storage.meta");
                    users_meta_file = new File("scramble" + File.separator + "users.meta");
                    storage_meta_file.deleteOnExit();
                    users_meta_file.deleteOnExit();

                    System.out.println("Storage found at path: \"" + storage_path + "\".");
                }
            }
        }



        if (storage_meta_file == null || users_meta_file == null) return;

//        Local_User local_user_obj = new Local_User();
//        Local_File local_file_obj = new Local_File();
//        Local_Directory local_directory_obj = new Local_Directory();
//
//        local_user_obj.setUsers_file(users_meta_file.getAbsolutePath());
//        local_file_obj.setStorage_path(storage.getAbsolutePath());              //get storage root
//        local_directory_obj.setStorage_path(storage.getAbsolutePath());
//
//        local_user_obj.init_users();        //

        db_user.setUsers_file(users_meta_file.getAbsolutePath());
        db_user.init_users();

        while (true) {
            System.out.print("[Cloud storage]: ");

            Scanner option_scanner = new Scanner(System.in);
            String option = option_scanner.nextLine();
            option = option.toLowerCase();

            switch (option) {

                case "log in":
                    if (db_user.getCurrent_user() == null) {
                        System.out.println("Please enter your username:");
                        Scanner sc = new Scanner(System.in);
                        String username = sc.nextLine();
                        System.out.println("Please enter your password:");
                        String password = sc.nextLine();
                        db_user.log_in(username, password);
                    } else {
                        System.out.println("You need to log out first!");
                    }
                    break;

                case "log out":
                    if (db_user.getCurrent_user() != null) db_user.log_out();
                    else System.out.println("You have to be loged in first!");
                    break;

                case "create user":
                    if (db_user.getCurrent_user() != null ){
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN){
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
                                    db_user.create_user(username, password, UserPriority.ADMIN);
                                    break;
                                case "basic":
                                    db_user.create_user(username, password, UserPriority.BASIC);
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
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter username of user you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String username = sc.nextLine();
                            db_user.delete_user(username);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create file":
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter new file name:");
                            Scanner sc = new Scanner(System.in);
                            String name = sc.nextLine();
                            if (check_extension(db_dir, name)) {
                                System.out.println("Please enter path where you want to create this new file:");
                                String path = sc.nextLine();
                                db_file.create_file(path, name);
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
                    System.out.println("not implemented");
                    break;

                case "delete file":
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            db_file.delete_file(path);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "copy file":

                    System.out.println("not implemented");

                    break;

                case "copy multiple files":
                    System.out.println("not implemented");
                    break;

                case "archive file":
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to archive:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this archive:");
                            String name = sc.nextLine();
                            if (check_extension(db_dir, name)) {
                                db_file.generate_archive_file(path, name);
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
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
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
                            if (check_extension(db_dir, archive_name)) {
                                System.out.println("Please enter path where you want to save this archive:");
                                String destination = sc.nextLine();
                                db_file.upload_multiple_archived_files(files, destination, archive_name);
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
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to move:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            if (check_extension(db_dir, source)) {
                                System.out.println("Please enter path where you want to move selected file:");
                                String destination = sc.nextLine();
                                db_file.move_file(source, destination);
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
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to rename:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter this file's new name:");
                            String new_name = sc.nextLine();
                            if (check_extension(db_dir, new_name)) {
                                db_file.rename_file(path, new_name);
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
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of where you want to create a directory:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this directory:");
                            String name = sc.nextLine();
                            db_dir.create_directory(path, name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create multiple dir":
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter path where you want to create these new directories:");
                            String path = sc.nextLine();
                            System.out.println("Please enter common name for these new directories:");
                            String common_name = sc.nextLine();
                            System.out.println("Please enter how many similar directories you want to create:");
                            int no_directories = sc.nextInt();
                            db_dir.create_multiple_directories(path, common_name, no_directories);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "delete dir":
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            db_dir.delete_directory(path);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "copy dir":
                    System.out.println("not implemented");
                    break;

                case "archive dir":
                    System.out.println("not implemented");
                    break;

                case "move dir":
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to move:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            System.out.println("Please enter path where you want to move selected directory:");
                            String destination = sc.nextLine();
                            db_dir.move_directory(source, destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "rename dir":
                    System.out.println("not implemented");
                    break;

                case "list files":
                    if (db_user.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        db_dir.list_files(path, null, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files r":
                    if (db_user.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        db_dir.list_files(path, null, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files with":
                    if (db_user.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        System.out.println("Please enter extensions of files you want to list: (separate them with ';')");
                        String extension_str = sc.nextLine();
                        String[] extensions = extension_str.split(";");
                        db_dir.list_files(path, extensions, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files with r":
                    if (db_user.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        System.out.println("Please enter extensions of files you want to list: (separate them with ';')");
                        String extension_str = sc.nextLine();
                        String[] extensions = extension_str.split(";");
                        db_dir.list_files(path, extensions, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list dirs":
                    if (db_user.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose directories you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        db_dir.list_directories(path, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list dirs r":
                    if (db_user.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose directories you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        db_dir.list_directories(path, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create blacklist":
                    if (db_user.getCurrent_user() != null ) {
                        if (db_user.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter extensions of files you want to blacklist: (separate paths with ';')");
                            String extensions = sc.nextLine();
                            String[] new_extensions = extensions.split(";");
                            db_dir.create_extension_blacklist(storage_path, new_extensions);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "search":
                    if (db_user.getCurrent_user() != null ) {
                        System.out.println("Please enter name of a file you want to search:");
                        Scanner scs = new Scanner(System.in);
                        String file_name = scs.nextLine();
                        db_dir.search_files(file_name);
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
